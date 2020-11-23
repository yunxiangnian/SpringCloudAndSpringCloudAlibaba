package com.cloud.scalibaba.service.impl;

import com.cloud.scalibaba.dao.StorageDao;
import com.cloud.scalibaba.service.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author lisw
 * @create 2020/11/17
 */
@Service
@Slf4j
public class StorageServiceImpl implements StorageService {
    private static final Logger LOGGER = LoggerFactory.getLogger(StorageServiceImpl.class);

    @Resource
    private StorageDao storageDao;

    @Override
    public void editStorage(Long productId, Integer count) {
        LOGGER.info("修改库存开始....");
        storageDao.editStorage(productId, count);
        LOGGER.info("修改库存结束...");
    }
}
