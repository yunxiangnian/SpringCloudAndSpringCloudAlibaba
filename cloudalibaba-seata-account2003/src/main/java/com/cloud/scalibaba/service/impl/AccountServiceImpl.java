package com.cloud.scalibaba.service.impl;

import com.cloud.scalibaba.dao.AccountDao;
import com.cloud.scalibaba.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * @author lisw
 * @create 2020/11/18
 */
@Service
@Slf4j
public class AccountServiceImpl implements AccountService {

    @Resource
    private AccountDao accountDao;

    @Override
    public void decrease(Long userId, BigDecimal money) {
        log.info("账户扣减余额开始....");
        //模拟超时异常，全局事务回滚
        accountDao.decrease(userId, money);
        log.info("账户扣减余额结束....");
    }
}
