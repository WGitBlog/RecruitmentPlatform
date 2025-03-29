package parttimejob.Config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * TODO:RabbitMq的理解
 *      ConfirmCallBack判断是否从生产者到达交换机
 *      ReturnCallBack判断是否从交换机投递到队列
 *      消息确认Manual判断是否消费者将消息成功消费
 */

@Slf4j
@Configuration
public class RabbitMqConfig implements ApplicationContextAware {
    //TODO:ReturnCallBack判断是否从交换机投递到队列
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // 获取RabbitTemplate
        RabbitTemplate rabbitTemplate = applicationContext.getBean(RabbitTemplate.class);
        // 设置ReturnCallback
        rabbitTemplate.setReturnsCallback(r -> {
            log.warn("消息发送到队列失败，应答码:{}，原因:{}，交换机:{}，路由KEY:{},消息:{}",
                    r.getReplyCode(), r.getReplyText(), r.getExchange(), r.getRoutingKey(), r.getMessage());}
                    // 如果有业务需要，可以重发消息
        );
    }


}
