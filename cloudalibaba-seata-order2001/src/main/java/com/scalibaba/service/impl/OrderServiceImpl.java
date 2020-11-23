package com.scalibaba.service.impl;

import com.scalibaba.dao.OrderDao;
import com.scalibaba.domain.Order;
import com.scalibaba.service.AccountService;
import com.scalibaba.service.OrderService;
import com.scalibaba.service.StorageService;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author lisw
 * @create 2020/11/17
 */
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderDao orderDao;
    @Resource
    private StorageService storageService;
    @Resource
    private AccountService accountService;

    @Override
    @GlobalTransactional(name = "yxn-create-order",rollbackFor = Exception.class)
    public void create(Order order) {
        //1、创建订单
        log.info("创建订单...");
        orderDao.create(order);

        //2、调用库存微服务，扣减商品数量
        log.info("订单微服务开始调用库存微服务...，做扣减");
        storageService.decrease(order.getProductId(), order.getCount());
        log.info("订单微服务调用库存微服务扣减数量结束...");

        //3、调用账户微服务，扣减money
        log.info("修改账户money 开始...");
        accountService.decrease(order.getUserId(), order.getMoney());
        log.info("修改账户money 结束....");

        //4、修改订单状态，从零到1 1表示已完成
        log.info("修改订单状态开始...");
        orderDao.update(order.getUserId(),0);
        log.info("修改订单状态结束....");
        log.info("下单成功...");
    }
}
