package com.cloud.scalibaba.service;

/**
 * @author lisw
 * @create 2020/11/17
 */
public interface StorageService {

    /**
     * 修改库存
     * @param productId
     * @param count
     */
    void editStorage(Long productId, Integer count);
}
