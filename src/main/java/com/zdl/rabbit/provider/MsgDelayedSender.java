package com.zdl.rabbit.provider;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson.JSON;
import com.zdl.rabbit.constant.ExchangeName;
import com.zdl.rabbit.constant.RoutingKey;
import com.zdl.rabbit.entity.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 延时队列实现释放座位数-发送消息方
 *
 * @author zhoudeli
 */
@Slf4j
@Component
public class MsgDelayedSender{

    @Resource
    private RabbitTemplate rabbitTemplate;

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
