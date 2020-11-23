package com.cloud.scalibaba.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * 流控控制器
 * @author lisw
 * @create 2020/11/5
 */
@Controller
public class FlowLimitController {

    @GetMapping("/testA")
    @ResponseBody
    public String testA(){
        return "---------testA";
    }

    @GetMapping("/testB")
    public String testB(){
        try {
            TimeUnit.SECONDS.sleep(1);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "forward:/testA";
    }

    @GetMapping("/testC")
    public String testC(){
        return "forward:/testA";
    }

    @GetMapping("/testHotKey")
    @ResponseBody
    @SentinelResource(value = "testHotKey",blockHandler = "deal_testHotKey")
    public String testHotKey(@RequestParam(value = "p1",required = false) String p1,
                             @RequestParam(value = "p2") String p2){
        return "----testHotKey";
    }

    public String deal_testHotKey(String p1, String p2, BlockException e){
        return "-----testHotKey_SentinelResource";
    }
}
