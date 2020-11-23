package com.cloud.scalibaba.myhandler;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.cloud.springcloud.entities.CommonResult;
import com.cloud.springcloud.entities.Payment;

import java.util.UUID;

/**
 * @author lisw
 * @create 2020/11/10
 */
public class CustomerBlockHandler {

    public static CommonResult handlerException(BlockException e){
        return new CommonResult(100, e.getClass() + "自定义方法异常 global",new Payment
                (2021L, UUID.randomUUID().toString()));
    }

    public static CommonResult handlerException2(BlockException e){
        return new CommonResult(100, e.getClass() + "自定义方法异常2 global",new Payment
                (2021L, UUID.randomUUID().toString()));
    }
}
