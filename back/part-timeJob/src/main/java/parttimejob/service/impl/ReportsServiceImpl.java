package parttimejob.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import parttimejob.Entity.Reports;
import parttimejob.service.ReportsService;
import parttimejob.mapper.ReportsMapper;
import org.springframework.stereotype.Service;

/**
* @author 86151
* @description 针对表【reports】的数据库操作Service实现
* @createDate 2024-12-15 14:12:56
*/
@Service
public class ReportsServiceImpl extends ServiceImpl<ReportsMapper, Reports>
    implements ReportsService{

}




