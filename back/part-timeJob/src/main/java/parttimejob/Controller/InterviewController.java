package parttimejob.Controller;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import io.lettuce.core.ScriptOutputType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import parttimejob.Dto.Common.Step;
import parttimejob.Dto.realDto.InterviewDto;
import parttimejob.Entity.Interview;
import parttimejob.Result.R;
import parttimejob.service.InterviewService;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@CrossOrigin("http://localhost:5173")
@RequestMapping("/interview")
public class InterviewController {
    @Autowired
    private InterviewService interviewService;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 新增面试邀约
     * @param interview
     * @return
     */
    @PostMapping("/sendInterview")
    public R<String> sendInterview(@RequestBody Interview interview){
        interviewService.saveReturnId(interview);
        /*
        * TODO:问题-插入返回ID这个自定义方法返回的值一直为1
        *      原因-应该从传入的参数中调用getId()才能获取插入之后返回的ID不能通过返回值返回
        * */
        Long interviewId=interview.getId();
        System.out.println("面试id:"+interview.getId());
        if (interview.getId()==null){
            R.error("邀约面试失败");
        }
        //待面试--->面试中  status 1--->2  延迟时间计算（单位：毫秒）
        Long timeCalculation1=Math.max(interview.getDate().getTime() - System.currentTimeMillis(), 0);
        System.out.println("timeCalculation1:"+timeCalculation1);
        //面试中--->待反馈  status 2--->3  延迟时间计算（单位：毫秒）
        Long timeCalculation2=Math.max(interview.getDate().getTime() + (long)(interview.getTime() * 3600 * 1000) - System.currentTimeMillis(), 0);
        System.out.println("timeCalculation2:"+timeCalculation2);
        // 发送两条延迟消息
        sendDelayedMessage(interviewId, 2,timeCalculation1);  // 触发状态 1→2
        sendDelayedMessage(interviewId, 3,timeCalculation2);    // 触发状态 2→3
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

    /**
     * 取消面试
     * @param interviewId
     * @return
     */
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

    //可以写一个Rabbitmq的Util方法
    private void sendDelayedMessage(Long interviewId,Integer targetStatus, Long delayTime ) {
        HashMap<String, Object> msg = new HashMap<>();
        msg.put("interviewId", interviewId);
        msg.put("targetStatus", targetStatus);  // 目标状态
        CorrelationData correlation = new CorrelationData(UUID.randomUUID().toString());
        correlation.getFuture().addCallback(
                confirm -> {
                    if (confirm != null && confirm.isAck()) {
                        log.info("消息成功发送到交换机 消息ID:{}", correlation.getId());
                    } else {
                        log.error("消息未到达交换机 消息ID:{}", correlation.getId());
                    }
                },
                ex -> log.error("消息发送异常", ex)
        );
        rabbitTemplate.convertAndSend("LazyExchange", "Lazy", msg, message -> {
            message.getMessageProperties().setHeader("x-delay", delayTime);
            return message;
        }, correlation);
    }








}
