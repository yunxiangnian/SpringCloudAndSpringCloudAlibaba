package com.cloud.springcloud.controller;

import com.cloud.springcloud.service.PaymentHystrixService;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author lisw
 * @create 2020/10/22
 */
@RestController
@Slf4j
//@DefaultProperties(defaultFallback = "paymentInfo_timeoutHandler")
public class OrderHystrixController {

    @Resource
    private PaymentHystrixService paymentHystrixService;


    @GetMapping("/consumer/payment/hystrix/ok/{id}")
    String paymentInfo_OK(@PathVariable("id") Integer id){
        return paymentHystrixService.paymentInfo_OK(id);
    }

    @GetMapping("/consumer/payment/hystrix/timeout/{id}")
    // 不写自己的 fallbackMethod 属性，就使用全局默认的
    @HystrixCommand(
//            fallbackMethod = "paymentInfo_timeoutHandlerMethod",
            commandProperties = {
            //设置峰值，超过 3 秒，就会调用兜底方法，这个时间也可以由feign控制
            @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds", value = "1500")})
    public String paymentInfo_Timeout(Integer id){
        return paymentHystrixService.paymentInfo_timeout(id);
    }

    /**
     兜底方法，根据上述配置，程序内发生异常、或者运行超时，都会执行该兜底方法
     */
    public String paymentInfo_timeoutHandlerMethod(Integer id){
        return "80自己出错或者服务器服务繁忙,请稍后重试";
    }


    /**
     * 下面是全局fallback方法
     */
    public String paymentInfo_timeoutHandler(){
        return "Global异常处理信息，请稍后重试";
    }
}
