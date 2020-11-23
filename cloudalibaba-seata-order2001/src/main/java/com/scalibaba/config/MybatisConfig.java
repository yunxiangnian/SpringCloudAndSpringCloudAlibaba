package com.scalibaba.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author lisw
 * @create 2020/11/17
 */
@Configuration
@MapperScan({"com.scalibaba.dao"})
public class MybatisConfig {

}
