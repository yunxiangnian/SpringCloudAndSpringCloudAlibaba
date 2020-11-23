package com.cloud.scalibaba.service;

import java.math.BigDecimal;

/**
 * @author lisw
 * @create 2020/11/18
 */
public interface AccountService {

    /**
     * 执行修改业务
     * @param userId
     * @param money
     */
    void decrease(Long userId, BigDecimal money);
}
