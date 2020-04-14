##### Consul    官网:https://www.consul.io/intro/index.html

下载：https://www.consul.io/downloads.html

怎么用(中文教程)：https://www.springcloud.cc/spring-cloud-consul.html

###### Consul 是一套开源的分布式服务发现和配置管理系统。由HashiCorp公司用Go语言开发

1. 提供了微服务系统中的服务治理、配置中心、控制总线等功能。这些功能中的每一个都可以根据需要单独使用，也可以一起使用以构建全方位的服务网络，总之consul提供了一种完整的服务网络解决方案。

2. 它具有很多优点。包括基于raft协议，比较简洁;支持健康检查，同时支持HTTP和DNS协议，支持跨数据中心的WAN集群，提供图形界面，跨平台，支持Linux、Mac、Windows。

3. 具备特性

   1. 服务发现

      提供HTTP和DNS两种发现方式

   2. 健康检查

      支持多种方式，HTTP、TCP、Docker、Shell脚本定制化

   3. KV存储

      Key、Value的存储方式

   4. 多数据中心

      Consul支持多数据中心

   5. 可视化web界面

------

安装： https://learn.hashicorp.com/consul/getting-started/install.html

下载完成后，只有一个consul.exe文件

将此consul.exe的路径 ，添加到环境变量配置的Path变量中去，即可

在目录中，使用cmd 打开命令窗口，输入 consul --version  即可查看到版本号 



使用 consul agent -dev  开发模式启动

consul 可视化界面地址： http://localhost:8500

------

开启服务发现 **@EnableDiscoveryClient**注解 ，适用于consul 和 zookeeper

一个系统 不可能同时满足CAP ，只能满足CP 或 AP ，CAP 理论关注粒度是数据，而不是整体

C: Consistency  强一致性

A: Availability  可用性

P: Partitiion tolerance 分区容错性  

**Eureka** 满足 AP , **Zookeeper/Consul** 满足AP 

体现 Eureka　的自我保护机制   ，服务断掉后，不会立即删掉。

zookeeper和consul 服务断掉后，立即删除该服务 



CP 架构:当网络分区出现后，为了保证一致性，就必须拒绝请求，否则无法保证一致性

违背了可用性A的要求，只满足一致性和分区容错，即CP

