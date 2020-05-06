1.Spring Cloud Stream 屏蔽底层消息中间件的差异，降低切换成本，统一消息的编程模型。

​	应用程序通过inputs 或者outputs 来与Spring Cloud Stream 中 binder对象交互，通过我们配置来binding(绑定)，而Spring Cloud Stream 交互就可以方便问个问题消息驱动的方式。

​	通过使用Spring Integration 来连接消息代理中间件以实现消息事件驱动，Spring Cloud Stream 为一些供应商的消息中间件产品提供了个性化的自动化的配置实现，引用了发布-订阅、消费组、分区的三个核心概念。



目前仅支持RabbitMQ，Kafka.

Spring Cloud Stream 是用于构建与共享消息传递系统连接的高度可伸缩的事件驱动微服务框架，该框架提供了一个灵活的编程模型，它建立在已经建立和熟悉的Spring熟语和最佳实践上，包括支持持久化的发布/订阅、消费组以及消息分区这三个核心概念.



2. 标准MQ

   1. 生产者/消费者之间靠消息媒介传递信息内容    Message

   2. 消息必须走特定的通道    消息通道   MessageChannel 

   3. 消息通道里的消息如何被消费呢，谁负责收发处理?

      消息通道MessageChannel 的子接口SubscribableChannel,由MessageHandler消息处理器所订阅

	 4. Spring Cloud Stream 遵循发布-请阅模式，Topic主题进行广播，在RabbitMQ就是Exchange，在 Kafka中就是Topic



3. Spring cloud stream 

   通过定义绑定器Binder作为中间层，完美地实现了应用程序与消息中间件细节之间的隔离

   通过向应用程序暴露统一的Channel通道，使用应用程序不需要再考虑各种不同的消息中间件实现  

   Binder : INPUT消费  OUTPUT生产



4. 重复消费：

   导致原因：默认分组group是不同的，组流水号不一样，被认为不同组，可以消费。

   解决：自定义配置分组，自定义配置分为同一个组，

   原理 ： 微服务应用放置于同一个group中，就能够保证消息只会被其中一个应用消费一次。不同的组是可以消费的，同一个组内会发生竞争关系，只有其中一个可以消费

   同一个组的多个微服务实例，每次只会有一个拿到

5.  消息丢失

   若没有group分组，服务在重新启动之后，不会去消费未被的消息

