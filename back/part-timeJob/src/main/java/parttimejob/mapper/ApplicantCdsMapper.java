package parttimejob.mapper;

import org.apache.ibatis.annotations.Mapper;
import parttimejob.Entity.ApplicantCds;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author 86151
* @description 针对表【applicant_cds】的数据库操作Mapper
* @createDate 2024-11-06 14:07:29
* @Entity parttimejob.Entity.ApplicantCds
*/
@Mapper
public interface ApplicantCdsMapper extends BaseMapper<ApplicantCds> {

    Long saveReturnId(ApplicantCds applicantCds);
}




