package com.cloud.springcloud.service.Impl;

import cn.hutool.core.lang.UUID;
import com.cloud.springcloud.service.IMessageProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;

import javax.annotation.Resource;


/**
 * @EnableBinding(Source.class)  可以理解为将生产者绑定到某一个通道上
 * @author lisw
 * @create 2020/10/28
 */
@EnableBinding(Source.class)
@Slf4j
public class IMessageProviderImpl implements IMessageProvider {


    /**
     * 消息发送通道
     */
    @Resource
    private MessageChannel output;

    @Override
    public String send() {
        String serial = UUID.randomUUID().toString();
        output.send(MessageBuilder.withPayload(serial).build());
        log.info("*******serial : " + serial);
        return serial;
    }
}
