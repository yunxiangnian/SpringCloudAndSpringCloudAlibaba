# SpringCloud

![WeChat Screenshot_20201027171859](D:\JavaStudy\JavaEE\SpringCloud\images\WeChat Screenshot_20201027171859.png)

> https://cloud.spring.io/spring-cloud-static/Hoxton.SR1/reference/htmlsingle/

## Payment

### 约定>配置>编码

### 子工程的POM文件

```xml
<dependencies>
        <!-- web 依赖 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <!-- actuator 依赖 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <!-- mybatis starter 依赖 -->
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
        </dependency>
        <!-- druid(德鲁伊) starter 依赖 -->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid-spring-boot-starter</artifactId>
            <version>1.1.10</version>
        </dependency>
        <!-- mysql -->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>
        <!-- jdbc	(在编写时，scope表情的内容一定不要为test，否则就会出现找不到类的问题) --> 
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-jdbc</artifactId>
        </dependency>
        <!-- devtools 热部署的实现 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>
        <!-- lombok  -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
        </dependency>
        <!-- spring-boot test -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
```

如何测试POST请求？

```text
 因为浏览器不太支持post请求，所以采用postman实现post自测
```

### Devtools热部署配置（发布到生产需要关闭）

1、具体模块添加jar包到工程中

```xml
<dependency>    
    <groupId>org.springframework.boot</groupId>    
    <artifactId>spring-boot-devtools</artifactId>    
    <scope>runtime</scope>    
    <optional>true</optional>
</dependency>
```

2、添加plugins文件（如果是多个module，可以添加到父工程中）

```xml
<build>
    <plugins>
      <plugin>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-maven-plugin</artifactId>
        <configuration>
          <fork>true</fork>
          <addResources>true</addResources>
        </configuration>
      </plugin>
    </plugins>
  </build>
```

3、配置idea

![QQ图片20201019112102](D:\JavaStudy\JavaEE\SpringCloud\images\QQ图片20201019112102.png)

4、shift + ctrl + alt + / 一起按，选择Reg项

![QQ截图20201019114144](D:\JavaStudy\JavaEE\SpringCloud\images\QQ截图20201019114144.png)

5、重启idea

## Consumer

### ==远程调用支付插入数据的时候要注意的问题==

```java
//Payment的插入方法(8001)
@PostMapping(value = "/payment/create")
    public CommonResult create(@RequestBody Payment payment){
        int result = paymentService.create(payment);
        log.info("*******插入结果:" + result);
        if(result > 0){
            return new CommonResult(200,"插入成功",result);
        }else {
            return new CommonResult(100,"插入失败",null);
        }
    }
//Consumer的插入方法(80)
//在这里简单的用get请求去代替post  在执行插入的数据时
//如果8001端口的插入方法的参数不加@RequestBody注解，可以插入成功，但是数据就不会传递过去，插入到数据的数据为空
@GetMapping("/consumer/payment/create")
    public CommonResult<Payment> create(Payment payment){
        return restTemplate.postForObject(PAYMENT_URL+"/payment/create",
                payment, CommonResult.class);
    }
```

## 工程重构

pom文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>missing</artifactId>
        <groupId>com.cloud.springcloud</groupId>
        <version>1.0-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>cloud-api-commons</artifactId>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>

        <!-- 这个是新添加的，之前没用到，后面会用到 -->
        <dependency>
            <groupId>cn.hutool</groupId>
            <artifactId>hutool-all</artifactId>
            <version>5.1.0</version>
        </dependency>
    </dependencies>
</project>
```

## 服务注册中心

> 如果是上面只有两个微服务，通过 RestTemplate ，是可以相互调用的，但是当微服务项目的数量增大，就需要服务注册中心。目前没有学习服务调用相关技术，使用 SpringCloud 自带的 RestTemplate 来实现RPC

### Eureka

> 官方停更不停用，以后可能用的越来越少。

POM文件的差异（1.x和2.x）

![QQ截图20201019165851](D:\JavaStudy\JavaEE\SpringCloud\images\QQ截图20201019165851.png)

####Server模块

> Server模块采用的是7001端口，pom文件的依赖

```xml
   <dependencies>
        <!-- eureka  Server -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency><!-- 引入自己定义的api通用包，可以使用Payment支付Entity -->
            <groupId>com.cloud.springcloud</groupId>
            <artifactId>cloud-api-commons</artifactId>
            <version>${project.version}</version>
        </dependency>
    </dependencies>
```

> yml文件的配置

```yml
server:
  port: 7001

# eureka的注册配置
eureka:
  instance:
    hostname: localhost  # eureka 服务器的实例名称

  client:
    # false 代表不向服务注册中心注册自己，因为它本身就是服务中心
    register-with-eureka: false
    # false 代表自己就是服务注册中心，自己的作用就是维护服务实例，并不需要去检索服务
    fetch-registry: false
    service-url:
      # 设置与 Eureka Server 交互的地址，查询服务 和 注册服务都依赖这个地址
      defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/
```

> 服务端主启动配置，如果报错就加exclude。如果不报错就不需要加

```java
// exclude ：启动时不启用 DataSource的自动配置检查
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
//表示是Eureka的服务端
@EnableEurekaServer
public class EurekaMain7001 {
    public static void main(String[] args) {
        SpringApplication.run(EurekaMain7001.class, args);
    }
}
```

####Client模块

版本差异（1.x和2.x）

![QQ截图20201019175233](D:\JavaStudy\JavaEE\SpringCloud\images\QQ截图20201019175233.png)

#####提供者

> pom配置文件

```xml
    <dependencies>
        <!--eureka-client-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
        <!-- web 依赖 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <!-- actuator 依赖 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <!-- mybatis starter 依赖 -->
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
        </dependency>
        <!-- druid(德鲁伊) starter 依赖 -->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid-spring-boot-starter</artifactId>
            <version>1.1.10</version>
        </dependency>
        <!-- mysql -->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>
        <!-- jdbc -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-jdbc</artifactId>
        </dependency>
        <!-- devtools 热部署的实现 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>
        <!-- lombok  -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <!-- spring-boot test -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency><!-- 引入自己定义的api通用包，可以使用Payment支付Entity -->
            <groupId>com.cloud.springcloud</groupId>
            <artifactId>cloud-api-commons</artifactId>
            <version>${project.version}</version>
        </dependency>
    </dependencies>
```

2、主启动类 加上注解 ： @EnableEurekaClient

3、yml 文件添加关于 Eureka 的配置：

```yml
eureka:
  client:
	# 注册进 Eureka 的服务中心
    register-with-eureka: true
    # 检索 服务中心 的其它服务
    fetch-registry: true
    service-url:
      # 设置与 Eureka Server 交互的地址
      defaultZone: http://localhost:7001/eureka/
```

==自我保护机制==

![self](D:\JavaStudy\JavaEE\SpringCloud\images\self.png)

##### 消费者

> pom文件依赖

```xml
 <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency><!-- 引入自己定义的api通用包，可以使用Payment支付Entity -->
            <groupId>com.cloud.springcloud</groupId>
            <artifactId>cloud-api-commons</artifactId>
            <version>${project.version}</version>
        </dependency>
    </dependencies>
```

> yml文件添加关于eureka的配置

```yml
spring:
  application:
    name: cloud-order-service
# eureka 配置
eureka:
  client:
    register-with-eureka: true
    fetch-registry: true
    service-url:
      defaultZone: http://localhost:7001/eureka/
```

> 主启动类 加上注解 ： @EnableEurekaClient

####Eureka集群

集群原理说明：![WeChat Screenshot_20201019182905](D:\JavaStudy\JavaEE\SpringCloud\images\WeChat Screenshot_20201019182905.png)

> Eureka集群的原则：互相注册，相互守望，一个Eureka中一定要有其他的服务（Eureka）的信息

集群7001服务yml文件配置

```yml
server:
  port: 7001

# eureka的注册配置
eureka:
  instance:
    hostname: eureka7001.com  # eureka 服务器的实例名称

  client:
    # false 代表不向服务注册中心注册自己，因为它本身就是服务中心
    register-with-eureka: false
    # false 代表自己就是服务注册中心，自己的作用就是维护服务实例，并不需要去检索服务
    fetch-registry: false
    service-url:
      # 设置与 Eureka Server 交互的地址，查询服务 和 注册服务都依赖这个地址
      defaultZone: http://eureka7002.com:7002
```

集群7002服务yml文件配置

```yml
server:
  port: 7002

#eureka 配置
eureka:
  instance:
    hostname: eureka7002.com
  client:
    register-with-eureka: false
    fetch-registry: false
    service-url:
      # 设置与 Eureka Server 交互的地址，查询服务 和 注册服务都依赖这个地址
      defaultZone: http://eureka7001.com:7001

```

8001端口微服务YML的Eureka相关配置（集群版）

```yml
# 注册到 eureka中心
eureka:
  client:
    # 注册进 Eureka 的服务中心
    register-with-eureka: true
    # 检索 服务中心 的其它服务，默认为true，单节点无所谓，集群必须设置为true才能配合ribbon使用负载均衡
    fetch-registry: true
    service-url:
      # 设置与 Eureka Server 交互的地址
      # defaultZone: http://localhost:7001/eureka/
      # 集群版配置
      defaultZone: http://eureka7001.com:7001/eureka/,http://eureka7002.com:7002/eureka/
```

#### 提供者集群

8001Controller的配置（提供者集群版）、8002与8001完全一样。只是配置文件不一样（也就是server.port不一样）

```java
package com.cloud.springcloud.controller;

import com.cloud.springcloud.entities.CommonResult;
import com.cloud.springcloud.entities.Payment;
import com.cloud.springcloud.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author lisw
 * @create 2020/10/17
 *
 * @Slf4j  : 输出一些日志，集成了 lombok
 */
@RestController
@Slf4j
public class PaymentController {

    @Resource
    private PaymentService paymentService;

    /**
     * @Value 注解可以直接读取yml配置文件的值
     */
    @Value("${server.port}")
    private String serverPort;

    @PostMapping(value = "/payment/create")
    public CommonResult create(@RequestBody Payment payment){
        int result = paymentService.create(payment);
        log.info("*******插入结果:" + result + " 端口号为: " + serverPort);
        if(result > 0){
            return new CommonResult(200,"插入成功",result);
        }else {
            return new CommonResult(100,"插入失败",null);
        }
    }

    @GetMapping(value = "/payment/get/{id}")
    public CommonResult create(@PathVariable("id")Long id){
        Payment payment = paymentService.getPaymentById(id);
        log.info("*******获取结果:" + payment + " 端口号为: " + serverPort);
        if(payment != null){
            return new CommonResult(200,"查询成功",payment);
        }else {
            return new CommonResult(100,"没有对应记录，查询失败 id :" + id,null);
        }
    }
}

```

> 消费者80端口使用@LoadBalance注解赋予RestTemplate负载均衡的能力，并且不能写死提供者地址

```java
@RestController
@Slf4j
public class OrderController {
    //private static final String PAYMENT_URL = "http://localhost:8001";
    //重点就在这里，不能写死提供者URL，要根据服务的别名从Eureka的注册中心去获取，不针对单一的服务
    private static final String PAYMENT_URL = "http://CLOUD-PAYMENT-SERVICE";
    
    @Resource
    private RestTemplate restTemplate;

    @GetMapping("/consumer/payment/create")
    public CommonResult<Payment> create(Payment payment){
        return restTemplate.postForObject(PAYMENT_URL+"/payment/create",
                payment, CommonResult.class);
    }

