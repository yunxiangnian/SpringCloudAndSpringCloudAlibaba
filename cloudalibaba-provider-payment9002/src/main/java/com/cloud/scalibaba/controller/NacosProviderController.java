package com.cloud.scalibaba.controller;

import cn.hutool.core.lang.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lisw
 * @create 2020/11/2
 */
@RestController
public class NacosProviderController {
    @Value("${server.port}")
    private String serverPort;

    @GetMapping("/nacos/hello")
    public String nacosHello(){
        return serverPort + " hi Nacos My First Test" + UUID.randomUUID();
    }

}
