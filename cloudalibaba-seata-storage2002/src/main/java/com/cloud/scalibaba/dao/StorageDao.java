package com.cloud.scalibaba.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author lisw
 * @create 2020/11/17
 */
@Mapper
public interface StorageDao {
    /**
     * 更新库存
     * @param productId
     * @param count
     */
    void editStorage(@Param("productId")Long productId,@Param("count")Integer count);
}