    @GetMapping("consumer/payment/get/{id}")
    public CommonResult<Payment> getPayment(@PathVariable("id")Long id){
        log.info("请求8001端口获取数据...记录id: " + id);
        return restTemplate.getForObject(PAYMENT_URL+"/payment/get/"+ id, CommonResult.class);
    }
}
```

实现效果

![WeChat Screenshot_20201020141538](D:\JavaStudy\JavaEE\SpringCloud\images\WeChat Screenshot_20201020141538.png)

> 对外暴露的是CLOUD-PAYMENT-SERVICE，但是此时要开启RestTemplate的负载均衡功能

在容器创建的时候就创建一个可以支持负载均衡的RestTemplate

```java
@Configuration
public class ApplicationConfig {

    /**
     * 进行远程访问Http服务的类
     * 是Spring提供的用于访问 Rest 服务的 客户端模板的工具集
     * @return
     */
    @Bean
    //赋予RestTemplate负载均衡的能力，整合的是Ribon的负载均衡机制
    @LoadBalanced
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }
}
```

#### actuator

主机名称服务的修改，达到的效果![WeChat Screenshot_20201020143509](D:\JavaStudy\JavaEE\SpringCloud\images\WeChat Screenshot_20201020143509.png)

> yml文件中的Eureka的完整配置

```yml
# 注册到 eureka中心
eureka:
  client:
    # 注册进 Eureka 的服务中心
    register-with-eureka: true
    # 检索 服务中心 的其它服务，默认为true，单节点无所谓，集群必须设置为true才能配合ribbon使用负载均衡
    fetch-registry: true
    service-url:
      # 设置与 Eureka Server 交互的地址
      # defaultZone: http://localhost:7001/eureka/
      # 集群版配置
      defaultZone: http://eureka7001.com:7001/eureka/,http://eureka7002.com:7002/eureka/
  instance:
    instance-id: payment8002
```

访问路径中有ip显示

```yml
eureka:
  client:
    # 注册进 Eureka 的服务中心
    register-with-eureka: true
    # 检索 服务中心 的其它服务，默认为true，单节点无所谓，集群必须设置为true才能配合ribbon使用负载均衡
    fetch-registry: true
    service-url:
      # 设置与 Eureka Server 交互的地址
      # defaultZone: http://localhost:7001/eureka/
      # 集群版配置
      defaultZone: http://eureka7001.com:7001/eureka/,http://eureka7002.com:7002/eureka/
  instance:
    instance-id: payment8002
    prefer-ip-address: true # 访问路径可以显示IP
```

#### 服务发现

Controller代码

> 注意是 org.springframework.cloud.client.discovery 的 DiscoveryClient

```java
@RestController
@Slf4j
public class PaymentController {

    @Resource
    private PaymentService paymentService;

    /**
     * @Value 注解可以直接读取yml配置文件的值
     */
    @Value("${server.port}")
    private String serverPort;


    //注入服务发现类
    @Resource
    private DiscoveryClient discoveryClient;

    @PostMapping(value = "/payment/create")
    public CommonResult create(@RequestBody Payment payment){
        int result = paymentService.create(payment);
        log.info("*******插入结果:" + result + " 端口号为: " + serverPort);
        if(result > 0){
            return new CommonResult(200,"插入成功",result);
        }else {
            return new CommonResult(100,"插入失败",null);
        }
    }

    @GetMapping(value = "/payment/get/{id}")
    public CommonResult create(@PathVariable("id")Long id){
        Payment payment = paymentService.getPaymentById(id);
        log.info("*******获取结果:" + payment + " 端口号为: " + serverPort);
        if(payment != null){
            return new CommonResult(200,"查询成功",payment);
        }else {
            return new CommonResult(100,"没有对应记录，查询失败 id :" + id,null);
        }
    }

    /**
     * 测试 discoveryClient获取的信息是什么
     */
    @GetMapping("/payment/disc")
    public Object discovery(){
        //获取已经注册在Eureka上的服务
        List<String> services = discoveryClient.getServices();
        for (String service : services) {
            log.info("服务名称:" + service);
        }

        //获取某个特定服务下的所有信息
        List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
        for (ServiceInstance instance : instances) {
            log.info(instance.getHost() +"\t" + instance.getPort() + "\t" + instance.getUri());
        }
        return discoveryClient;
    }
}

```

主控制类添加一个注解@EnableDiscoveryClient

> 注意是 org.springframework.cloud.client.discovery 包下的 EnableDiscoveryClient 

```java
@SpringBootApplication
@EnableEurekaClient
@EnableDiscoveryClient
public class PaymentMain8002 {
    public static void main(String[] args) {
        SpringApplication.run(PaymentMain8002.class, args);
    }
}

```

==也就是关于我们这个功能可以用这个实现==

#### Eureka 自我保护机制

在自我保护模式中，Eureka Server会保护服务注册表中的信息，不再注销任何服务实例

它的设计哲学就是：好死不如赖活着

> 自我保护模式是一种应对网络异常的安全保护措施。它的架构哲学是宁可同时保留所有微服务（健康的微服务和不健康的微服务都会保留）也不盲目注销任何健康的微服务，使用自我保护模式，可以让Eureka集群更加的健壮、稳定

### Zookeeper

SpringCloud整合Zookeeper代替Eureka

> POM文件的新项目的依赖

```xml
<dependencies>
        <!--springcloud 整合 zookeeper 组件-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-zookeeper-discovery</artifactId>
            <exclusions>
                <!-- 排除自带的版本 -->
                <exclusion>
                    <groupId>org.apache.zookeeper</groupId>
                    <artifactId>zookeeper</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
        <dependency>
            <groupId>org.apache.zookeeper</groupId>
            <artifactId>zookeeper</artifactId>
            <version>3.4.9</version>
            <exclusions>
                <exclusion>
                    <groupId>org.slf4j</groupId>
                    <artifactId>slf4j-log4j12</artifactId>
                </exclusion>
            </exclusions>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
        </dependency>
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid-spring-boot-starter</artifactId>
            <version>1.1.10</version>
        </dependency>
        <!--mysql-connector-java-->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>
        <!--jdbc-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-jdbc</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency><!-- 引入自己定义的api通用包，可以使用Payment支付Entity -->
            <groupId>com.cloud.springcloud</groupId>
            <artifactId>cloud-api-commons</artifactId>
            <version>${project.version}</version>
        </dependency>
    </dependencies>
```

Controller类代码

```java
 	@Value("${server.port}")
    private String serverPort;

    @RequestMapping("/payment/zk")
    public String paymentzk(){
        return "springcloud with zookeeper :" + serverPort + "\t" + UUID.randomUUID().toString();
    }   
```

> ***如果 zookeeper 的版本和导入的jar包版本不一致，启动就会报错，由jar包冲突的问题。
>
> 解决这种冲突，需要在 pom 文件中，排除掉引起冲突的jar包，添加和服务器zookeeper版本一致的 jar 包，
>
> 但是新导入的 zookeeper jar包 又有 slf4j 冲突问题，于是再次排除引起冲突的jar包

zookeeper的节点是临时节点还是持久节点？

> 临时节点，在一定的时间后，如果zookeeper检测到该服务已经没有心跳，那就会剔除该服务

### Consul

> 是一套开源的分布式服务发现和配置管理系统，使用GO语言开发
>
> consul也是服务注册中心的一个实现，是由go语言写的。官网地址： https://www.consul.io/intro 
>
> 中文地址： https://www.springcloud.cc/spring-cloud-consul.html 

#####安装并运行

>  下载地址：https://www.consul.io/downloads
>
>  打开下载的压缩包，只有一个exe文件，实际上是不用安装的，在exe文件所在目录打开dos窗口使用即可。
>
>  使用开发模式启动：consul agent -dev
>
>  访问8500端口，即可访问首页

##### 服务提供者

POM文件

```xml
 <dependencies>
        <!--springcloud consul server-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-consul-discovery</artifactId>
        </dependency>

        <!-- springboot整合Web组件 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>

        <!-- 日常通用jar包 -->
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency><!-- 引入自己定义的api通用包，可以使用Payment支付Entity -->
            <groupId>com.cloud.springcloud</groupId>
            <artifactId>cloud-api-commons</artifactId>
            <version>${project.version}</version>
        </dependency>
    </dependencies>
```

yml文件

```yml
server:
  port: 8006

spring:
  application:
    name: cloud-consul-provider
  cloud:
    consul:
      host: localhost
      port: 8500
      discovery:    # 指定注册对外暴露的服务名称
        service-name: ${spring.application.name}
```

### 三个中心的异同点

| 组件名    | 语言 | CAP  | 服务健康检查 | 对外接口暴露 | SpringCloud集成 |
| --------- | ---- | ---- | ------------ | ------------ | --------------- |
| Eureka    | Java | AP   | 可配支持     | HTTP         | 已集成          |
| Consul    | Go   | CP   | 支持         | HTTP/DNS     | 已集成          |
| Zookeeper | Java | CP   | 支持         | 客户端       | 已集成          |

> C：Consistency（强一致性） A：Availability（可用性） P：Partition tolerance（分区容错性） CAP（分布式一定会有P）

经典CAP图

AP（Eureka）

![1597384508291](D:\JavaStudy\JavaEE\SpringCloud\images\1597384508291.png)

CP（Zookeeper/Consul）

![1597384554249](D:\JavaStudy\JavaEE\SpringCloud\images\1597384554249.png)

## 服务调用

> 都是使用在 client端，即有 ”消费者“ 需求的模块中。

###Ribbon

##### 简介

![WeChat Screenshot_20201021154055](D:\JavaStudy\JavaEE\SpringCloud\images\WeChat Screenshot_20201021154055.png)

集中式LB（Load Balance）![WeChat Screenshot_20201021154314](D:\JavaStudy\JavaEE\SpringCloud\images\WeChat Screenshot_20201021154314.png)

进程内LB

![WeChat Screenshot_20201021154419](D:\JavaStudy\JavaEE\SpringCloud\images\WeChat Screenshot_20201021154419.png)

Ribbon的负载均衡

![WeChat Screenshot_20201021155041](D:\JavaStudy\JavaEE\SpringCloud\images\WeChat Screenshot_20201021155041.png)![1597385250463](D:\JavaStudy\JavaEE\SpringCloud\images\1597385250463.png)

> 一句话：Ribbon 就是 负载均衡 + RestTemplate 调用。实际上不止eureka的jar包有，zookeeper的jar包，还有consul的jar包都包含了他，就是上面使用的服务调用。

##### 负载均衡

![BalanceType](D:\JavaStudy\JavaEE\SpringCloud\images\BalanceType.png)

配置负载均衡规则：![1597386638507](D:\JavaStudy\JavaEE\SpringCloud\images\1597386638507.png)

> 注意上面说的，而Springboot主启动类上的 @SpringBootApplication 注解，相当于加了@ComponentScan注解，会自动扫描当前包及子包，所以注意不要放在SpringBoot主启动类的包内。即创建一个新包，跟启动类的父包平级

定义替换的负载均衡算法

```java
@Configuration
public class MySelfRule {

