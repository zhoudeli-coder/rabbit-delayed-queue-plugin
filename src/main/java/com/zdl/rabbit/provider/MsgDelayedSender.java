package com.zdl.rabbit.provider;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson.JSON;
import com.zdl.rabbit.constant.ExchangeName;
import com.zdl.rabbit.constant.QueueName;
import com.zdl.rabbit.constant.RoutingKey;
import com.zdl.rabbit.entity.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * 延时队列实现释放座位数-发送消息方
 *
 * @author zhoudeli
 */
@Slf4j
@Component
public class MsgDelayedSender implements RabbitTemplate.ReturnCallback, RabbitTemplate.ConfirmCallback {

    private static final Integer CONFIRM_FAILED = 0;
    private static final Integer CONFIRM_SUCCESS = 1;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void afterConstruct(){
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnCallback(this);
    }


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
            log.info("消息已确认， {}", messageId);
            // 如果消息到达exchange,则confirm回调,ack=true
        } else {
            log.info("消息未确认， {}", messageId);
            // 如果消息没有到exchange,则confirm回调,ack=false
        }
    }

    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        // xchange到queue失败,则回调return(需设置mandatory=true,否则不回回调,消息就丢了
        // 延时消费消息未及时确认，所以也会回调这个方法
        log.debug("return--message:" + new String(message.getBody()) + ",replyCode:" + replyCode + "," +
                "replyText:" + replyText + ",exchange:" + exchange + ",routingKey:" + routingKey);
    }

    /**
     * 半小时未支付自动取消订单
     *
     * @param userInfo 参数
     * @param seconds 延迟时间-秒
     */
    public void sendDelayed(UserInfo userInfo, int seconds) {
        log.debug("消息已发送: {} delayTime: {}秒", DateUtil.now(), seconds);
        String content = JSON.toJSONString(userInfo);
        CorrelationData correlationData = new CorrelationData(String.valueOf(UUID.randomUUID()));
        this.rabbitTemplate.convertAndSend(ExchangeName.DELAY, RoutingKey.DELAY_KEY, content,
                (message) -> {
                    long times = seconds * 1000;
                    message.getMessageProperties().setHeader("x-delay", times);
                    return message;
                }, correlationData);
    }
}
