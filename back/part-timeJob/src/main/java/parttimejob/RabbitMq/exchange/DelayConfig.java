package parttimejob.RabbitMq.exchange;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class DelayConfig {
    //声明交换机和队列
    @Bean
    public TopicExchange lazyExchange(){
        //名字-开启持久化-没绑定是否删除
        return ExchangeBuilder
                .topicExchange("LazyExchange")
                .delayed()//开启惰性交换机
                .durable(true)//持久化
                .build();
    }
    @Bean
    public Queue lazyQueue(){
        return QueueBuilder
                .durable("LazyQueue")
                .build();
    }
    @Bean
    public Binding lazyBinding(Queue lazyQueue, TopicExchange lazyExchange){
        return BindingBuilder
                .bind(lazyQueue)
                .to(lazyExchange)
                .with("Lazy");
    }
}
