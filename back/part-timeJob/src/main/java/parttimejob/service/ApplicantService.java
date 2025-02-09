package parttimejob.service;

import parttimejob.Entity.Applicant;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 86151
* @description 针对表【applicant】的数据库操作Service
* @createDate 2024-10-21 11:03:24
*/
public interface ApplicantService extends IService<Applicant> {

    Long saveReturnId(Applicant applicant);
}
