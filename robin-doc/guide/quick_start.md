## 快速开始

目前提供的[示例](https://github.com/ly-chn/robin/tree/master/robin-spring-boot-samples)是比较生硬的, 直接跑起来可能会产生疑惑, 所以在此进行梳理

本实例默认技能: 

- `IDEA` ( eclipse 与 IDEA 之间的区别)
- 熟悉`Spring Boot 2.x` Web
- 熟悉`Apache Maven`
- 了解爬虫

---

### 初始化项目

可以使用`IDEA`自带的`Spring Initializr`插件, 或者使用 [Spring Initializer](https://start.spring.io/) 快速初始化一个 Spring Boot 项目

### 添加依赖

```xml
<dependency>
    <groupId>kim.nzxy</groupId>
    <artifactId>robin-spring-boot-starter</artifactId>
    <version>最新版本</version>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-configuration-processor</artifactId>
    <optional>true</optional>
</dependency>
```

### 基本配置

在 `application.yml`(renamed from `application.properties`) 配中添加相关配置：

```yml
robin:
  # 启用ip访问频率限制检测
  include-rule: frequent_ip_access
# 当然如果你是localhost:6379也可以直接用缺省配置
spring:
  redis:
    host: redis hsot
    port: redis port
    or password...
```

### 编码

其实只需要添加一个默认的controller即可看出效果

```java
package kim.nzxy.robin.sample.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xy
 * @since 2021/6/5
 */
@RestController
public class TestController {
    @RequestMapping
    public String test() {
        return "success";
    }
}
```

### 查看效果

启动项目打开浏览器[访问localhost:8080](localhost:8080)将会看到`success`的返回

而如果你在一分钟内访问达到了一定次数(默认是10次), 看到的将会是一个错误页面

如果使用的示例demo的异常拦截, 将会看到类似下面的内容: 

```json
{"message":"IP 访问频繁: 127.0.0.1","code":500,"data":null,"list":null}
```

这里你可能会疑惑: 

>  每分钟10次调用够谁用的, 127.0.0.1也拦截, 什么垃圾设计? 

简单讲一下这些疑惑: 

> 首先框架并不知道你的系统是否足够大, 故而也无法为你设置一个合理的值, 比如每分钟10次, 比如内网访问是否应当作出限制, 所以这里应当由框架的使用者来动态管理这些内容. 具体请参考每个策略的配置项以及框架配置.

