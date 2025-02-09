package parttimejob.mapper;

import org.apache.ibatis.annotations.Mapper;
import parttimejob.Entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author 86151
* @description 针对表【user】的数据库操作Mapper
* @createDate 2024-10-20 14:30:55
* @Entity parttimejob.Entity.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




