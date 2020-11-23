package com.cloud.springcloud.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lisw
 * @create 2020/10/27
 */
@Configuration
public class GatewayConfig {

    /**
     * 当你访问 http:localhost:9527/guonei  就会自动跳转到 http://news.baidu.com/guonei
     * @param routeLocatorBuilder
     * @return
     */
    @Bean
    public RouteLocator customerRouteLocator(RouteLocatorBuilder routeLocatorBuilder){
        RouteLocatorBuilder.Builder routes = routeLocatorBuilder.routes();
        routes.route("gateway_route",
                r -> r.path("/guonei").
                uri("http://news.baidu.com/guonei")).build();
        return routes.build();
    }
}
