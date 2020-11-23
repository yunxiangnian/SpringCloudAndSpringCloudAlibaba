package com.cloud.springcloud.service;

import org.springframework.stereotype.Component;

/**
 * @author lisw
 * @create 2020/10/23
 */
@Component
public class PaymentFallbackService implements PaymentHystrixService{
    @Override
    public String paymentInfo_OK(Integer id) {
        return "客户端异常....";
    }

    @Override
    public String paymentInfo_timeout(Integer id) {
        return "客户端发生连接超时...";
    }
}