    @Bean
    public IRule myRule(){
        //定义为随机
        return new RandomRule();
    }
}
```

修改之后的主配置类

```java
@SpringBootApplication
@EnableEurekaClient
@RibbonClient(name = "CLOUD-PAYMENT-SERVICE", configuration = {MySelfRule.class})
public class OrderMain80 {
    public static void main(String[] args) {
        SpringApplication.run(OrderMain80.class, args);
    }
}
```

##### 轮询算法原理

![WeChat Screenshot_20201021165815](D:\JavaStudy\JavaEE\SpringCloud\images\WeChat Screenshot_20201021165815.png)

###Open Feign

==也就是创建一个接口，并在接口上添加注解==

> 说白一点就是  微服务调用接口 + @FeignClient注解

##### 使用

> <font color=red>Feign自带负载均衡配置</font>，所以不用手动配置

pom

```xml
<dependencies>
        <!-- Open Feign -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-openfeign</artifactId>
        </dependency>
        <!-- eureka Client -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
        <dependency><!-- 引入自己定义的api通用包，可以使用Payment支付Entity -->
            <groupId>com.cloud.springcloud</groupId>
            <artifactId>cloud-api-commons</artifactId>
            <version>${project.version}</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
```

主启动类

```java
@SpringBootApplication
@EnableFeignClients
public class OrderFeignMain80 {
    public static void main(String[] args) {
        SpringApplication.run(OrderFeignMain80.class, args);
    }
}
```

yml

```yml
server:
  port: 80

spring:
  application:
    name: cloud-order-consumer

# eureka 配置
eureka:
  client:
    register-with-eureka: true
    fetch-registry: true
    service-url:
      # 集群版配置
      defaultZone: http://eureka7001.com:7001/eureka/,http://eureka7002.com:7002/eureka/
```

##### 超时控制

yml文件

```yml
# ribbon 配置 设置feign客户端超时时间(OpenFeign默认支持ribbon)
ribbon:
  # 指的是建立连接所用的时间，适用于网络状态正常的情况下。两端连接所用的时间
  ReadTimeout: 5000
  # 指的是建立连接后从服务器读取到可用资源所用的时间
  ConnectTimeout: 5000
```

## 服务降级

###Hystrix

> 官方地址：https://github.com/Netflix/Hystrix/wiki/How-To-Use

服务雪崩

![image-20201022143537002](D:\JavaStudy\JavaEE\SpringCloud\images\image-20201022143537002.png)

服务降级

> 服务器忙碌或者网络拥堵时，不让客户端等待并立刻返回一个友好提示，fallback

服务熔断

> <font color=red>类比保险丝达到最大服务访问后，直接拒绝访问，拉闸限电，然后调用服务降级的方法返回友好提示</font>

服务限流

> 秒杀高并发等操作，严禁一窝蜂的过来拥挤，大家排队，一秒钟N个，有序进行

==可见，上面的技术不论是消费者还是提供者，根据真实环境都是可以加入配置的。==

##### 案例

提供者

pom文件

```xml
<dependencies>
        <!-- hystrix -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
        </dependency>
        <!--eureka-client-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency><!-- 引入自己定义的api通用包，可以使用Payment支付Entity -->
            <groupId>com.cloud.springcloud</groupId>
            <artifactId>cloud-api-commons</artifactId>
            <version>${project.version}</version>
        </dependency>
    </dependencies>
```

yml

```yml
server:
  port: 8001

spring:
  application:
    name: cloud-provider-hystrix-payment

# 注册到 eureka中心
eureka:
  client:
    # 注册进 Eureka 的服务中心
    register-with-eureka: true
    # 检索 服务中心 的其它服务，默认为true，单节点无所谓，集群必须设置为true才能配合ribbon使用负载均衡
    fetch-registry: true
    service-url:
      # 设置与 Eureka Server 交互的地址
      # defaultZone: http://localhost:7001/eureka/
      # 集群版配置
      # defaultZone: http://eureka7001.com:7001/eureka/,http://eureka7002.com:7002/eureka/
      #单机版
      defaultZone: http://eureka7001.com:7001/eureka/
