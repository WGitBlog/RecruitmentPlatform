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
import java.util.Objects;

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

    @Override
    public void updatePassword(UserDto user) throws Exception {
            User selectOne = userMapper.selectById(user.getId());
            if (selectOne == null || !PasswordTools.decrypt(user.getOldPassword(),selectOne.getPassword())) {
                throw new Exception("旧密码错误");
            }
            //密码加密
            String encryptPassword2 = PasswordTools.encrypt(user.getPassword());
            LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(User::getId,user.getId()).set(User::getPassword,encryptPassword2);
            int rows = userMapper.update(null, updateWrapper);
            if (rows != 1) {
                throw new Exception("密码更新失败");
            }
    }

}




