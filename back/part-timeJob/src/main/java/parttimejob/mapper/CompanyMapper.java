package parttimejob.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import parttimejob.Entity.Company;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author 86151
* @description 针对表【company】的数据库操作Mapper
* @createDate 2024-10-17 09:35:05
* @Entity parttimejob.Entity.Company
*/
@Mapper
public interface CompanyMapper extends BaseMapper<Company> {

    //根据id查询company
    @Select("SELECT * FROM company WHERE id=#{companyId}")
    Company getCompanyById(Long companyId);

    void saveReturnId(Company company);
}




