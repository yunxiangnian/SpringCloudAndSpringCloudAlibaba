package com.scalibaba.config;

import org.springframework.beans.factory.wiring.BeanWiringInfo;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author lisw
 * @create 2020/11/12
 */
@Configuration
public class Myconfig {

    @Bean
    @LoadBalanced
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }
}
