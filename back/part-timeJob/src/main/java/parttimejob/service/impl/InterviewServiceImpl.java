package parttimejob.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import parttimejob.Dto.realDto.InterviewDto;
import parttimejob.Entity.*;
import parttimejob.mapper.*;
import parttimejob.service.InterviewService;
import org.springframework.stereotype.Service;
import parttimejob.service.JobService;

import java.util.List;
import java.util.stream.Collectors;

/**
* @author 86151
* @description 针对表【interview(面试流程表，用于存储应聘者的面试信息)】的数据库操作Service实现
* @createDate 2025-03-22 15:23:12
*/
@Service
public class InterviewServiceImpl extends ServiceImpl<InterviewMapper, Interview>
    implements InterviewService{
    @Autowired
    private JobMapper jobMapper;
    @Autowired
    private InterviewMapper interviewMapper;
    @Autowired
    private CompanyMapper companyMapper;
    @Autowired
    private CandidateMapper candidateMapper;
    @Autowired
    private UserMapper userMapper;
    @Override
    public List<InterviewDto> getInterview(Long userId) {
        List<Interview> interviewList=interviewMapper.getInterview(userId);
        List<InterviewDto> interviewDtoList=interviewList.stream().map((item)->{
            InterviewDto interviewDto = new InterviewDto();
            BeanUtils.copyProperties(item,interviewDto);


            Company company = companyMapper.getCompanyById(item.getCompanyId());
            interviewDto.setCompanyName(company.getCompanyName());
            interviewDto.setCompanyImgUrl(company.getCompanyImg());



            Job job = jobMapper.selectById(item.getJobId());
            interviewDto.setJobTitle(job.getJobTitle());


            User recipientUser = userMapper.selectById(item.getRecipientId());
            Candidate candidate = candidateMapper.selectById(recipientUser.getCandidateId());
            interviewDto.setCandidateName(candidate.getName());
            interviewDto.setCandidateImgUrl(candidate.getTiny());





            return  interviewDto;
        }).collect(Collectors.toList());
        return interviewDtoList;
    }

    @Override
    public void saveReturnId(Interview interview) {
        interviewMapper.saveReturnId(interview);
    }
}




