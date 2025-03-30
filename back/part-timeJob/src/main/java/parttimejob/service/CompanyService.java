package parttimejob.service;

import parttimejob.Entity.Company;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 86151
* @description 针对表【company】的数据库操作Service
* @createDate 2024-10-17 09:35:05
*/
public interface CompanyService extends IService<Company> {
    //根据id查询company
    Company getCompanyById(Long companyId);

    void saveReturnId(Company company);
}
