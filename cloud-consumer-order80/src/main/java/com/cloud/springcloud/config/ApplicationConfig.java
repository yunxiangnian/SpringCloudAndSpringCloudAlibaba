package com.cloud.springcloud.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.security.PublicKey;

/**
 * @author lisw
 * @create 2020/10/19
 */
@Configuration
public class ApplicationConfig {

    /**
     * 进行远程访问Http服务的类
     * 是Spring提供的用于访问 Rest 服务的 客户端模板的工具集
     * @return
     */
    @Bean
    //@LoadBalanced
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }
}
