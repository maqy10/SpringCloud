##### Eureka 自我保护机制

1. 某时刻某一个微服务不可用了，Eureka不会立刻清理，依旧会对该微服务的信息进行保存

2. 属于CAP原则里的AP分支

   C：数据一致性
   A:  高可用
   P: 分区容错性

3.  默认情况下，**如果EurekaServer在一定时间内没有接收到某个微服务实例的心跳，EurekaServer 将会注销该实例（默认90秒）**。**但是当网络分区故障发生（延时、卡顿、拥挤）时，微服务与EurekaServer之间无法正常通信，**以上行为可能变得非常危险， 微服务本身是健康的，此时不应该这个微服务。**Eureka通过“自我保护模式” 来解决这个问题，当EurekaServer节点在短时间内丢失过多客户端时（可能发生了网络分区故障）,那么这个节点就会进入自我保护模式.**

4. 使用自我保护机制，让微服务集群更加健壮稳定

5. CAP原则参考链接

https://blog.csdn.net/w372426096/article/details/80437198