```

主启动

```java
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableDiscoveryClient
@EnableEurekaClient
public class PaymentHystrixMain8001 {
    public static void main(String[] args) {
        SpringApplication.run(PaymentHystrixMain8001.class, args);
    }
}
```

##### 服务降级

> <font color=red>一般服务降级放在客户端，即 消费者端 ，</font>但是提供者端一样能使用。
>
> 首先提供者，即8001 先从自身找问题，设置自身调用超时的峰值，峰值内正常运行，超出峰值需要有兜底的方法处理，作服务降级fallback

解决

![WeChat Screenshot_20201022162108](D:\JavaStudy\JavaEE\SpringCloud\images\WeChat Screenshot_20201022162108.png)

降级配置

> <font color=red>@HystrixCommand</font>

8001service

```java
@HystrixCommand(fallbackMethod = "paymentInfo_timeoutHandler")
    //设置峰值，超过 3 秒，就会调用兜底方法，这个时间也可以由feign控制
    @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds", value = "3000")
    public String paymentInfo_Timeout(Integer id){
        int timeoutNum = 5;
        try {
            TimeUnit.SECONDS.sleep(timeoutNum);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "线程池: " + Thread.currentThread().getName() + "  paymentInfo_OK id:" +id + "\t"+ "耗时(s)" + timeoutNum;
    }

    /**
        兜底方法，根据上述配置，程序内发生异常、或者运行超时，都会执行该兜底方法
     */
    public String paymentInfo_timeoutHandler(Integer id){

        return "线程池: " + Thread.currentThread().getName() + "  paymentInfo_timeoutHandler id:" +id + "\t"+ "耗时(s) X";
    }
```

主启动类

```java
SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableEurekaClient
@EnableCircuitBreaker
public class PaymentHystrixMain8001 {
    public static void main(String[] args) {
        SpringApplication.run(PaymentHystrixMain8001.class, args);
    }
}
```

> <font color=red>不论是超时异常，还是系统运行异常，都会执行那个兜底的方法</font>

80端口微服务改造

yml

```yml
# 支持Hystrix
feign:
  hystrix:
    enabled: true
```

主启动

```java
@SpringBootApplication
@EnableFeignClients
@EnableHystrix
public class OrderHystrixMain80 {
    public static void main(String[] args) {
        SpringApplication.run(OrderHystrixMain80.class, args);
    }
}
```

controller

```java
 	@GetMapping("/consumer/payment/hystrix/ok/{id}")
    String paymentInfo_OK(@PathVariable("id") Integer id){
        return paymentHystrixService.paymentInfo_OK(id);
    }

    @GetMapping("/consumer/payment/hystrix/timeout/{id}")
    @HystrixCommand(fallbackMethod = "paymentInfo_timeoutHandlerMethod",commandProperties = {
            //设置峰值，超过 3 秒，就会调用兜底方法，这个时间也可以由feign控制
            @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds", value = "1500")})
    public String paymentInfo_Timeout(Integer id){
        return paymentHystrixService.paymentInfo_timeout(id);
    }

    /**
     兜底方法，根据上述配置，程序内发生异常、或者运行超时，都会执行该兜底方法
     */
    public String paymentInfo_timeoutHandlerMethod(Integer id){
        return "80自己出错或者服务器服务繁忙,请稍后重试";
    }
```

##### 全局服务降级兜底方法

> 上面的降级策略，很明显造成了代码的杂乱，提升了耦合度，而且按照这样，每个方法都需要配置一个兜底方法，很繁琐。现在将降级处理方法（兜底方法）做一个全局的配置，设置共有的兜底方法和独享的兜底方法。

controller![WeChat Screenshot_20201022180133](D:\JavaStudy\JavaEE\SpringCloud\images\WeChat Screenshot_20201022180133.png)

通配服务降级FeignFallback(解决代码混乱)

修改Service类

```java
@Component
@FeignClient(value = "CLOUD-PROVIDER-HYSTRIX-PAYMENT",fallback = PaymentFallbackService.class)
public interface PaymentHystrixService {

    @GetMapping("/payment/hystrix/ok/{id}")
    String paymentInfo_OK(@PathVariable("id") Integer id);

    @GetMapping("/payment/hystrix/timeout/{id}")
    String paymentInfo_timeout(@PathVariable("id") Integer id);
}
```

PaymentFallbackServcie

```java
package com.cloud.springcloud.service;

import org.springframework.stereotype.Component;

/**
 * @author lisw
 * @create 2020/10/23
 */
@Component
public class PaymentFallbackService implements PaymentHystrixService{
    @Override
    public String paymentInfo_OK(Integer id) {
        return "客户端异常....";
    }

    @Override
    public String paymentInfo_timeout(Integer id) {
        return "客户端发生连接超时...";
    }
}
```

新问题，这样配置如何设置超时时间？

> 首先要知道 下面两个 yml 配置项：
>
> ```properties
> hystrix.command.default.execution.timeout.enable=true    ## 默认值
> 
> ## 为false则超时控制有ribbon控制，为true则hystrix超时和ribbon超时都是用，但是谁小谁生效，默认为true
> 
> hystrix.command.default.execution.isolation.thread.timeoutInMilliseconds=1000  ## 默认值
> 
> ## 熔断器的超时时长默认1秒，最常修改的参数
> ```
>
> 看懂以后，所以：
>
> 只需要在yml配置里面配置 Ribbon 的 超时时长即可。注意：hystrix 默认自带 ribbon包。
>
> ```yml
> ribbon:
> 	ReadTimeout: xxxx
> 	ConnectTimeout: xxx
> ```

##### 服务熔断

> 实际上服务熔断 和 服务降级 没有任何关系，就像 java 和 javaScript
>
> 服务熔断，有点自我恢复的味道

![rongduan](D:\JavaStudy\JavaEE\SpringCloud\images\rongduan.png)

8001项目修改后的PaymentService

```java
/**
     * 服务熔断
     */
    具体的commandProperties name可以参考HystrixCommandProperties类
    @HystrixCommand(fallbackMethod = "paymentCircuitBreaker_fallback", commandProperties = {
            @HystrixProperty(name="circuitBreaker.enabled", value="true"),  // 是否开启断路器
            @HystrixProperty(name="circuitBreaker.requestVolumeThreshold", value="10"),  //请求次数
            @HystrixProperty(name="circuitBreaker.sleepWindowInMilliseconds", value="10000"), // 时间窗口期
            @HystrixProperty(name="circuitBreaker.errorThresholdPercentage", value="60"),  // 失败率达到多少后跳闸
            //整体意思：10秒内 10次请求，有6次失败，就跳闸
    })
    public String paymentCircuitBreaker(@PathVariable("id") Integer id){
        if(id < 0){
            throw new RuntimeException("******id 不能为负数");
        }
        //生成唯一的流水号
        String serialNumber = IdUtil.simpleUUID();

        return Thread.currentThread().getName() + "\t" + "调用成功，流水号 : " + serialNumber;
    }
    public String paymentCircuitBreaker_Fallback(@PathVariable("id") Integer id){
        return "id 不能为负数，请稍后重试， id :" + id;
    }
```

PaymentController

```java
	//====服务熔断
    @GetMapping("/payment/circuit/{id}")
    public String paymentCircuitBreaker(@PathVariable("id")Integer id){
        return paymentService.paymentCircuitBreaker(id);
    }
```

##### HystrixDashBoard

> 创建项目cloud-consumer-hystrix-dashboard9001

pom文件

```xml
  <dependencies>
        <!-- hystrix Dashboard-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-hystrix-dashboard</artifactId>
        </dependency>
        <!-- 常规 jar 包 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency><!-- 引入自己定义的api通用包，可以使用Payment支付Entity -->
            <groupId>com.cloud.springcloud</groupId>
            <artifactId>cloud-api-commons</artifactId>
            <version>${project.version}</version>
        </dependency>
    </dependencies>
```

yml

```yml
server:
  port: 9001


spring:
  application:
    name: cloud-hystrix-dashBoard
```

主启动类

```java
@SpringBootApplication
@EnableHystrixDashboard
public class HystrixDashBoardMain9001 {
    public static void main(String[] args) {
        SpringApplication.run(HystrixDashBoardMain9001.class, args);
    }
}
```

![WeChat Screenshot_20201023162223](D:\JavaStudy\JavaEE\SpringCloud\images\WeChat Screenshot_20201023162223.png)

8001(被监控的启动类)

```java
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableEurekaClient
@EnableCircuitBreaker
public class PaymentHystrixMain8001 {
    public static void main(String[] args) {
        SpringApplication.run(PaymentHystrixMain8001.class, args);
    }

    @Bean
    public ServletRegistrationBean getServlet(){
        HystrixMetricsStreamServlet streamServlet = new HystrixMetricsStreamServlet();
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(streamServlet);
        servletRegistrationBean.setLoadOnStartup(1);
        servletRegistrationBean.addUrlMappings("/hystrix.stream");
        servletRegistrationBean.setName("HystrixMetricsStreamServlet");
        return servletRegistrationBean;
    }
}
```

![WeChat Screenshot_20201023163030](D:\JavaStudy\JavaEE\SpringCloud\images\WeChat Screenshot_20201023163030.png)![WeChat Screenshot_20201023162558](D:\JavaStudy\JavaEE\SpringCloud\images\WeChat Screenshot_20201023162558.png)

## 服务网关

###GateWay

> 一句话：<font color=red>Spring Cloud Gateway 使用的是Wevflux中的reactor-netty响应式编程组件，底层使用了Netty通讯框架，异步非阻塞式</font>
>
> 开发可参考 https://docs.spring.io/  官网文档

![WeChat Screenshot_20201026162103](D:\JavaStudy\JavaEE\SpringCloud\images\WeChat Screenshot_20201026162103.png)

![WeChat Screenshot_20201026163047](D:\JavaStudy\JavaEE\SpringCloud\images\WeChat Screenshot_20201026163047.png)

工作流程

![liucheng](D:\JavaStudy\JavaEE\SpringCloud\images\liucheng.png)

POM文件

```xml
<dependencies>
        <!--gateway-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-gateway</artifactId>
        </dependency>
        <!--eureka-client gateWay作为网关，也要注册进服务中心-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
        <!-- springboot整合Web组件 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency><!-- 引入自己定义的api通用包，可以使用Payment支付Entity -->
            <groupId>com.cloud.springcloud</groupId>
            <artifactId>cloud-api-commons</artifactId>
            <version>${project.version}</version>
        </dependency>
    </dependencies>
```

yml文件

```yml
server:
  port: 9527

spring:
  application:
    name: cloud-gateway
 ## GateWay配置
  cloud:
    gateway:
      routes:
        - id: payment_routh  # 路由ID ， 没有固定的规则但要求唯一，建议配合服务名
          uri: http://localhost:8001  # 匹配后提供服务的路由地址
          predicates:
            - Path=/payment/get/**  # 断言，路径相匹配的进行路由

        - id: payment_routh2  # 路由ID ， 没有固定的规则但要求唯一，建议配合服务名
          uri: http://localhost:8001  # 匹配后提供服务的路由地址
          predicates:
            - Path=/payment/lb/**  # 断言，路径相匹配的进行路由
eureka:
  instance:
    hostname: cloud-gateway-service
  client:
    register-with-eureka: true
    fetch-registry: true
    service-url:
      defaultZone: http://eureka7001.com:7001/eureka
```

注入RouteLocator的Bean

```java
 @Bean
    public RouteLocator customerRouteLocator(RouteLocatorBuilder routeLocatorBuilder){
        RouteLocatorBuilder.Builder routes = routeLocatorBuilder.routes();
        routes.route("gateway_route",
                r -> r.path("/guonei").
                uri("http://news.baidu.com/guonei")).build();
        return routes.build();
    }
```

##### 动态路由

> pom文件中添加eureka依赖

yml文件

```yml
server:
  port: 9527

spring:
  application:
    name: cloud-gateway
  ## GateWay配置
  cloud:
    gateway:
      routes:
        - id: payment_routh  # 路由ID ， 没有固定的规则但要求唯一，建议配合服务名
          # uri: http://localhost:8001  # 匹配后提供服务的路由地址
          uri: get://cloud-payment-service # 动态路由匹配的地址
          predicates:
            - Path=/payment/get/**  # 断言，路径相匹配的进行路由

        - id: payment_routh2  # 路由ID ， 没有固定的规则但要求唯一，建议配合服务名
          # uri: http://localhost:8001  # 匹配后提供服务的路由地址
          # 采用 LoadBalanceClient 方式请求，以 lb://开头  LoadBalanceClient 为SpringCloud提供的负载均衡的客户端
          uri: lb://cloud-payment-service
          predicates:
            - Path=/payment/lb/**  # 断言，路径相匹配的进行路由
      discovery:
        locator:
          enabled: true # 开启从注册中心动态创建路由的功能，利用微服务名进行路由

eureka:
  instance:
    hostname: cloud-gateway-service
  client:
    register-with-eureka: true
    fetch-registry: true
    service-url:
      defaultZone: http://eureka7001.com:7001/eureka
```

##### Predicates

> yml中的predicates的属性可以包含很多个

![image-20201027103714129](C:\Users\11692\AppData\Roaming\Typora\typora-user-images\image-20201027103714129.png)

> Java8 获取当前时区的时间用于After写断言
>
> 2020-10-27T10:45:42.898+08:00[Asia/Shanghai]

```java
	ZonedDateTime zonedDateTime = ZonedDateTime.now(); //默认时区
    System.out.println(zonedDateTime);
```

![WeChat Screenshot_20201027105354](D:\JavaStudy\JavaEE\SpringCloud\images\WeChat Screenshot_20201027105354.png)

> 使用cmd curl命令测试这个
>
> curl http://localhost:9527/payment/lb --cookie "username=1"

![WeChat Screenshot_20201027112303](D:\JavaStudy\JavaEE\SpringCloud\images\WeChat Screenshot_20201027112303.png)

![WeChat Screenshot_20201027112822](D:\JavaStudy\JavaEE\SpringCloud\images\WeChat Screenshot_20201027112822.png)

> 放爬虫思路，前后端分离的话，只限定前端项目主机访问，这样可以屏蔽大量爬虫。
>
> 例如我加上： - Host=localhost:**       ** 代表允许任何端口
>
> 就只能是主机来访

配置错误页面:

> 注意，springboot默认/static/error/ 下错误代码命名的页面为错误页面，即 404.html
>
> 而且不需要导入额外的包，Gateway 里面都有。

##### Filter

![Filter](D:\JavaStudy\JavaEE\SpringCloud\images\Filter.png)

> 主要看自定义过滤器，SpringCloud内置的过滤器看官网

##### 自定义过滤器

自定义全局过滤器配置类

```java
@Component
@Slf4j
public class MyGlobalFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("********** com in MyGlobalFilter :" + new Date());
        String username = exchange.getRequest().getQueryParams().getFirst("username");
        if(username == null){
            log.info("非法用户null,用户名：" + username);
            //返回状态码
            exchange.getResponse().setStatusCode(HttpStatus.NOT_ACCEPTABLE);
            //设置为完成，之后下一步就会进入到按照chain链进入下一个过滤器
            return exchange.getResponse().setComplete();
        }
        return chain.filter(exchange);
    }

    /**
     * 数字越小，优先级越高
     * @return
     */
    @Override
    public int getOrder() {
        return 0;
    }
}
```

##服务配置

###Config

![config](D:\JavaStudy\JavaEE\SpringCloud\images\config.png)

服务端配置

> 首先在github上新建一个仓库 springcloud-config
>
> 然后使用git命令克隆到本地，命令：git clone https://github.com/yunxiangnian/spring-cloud-config.git
>
> 注意上面的操作不是必须的，只要github上有就可以，克隆到本地只是修改文件。

##### 服务端配置

pom文件

```xml
<dependencies>
        <!-- config Server -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-config-server</artifactId>
        </dependency>
        <!--eureka-client config Server也要注册进服务中心-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency><!-- 引入自己定义的api通用包，可以使用Payment支付Entity -->
            <groupId>com.cloud.springcloud</groupId>
            <artifactId>cloud-api-commons</artifactId>
            <version>${project.version}</version>
        </dependency>
    </dependencies>
```

yml文件

```yml
server:
  port: 3344

spring:
  application:
    name: cloud-config-center
  cloud:
    config:
      server:
        git:
          # 仓库地址
          uri: https://github.com/yunxiangnian/spring-cloud-config.git
          search-paths:
            # 搜索目录
            - spring-cloud-config
      #读取分支
      label: master
eureka:
  client:
    fetch-registry: true
    register-with-eureka: true
    service-url:
      defaultZone: http://eureka7001.com:7001/eureka

```

主配置类

```java
@SpringBootApplication
@EnableConfigServer
public class ConfigCenterMain3344 {
    public static void main(String[] args) {
        SpringApplication.run(ConfigCenterMain3344.class, args);
    }
}
```

配置读取规则

![WeChat Screenshot_20201027152745](D:\JavaStudy\JavaEE\SpringCloud\images\WeChat Screenshot_20201027152745.png)

![WeChat Screenshot_20201027152947](D:\JavaStudy\JavaEE\SpringCloud\images\WeChat Screenshot_20201027152947.png)

重点细节总结

![WeChat Screenshot_20201027153337](D:\JavaStudy\JavaEE\SpringCloud\images\WeChat Screenshot_20201027153337.png)

##### 客户端配置

POM文件

```xml
<dependencies>
        <!-- config Server -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-config</artifactId>
        </dependency>
        <!--eureka-client config Server也要注册进服务中心-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <!-- actuator 依赖 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency><!-- 引入自己定义的api通用包，可以使用Payment支付Entity -->
            <groupId>com.cloud.springcloud</groupId>
            <artifactId>cloud-api-commons</artifactId>
            <version>${project.version}</version>
        </dependency>
    </dependencies>
```

> yml 改为用 bootstrap.yml 为系统的配置文件，优先级更高

bootstrap.yml

```yml
server:
  port: 3355

spring:
  application:
    name: cloud-config-client
  cloud:
    # config 客户端配置
    config:
      label: master # 分支名称
      name: config # 配置文件名称，文件也可以是client-config-dev.yml这种格式的，这里就写 client-config
      profile: dev # 使用配置环境
      uri: http://localhost:3344  # config Server 地址
      # 综合上面四个 即读取配置文件地址为： http://config-3344.com:3344/master/config-dev.yml

eureka:
  client:
    service-url:
      defaultZone: http://eureka7001.com:7001/eureka/
```

主启动类

```java
@SpringBootApplication
@EnableEurekaClient
public class ConfigClientMain3355 {
    public static void main(String[] args) {
        SpringApplication.run(ConfigClientMain3355.class, args);
    }
}
```

Controller

```java
@RestController
@Slf4j
public class ConfigController {

    @Value("${config.info}")
    private String configInfo;

    @GetMapping("/configInfo")
    public String getConfigInfo(){
        return configInfo;
    }
}
```

##### 动态刷新

![WeChat Screenshot_20201027162733](D:\JavaStudy\JavaEE\SpringCloud\images\WeChat Screenshot_20201027162733.png)

> 就是github上面配置更新了，config Server 项目上是动态更新的，但是，client端的项目中的配置，目前还是之前的，它不能动态更新，必须重启才行。

Controller

```java
@RestController
@Slf4j
@RefreshScope
public class ConfigController {

    @Value("${config.info}")
    private String configInfo;

    @GetMapping("/configInfo")
    public String getConfigInfo(){
        return configInfo;
    }
}
```

yml

```yml

# 暴露监控断点
management:
  endpoints:
    web:
      exposure:
        include: "*"
```

> 在3344更新之后，做完这些配置之后，需要再重新POST请求刷新3355
>
> curl -X POST "http://localhost:3355/actuator/refresh"

但是又有一个问题，就是要向每个微服务发送一次POST请求，当微服务数量庞大，又是一个新的问题。

就有下面的消息总线！

## 消息总线

#####Bus

> <font color=red>分布式自动刷新配置功能</font>，SpringCloud Bus 配合 SpringCloud Config 使用可以实现配置的动态刷新
>
> Bus 只支持 RabbitMQ和Kafka

![WeChat Screenshot_20201027172936](D:\JavaStudy\JavaEE\SpringCloud\images\WeChat Screenshot_20201027172936.png)

![WeChat Screenshot_20201027173046](D:\JavaStudy\JavaEE\SpringCloud\images\WeChat Screenshot_20201027173046.png)

为什么称之为总线

![WeChat Screenshot_20201027173154](D:\JavaStudy\JavaEE\SpringCloud\images\WeChat Screenshot_20201027173154.png)

cloud-config-client3366

POM文件

```xml
<dependencies>
        <!-- config Server -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-config</artifactId>
        </dependency>
        <!--eureka-client config Server也要注册进服务中心-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <!-- actuator 依赖 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency><!-- 引入自己定义的api通用包，可以使用Payment支付Entity -->
            <groupId>com.cloud.springcloud</groupId>
            <artifactId>cloud-api-commons</artifactId>
            <version>${project.version}</version>
        </dependency>
    </dependencies>
```

设计思想

![WeChat Screenshot_20201027180215](D:\JavaStudy\JavaEE\SpringCloud\images\WeChat Screenshot_20201027180215.png)

> 尽量使用第二种设计思想

#####服务端

给3344服务端添加rabbitMQ总线的支持

```xml
	<!-- 添加rabbitMQ的消息总线支持包 -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-bus-amqp</artifactId>
        </dependency>
```

yml文件增加的配置

```yml
spring:
  application:
    name: cloud-config-center
  cloud:
    config:
      server:
        git:
          # 仓库地址
          uri: https://github.com/yunxiangnian/spring-cloud-config.git
          search-paths:
            # 搜索目录
            - spring-cloud-config
      #读取分支
      label: master
    # rabbitMq的相关配置
  rabbitmq:
    host: 192.168.1.133
    port: 5672  # 这里没错，虽然rabbitMQ网页是 15672
    username: root
    password: root

# rabbitmq 的相关配置2 暴露bus刷新配置的端点
management:
  endpoints:
    web:
      exposure:
        include: 'bus-refresh'    
```

##### 客户端

3355客户端

pom添加内容

```xml
 <!-- 添加rabbitMQ的消息总线支持包 -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-bus-amqp</artifactId>
        </dependency>
```

yml文件添加内容

```yml
spring:
  application:
    name: cloud-config-client
  cloud:
    # config 客户端配置
    config:
      label: master # 分支名称
      name: config # 配置文件名称，文件也可以是client-config-dev.yml这种格式的，这里就写 client-config
      profile: dev # 使用配置环境
      uri: http://localhost:3344  # config Server 地址
      # 综合上面四个 即读取配置文件地址为： http://config-3344.com:3344/master/config-dev.yml
  # rabbitMq的相关配置
  rabbitmq:
    host: 192.168.1.133
    port: 5672  # 这里没错，虽然rabbitMQ网页是 15672
    username: root
    password: root
```

3366客户端同样的配置

##### 定点动态刷新

> 总结

![WeChat Screenshot_20201028095323](D:\JavaStudy\JavaEE\SpringCloud\images\WeChat Screenshot_20201028095323.png)

## 消息驱动

> 屏蔽底层消息中间件的差异，降低切换成本，统一消息的编程模型

![WeChat Screenshot_20201028100839](D:\JavaStudy\JavaEE\SpringCloud\images\WeChat Screenshot_20201028100839.png)

标准流程图

![WeChat Screenshot_20201028102822](D:\JavaStudy\JavaEE\SpringCloud\images\WeChat Screenshot_20201028102822.png)

![WeChat Screenshot_20201028103354](D:\JavaStudy\JavaEE\SpringCloud\images\WeChat Screenshot_20201028103354.png)

##### 生产客户端

创建cloud-stream-rabbitmq-provider8801

POM文件

```xml
<dependencies>
        <!-- stream-rabbit -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-stream-rabbit</artifactId>
        </dependency>
        <!--eureka-client 目前，这个不是必须的-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency><!-- 引入自己定义的api通用包，可以使用Payment支付Entity -->
            <groupId>com.cloud.springcloud</groupId>
            <artifactId>cloud-api-commons</artifactId>
            <version>${project.version}</version>
        </dependency>
    </dependencies>
```

yml文件

```yml
server:
  port: 8801
spring:
  application:
    name: cloud-stream-provider
  cloud:
    stream:
      binders: # 在次配置要绑定的rabbitMQ的服务信息
        defaultRabbit: # 表示定义的名称，用于和binding整合
          type: rabbit  # 消息组件类型
          environment:  # 设置rabbitmq的相关环境配置
            spring:
              rabbitmq:
                host: 192.168.1.133
                port: 5672
                username: root
                password: root
      bindings: # 服务的整合处理
        output:   # 表示是生产者，向rabbitMQ发送消息
          destination: studyExchange  # 表示要使用的Exchange名称
          content-type: application/json  # 设置消息类型，本次是json，文本是 "text/plain"
          binder: defaultRabbit  # 设置要绑定的消息服务的具体配置 如果出现报红，无所谓。写就完事了。
eureka:
  client:
    service-url:
      defaultZone: http://eureka7001.com:7001/eureka/
  instance:
    lease-renewal-interval-in-seconds: 2 # 设置心跳时间，默认是30秒
    lease-expiration-duration-in-seconds: 5 # 最大心跳间隔不能超过5秒,默认90秒
    instance-id: send-8801.com # 在信息列表显示主机名称
    prefer-ip-address: true # 访问路径变为ip地址
```

发送消息的服务类

```java
/**
 * @EnableBinding(Source.class)  可以理解为将生产者绑定到某一个通道上,不用再加@Service注解
 * @author lisw
 * @create 2020/10/28
 */
@EnableBinding(Source.class)
@Slf4j
public class IMessageProviderImpl implements IMessageProvider {


    @Resource
    //消息发送管道
    private MessageChannel output; // 这个实例名称必须为output、noChannel、NullChannel 使用其他就会报错

    @Override
    public String send() {
        String serial = UUID.randomUUID().toString();
        output.send(MessageBuilder.withPayload(serial).build());
        log.info("*******serial : " + serial);
        return null;
    }
}
```

#####消费客户端

POM文件

> 和生产者一样

YML文件

```yml
server:
  port: 8802
spring:
  application:
    name: cloud-stream-consumer
  cloud:
    stream:
      binders: # 在次配置要绑定的rabbitMQ的服务信息
        defaultRabbit: # 表示定义的名称，用于和binding整合
          type: rabbit  # 消息组件类型
          environment:  # 设置rabbitmq的相关环境配置
            spring:
              rabbitmq:
                host: 192.168.1.133
                port: 5672
                username: root
                password: root
      bindings: # 服务的整合处理
        input:   # 表示是消费者，从RabbitMQ接受消息
          destination: studyExchange  # 表示要使用的Exchange名称
          content-type: application/json  # 设置消息类型，本次是json，文本是 "text/plain"
          binder: defaultRabbit  # 设置要绑定的消息服务的具体配置
eureka:
  client:
    service-url:
      defaultZone: http://eureka7001.com:7001/eureka/
  instance:
    lease-renewal-interval-in-seconds: 2 # 设置心跳时间，默认是30秒
    lease-expiration-duration-in-seconds: 5 # 最大心跳间隔不能超过5秒,默认90秒
    instance-id: receive-8802.com # 在信息列表显示主机名称
    prefer-ip-address: true # 访问路径变为ip地址
```

消费服务类

```java
@Component
@Slf4j
@EnableBinding(Sink.class)
public class ReceiveMessageListenerController {

    @Value("${server.port}")
    private String serverPort;

    @StreamListener(Sink.INPUT)
    public void input(Message<String> message){
        log.info("消费者： " + serverPort + "订阅的消息" + message.getPayload());
    }
}
```

##### 分组消费

> 如果配置了某个服务端集群，那么消息会被重复消费。也就是每个服务端都会接收到该消息
>
> 不同组是可以全面消费的（重复消费）
>
> 同一组内会发送竞争关系，只有其中一个可以被消费

![WeChat Screenshot_20201030181549](D:\JavaStudy\JavaEE\SpringCloud\images\WeChat Screenshot_20201030181549.png)

对于重复消费这种问题，导致的原因是默认每个微服务是不同的group，组流水号不一样，所以被认为是不同组，两个都可以消费。

解决办法：1、自定义分组要修改的yml文件（消费者）

> 同一个组的多个微服务，一次只能拿到一个

8803

```yml
bindings: # 服务的整合处理
        input:   # 表示是消费者，从RabbitMQ接受消息
          destination: studyExchange  # 表示要使用的Exchange名称
          content-type: application/json  # 设置消息类型，本次是json，文本是 "text/plain"
          binder: defaultRabbit  # 设置要绑定的消息服务的具体配置
          gruop: yxn2
```

8802

```yml
  bindings: # 服务的整合处理
        input:   # 表示是消费者，从RabbitMQ接受消息
          destination: studyExchange  # 表示要使用的Exchange名称
          content-type: application/json  # 设置消息类型，本次是json，文本是 "text/plain"
          binder: defaultRabbit  # 设置要绑定的消息服务的具体配置
          group: yxn2
```

#####消息持久化

> 加上group配置，就已经实现了消息的持久化。

## Sleuth

![lianlugenzong](D:\JavaStudy\JavaEE\SpringCloud\images\lianlugenzong.png)

> sleuth 负责跟踪，而zipkin负责展示。
>
> zipkin 下载地址： http://dl.bintray.com/openzipkin/maven/io/zipkin/java/zipkin-server/2.12.9/zipkin-server-2.12.9-exec.jar
>
> 使用 【java -jar】 命令运行下载的jar包，访问地址：【 http://localhost:9411/zipkin/ 】

完整的链路调用

![image-20201102154109791](D:\JavaStudy\JavaEE\SpringCloud\images\image-20201102154109791.png)

![WeChat Screenshot_20201102154307](D:\JavaStudy\JavaEE\SpringCloud\images\WeChat Screenshot_20201102154307.png)

案例

> 使用之前的 提供者8001 和  消费者80

分别给他们引入依赖：

```xml
	<!-- 引入sleuth + zipkin -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-zipkin</artifactId>
        </dependency>
```

yml增加配置

```yml
spring:
  zipkin:
    base-url: http://localhost:9411  # zipkin 地址
  sleuth:
    sampler:
      # 采样率值 介于0-1之间 ，1表示全部采集
      probability: 1
```

# SpringCloud Alibaba

> alibaba 的 github上有中文文档

##### 大简介

![WeChat Screenshot_20201102163003](D:\JavaStudy\JavaEE\SpringCloud\images\WeChat Screenshot_20201102163003.png)

#### Nacos

> > Nacos = Eureka + Config + Bus
>
> > github地址：  https://github.com/alibaba/Nacos 
> >
> > Nacos 地址：  https://nacos.io/zh-cn/ 
> >
> > 开发手册：https://nacos.io/zh-cn/docs/what-is-nacos.html

注册中心的比较

![Namingcompare](D:\JavaStudy\JavaEE\SpringCloud\images\Namingcompare.png)

>  nacos可以切换CP和AP <font color=red>C是所有节点在同一时间看到的数据是一致的，而A的定义是所有请求都会得到响应,CP模式下支持注册持久化实例</font>

![1597756097369](D:\JavaStudy\JavaEE\SpringCloud\images\1597756097369.png)

##### 下载

> 下载地址：  https://github.com/alibaba/nacos/releases/tag/1.1.4 
>
> 直接下载网址： https://github.com/alibaba/nacos/releases/download/1.1.4/nacos-server-1.1.4.zip
>
> 下载压缩包以后解压，进入bin目录，打开dos窗口，执行startup命令启动它。
>
> 可访问 ： 【 http://192.168.101.105:8848/nacos/index.html】地址，默认账号密码都是nacos

##### 服务注册中心

提供者 新建模块 cloudalibaba-provider-payment9001（后面为了负载均衡再建一个一模一样的cloudalibaba-provider-payment9002）

新建一个项目

POM文件

```xml
 <dependencies>
        <!-- springcloud alibaba nacos 依赖 -->
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
        </dependency>
        <!-- springboot整合Web组件 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>

        <!-- 日常通用jar包 -->
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency><!-- 引入自己定义的api通用包，可以使用Payment支付Entity -->
            <groupId>com.cloud.springcloud</groupId>
            <artifactId>cloud-api-commons</artifactId>
            <version>${project.version}</version>
        </dependency>
    </dependencies>
```

yml文件

```yml
server:
  port: 9001
spring:
  application:
    name: nacos-provider
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848
management:
  endpoints:
    web:
      exposure:
        include: '*'
```

<font color=red>Nacos 自带负载均衡机制</font>

消费者cloudalibaba-customer-order83

POM文件与提供者一致

yml文件

```yml
server:
  port: 83
spring:
  application:
    name: nacos-consumer-order
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848


# consumer要访问的微服务名称（注册成功进nacos的微服务提供者） 实现了从配置文件中读取微服务名称
service-url:
  nacos-user-service: http://nacos-payment-provider
```

controller

```java

@RestController
@Slf4j
public class OrderController {

    @Resource
    private RestTemplate restTemplate;

    @Value("${service-url.nacos-user-service}")
    private String service_URL;


    @GetMapping("consumer/nacos/hello")
    public String paymentInfo(){
        return restTemplate.getForObject(service_URL+"/nacos/hello",String.class);
    }
}
```

config

```java
@Configuration
public class ApplicationConfig {

    @Bean
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }
}
```

##### 服务配置中心

> nacos 还可以作为服务配置中心，下面是案例，创建一个模块，从nacos上读取配置信息。
>
> nacos 作为配置中心，不需要像springcloud config 一样做一个Server端模块。

新建模块 cloudalibaba-nacos-config3377

POM文件

```xml
    <dependencies>
        <!-- 以 nacos 做服务配置中心的依赖 -->
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
        </dependency>
        <!-- springcloud alibaba nacos 依赖 -->
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
        </dependency>
        <!-- springboot整合Web组件 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>

        <!-- 日常通用jar包 -->
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency><!-- 引入自己定义的api通用包，可以使用Payment支付Entity -->
            <groupId>com.cloud.springcloud</groupId>
            <artifactId>cloud-api-commons</artifactId>
            <version>${project.version}</version>
        </dependency>
    </dependencies>
```

主启动类也是极其普通：

```java
@SpringBootApplication
@EnableDiscoveryClient
public class CloudAlibabaConfigMain3377 {
    public static void main(String[] args) {
        SpringApplication.run(CloudAlibabaConfigMain3377.class,args);
    }
}
```

***bootstrap.yml 配置：

```yml
server:
  port: 3377
spring:
  application:
    name: nacos-config-client
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848  # nacos作为服务注册中心
      config:
        server-addr: localhost:8848 # nacos作为服务配置中心
        file-extension: yaml # 指定yaml 格式的配置
```

yml文件

```yml
spring:
  profiles:
    active: dev #表示开发环境 prod 生产环境 test 测试环境
```

controller 层进行读取配置测试：

```java
@RestController
@RefreshScope  //支持Nacos的动态刷新
public class ConfigClientController {

    @Value("${config.info}")
    private String configInfo;

    @GetMapping("configclient/getconfiginfo")
    public String getConfigInfo(){
        return configInfo;
    }
}
```

> 下面在 Nacos 中添加配置文件，需要遵循如下规则：

![WeChat Screenshot_20201103102406](D:\JavaStudy\JavaEE\SpringCloud\images\WeChat Screenshot_20201103102406.png)

> 从上面可以看到重要的一点，配置文件的名称第二项，spring.profiles.active 是依据当前环境的profile属性值的，也就是这个值如果是 dev，即开发环境，它就会读取 dev 的配置信息，如果是test，测试环境，它就会读取test的配置信息，就是从 spring.profile.active 值获取当前应该读取哪个环境下的配置信息。

Nacos的dataid（类似文件名）应该为：nacos-config-client-dev.yaml

>  ${prefix}-${spring.profiles.active}.${file-extension}

![image-20201103103346507](D:\JavaStudy\JavaEE\SpringCloud\images\image-20201103103346507.png)

![WeChat Screenshot_20201103103920](D:\JavaStudy\JavaEE\SpringCloud\images\WeChat Screenshot_20201103103920.png)

其它说明：

> Nacos 的 Group ,默认创建的配置文件，都是在DEFAULT_GROUP中，可以在创建配置文件时，给文件指定分组。
>
> yml 配置如下，当修改开发环境时，只会从同一group中进行切换。
>
> ![1597807821441](D:\JavaStudy\JavaEE\SpringCloud\images\1597807821441.png)
>
> Nacos 的namespace ,默认的命名空间是public ,这个是不允许删除的，可以创建一个新的命名空间，会自动给创建的命名空间一个流水号。
>
> 在yml配置中，指定命名空间：
>
> ![1597808181104](D:\JavaStudy\JavaEE\SpringCloud\images\1597808181104.png)
>
> 最后，dataid、group、namespace 三者关系如下：（不同的dataid，是相互独立的，不同的group是相互隔离的，不同的namespace也是相互独立的）
>
> ![1597808385154](D:\JavaStudy\JavaEE\SpringCloud\images\1597808385154.png)

##### 集群部署

> 硬菜来了。。。

![WeChat Screenshot_20201103142438](D:\JavaStudy\JavaEE\SpringCloud\images\WeChat Screenshot_20201103142438.png)

> 搭建集群必须持久化，不然多台机器上的nacos的配置信息不同，造成系统错乱。它不同于单个springcloud config，没有集群一说，而且数据保存在github上，也不同于eureka，配置集群就完事了，没有需要保存的配置信息。
>
> Nacos默认使用它自带的嵌入式数据库derby:
>
> ![1597809107487](D:\JavaStudy\JavaEE\SpringCloud\images\1597809107487.png)

Nacos持久化配置：

> 在 nacos的 conf目录下，有个nacos-mysql.sql 的sql文件，创建一个名为【nacos_config】的数据库，执行里面内容，在nacos_config数据库里面创建数据表。
>
> 找到conf/application.properties 文件，尾部追加如下内容：
>
> ```properties
> spring.datasource.platform=mysql
> 
> db.num=1
> db.url.0=jdbc:mysql://localhost:3306/nacos_config?characterEncoding=utf-8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true
> db.user=root
> db.password=*****
> ```
>
> 重启nacos，即完成持久化配置。

Linux下的cluster.conf配置 

> <font color=red>前面的ip 是用  hostname -I (大写的i) 命令下查出来的ip地址</font>

```text
192.168.1.133:3333
192.168.1.133:4444
192.168.1.133:5555
```

多nacos启动命令

> ![image-20201103164230865](D:\JavaStudy\JavaEE\SpringCloud\images\image-20201103164230865.png)

sh命令的修改(先cp一份保险起见)

![WeChat Screenshot_20201103164525](D:\JavaStudy\JavaEE\SpringCloud\images\WeChat Screenshot_20201103164525.png)

![WeChat Screenshot_20201103164626](D:\JavaStudy\JavaEE\SpringCloud\images\WeChat Screenshot_20201103164626.png)

执行命令

> ./startup.sh -p port

将9002服务提供者注册进nacos集群中

yml配置

```yml
spring:
  application:
    name: nacos-provider
  cloud:
    nacos:
      discovery:
        # 换成nginx的1111端口,做集群
        server-addr: 192.168.1.133:1111
      # server-addr: localhost:8848
```

#### Sentinel

![WeChat Screenshot_20201104180134](D:\JavaStudy\JavaEE\SpringCloud\images\WeChat Screenshot_20201104180134.png)

> sentinel在 springcloud Alibaba 中的作用是实现熔断和限流
>
> 下载地址：https://github.com/alibaba/Sentinel/releases
>
> 下载jar包以后，使用【java -jar】命令启动即可。
>
> 它使用 8080 端口，用户名和密码都为 ： sentinel

![WeChat Screenshot_20201104181557](D:\JavaStudy\JavaEE\SpringCloud\images\WeChat Screenshot_20201104181557.png)

##### 将服务注册进Nacos，并用Sentinel监控

Demo ： cloudalibaba-sentinel-provider8401

POM文件

```xml
    <dependencies>
        <!-- 后续做Sentinel的持久化会用到的依赖 -->
        <dependency>
            <groupId>com.alibaba.csp</groupId>
            <artifactId>sentinel-datasource-nacos</artifactId>
        </dependency>
        <!-- sentinel  -->
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-sentinel</artifactId>
        </dependency>
        <!-- springcloud alibaba nacos 依赖,Nacos Server 服务注册中心 -->
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
        </dependency>
        <!-- springboot整合Web组件 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>

        <!-- 日常通用jar包 -->
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency><!-- 引入自己定义的api通用包，可以使用Payment支付Entity -->
            <groupId>com.cloud.springcloud</groupId>
            <artifactId>cloud-api-commons</artifactId>
            <version>${project.version}</version>
        </dependency>
    </dependencies>
```

YML文件

```yml
server:
  port: 8401
spring:
  application:
    name: cloudalibaba-sentinel-provider
  cloud:
    nacos:
      discovery:
        # 服务注册中心
        server-addr: localhost:8848
    sentinel:
      transport:
        # 配置 Sentinel Dashboard 的地址
        dashboard: localhost:8080
        # 默认8719 ，如果端口被占用，端口号会自动 +1，提供给 sentinel 的监控端口
        port: 8719
# 暴露监控端点 图形化界面都会用得到  actuator包 spring-boot-starter-actuator
management:
  endpoints:
    web:
      exposure:
        include: '*'
```

主启动类

```java
@SpringBootApplication
@EnableDiscoveryClient
public class SentinelProviderMain8401 {
    public static void main(String[] args) {
        SpringApplication.run(SentinelProviderMain8401.class, args);
    }
}

```

Controller类

```java
@RestController
public class FlowLimitController {

    @GetMapping("/testA")
    public String testA(){
        return "---------testA";
    }

    @GetMapping("/testB")
    public String testB(){
        return "---------testB";
    }
}
```

> <font color=red>Sentinel采用的是懒加载的机制，注册上去的服务没有被用到，就不会显示</font>

![WeChat Screenshot_20201105102713](D:\JavaStudy\JavaEE\SpringCloud\images\WeChat Screenshot_20201105102713.png)

执行一次8401的访问之后

![WeChat Screenshot_20201105102835](D:\JavaStudy\JavaEE\SpringCloud\images\WeChat Screenshot_20201105102835.png)

##### 流控规则

![WeChat Screenshot_20201105104008](D:\JavaStudy\JavaEE\SpringCloud\images\WeChat Screenshot_20201105104008.png)

![WeChat Screenshot_20201105141214](D:\JavaStudy\JavaEE\SpringCloud\images\WeChat Screenshot_20201105141214.png)

流控效果

> 预热  公式：<font color=red>阈值除以coldFactor(默认值为3)，经过预热时长后才会达到阈值</font>

![yure](D:\JavaStudy\JavaEE\SpringCloud\images\yure.png)

![WeChat Screenshot_20201105152054](D:\JavaStudy\JavaEE\SpringCloud\images\WeChat Screenshot_20201105152054.png)

![WeChat Screenshot_20201105152123](D:\JavaStudy\JavaEE\SpringCloud\images\WeChat Screenshot_20201105152123.png)

匀速排队

![WeChat Screenshot_20201105152459](D:\JavaStudy\JavaEE\SpringCloud\images\WeChat Screenshot_20201105152459.png)

##### 熔断降级

![WeChat Screenshot_20201105160031](D:\JavaStudy\JavaEE\SpringCloud\images\WeChat Screenshot_20201105160031.png)

![WeChat Screenshot_20201105160126](D:\JavaStudy\JavaEE\SpringCloud\images\WeChat Screenshot_20201105160126.png)

![WeChat Screenshot_20201105160226](D:\JavaStudy\JavaEE\SpringCloud\images\WeChat Screenshot_20201105160226.png)

![WeChat Screenshot_20201105160240](D:\JavaStudy\JavaEE\SpringCloud\images\WeChat Screenshot_20201105160240.png)

![sentinelrongduanandjiangji](D:\JavaStudy\JavaEE\SpringCloud\images\sentinelrongduanandjiangji.png)

> Sentinel断路器没有半开状态

##### 热点key限流

Controller层的Demo

```java
	@GetMapping("/testHotKey")
    @ResponseBody
    @SentinelResource(value = "testHotKey",blockHandler = "deal_testHotKey")
	//这个value是随意的值，并不和请求路径必须一致
    //在填写热点限流的 资源名 这一项时，可以填 /testhotkey 或者是 @SentinelResource的value的值
    public String testHotKey(@RequestParam(value = "p1",required = false) String p1,
                             @RequestParam(value = "p2") String p2){
        return "----testHotKey";
    }
	//类似Hystrix 的兜底方法
    public String deal_testHotKey(String p1, String p2, BlockException e){
        return "-----testHotKey_SentinelResource";
    }
```

![WeChat Screenshot_20201105175535](D:\JavaStudy\JavaEE\SpringCloud\images\WeChat Screenshot_20201105175535.png)

![WeChat Screenshot_20201105175551](D:\JavaStudy\JavaEE\SpringCloud\images\WeChat Screenshot_20201105175551.png)

![WeChat Screenshot_20201105180915](D:\JavaStudy\JavaEE\SpringCloud\images\WeChat Screenshot_20201105180915.png)

##### 系统规则

> 对全局有效，不是针对某个rest地址有效

#####SentinelResource注解

> 按资源名称限流

修改8401Module，添加Controller

```java
@RestController
public class RateLimitController {

    @GetMapping("byResource")
    @SentinelResource(value = "byResource",blockHandler = "handlerException")
    public CommonResult byResource(){
        return new CommonResult(200, "按资源名称限流测试成功",
                new Payment(2020L, UUID.randomUUID().toString()));
    }
    //降级方法
    private CommonResult handlerException(){
        return new CommonResult(100, "按资源名称限流测试失败");
    }
}

```

> 很明显，上面虽然自定义了兜底方法，但是耦合度太高，下面要解决这个问题。而且代码膨胀

客户自定义Controller

```java
 @GetMapping("/customerBlockHandler")
    @SentinelResource(value = "customerBlockHandler",
            blockHandlerClass = CustomerBlockHandler.class,
            blockHandler = "handlerException"
        )
    public CommonResult customerBlockHandler(){
        return new CommonResult(200,"按客户自定义处理方法", new Payment(2021L, UUID.randomUUID().toString()));
    }
```

CustomerBlockHandler

```java
public class CustomerBlockHandler {

    public static CommonResult handlerException(BlockException e){
        return new CommonResult(100, e.getClass() + "自定义方法异常 global",new Payment
                (2021L, UUID.randomUUID().toString()));
    }

    public static CommonResult handlerException2(BlockException e){
        return new CommonResult(100, e.getClass() + "自定义方法异常2 global",new Payment
                (2021L, UUID.randomUUID().toString()));
    }
}

```

> 也可以使用代码去定义流控规则

##### 服务熔断

order84 yml配置

```yml
server:
  port: 84

spring:
  application:
    name: consumer-sentinel-order
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848
    # 固定的sentinel配置
    sentinel:
      transport:
        # 配置 Sentinel Dashboard 的地址
        dashboard: localhost:8080
        # 默认8719 ，如果端口被占用，端口号会自动 +1，提供给 sentinel 的监控端口
        port: 8719
```

POM文件

```xml
<dependencies>
    	<!-- 如果用到sentinel  datasource-nacos 和 alibaba-sentinel的依赖组合导入 -->
        <!-- 后续做Sentinel的持久化会用到的依赖 -->
        <dependency>
            <groupId>com.alibaba.csp</groupId>
            <artifactId>sentinel-datasource-nacos</artifactId>
        </dependency>
        <!-- sentinel  -->
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-sentinel</artifactId>
        </dependency>
        <!-- springcloud alibaba nacos 依赖 -->
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
        </dependency>

        <!-- springboot整合Web组件 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>

        <!-- 日常通用jar包 -->
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency><!-- 引入自己定义的api通用包，可以使用Payment支付Entity -->
            <groupId>com.cloud.springcloud</groupId>
            <artifactId>cloud-api-commons</artifactId>
            <version>${project.version}</version>
        </dependency>
    </dependencies>
```

config类

```java
@Configuration
public class Myconfig {

    @Bean
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }
}
```

Controller类

```java
@RestController
public class OrderController {
    private static final String SERVER_URL = "http://cloudalibaba-provider-nacos";

