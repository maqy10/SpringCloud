package com.atguigu.myrule;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 此配置类不可以放在ComponentScan可以扫描到的包下
 */

@Configuration
public class MyselfRule {

    @Bean
    public IRule myRule(){
        return new RandomRule();//定义为随机
    }
}
