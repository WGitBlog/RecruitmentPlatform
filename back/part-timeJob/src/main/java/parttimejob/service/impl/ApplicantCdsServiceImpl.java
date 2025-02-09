package parttimejob.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import parttimejob.Entity.ApplicantCds;
import parttimejob.service.ApplicantCdsService;
import parttimejob.mapper.ApplicantCdsMapper;
import org.springframework.stereotype.Service;

/**
* @author 86151
* @description 针对表【applicant_cds】的数据库操作Service实现
* @createDate 2024-11-06 14:07:29
*/
@Service
public class ApplicantCdsServiceImpl extends ServiceImpl<ApplicantCdsMapper, ApplicantCds>
    implements ApplicantCdsService{

    @Autowired
    private ApplicantCdsMapper applicantCdsMapper;
    @Override
    public Long saveReturnId(ApplicantCds applicantCds) {
        Long applicantCdsId= applicantCdsMapper.saveReturnId(applicantCds);
        return applicantCdsId;
    }
}




