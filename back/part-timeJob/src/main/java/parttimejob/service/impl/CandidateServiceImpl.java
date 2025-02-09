package parttimejob.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import parttimejob.Dto.JobDto;
import parttimejob.Entity.Candidate;
import parttimejob.service.CandidateService;
import parttimejob.mapper.CandidateMapper;
import org.springframework.stereotype.Service;

/**
* @author 86151
* @description 针对表【candidate】的数据库操作Service实现
* @createDate 2024-10-20 15:22:57
*/
@Service
public class CandidateServiceImpl extends ServiceImpl<CandidateMapper, Candidate>
    implements CandidateService{

    @Autowired
    private CandidateMapper candidateMapper;

    //根据jobId查询相对应的company和boos数据
    @Override
    public JobDto getRelatedByJobId(Integer jobId) {
        return candidateMapper.getRelatedByJobId(jobId);
    }

    @Override
    public Boolean changeDetails(Candidate candidate) {
        try {
            candidateMapper.changeDetails(candidate);
            // 如果更新成功，返回 true
            return true;
        } catch (Exception e) {
            // 如果发生异常，返回 false
            e.printStackTrace(); // 可选：打印异常信息
            return false;
        }
    }

    @Override
    public Long saveReturnId(Candidate candidate) {
        Long candidateId=candidateMapper.saveReturnId(candidate);
        return candidateId;
    }


}




