package com.cloud.springcloud.dao;

import com.cloud.springcloud.entities.Payment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author lisw
 * @create 2020/10/16
 */
@Mapper
public interface PaymentDao {
    /**
     * 创建一个支付记录
     * @param payment
     * @return
     */
    public int create(Payment payment);

    /**
     * 通过id 获取一条支付记录
     * @param id
     * @return
     */
    public Payment getPaymentById(@Param("id") Long id);
}
