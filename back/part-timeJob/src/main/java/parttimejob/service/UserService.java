package parttimejob.service;

import parttimejob.Entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
* @author 86151
* @description 针对表【user】的数据库操作Service
* @createDate 2024-10-20 14:30:55
*/
public interface UserService extends IService<User> {

    List<Map<String, Object>> getTableData();

    List<Map<String, Object>> getJobTableData();
}