    @Resource
    private RestTemplate restTemplate;

    @GetMapping("/consumer/nacos/ribbon/{id}")
    @SentinelResource(fallback = "fallback")
    public CommonResult consumerNacos(@PathVariable("id") Integer id){
        CommonResult result = restTemplate.getForObject(SERVER_URL + "/nacos/ribbon/" + id, CommonResult.class);
        //这就是java的运行时异常
        if(id == 4){
            throw new IllegalArgumentException("非法参数异常....");
        }else if(result.getData() == null){
            throw new NullPointerException("空指针异常....");
        }
        return result;
    }

    //fallbackHandler
    public CommonResult handlerFallback(@PathVariable("id") Long id,Throwable e){
        Payment payment = new Payment(id, "null");
        return new CommonResult(100, "fallback兜底异常" + e.getMessage(),payment);
    }
}
```

> <font color=red>fallback 管运行异常 
> blockHandler 管配置违规，如果都配置了，都会有效果，java异常找fallback，配置异常找blockHandler</font>

OpenFeign

1、POM文件

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
```

2、YML文件

```yml
# 超级重要，激活sentinel对feign的支持
feign:
  sentinel:
    enabled: true
```

3、主启动类添加注解 ： @EnableFeignClients  激活open-feign

![Snipaste_2020-11-13_17-00-06](D:\JavaStudy\JavaEE\SpringCloud\images\Snipaste_2020-11-13_17-00-06.png)

