package com.cloud.scalibaba.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * @author lisw
 * @create 2020/11/2
 */
@RestController
@Slf4j
public class OrderController {

    @Resource
    private RestTemplate restTemplate;

    @Value("${service-url.nacos-user-service}")
    private String service_URL;


    @GetMapping("consumer/nacos/hello")
    public String paymentInfo(){
        return restTemplate.getForObject(service_URL+"/nacos/hello",String.class);
    }
}
