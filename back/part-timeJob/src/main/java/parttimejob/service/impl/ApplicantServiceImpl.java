package parttimejob.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import parttimejob.Entity.Applicant;
import parttimejob.service.ApplicantService;
import parttimejob.mapper.ApplicantMapper;
import org.springframework.stereotype.Service;

/**
* @author 86151
* @description 针对表【applicant】的数据库操作Service实现
* @createDate 2024-10-21 11:03:24
*/
@Service
public class ApplicantServiceImpl extends ServiceImpl<ApplicantMapper, Applicant>
    implements ApplicantService{
    @Autowired
    private ApplicantMapper applicantMapper;

    @Override
    public Long saveReturnId(Applicant applicant) {
        Long applicantId=applicantMapper.saveReturnId(applicant);
        return applicantId;
    }
}




