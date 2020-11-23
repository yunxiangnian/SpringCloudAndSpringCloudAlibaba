package com.scalibaba.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.cloud.springcloud.entities.CommonResult;
import com.cloud.springcloud.entities.Payment;
import com.scalibaba.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * @author lisw
 * @create 2020/11/12
 */
@RestController
@Slf4j
public class OrderController {
    private static final String SERVER_URL = "http://cloudalibaba-provider-nacos";

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private PaymentService paymentService;

    /**
     *  客户端调用注册中心中对应的服务
     *  @SentinelResource(value = "fallback") 没有配置
     *  @SentinelResource(value = "fallback",fallback = "handleFallback") //fallback只负责业务异常
     *  @SentinelResource(value = "fallback",blockHandler = "blockHandlerMethod") //blockHandlerMethod 只负责服务配置异常
     * @param id
     * @return
     */
    @GetMapping("/consumer/nacos/ribbon/{id}")
    @SentinelResource(value = "fallback")
    public CommonResult consumerNacos(@PathVariable("id") Long id){
        CommonResult result = restTemplate.getForObject(SERVER_URL + "/nacos/ribbon/" + id, CommonResult.class);

        if(id == 4){
            throw new IllegalArgumentException("非法参数异常....");
        }else if(result.getData() == null){
            throw new NullPointerException("空指针异常....");
        }
        return result;
    }

    //fallbackHandler
    public CommonResult handleFallback(@PathVariable("id") Long id,Throwable e){
        log.info("fallbackHandler  Logger Info........");
        Payment payment = new Payment(id, "null");
        return new CommonResult(100, "fallback兜底异常" + e.getMessage(),payment);
    }

    /**
     * blockHandler 兜底方法
     */
    public CommonResult blockHandlerMethod(@PathVariable("id")Long id, BlockException e){
        return new CommonResult(100, "blockHandler-sentinel限流，无此流水,BlockException:" + e.getStackTrace());
    }

    /**
     * OpenFeign
     */
    @GetMapping("/consumer/nacos//openfeign/{id}")
    public CommonResult providerRibbon(@PathVariable("id")Long id){
        return paymentService.providerRibbon(id);
    }
}
