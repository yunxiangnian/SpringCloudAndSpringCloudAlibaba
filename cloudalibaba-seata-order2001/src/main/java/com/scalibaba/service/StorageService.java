package com.scalibaba.service;

import com.scalibaba.domain.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author lisw
 * @create 2020/11/17
 */
@FeignClient(value = "seata-storage-service")
public interface StorageService {
    /**
     * 调用订单微服务，进行对商品的扣减
     * @param productId
     * @param count
     * @return
     */
    @PostMapping(value = "/storage/decrease")
    CommonResult decrease(@RequestParam("productId") Long productId, @RequestParam("count")Integer count);
}
