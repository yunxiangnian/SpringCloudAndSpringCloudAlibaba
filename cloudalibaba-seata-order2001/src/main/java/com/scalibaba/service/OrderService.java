package com.scalibaba.service;


import com.scalibaba.domain.Order;

/**
 * @author lisw
 * @create 2020/11/17
 */
public interface OrderService {
    /**
     * 创建订单
     * @param order
     */
    void create(Order order);
}
