package com.cloud.springcloud.lb;

import org.springframework.cloud.client.ServiceInstance;

import java.util.List;

/**
 * @author lisw
 * @create 2020/10/21
 */
public interface LoadBalancer {
    ServiceInstance instances(List<ServiceInstance> instances);
}
