package com.scalibaba.service.impl;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.cloud.springcloud.entities.CommonResult;
import com.cloud.springcloud.entities.Payment;
import com.scalibaba.service.PaymentService;
import org.springframework.stereotype.Component;

/**
 * @author lisw
 * @create 2020/11/13
 */
@Component
public class PaymentFallbackService implements PaymentService {
    @Override
    public CommonResult providerRibbon(Long id) {
        return new CommonResult(100, "OpenFeign的兜底方法,服务降级返回",new Payment(id,"OpenFeign"));
    }
}
