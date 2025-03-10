package parttimejob.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import parttimejob.Dto.UserDto;
import parttimejob.Entity.Boos;
import parttimejob.Entity.Candidate;
import parttimejob.Entity.User;
import parttimejob.Utils.saltMd5.PasswordTools;
import parttimejob.mapper.BoosMapper;
import parttimejob.mapper.CandidateMapper;
import parttimejob.service.UserService;
import parttimejob.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
* @author 86151
* @description 针对表【user】的数据库操作Service实现
* @createDate 2024-10-20 14:30:55
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CandidateMapper candidateMapper;
    @Autowired
    private BoosMapper boosMapper;
    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;
    @Override
    public List<Map<String, Object>> getTableData() {
        return userMapper.getTableData();
    }

    @Override
    public List<Map<String, Object>> getJobTableData() {
        return userMapper.getJobTableData();
    }
    @Override

    public void resetPassword(UserDto user) throws Exception {
        try {
            // 1. 参数校验
            if (user == null || StringUtils.isBlank(user.getEmail())
                    || StringUtils.isBlank(user.getPassword())
                    || StringUtils.isBlank(user.getCode())
                    || StringUtils.isBlank(user.getUuid())) {
                throw new Exception("参数不完整");
            }

            // 2. 验证码校验
            String key = "email_captcha:" + user.getUuid();
            String realCode = (String) redisTemplate.opsForValue().get(key);
            if (realCode == null) {
                throw new Exception("验证码已过期");
            }
            if (!user.getCode().equalsIgnoreCase(realCode)) {
                throw new Exception("验证码错误");
            }

            // 3. 密码加密
            String encryptPassword = PasswordTools.encrypt(user.getPassword());

            // 4. 查找用户并更新密码
            // 4.1 先查找候选人
            Candidate candidate = candidateMapper.selectOne(
                    new LambdaQueryWrapper<Candidate>()
                            .eq(Candidate::getEmail, user.getEmail())
            );

            if (candidate != null) {
                // 更新候选人密码
                LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
                updateWrapper.eq(User::getCandidateId, candidate.getId())
                        .set(User::getPassword, encryptPassword);

                int rows = userMapper.update(null, updateWrapper);
                if (rows != 1) {
                    throw new Exception("密码重置失败");
                }
            } else {
                // 4.2 查找企业用户
                Boos boos = boosMapper.selectOne(
                        new LambdaQueryWrapper<Boos>()
                                .eq(Boos::getBoosEmail, user.getEmail())
                );

                if (boos != null) {
                    // 更新企业用户密码
                    LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
                    updateWrapper.eq(User::getBoosId, boos.getId())
                            .set(User::getPassword, encryptPassword);
                    int rows = userMapper.update(null, updateWrapper);
                    if (rows != 1) {
                        throw new Exception("密码重置失败");
                    }
                } else {
                    throw new Exception("该邮箱未注册");
                }
            }
        } catch (Exception e) {
            throw new Exception("重置密码失败: " + e.getMessage());
        }
    }
}




