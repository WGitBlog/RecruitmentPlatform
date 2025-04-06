package parttimejob.service;

import parttimejob.Dto.JobDto;
import parttimejob.Entity.Candidate;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.ArrayList;
import java.util.List;

/**
* @author 86151
* @description 针对表【candidate】的数据库操作Service
* @createDate 2024-10-20 15:22:57
*/
public interface CandidateService extends IService<Candidate> {
    //根据jobId查询相对应的company和boos数据
    JobDto getRelatedByJobId(Integer jobId);
    //根据传来的相应字段来修改Candidate信息
    Boolean changeDetails(Candidate candidate);

    Long saveReturnId(Candidate candidate);

    ArrayList<Candidate> getCandidatesByIds(List<Long> ids);
}
