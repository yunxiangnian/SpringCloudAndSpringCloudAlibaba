package com.scalibaba.dao;

import com.scalibaba.domain.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author lisw
 * @create 2020/11/17
 */
@Mapper
public interface OrderDao {
    /**
     * 1、新建订单
     * @param order
     */
    void create(Order order);

    /**
     * 2、修改订单状态：从0-1
     * @param userId
     * @param status
     * @return
     */
    int update(@Param("userId")Long userId,@Param("status")Integer status);
}
