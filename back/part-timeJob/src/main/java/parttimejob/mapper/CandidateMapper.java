package parttimejob.mapper;

import org.apache.ibatis.annotations.Mapper;
import parttimejob.Dto.JobDto;
import parttimejob.Entity.Candidate;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

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
}




