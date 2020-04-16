官网文档地址:

https://cloud.spring.io/spring-cloud-static/Hoxton.SR1/reference/htmlsingle/#spring-cloud-openfeign

源码地址：https://github.com/spring-cloud/spring-cloud-openfeign

1. Feign简介 

Feign 是一个声明式WebService客户端。使用Feign能让编写Web Service客户端更加简单。

它的使用方法是定义一个服务接口然后在上面添加注解。Feign也支持可拔插式的编码器和解码器。Spring Cloud对Feign进行了封装，使其支持了Spring MVC标准注解和HttpMessageConverts。Feign可以与Eureka和Ribbon组合使用以支持负载均衡

2. Feign 旨在使编写Java Http 客户端变得更容易。

Ribbon+RestTemplate，利用RestTemplate对http请求的封装处理，形成了一套模板化的调用方法。但是在实际开发中**，由于对服务依赖的调用可能不止一处，往往一个接口会被多处调用，所以通常都会针对每个微服务自行封装一些客户端类来包装这些依赖服务的调用**。所以Feign在此基础上做了进一步封装，由他来帮助我们定义和实现依赖服务接口的定义。在**Feign**的实现下，我们**只需创建一个接口并使用注解的方式来配置它（以前是Dao接口上面标注Mapper注解，现在是一个微服务接口上面标注一个Feign注解即可），即可完成对服务提供方的接口绑定，简化了使用Spring cloud Ribbon时，自动封装服务调用客户端的开发量**。

Feign 集成了Ribbon 

**利用Ribbon维护了Payment的服务列表信息，并且通过轮询实现了客户端的负载均衡**。而与Ribbon不同的是，通过**feign只需要定义服务绑定接口且以声明式的方法**，优雅而简单的实现了服务调用.



3. Feign :  服务接口绑定器。接口中定义的方法，与服务端方法一致 ,用在消费侧
4. ![Feign与OpenFeign的区别](F:\Project\SpringCloud\SpringCloud\cloud-consumer-feign-order80\Feign与OpenFeign的区别.png)



5.  Feign 日志打印：

   NONE: 默认，不显示任何日志

   BASIC: 仅记录请求方法，URL、响应状态码以及执行时间

   HEADERS: 除了BASIC中定义的信息之外，还有请求和响应的头信息

   FULL：除了HEADERS中定义的信息之外，还有请求和响应的正文及元数据

```java
@Configuration
public class FeignConfig {

    @Bean
    Logger.Level feignLoggerLevel(){
        return Logger.Level.FULL;
    }
}
```

```yml
#日志配置
logging:
  level:
    # feign日志以什么级别监控哪个接口
    com.atguigu.springcloud.service.PaymentFeignService: debug
```