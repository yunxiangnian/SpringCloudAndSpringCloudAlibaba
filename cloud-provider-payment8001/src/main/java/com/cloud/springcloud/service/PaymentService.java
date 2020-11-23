package com.cloud.springcloud.service;

import com.cloud.springcloud.entities.Payment;
import org.apache.ibatis.annotations.Param;

/**
 * @author lisw
 * @create 2020/10/17
 */
public interface PaymentService {
    /**
     * 创建一个支付记录
     * @param payment
     * @return
     */
    int create(Payment payment);


    /**
     * 通过id 获取一条支付记录
     * @param id
     * @return
     */
    Payment getPaymentById(@Param("id") Long id);
}
