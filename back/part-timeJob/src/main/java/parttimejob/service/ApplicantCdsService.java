package parttimejob.service;

import parttimejob.Entity.ApplicantCds;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 86151
* @description 针对表【applicant_cds】的数据库操作Service
* @createDate 2024-11-06 14:07:29
*/
public interface ApplicantCdsService extends IService<ApplicantCds> {

    Long saveReturnId(ApplicantCds applicantCds);
}
