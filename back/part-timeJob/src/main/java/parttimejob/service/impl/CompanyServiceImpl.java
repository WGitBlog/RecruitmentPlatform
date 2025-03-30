package parttimejob.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import parttimejob.Entity.Company;
import parttimejob.service.CompanyService;
import parttimejob.mapper.CompanyMapper;
import org.springframework.stereotype.Service;

/**
* @author 86151
* @description 针对表【company】的数据库操作Service实现
* @createDate 2024-10-17 09:35:05
*/
@Service
public class CompanyServiceImpl extends ServiceImpl<CompanyMapper, Company>
    implements CompanyService{
    @Autowired
    private CompanyMapper companyMapper;

    //根据id查询company
    public Company getCompanyById(Long companyId) {
        return companyMapper.getCompanyById(companyId);
    }

    @Override
    public void saveReturnId(Company company) {
        companyMapper.saveReturnId(company);
    }
}




