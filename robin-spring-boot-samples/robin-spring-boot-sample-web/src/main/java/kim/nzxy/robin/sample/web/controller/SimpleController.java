package kim.nzxy.robin.sample.web.controller;

import kim.nzxy.robin.annotations.RobinIgnore;
import kim.nzxy.robin.annotations.RobinTopic;
import kim.nzxy.robin.sample.web.common.annocations.RobinSensitive;
import kim.nzxy.robin.util.RobinUtil;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 简单使用
 *
 * @author ly-chn
 * @since 2021/6/5
 */
@RestController
@RequestMapping("sample")
public class SimpleController {
    /**
     * 常规请求, 适用于默认配置
     */
    @GetMapping("simple")
    public String test() {
        return "success";
    }

    /**
     * 忽略, 不执行任何逻辑
     */
    @GetMapping("skip")
    @RobinIgnore
    public String skip() {
        return "success";
    }

    /**
     * 扩展配置, @RobinTopic可以写多次
     */
    @GetMapping("extra")
    @RobinTopic("ip-normal")
    @RobinTopic("ip-sensitive")
    public String extra() {
        return "success";
    }

    /**
     * 更好的用法: 注解继承
     */
    @GetMapping("extra-better")
    @RobinSensitive
    public String extraBetter() {
        return "success";
    }

    /**
     * 动态参数, 支持SpEL表达式, 支持读取方法参数
     */
    @GetMapping("dynamic/{id}")
    @RobinTopic(value = "api-sensitive", metadata = "#id")
    public String dynamicMetadata(@PathVariable String id) {
        return "dynamic" + id;
    }
}
