package com.zdl.rabbit.configration;

import com.zdl.rabbit.constant.ExchangeName;
import com.zdl.rabbit.constant.QueueName;
import com.zdl.rabbit.constant.RoutingKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 1、TTL ，即 Time-To-Live，存活时间，消息和队列都可以设置存活时间
 * 2、Dead Letter，即死信，若给消息设置了存活时间，当超过存活时间后消息还没有被消费，则该消息变成了死信
 * 3、Dead Letter Exchanges（DLX），即死信交换机
 * 4、Dead Letter Routing Key （DLK），死信路由键
 *
 * @author zhoudeli
 */
@Configuration
@Slf4j
public class RabbitMqConfig {
    @Bean
    public Queue delayedQueue() {
        return new Queue(QueueName.DELAY_QUEUE, true, false, false);
    }

    @Bean
    public CustomExchange delayedExchange() {
        Map<String, Object> args = new HashMap<>(4);
        args.put("x-delayed-type", "direct");
        return new CustomExchange(ExchangeName.DELAY, "x-delayed-message", true, false, args);
    }

    @Bean
    public Binding bind(@Qualifier("delayedQueue") Queue queue,
                                @Qualifier("delayedExchange") CustomExchange delayedExchange) {
        return BindingBuilder.bind(queue).to(delayedExchange).with(RoutingKey.DELAY_KEY).noargs();
    }
}