4、Service接口类

```java
@FeignClient(value = "cloudalibaba-provider-nacos",fallback = PaymentFallbackService.class)
public interface PaymentService {

    @GetMapping("/nacos/ribbon/{id]")
    public CommonResult providerRibbon(@PathVariable("id")Long id);
}
```

5、Service实现类

```java
@Component
public class PaymentFallbackService implements PaymentService {
    @Override
    public CommonResult providerRibbon(Long id) {
        return new CommonResult(100, "OpenFeign的兜底方法,服务降级返回",new Payment(id,"null"));
    }
}

```

<img src="D:\JavaStudy\JavaEE\SpringCloud\images\Snipaste_2020-11-16_10-14-30.png" alt="Snipaste_2020-11-16_10-14-30" style="zoom: 80%;" />

Controller类(添加内容)

```java
    @Resource
    private PaymentService paymentService;
	/**
     * OpenFeign
    */
    @GetMapping("/consumer/nacos/ribbon/{id}")
    public CommonResult providerRibbon(@PathVariable("id")Long id){
        return paymentService.providerRibbon(id);
    }
```



6、测试，关闭提供者的项目，会触发service实现类的方法

7、总结：这个全局熔断，是针对“访问提供者”这个过程的，只有访问提供者过程中发生异常才会触发降级，也就是这些降级，<font color=red>是给service接口上这些提供者的方法加的</font>，以保证在远程调用时能顺利进行。而且这明显是 fallback，而不是 blockHandler。

