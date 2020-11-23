package com.cloud.springcloud.controller;

import com.cloud.springcloud.entities.CommonResult;
import com.cloud.springcloud.entities.Payment;
import com.cloud.springcloud.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author lisw
 * @create 2020/10/17
 *
 * @Slf4j  : 输出一些日志，集成了 lombok
 */
@RestController
@Slf4j
public class PaymentController {

    @Resource
    private PaymentService paymentService;

    /**
     * @Value 注解可以直接读取yml配置文件的值
     */
    @Value("${server.port}")
    private String serverPort;


    @Resource
    private DiscoveryClient discoveryClient;

    @PostMapping(value = "/payment/create")
    public CommonResult create(@RequestBody Payment payment){
        int result = paymentService.create(payment);
        log.info("*******插入结果:" + result + " 端口号为: " + serverPort);
        if(result > 0){
            return new CommonResult(200,"插入成功",result);
        }else {
            return new CommonResult(100,"插入失败",null);
        }
    }

    @GetMapping(value = "/payment/get/{id}")
    public CommonResult create(@PathVariable("id")Long id){
        Payment payment = paymentService.getPaymentById(id);
        log.info("*******获取结果:" + payment + " 端口号为: " + serverPort);
        if(payment != null){
            return new CommonResult(200,"查询成功",payment);
        }else {
            return new CommonResult(100,"没有对应记录，查询失败 id :" + id,null);
        }
    }

    /**
     * 测试 discoveryClient获取的信息是什么
     */
    @GetMapping("/payment/disc")
    public Object discovery(){
        //获取已经注册在Eureka上的服务
        List<String> services = discoveryClient.getServices();
        for (String service : services) {
            log.info("服务名称:" + service);
        }

        //获取某个特定服务下的所有信息
        List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
        for (ServiceInstance instance : instances) {
            log.info(instance.getHost() +"\t" + instance.getPort() + "\t" + instance.getUri());
        }
        return discoveryClient;
    }

    @GetMapping("payment/lb")
    public String getlb(){
        return serverPort;
    }

    @GetMapping("/timeout/get")
    public String testTimeout(){
        try {
            TimeUnit.SECONDS.sleep(3);
        }catch (Exception e){
            e.printStackTrace();
        }
        return serverPort;
    }
}
