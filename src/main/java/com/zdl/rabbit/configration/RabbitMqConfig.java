package com.zdl.rabbit.configration;

import com.zdl.rabbit.controller.ReturnAndConfirmCallback;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

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
    //    @Bean
//    public Queue delayedQueue() {
//        return new Queue(QueueName.DELAY_QUEUE, true, false, false);
//    }
//
//    @Bean
//    public CustomExchange delayedExchange() {
//        Map<String, Object> args = new HashMap<>(4);
//        args.put("x-delayed-type", "direct");
//        return new CustomExchange(ExchangeName.DELAY, "x-delayed-message", true, false, args);
//    }
//
//    @Bean
//    public Binding bind(@Qualifier("delayedQueue") Queue queue,
//                                @Qualifier("delayedExchange") CustomExchange delayedExchange) {
//        return BindingBuilder.bind(queue).to(delayedExchange).with(RoutingKey.DELAY_KEY).noargs();
//    }
    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, ReturnAndConfirmCallback returnAndConfirmCallback) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory);
        rabbitTemplate.setConfirmCallback(returnAndConfirmCallback);
        rabbitTemplate.setReturnCallback(returnAndConfirmCallback);
        rabbitTemplate.setMandatory(true);
        return rabbitTemplate;
    }

    @Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(5000);
        factory.setConnectTimeout(15000);
        return factory;
    }
}