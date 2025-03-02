package parttimejob.mapper;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import parttimejob.Entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
* @author 86151
* @description 针对表【user】的数据库操作Mapper
* @createDate 2024-10-20 14:30:55
* @Entity parttimejob.Entity.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {


    @MapKey(value = "registration_date")
    List<Map<String, Object>> getTableData();
    @MapKey(value = "category_date")
    List<Map<String, Object>> getJobTableData();
}




