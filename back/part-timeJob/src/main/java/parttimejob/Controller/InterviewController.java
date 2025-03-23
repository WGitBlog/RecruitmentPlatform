package parttimejob.Controller;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import parttimejob.Dto.Common.Step;
import parttimejob.Dto.realDto.InterviewDto;
import parttimejob.Entity.Interview;
import parttimejob.Result.R;
import parttimejob.service.InterviewService;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin("http://localhost:5173")
@RequestMapping("/interview")
public class InterviewController {
    @Autowired
    private InterviewService interviewService;

    /**
     * 新增面试邀约
     * @param interview
     * @return
     */
    @PostMapping("/sendInterview")
    public R<String> sendInterview(@RequestBody Interview interview){
        List<Step> steps = interview.getSteps();
        System.out.println("steps:"+steps);
        interviewService.save(interview);
        return R.success("邀约面试成功");
    }

    /**
     * 获取面试信息
     * @param userId
     * @return
     */
    @GetMapping("/getInterview/{userId}")
    public R<List<InterviewDto>> getInterview(@PathVariable Long userId){
        if (userId == null){
            R.success("参数丢失");
        }
        List<InterviewDto> result=interviewService.getInterview(userId);

        return R.success(result);
    }

    /**
     * 提交反馈
     * @param interview
     * @return
     */
    @PostMapping("/submitFeedbackInfo")
    public R<String> submitFeedbackInfo(@RequestBody Interview interview){
        LambdaUpdateWrapper<Interview> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Interview::getId,interview.getId())
                .set(Interview::getRating,interview.getRating())
                .set(Interview::getResult,interview.getResult())
                .set(Interview::getStatus,interview.getStatus())
                .set(Interview::getFeedbackContent,interview.getFeedbackContent());

        boolean update = interviewService.update(updateWrapper);
        if (!update){
            R.error("反馈失败");
        }
        R.success("反馈成功");
        return null;
    }


    @PutMapping("/cancelInterviewInfo/{interviewId}")
    public R<String> cancelInterviewInfo(@PathVariable Long interviewId){
        LambdaUpdateWrapper<Interview> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Interview::getId,interviewId)
                .set(Interview::getStatus,0);
        boolean update = interviewService.update(updateWrapper);
        if (!update){
            return R.error("取消面试失败");
        }
        return R.success("取消面试成功");
    }










}
