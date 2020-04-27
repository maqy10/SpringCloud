package com.atguigu.springcloud.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GateWayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder routeLocatorBuilder){
        RouteLocatorBuilder.Builder routes = routeLocatorBuilder.routes();
        // 构建路由：
        routes.route("path_route_atguigu1", //映射id
                r -> r.path("/guonei"). // 映射是predicates地址,就访问地址
                        uri("http://news.baidu.com/guonei"))  // 映射的是实际地址
                .build();
        return routes.build();
    }

    @Bean
    public RouteLocator customRouteLocator1(RouteLocatorBuilder routeLocatorBuilder){
        RouteLocatorBuilder.Builder routes = routeLocatorBuilder.routes();
        // 构建路由：
        routes.route("path_route_atguigu2", //映射id
                r -> r.path("/guoji"). // 映射是predicates地址,就访问地址
                        uri("http://news.baidu.com/guoji"))  // 映射的是实际地址
                .build();
        return routes.build();
    }
}
