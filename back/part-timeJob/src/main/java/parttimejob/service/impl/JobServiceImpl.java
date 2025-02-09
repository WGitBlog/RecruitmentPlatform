package parttimejob.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import parttimejob.Entity.Job;
import parttimejob.service.JobService;
import parttimejob.mapper.JobMapper;
import org.springframework.stereotype.Service;

/**
* @author 86151
* @description 针对表【job】的数据库操作Service实现
* @createDate 2024-10-18 17:45:50
*/
@Service
public class JobServiceImpl extends ServiceImpl<JobMapper, Job>
    implements JobService{

}




