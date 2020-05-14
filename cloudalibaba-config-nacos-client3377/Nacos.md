1. Nacos 简介

Nacos(前四个字母分别为Naming和Configuration的前两个字母，最后的s为Service)

Nacos 是一个更易于构建云原生应用的动态服务发现、配置管理和服务管理平台

Dynamic Naming and Configuration Servie 

Nacos 就是注册中心 +配置中心的组合 

Nacos = Eureka + Config + Bus

2.  安装

   官网下载  .zip文件

   解压进入到bin目录 ，打开cmd ,输入startup.cmd启动 ，

   使用http://localhost:8848/nacos 进入后台查看，出现登录界面，默认用户名密码均为nacos

   1.2.0 版本以后可以不用输入账号密码

   ![复制以新端口号启动](F:\Project\SpringCloud\SpringCloud\cloudalibaba-config-nacos-client3377\复制以新端口号启动.png)

**Nacos 服务发现**

1. Nacos 支持AP 和CP的切换

C 是所有节点在同一时间看到的数据是一致的;而A的定义是所有的请求都会收到响应

如何选择？

一般来说，如果不需要存储服务级别的信息且服务实例是通过nacos-client注册，并能够保持心跳上报，那么就可以选择AP，当前主流的服务如spring cloud 和 Dubbo服务，都适用于AP模式，AP模式为了服务的可能性而减弱了一致性，因此AP模式下只支持注册临时实例。

如果需要在服务级别编辑或者存储配置信息，那么CP是必须，K8S 服务和DNS服务都适用于CP模式，CP模式下则支持注册持久化实例，此时是以Raft协议为集群运行模式，该模式下注册实例之前必须先注册服务，如果服务不存在，则会返回错误。



**Nacos 配置中心**

Nacos 同 Springcloud- config 一样，在项目初始化时，要保证先从配置中心进行配置摘取，摘取配置之后，才能保证项目的正常启动。

springboot中配置文件的加载是存在优先级顺序的，bootstrap优先级高于application 



配置分类管理:

![nacos分类](F:\Project\SpringCloud\SpringCloud\cloudalibaba-config-nacos-client3377\nacos分类.png)

默认情况 ： Namespace = public ，Group = DEFAULT_GROUP,默认Cluster 是DEFAULT 

Nacos 默认的命名空间是public,Namespace 主要用来实现隔离，

比方说，现在有三个环境 ： 开发，测试，生产环境 ，我们就可以创建三个Namespace，不同的Namespace之间是隔离的。

Group 默认是 DEFAULT_GROUP,Group可以把不同的微服务划分到同一个分组里面去



Service 就是微服务，一个Service可以包含多个cluster(集群),Nacos 默认Cluster是DEFAULT ，Cluster是对指定微服务的一个虚拟划分。

比方说为了容灾，将Service微服务分别部署在了杭州机房和广州机房，

这时就可以给杭州机房的Service微服务起一个集群名称（HZ)，给广州机房的Service微服务起一个集群名称（GZ),还可以尽量让同一个机房的微服务互相调用，以提升性能。

最后是Instance,就是微服务的实例 。



Config :

DataID方案：指定spring.profile.active和配置文件的DataID来使不同环境下读取不同的配置

默认空间+默认分组+新建dev和test两个DataID

group方案： 默认为default_group组

Namespace方案：

​	新建空间，按照域名配置填写，yml 新增配置namespace.



**Nacos集群和持久化配置**

