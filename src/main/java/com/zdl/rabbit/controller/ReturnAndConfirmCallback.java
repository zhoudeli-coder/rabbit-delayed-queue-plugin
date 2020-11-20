package com.zdl.rabbit.controller;

import com.zdl.rabbit.constant.ExchangeName;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

/**
 * 延时队列实现释放座位数-消息消費方
 *
 * @author zhoudeli
 */
@Slf4j
@Component
public class ReturnAndConfirmCallback implements RabbitTemplate.ReturnCallback, RabbitTemplate.ConfirmCallback {

    @Override
    public void confirm(@Nullable CorrelationData correlationData, boolean ack, @Nullable String cause) {
        log.info("args:{} {} {}", correlationData, ack, cause);
        String messageId = correlationData.getId();
        // 消息发送成功,消息已经写入队列
        /**
         * 如果消息没有到exchange,则confirm回调,ack=false
         * 如果消息到达exchange,则confirm回调,ack=true
         * exchange到queue成功,则不回调return
         * exchange到queue失败,则回调return(需设置mandatory=true,否则不回回调,消息就丢了
         *
         */
        if (ack) {
        } else {
        }
    }

    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        //如果是延迟队列则忽略，使用插件延迟队列不管成功都会调用
        if (ExchangeName.DELAY.equals(exchange)) {
            return;
        }
        log.debug("return--message:" + new String(message.getBody()) + ",replyCode:" + replyCode + "," +
                "replyText:" + replyText + ",exchange:" + exchange + ",routingKey:" + routingKey);
    }
}