熔断框架的比较：

> Sentinel    Hystrix  



![Snipaste_2020-11-17_09-45-09](D:\JavaStudy\JavaEE\SpringCloud\images\Snipaste_2020-11-17_09-45-09.png)

#####Sentinel规则持久化

> 将限流规则保存到Nacos，可以但不仅限于Nacos，从而不需要每次重启sentinel之后重新配置规则

修改cloudalibaba-sentinel-provider8401

POM

```xml
  <!-- 后续做Sentinel的持久化会用到的依赖 -->
        <dependency>
            <groupId>com.alibaba.csp</groupId>
            <artifactId>sentinel-datasource-nacos</artifactId>
        </dependency>
```

YML

```yml
spring:
  cloud:
    sentinel:
      datasource:
        ds1:  
          nacos:
            server-addr: localhost:8848
            dataId: ${spring.application.name}
            group: DEFAULT_GROUP
            data-type: json
            rule-type: flow
```

在nacos上配置一个dataID名为YML配置文件中对应的dataId

```json
{
    {
        "resource": "/testA",
        "limitApp": "default",
        "grade": 1,
        "count": 1,
        "strategy": 0,
        "controlBehavior": 0,
        "clusterMode": false
    }
}
```

对应的字段的解释

![Snipaste_2020-11-17_10-14-24](D:\JavaStudy\JavaEE\SpringCloud\images\Snipaste_2020-11-17_10-14-24.png)

####Seata

> Seate 处理分布式事务。
>
> 微服务模块，连接多个数据库，多个数据源，而数据库之间的数据一致性需要被保证。
>
> 官网：  http://seata.io/zh-cn/ 
>
> Seata的一ID + 3组件

![Snipaste_2020-11-17_10-46-35](D:\JavaStudy\JavaEE\SpringCloud\images\Snipaste_2020-11-17_10-46-35.png)

处理过程

![Snipaste_2020-11-17_10-51-47](D:\JavaStudy\JavaEE\SpringCloud\images\Snipaste_2020-11-17_10-51-47.png)

下载

> GitHub网址：https://github.com/seata/seata/releases

![Snipaste_2020-11-17_11-13-32](D:\JavaStudy\JavaEE\SpringCloud\images\Snipaste_2020-11-17_11-13-32.png)

##### 案例

> 创建数据库、创建数据表的sql脚本

