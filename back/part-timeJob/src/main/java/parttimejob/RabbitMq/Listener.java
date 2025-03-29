package parttimejob.RabbitMq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import parttimejob.Entity.Interview;
import parttimejob.service.InterviewService;

import java.util.HashMap;
@Slf4j
@Component
public class Listener {
    @Autowired
    private InterviewService interviewService;
    @RabbitListener(queues = "LazyQueue")
    public void lazyListener(HashMap<String, Object> payload){
        Number interviewIdNumber = (Number) payload.get("interviewId");
        Long interviewId = interviewIdNumber.longValue();
        /*
        * Long interviewId = (Long) payload.get("interviewId");
        * TODO:这里会报错 int 不能强制转换成 Long 原因：RabbitMq默认用的Java的消息转化器当数值较小会采用Integer来接受
        * */
        Integer targetStatus = (Integer) payload.get("targetStatus");
        Interview interview = interviewService.getById(interviewId);
        if (interview == null) {
            log.error("面试记录不存在: {}", interviewId);
            return;
        }
        // 根据目标状态执行更新逻辑
        switch (targetStatus) {
            case 2:
                // 状态 1→2 的条件：当前时间 >= 面试时间
                if (System.currentTimeMillis() >= interview.getDate().getTime()) {
                    interview.setStatus(2);
                    interviewService.updateById(interview);
                    log.info("面试ID: {} 状态更新为 2（面试中）", interviewId);
                }
                break;
            case 3:
                // 状态 2→3 的条件：当前时间 >= 面试时间 + 时长
                long endTime = interview.getDate().getTime() + (long)(interview.getTime() * 3600 * 1000);
                if (System.currentTimeMillis() >= endTime) {
                    interview.setStatus(3);
                    interviewService.updateById(interview);
                    log.info("面试ID: {} 状态更新为 3（待反馈）", interviewId);
                }
                break;
            default:
                log.error("无效的目标状态: {}", targetStatus);
        }
    }
}
