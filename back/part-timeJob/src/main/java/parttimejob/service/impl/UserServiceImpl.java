package parttimejob.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import parttimejob.Entity.User;
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

    @Override
    public List<Map<String, Object>> getTableData() {
        return userMapper.getTableData();
    }

    @Override
    public List<Map<String, Object>> getJobTableData() {
        return userMapper.getJobTableData();
    }
}