```sql
create database seata_order;
create database seata_storage;
create database seata_account;

CREATE TABLE `t_order`  (
  `int` bigint(11) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `product_id` bigint(11) DEFAULT NULL COMMENT '产品id',
  `count` int(11) DEFAULT NULL COMMENT '数量',
  `money` decimal(11, 0) DEFAULT NULL COMMENT '金额',
  `status` int(1) DEFAULT NULL COMMENT '订单状态:  0:创建中 1:已完结',
  PRIMARY KEY (`int`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '订单表' ROW_FORMAT = Dynamic;

CREATE TABLE `t_storage`  (
  `int` bigint(11) NOT NULL AUTO_INCREMENT,
  `product_id` bigint(11) DEFAULT NULL COMMENT '产品id',
  `total` int(11) DEFAULT NULL COMMENT '总库存',
  `used` int(11) DEFAULT NULL COMMENT '已用库存',
  `residue` int(11) DEFAULT NULL COMMENT '剩余库存',
  PRIMARY KEY (`int`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '库存' ROW_FORMAT = Dynamic;
INSERT INTO `t_storage` VALUES (1, 1, 100, 0, 100);


CREATE TABLE `t_account`  (
  `id` bigint(11) NOT NULL COMMENT 'id',
  `user_id` bigint(11) DEFAULT NULL COMMENT '用户id',
  `total` decimal(10, 0) DEFAULT NULL COMMENT '总额度',
  `used` decimal(10, 0) DEFAULT NULL COMMENT '已用余额',
  `residue` decimal(10, 0) DEFAULT NULL COMMENT '剩余可用额度',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '账户表' ROW_FORMAT = Dynamic;
INSERT INTO `t_account` VALUES (1, 1, 1000, 0, 1000);
```

> 创建回滚日志的sql脚本

```sql
CREATE TABLE `undo_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `branch_id` bigint(20) NOT NULL,
  `xid` varchar(100) NOT NULL,
  `context` varchar(128) NOT NULL,
  `rollback_info` longblob NOT NULL,
  `log_status` int(11) NOT NULL,
  `log_created` datetime NOT NULL,
  `log_modified` datetime NOT NULL,
  `ext` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
```

订单模块

> 实现 下订单-> 减库存 -> 扣余额 -> 改（订单）状态
>
> 需要注意的是，下面做了 seata 与 mybatis 的整合，所以注意一下，和以往的mybatis的使用不太一样。

创建 cloudalibaba-seata-order2001

POM文件

```xml
<dependencies>
        <!-- seata -->
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-seata</artifactId>
            <exclusions>
                <exclusion>
                    <artifactId>seata-all</artifactId>
                    <groupId>io.seata</groupId>
                </exclusion>
            </exclusions>
        </dependency>
    	<!-- 剔除seata自带的版本，加入本次启动的seata服务端版本 -->
        <dependency>
            <groupId>io.seata</groupId>
            <artifactId>seata-all</artifactId>
            <version>1.0.0</version>
        </dependency>
        <!-- springcloud alibaba nacos 依赖,Nacos Server 服务注册中心 -->
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
        </dependency>

        <!-- open feign 服务调用 -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-openfeign</artifactId>
        </dependency>

        <!-- springboot整合Web组件 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>

        <!-- 持久层支持 -->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid-spring-boot-starter</artifactId>
            <version>1.1.10</version>
        </dependency>
        <!--mysql-connector-java-->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>
        <!--jdbc-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-jdbc</artifactId>
        </dependency>
        <!-- mybatis -->
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
        </dependency>

        <!-- 日常通用jar包 -->
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency><!-- 引入自己定义的api通用包，可以使用Payment支付Entity -->
            <groupId>com.cloud.springcloud</groupId>
            <artifactId>cloud-api-commons</artifactId>
            <version>${project.version}</version>
        </dependency>
    </dependencies>
```

YML文件

```yml
server:
  port: 2001
spring:
  application:
    name: seata-order-service
  cloud:
    alibaba:
      seata:
        # 自定义事务组，需要和当时在 seata/conf/file.conf 中的一致
        tx-service-group: yxn_tx_group
    nacos:
      discovery:
        server-addr: localhost:8848
  datasource:
    driver-class-name: com.mysql.jdbc.Driver
    url: jdbc:mysql://localhost:3307/seata_order
    username: root
    password: ******


# 注意，这是自定义的，原来的是mapper_locations
mybatis:
  mapperLocations: classpath:mapper/*.xml

logging:
  level:
    io:
      seata: info
```

> 在order项目的resource下，将file.conf和registry.conf拷贝到里面
>
> 创建两个domain实体类：CommonResult和Order

Dao层

```java
@Mapper
public interface OrderDao {
    /**
     * 1、新建订单
     * @param order
     */
    void create(Order order);

    /**
     * 2、修改订单状态：从0-1
     * @param userId
     * @param status
     * @return
     */
    int update(@Param("userId")Long userId,@Param("status")Integer status);
}
```

Mapper文件

```xml
<?xml version="1.0" encoding="utf-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="com.scalibaba.Dao.OrderDao">

    <resultMap id="orderMap" type="com.scalibaba.domain.Order">
        <id property="id" column="id" jdbcType="BIGINT"></id>
        <result column="user_id" property="userId" jdbcType="BIGINT"></result>
        <result column="product_id" property="productId" jdbcType="BIGINT"></result>
        <result column="count" property="count" jdbcType="INTEGER"></result>
        <result column="money" property="money" jdbcType="DECIMAL"></result>
        <result column="status" property="status" jdbcType="INTEGER"></result>
    </resultMap>

    <insert id="create">
        insert into t_order(id,user_id,product_id,count,mone,status)
        values(null,#{userId},#{productId},#{count},#{money},0);
    </insert>

    <update id="update">
        update t_order set status = 1
        where user_id = #{userId} and status = #{status};
    </update>
</mapper>
```

Service层

> 除了orderService之外，其他service类都是通过OpenFeign来进行分布式调用

orderService

```java
public interface OrderService {
    /**
     * 创建订单
     * @param order
     */
    void create(Order order);
}
```

AccountService

```java
@FeignClient(value = "seata-account-service")
public interface AccountService {

    /**
     * 订单接口调用账户微服务做扣减
     * @param userId
     * @param money
     * @return
     */
    @PostMapping("/account/decrease")
    CommonResult decrease(@RequestParam("userId") Long userId, @RequestParam("money") BigDecimal money);
}
```

StorageService

```java
@FeignClient(value = "seata-storage-service")
public interface StorageService {
    /**
     * 调用订单微服务，进行对商品的扣减
     * @param productId
     * @param count
     * @return
     */
    @PostMapping(value = "/storage/decrease")
    CommonResult decrease(@RequestParam("productId") Long productId, @RequestParam("count")Integer count);
}

```

OrderServiceImpl

```java
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderDao orderDao;
    @Resource
    private StorageService storageService;
    @Resource
    private AccountService accountService;

    @Override
    public void create(Order order) {
        //1、创建订单
        log.info("创建订单...");
        orderDao.create(order);

        //2、调用库存微服务，扣减商品数量
        log.info("订单微服务开始调用库存微服务...，做扣减");
        storageService.decrease(order.getProductId(), order.getCount());
        log.info("订单微服务调用库存微服务扣减数量结束...");

        //3、调用账户微服务，扣减money
        log.info("修改账户money 开始...");
        accountService.decrease(order.getUserId(), order.getMoney());
        log.info("修改账户money 结束....");

        //4、修改订单状态，从零到1 1表示已完成
        log.info("修改订单状态开始...");
        orderDao.update(order.getUserId(),0);
        log.info("修改订单状态结束....");
        log.info("下单成功...");
    }
}
```

Controller

```java
@RestController
public class OrderController {

    @Resource
    private OrderService orderService;

    /**
     * 之所以用 GetMapping 是因为浏览器无法发送post请求，所以用OpenFeign底层实现POST
     * @param order
     * @return
     */
    @GetMapping("/order/create")
    public CommonResult create(Order order){
        orderService.create(order);
        return new CommonResult(200, "创建订单成功..");
    }
}
```

config

```java
//下面是两个配置类，这个是和mybatis整合需要的配置
@Configuration
@MapperScan({"com.dkf.springcloud.alibaba.dao"})
public class MybatisConfig {
}

//这个是配置使用 seata 管理数据源，所以必须配置
import com.alibaba.druid.pool.DruidDataSource;
import io.seata.rm.datasource.DataSourceProxy;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

/**
 * @author lisw
 * @create 2020/11/17
 */
@Configuration
public class DataSourceProxyConfig {
    @Value("${mybatis.mapperLocations}")
    private String mapperLocations;

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource druidDataSource(){
        return new DruidDataSource();
    }

    @Bean
    public DataSourceProxy dataSourceProxy(DataSource dataSource){
        return new DataSourceProxy(dataSource);
    }

    @Bean
    public SqlSessionFactory sqlSessionFactoryBean(DataSourceProxy dataSourceProxy) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSourceProxy);
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(mapperLocations));
        sqlSessionFactoryBean.setTransactionFactory(new SpringManagedTransactionFactory());
        return sqlSessionFactoryBean.getObject();
    }
}

```

先启动 nacos  --》 再启动 seata --> 再启动此order服务，测试，可以启动。

仿照上面 创建  cloudalibaba-seata-storage2002 和  cloudalibaba-seata-account2003 两个模块，唯一大的区别就是这两个不需要导入 open-feign 远程调用其它模块。

##### Seata的使用

```java
//添加这个注解，如果出现异常，就进行数据回滚，从而实现分布式事务
@GlobalTransactional(name = "yxn-create-order",rollbackFor = Exception.class)
    public void create(Order order) {
        //1、创建订单
        log.info("创建订单...");
        orderDao.create(order);

        //2、调用库存微服务，扣减商品数量
        log.info("订单微服务开始调用库存微服务...，做扣减");
        storageService.decrease(order.getProductId(), order.getCount());
        log.info("订单微服务调用库存微服务扣减数量结束...");

        //3、调用账户微服务，扣减money
        log.info("修改账户money 开始...");
        accountService.decrease(order.getUserId(), order.getMoney());
        log.info("修改账户money 结束....");

        //4、修改订单状态，从零到1 1表示已完成
        log.info("修改订单状态开始...");
        orderDao.update(order.getUserId(),0);
        log.info("修改订单状态结束....");
        log.info("下单成功...");
    }
```

![Snipaste_2020-11-18_11-09-56](D:\JavaStudy\JavaEE\SpringCloud\images\Snipaste_2020-11-18_11-09-56.png)

![Snipaste_2020-11-18_11-12-36](D:\JavaStudy\JavaEE\SpringCloud\images\Snipaste_2020-11-18_11-12-36.png)

###### Seata三阶段

> <font color=red>一阶段加载---二阶段提交----二阶段回滚</font>

![Snipaste_2020-11-18_11-16-32](D:\JavaStudy\JavaEE\SpringCloud\images\Snipaste_2020-11-18_11-16-32.png)

![Snipaste_2020-11-18_11-35-48](D:\JavaStudy\JavaEE\SpringCloud\images\Snipaste_2020-11-18_11-35-48.png)

![Snipaste_2020-11-18_14-24-54](D:\JavaStudy\JavaEE\SpringCloud\images\Snipaste_2020-11-18_14-24-54.png)

![Snipaste_2020-11-18_11-38-32](D:\JavaStudy\JavaEE\SpringCloud\images\Snipaste_2020-11-18_11-38-32.png)

> 二阶段回顾的反向补偿就是记录对beforeImage的一个恢复，还原之前的数据，但是如果高并发情况下，会有可能出现脏写的情况，所以要再查询一边当前的记录值，进行比对。如果一直就进行反向补偿