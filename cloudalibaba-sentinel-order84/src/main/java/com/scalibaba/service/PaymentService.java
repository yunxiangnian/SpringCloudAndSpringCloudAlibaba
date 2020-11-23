package com.scalibaba.service;

import com.cloud.springcloud.entities.CommonResult;
import com.scalibaba.service.impl.PaymentFallbackService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author lisw
 * @create 2020/11/13
 */
@FeignClient(value = "cloudalibaba-provider-nacos",fallback = PaymentFallbackService.class)
public interface PaymentService {

    @GetMapping("/nacos/ribbon/{id}")
    CommonResult providerRibbon(@PathVariable("id")Long id);
}
