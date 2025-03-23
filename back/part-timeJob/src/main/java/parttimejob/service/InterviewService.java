package parttimejob.service;

import org.springframework.stereotype.Service;
import parttimejob.Dto.realDto.InterviewDto;
import parttimejob.Entity.Interview;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author 86151
* @description 针对表【interview(面试流程表，用于存储应聘者的面试信息)】的数据库操作Service
* @createDate 2025-03-22 15:23:12
*/
@Service
public interface InterviewService extends IService<Interview> {

    List<InterviewDto> getInterview(Long userId);
}
