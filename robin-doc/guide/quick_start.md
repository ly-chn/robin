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



