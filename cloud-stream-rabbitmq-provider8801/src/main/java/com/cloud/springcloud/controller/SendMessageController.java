package com.cloud.springcloud.controller;

import com.cloud.springcloud.service.IMessageProvider;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author lisw
 * @create 2020/10/28
 */
@RestController
public class SendMessageController {
    @Resource
    private IMessageProvider iMessageProvider;

    @GetMapping("/send")
    public String sendMes(){
        return iMessageProvider.send();
    }
}
