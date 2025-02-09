package parttimejob.mapper;

import org.apache.ibatis.annotations.Mapper;
import parttimejob.Entity.Job;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author 86151
* @description 针对表【job】的数据库操作Mapper
* @createDate 2024-10-18 17:45:50
* @Entity parttimejob.Entity.Job
*/
@Mapper
public interface JobMapper extends BaseMapper<Job> {

}




