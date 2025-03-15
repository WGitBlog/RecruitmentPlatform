package parttimejob.mapper;

import org.apache.ibatis.annotations.Mapper;
import parttimejob.Entity.Interview;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author 86151
* @description 针对表【interview(面试流程表，用于存储应聘者的面试信息)】的数据库操作Mapper
* @createDate 2025-03-15 17:33:20
* @Entity parttimejob.Entity.Interview
*/
@Mapper
public interface InterviewMapper extends BaseMapper<Interview> {

}




