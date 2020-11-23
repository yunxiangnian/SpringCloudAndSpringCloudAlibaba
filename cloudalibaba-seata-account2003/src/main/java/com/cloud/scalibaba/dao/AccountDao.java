package com.cloud.scalibaba.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * @author lisw
 * @create 2020/11/18
 */
@Mapper
public interface AccountDao {

    /**
     * 修改账户余额
     * @param userId 账户id
     * @param money 金额
     */
    void decrease(@Param("userId")Long userId,@Param("money") BigDecimal money);

}
