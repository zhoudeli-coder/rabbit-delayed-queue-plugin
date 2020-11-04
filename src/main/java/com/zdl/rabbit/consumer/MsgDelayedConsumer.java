package com.zdl.rabbit.consumer;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import com.zdl.rabbit.constant.QueueName;
import com.zdl.rabbit.entity.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 延时队列实现释放座位数-消息消費方
 *
 * @author zhoudeli
 */
@Slf4j
@Component
public class MsgDelayedConsumer {
    /**
     * 延时消费
     *
     * @param channel
     * @param msg
     */
    @RabbitListener(queues = QueueName.DELAY_QUEUE)
    @RabbitHandler
    public void listener(Channel channel, Message msg) {
        try {
            UserInfo userInfo = JSON.parseObject(msg.getBody(), UserInfo.class);
            log.info("开始消费, {}", userInfo);
            channel.basicAck(msg.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error("异常 {}", e);
        }
    }
}
