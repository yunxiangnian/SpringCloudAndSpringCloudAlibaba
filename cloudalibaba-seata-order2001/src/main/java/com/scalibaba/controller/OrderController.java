package com.scalibaba.controller;

import com.scalibaba.domain.CommonResult;
import com.scalibaba.domain.Order;
import com.scalibaba.service.OrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author lisw
 * @create 2020/11/17
 */
@RestController
public class OrderController {

    @Resource
    private OrderService orderService;

    /**
     * 之所以用 GetMapping 是因为浏览器无法发送post请求，所以用OpenFeign底层实现POST
     * @param order
     * @return
     */
    @GetMapping("/order/create")
    public CommonResult create(Order order){
        orderService.create(order);
        return new CommonResult(200, "创建订单成功..");
    }
}
