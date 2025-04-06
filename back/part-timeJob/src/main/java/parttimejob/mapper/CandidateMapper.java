package parttimejob.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import parttimejob.Dto.JobDto;
import parttimejob.Entity.Candidate;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.ArrayList;
import java.util.List;

/**
* @author 86151
* @description 针对表【candidate】的数据库操作Mapper
* @createDate 2024-10-20 15:22:57
* @Entity parttimejob.Entity.Candidate
*/
@Mapper
public interface CandidateMapper extends BaseMapper<Candidate> {
    //根据jobId查询相对应的company和boos数据
    JobDto getRelatedByJobId(Integer jobId);
    void changeDetails(Candidate candidate);

    Long saveReturnId(Candidate candidate);
    //在 Java 方法里，如果只有一个 List 类型的参数传入 MyBatis 的 Mapper 方法，MyBatis 会默认把这个参数命名为 arg0、collection 或者 list，不会使用你期望的 ids。为了能在 SQL 里使用 ids 作为参数名，需要在 Java 方法的参数上添加 @Param("ids") 注解。
    ArrayList<Candidate> getCandidatesByIds(@Param("ids") List<Long> ids);
}




