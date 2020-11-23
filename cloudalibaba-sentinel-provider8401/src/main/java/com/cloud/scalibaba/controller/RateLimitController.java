package com.cloud.scalibaba.controller;

import cn.hutool.core.lang.UUID;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.cloud.scalibaba.myhandler.CustomerBlockHandler;
import com.cloud.springcloud.entities.CommonResult;
import com.cloud.springcloud.entities.Payment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lisw
 * @create 2020/11/9
 */
@RestController
public class RateLimitController {

    @GetMapping("/byResource")
    @SentinelResource(value = "byResource",blockHandler = "handlerException")
    public CommonResult byResource(){
        return new CommonResult(200, "按资源名称限流测试成功",
                new Payment(2020L, UUID.randomUUID().toString()));
    }

    public CommonResult handlerException(BlockException e){
        return new CommonResult(100, e.getMessage() + ",按资源名称限流测试失败");
    }

    @GetMapping("/customerBlockHandler")
    @SentinelResource(value = "customerBlockHandler",
            blockHandlerClass = CustomerBlockHandler.class,
            blockHandler = "handlerException"
        )
    public CommonResult customerBlockHandler(){
        return new CommonResult(200,"按客户自定义处理方法", new Payment(2021L, UUID.randomUUID().toString()));
    }
}
