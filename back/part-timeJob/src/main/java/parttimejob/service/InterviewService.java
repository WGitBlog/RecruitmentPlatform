package parttimejob.service;

import org.springframework.stereotype.Service;
import parttimejob.Entity.Interview;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 86151
* @description 针对表【interview(面试流程表，用于存储应聘者的面试信息)】的数据库操作Service
* @createDate 2025-03-15 17:33:20
*/
@Service
public interface InterviewService extends IService<Interview> {

}
