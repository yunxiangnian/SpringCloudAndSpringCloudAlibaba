package com.scalibaba.service;

import com.scalibaba.domain.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

/**
 * @author lisw
 * @create 2020/11/17
 */
@FeignClient(value = "seata-account-service")
public interface AccountService {

    /**
     * 订单接口调用账户微服务做扣减
     * @param userId
     * @param money
     * @return
     */
    @PostMapping("/account/decrease")
    CommonResult decrease(@RequestParam("userId") Long userId, @RequestParam("money") BigDecimal money);
}
