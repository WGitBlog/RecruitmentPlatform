package parttimejob;


import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.UUID;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringAmqpTest {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testSendMessage2SimpleQueue() {
        Message sss=MessageBuilder.withBody("ajsdjasldja".getBytes(StandardCharsets.UTF_8)).setHeader("x-delay", 20000).build();
        rabbitTemplate.convertAndSend("LazyExchange","Lazy", sss);
    }

    @Test
    public void testSendMessageWithCorrelation(){
        //TODO:ConfirmCallBack判断是否投递到交换机
        HashMap<String, Object> msg= new HashMap<>();
        msg.put("1","first");
        msg.put("2","second");
        CorrelationData correlation = new CorrelationData(UUID.randomUUID().toString());
        correlation.getFuture().addCallback(
                confirm -> {
                    //判断结果是否为ACK
                    if (confirm != null && confirm.isAck()) {
                        //ACK说明成功发送到交换机
                        log.info("消息成功发送到交换机 消息ID:{}",correlation.getId());
                    }else {
                        //NACK说明消息没到交换机
                        log.error("消息成功失败到交换机 消息ID:{}",correlation.getId());
                    }
                },
                ex -> {
                    //NACK说明消息没到交换机
                    log.error("消息成功失败到交换机 消息ID:{}",correlation.getId());
                }
        );
        rabbitTemplate.convertAndSend("LazyExchange","Lazy",msg,message -> {
            message.getMessageProperties().setHeader("x-delay", 10000);  // 关键延迟设置
            return message;
        },correlation);
    }
}