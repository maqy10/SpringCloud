zookeeper centos7 下载地址:

wget https://mirrors.tuna.tsinghua.edu.cn/apache/zookeeper/zookeeper-3.5.7/apache-zookeeper-3.5.7-bin.tar.gz

**3.5.5及以上版本  需要下载带有-bin的包，否则会报错，找不到主类**

tar -zxvf  apache-zookeeper-3.5.7-bin.tar.gz

**在 bin 目录下，使用 ./zkServer.sh start 启动**

  **安装之后，关闭防火墙，systemctl stop firewalld 否则无法通信**

使用 ./zkCli.sh  启动Eureka 客户端测试是否能够连接上 

**ls / 可以显示根目录下的服务**

get 会获得一个json串 ,可以查看其详细信息

![](F:\教程\zookeeperClientOpera.png)



get到的详细信息

```json
{
  "name": "cloud-payment-service",
  "id": "49991c28-d5da-48e2-9937-6a1d05a4a7a3",
  "address": "DESKTOP-7SR0918",
  "port": 8004,
  "sslPort": null,
  "payload": {
    "@class": "org.springframework.cloud.zookeeper.discovery.ZookeeperInstance",
    "id": "application-1",
    "name": "cloud-payment-service",
    "metadata": {}
  },
  "registrationTimeUTC": 1585558253253,
  "serviceType": "DYNAMIC",
  "uriSpec": {
    "parts": [
      {
        "value": "scheme",
        "variable": true
      },
      {
        "value": "://",
        "variable": false
      },
      {
        "value": "address",
        "variable": true
      },
      {
        "value": ":",
        "variable": false
      },
      {
        "value": "port",
        "variable": true
      }
    ]
  }
}
```



#### **解决程序启动后，jar包冲突，引入的spring-cloud-starter-zookeeper-discovery中自带 zookeeper3.5.3-beta** 

```maven
<!-- SpringBoot 整合zookeeper 客户端 -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-zookeeper-discovery</artifactId>
    <!-- 排除自带的zookeeper 3.5.3-->
    <exclusions>
        <exclusion>
            <groupId>org.apache.zookeeper</groupId>
            <artifactId>zookeeper</artifactId>
        </exclusion>
    </exclusions>
</dependency>
<!-- 添加zookeep版本3.5.7   与服务器一致-->
<dependency>
    <groupId>org.apache.zookeeper</groupId>
    <artifactId>zookeeper</artifactId>
    <version>3.5.7</version>
</dependency>
```



zookeeper 为临时节点 ，同eureka 一段时间不再发送心跳包后，会删除掉该服务

zookeeper 是CAP原则中AP原则

 zookeeper集群配置原理同eureka

