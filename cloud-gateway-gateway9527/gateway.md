Zuul官网：github.com/Netflix/zuul/wiki

GateWay:   cloud.spring.io/sring-cloud-static/spring-cloud-gateway



1. Gateway介绍：

   Gateway 是在Spring生态系统之上构建的API网关服务，基于Spring 5,Spring Boot 2 和 Project Reactor等技术。

   Gateway旨在提供一种简单而有效的方式来对API进行路由，以及提供一些强大的过滤器功能，例如：熔断、限流、重试等。

SpringCloud Gateway使用Webflux中的reactor-netty响应式编程组件，底层使用了Netty通讯框架。 

SpringCloud Gateway的目标提供统一的路由方式且基于Filter链的方式提供了网关基本的功能。

2. 为什么要选择Gateway 

   

   2.1  Zuul不更新

   ​		1）Zuul1.0  已进入了维护阶段，而且Gateway是SpringCloud团队研发的，值得依赖，而且很多功能Zuul都没有，用起来也非常的简单便捷。

   ​		2） Gateway是基于 异步非阻塞模型上进行开发的，性能方面不需要担心。虽然Netflix早就发布了最新的Zuul2.x ，但Spring Cloud貌似没有整合计划。而且Netflix相关组件都宣布进入维护期：不知前景如何。

   多方面综合考虑Gateway是很理想的网关选择。

   

   2.2  Spring Cloud Gateway 具有如下特性：

    	 1） 基于Spring Framework 5,Project Reactor 和 Spring Boot 2.0 进行构建

   ​	  2） 动态路由： 能够匹配任何请求属性

    	 3） 可以对路由指定Predicate(断言) 和 Filter（过滤器）

     	4） 集成Hystrix断路器功能

     	5） 集成Spring Cloud 服务发现功能

     	6） 易于编写的Predicate(断言) 和 Filter（过滤器）

     	7） 请求限流功能 

   ​	  8） 支持路径重写

   

   2.3  Spring Cloud Gateway 与Zuul 的区别 

   在Spring cloud Finchley 正式版之前，Spring Cloud 推荐的网关的是Netflix提供的Zuul

   ​	1） Zuul  1.x 是一个基于阻塞I/O的API Gateway

   ​	2） Zuul  1.x 基于Servlet 2.5 使用阻塞架构它不支持任何长连接（如 WebSocket) Zuul的设计模式和Nginx较像，每次I/O操作都是从工作线程中选择一个执行，请求线程被阻塞到工作线程完成，但是差别是Nginx用C++实现，Zuul用Java实现，而JVM本身会有第一次加载较慢的情况，使得Zuul的性能相对较差。

   ​	3） Zuul 2.x 理念更先进，想基于Netty非阻塞和支持长连接，但是Spring Cloud 目前还没有整合。 Zuul 2.x 的性能较Zuul 1.x有较大提升。在性能方面，根据官方提供的基准测试，Spring Cloud Gateway 的 RPS（每秒请求数）是Zuul 的 1.6倍。

   ​	4） Spring Cloud Gateway 建立 在Spring Framework 5 、Project Reactor 和 Spring Boot 2 之上，问个问题非阻塞API.

   ​	5） Spring Cloud Gateway 还支持WebSocket，并且与Spring 紧密集成拥有更好的开发体验

   

   2.4  Zuul 用的是传统的servlet模型，gateway 使用的是异步非阻塞的netty ，性能更高

   

3. Gateway 三大核心理念

   1）Route(路由)

   ​	路由是构建网关的基本模块，它由ID，目标URI，一系列的断言和过滤器组成，如果为断言为true则匹配该路由

   2）Predicate（断言）

   参考的是Java8的java.util.function.Predicate ,开发人员可以匹配HTTP请求中的所有内容（例如请求头或请求参数），如果请求与断言相匹配则进行路由

   3）Filter（过滤器）
   
   ​	指的是Spring框架中GatewayFilter的实例，使用过滤器，可以在请求被路由前或者之后对请求进行修改。

总结：web请求，通过一些匹配条件，定位到真正的服务节点。并在这个转发过程的前后，进行一些精细化控制。predicate就是我们的匹配条件;而filter，就可以理解为一个无所不能的拦截器。有了这两个元素，再加上目标uri,就可以实现一个具体的路由了。

4.  Gateway执行过程 

   客户端向Spring Cloud Gateway发出请求。然后在Gateway Handler Mapping中找到与请求相匹配的路由，将其发送到Gateway Web Handler.

   Handler 再通过指定的过滤器链来将请求发送到我们实际的服务执行业务逻辑，然后返回。

   过滤器之间用虚线分开是因为过滤器可能会在发送代理请求之前（"pre") 或之后（“post")执行业务逻辑。

   Filter在”pre" 类型的过滤器可以做参数校验、权限检验、流量监控、日志输出、协议转换等。

   在“post"类型的过滤器中可以做响应内容、响应头的修改，日志的输出，流量监控等有着非常重要的作用。

5. 网关路由两种配置方式 

   1. yaml文件配置

      ```yaml
      spring:
        application:
          name: cloud-gateway
        cloud:
          gateway:
            routes:
              - id: payment_routh #payment_route #路由的ID，没有固定规则但要求唯一，建议配合服务名
                uri: http://localhost:8001 #匹配后提供服务的路由地址
                predicates:
                - Path=/payment/get/**  #断言，路径相匹配的进行路由
              - id: payment_routh2  #payment_route
                uri: http://localhost:8001
                predicates:
                  - Path=/payment/lb/**
      ```

   2. 代码配置

      ```java
      public RouteLocator customRouteLocator(RouteLocatorBuilder routeLocatorBuilder){
          RouteLocatorBuilder.Builder routes = routeLocatorBuilder.routes();
          // 构建路由：
          routes.route("path_route_atguigu1", //映射id
                  r -> r.path("/guonei"). // 映射是predicates地址,就访问地址
                          uri("http://news.baidu.com/guonei"))  // 映射的是实际地址
                  .build();
          return routes.build();
      }
      ```



6. curl 访问（用于测试）：

   不带cookie : curl http://localhost:9527/payment/lb   此种表示发送get请求

   带cookie: curl http://localhost:9527/payment/lb --cookie "username=zzyy"

   带请求头访问：curl http://localhost:9527/payment/lb -H "X-Request_Id:123"

若返回中文乱码，解决办法参考 ： https://blog.csdn.net/leedee/atricle/details/82685636 [pom.xml](pom.xml) 



![一般企业技术架构](F:\Project\SpringCloud\SpringCloud\cloud-gateway-gateway9527\一般企业技术架构.png)