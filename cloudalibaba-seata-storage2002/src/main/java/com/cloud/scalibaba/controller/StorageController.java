package com.cloud.scalibaba.controller;

import com.cloud.scalibaba.service.StorageService;
import com.cloud.springcloud.entities.CommonResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sun.plugin2.main.client.PrintBandDescriptor;

import javax.annotation.Resource;

/**
 * @author lisw
 * @create 2020/11/17
 */
@RestController
public class StorageController {

    @Resource
    private StorageService storageService;

    @PostMapping("/storage/decrease")
    public CommonResult decrease(@RequestParam("productId") Long productId,@RequestParam("count")Integer count){
        storageService.editStorage(productId, count);
        return new CommonResult(200, "更新成功");
    }

}
