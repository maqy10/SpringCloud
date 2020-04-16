###### 1.Spring Cloud Ribbon 是基于 Netflix Ribbon实现的一套**客户端 负载均衡**的工具。**结合RestTemplate实现调用** ,可以和其他所需请求的客户端结合使用，和eureka结合只是其中的一个实例。

###### 2.**Ribbon**

是Netflix发布的开源项目，主要功能是提供客户端的软件负载均衡算法和服务调用。Ribbon客户端组件提供一系列完善的配置项如连接超时，重试等。简单的说，就是在配置文件中列出Load Balancer后面所有的机器，Ribbon会自动的帮助你基于某种规则(简单轮询，随机连接等)去连接这些机器。我们很容易使用Ribbon实现自定义的负载均衡算法



###### 3.**负载均衡**：

将用户的请求平摊的分配到多个服务上，从而达到系统的HA(高可用)，常见的负载均衡有软件Nginx，LVS,硬件F5等。

**Ribbon：本地负载均衡，进程内LB，**在调用微服务接口时候，会在注册中心上获取注册信息服务列表之后缓存到 JVM本地，从而在本地实现RPC远程服务调用技术。

**Nginx ：服务端负载均衡，集中式LB,**客户端所有请求都会交给Nginx，然后由nginx实现转发请求。

**集中式LB:** 即在**服务的消费方和提供方之间使用独立的LB设施**（可以是硬件，如F5，也可以是软件，如Nginx),由该**设施负责把访问请求通过某种策略转发至服务的提供方**。

**进程内LB：将LB逻辑集成到消费方，**消费方**从服务注册中心获知有哪些地址可用**，然后自己再从这些地址中选择出一个合适的服务器。

**Ribbon就属性进程内LB**，**它只是一个类库，集成于消费方进程，消费方通过它来获取到服务提供方的地址。**



###### **4.Ribbon的工作步骤：**

1）先选择EurekaServer，它优先选择在同一个区域内负载较少的server.

2）根据用户指定的策略，在从server取到的服务注册列表中选择一个地址。

其中Ribbon提供了多种策略：比如轮询，随机和根据响应时间加权。



###### **5.Ribbon核心组件IRule**

根据特定算法中从服务列表中选择一个要访问的服务 以下算法均是IRule接口的实现



![ribbon轮询策略](F:\Project\SpringCloud\SpringCloud\cloud-consumer-order80\ribbon轮询策略.png)



替换默认的轮询算法 

新建package  此包中的类不被 SpringBootApplication 注解中的ComponentScan扫描到。

在此包下，新建规则类

```java
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
```

在启动类，添加注解

```java
// 不使用默认轮询，使用自定义负载均衡算法
@RibbonClient(name = "CLOUD-PAYMENT-SERVICE",configuration = MyselfRule.class)
```



###### **6.负载均衡算法**

**rest接口第几次请求数%服务器集群总数量= 实际调用服务器位置下载，每次服务重启动后rest接口计数从1开始**

```java
// 获取服务实例
List<ServiceInstance> instances  = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
```



```java
 Server server = null;
    int count = 0;
    while (server == null && count++ < 10) {
        // 获取正常运行的服务器实例
        List<Server> reachableServers = lb.getReachableServers();
        // 获取所有服务器实例
        List<Server> allServers = lb.getAllServers();
        int upCount = reachableServers.size();
        int serverCount = allServers.size();

        if ((upCount == 0) || (serverCount == 0)) {
            log.warn("No up servers available from load balancer: " + lb);
            return null;
        }

        int nextServerIndex = incrementAndGetModulo(serverCount);
        server = allServers.get(nextServerIndex);

        if (server == null) {
            /* Transient. */
            Thread.yield();
            continue;
        }

        if (server.isAlive() && (server.isReadyToServe())) {
            return (server);
        }

        // Next.
        server = null;
    }

    if (count >= 10) {
        log.warn("No available alive servers after 10 tries from load balancer: "
                + lb);
    }
    return server;
}


//  https://www.cnblogs.com/cxuanBlog/p/11679883.html  自旋锁参考链接
    private int incrementAndGetModulo(int modulo) {
        // 自旋锁
        for (;;) {
            int current = nextServerCyclicCounter.get();
            int next = (current + 1) % modulo;
            // compareAndSet() 判断内存中的值与current是否一致，一致则用next的值替换
            if (nextServerCyclicCounter.compareAndSet(current, next))
                return next;
        }
    }


  public final boolean compareAndSet(int expect, int update) {
        return unsafe.compareAndSwapInt(this, valueOffset, expect, update);
    }

```



