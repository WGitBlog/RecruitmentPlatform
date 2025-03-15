package parttimejob.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import parttimejob.Entity.Interview;
import parttimejob.service.InterviewService;
import parttimejob.mapper.InterviewMapper;
import org.springframework.stereotype.Service;

/**
* @author 86151
* @description 针对表【interview(面试流程表，用于存储应聘者的面试信息)】的数据库操作Service实现
* @createDate 2025-03-15 17:33:20
*/
@Service
public class InterviewServiceImpl extends ServiceImpl<InterviewMapper, Interview>
    implements InterviewService{

}




