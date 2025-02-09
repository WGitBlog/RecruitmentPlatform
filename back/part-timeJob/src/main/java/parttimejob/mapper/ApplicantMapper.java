package parttimejob.mapper;

import org.apache.ibatis.annotations.Mapper;
import parttimejob.Entity.Applicant;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author 86151
* @description 针对表【applicant】的数据库操作Mapper
* @createDate 2024-10-21 11:03:24
* @Entity parttimejob.Entity.Applicant
*/
@Mapper
public interface ApplicantMapper extends BaseMapper<Applicant> {

    Long saveReturnId(Applicant applicant);
}




