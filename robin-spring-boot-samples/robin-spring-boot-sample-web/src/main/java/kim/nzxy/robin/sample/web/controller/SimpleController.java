package kim.nzxy.robin.sample.web.controller;

import kim.nzxy.robin.annotations.RobinIgnore;
import kim.nzxy.robin.annotations.RobinTopic;
import kim.nzxy.robin.sample.web.common.annocations.RobinSensitive;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 简单使用
 *
 * @author lyun-chn
 * @since 2021/6/5
 */
@RestController
@RequestMapping("sample")
public class SimpleController {
    @GetMapping("simple")
    public String test() {
        return "success";
    }

    @GetMapping("skip")
    @RobinIgnore
    public String skip() {
        return "success";
    }

    @GetMapping("extra")
    @RobinTopic("ip-sensitive")
    public String extra() {
        return "success";
    }

    @GetMapping("extra-better")
    @RobinSensitive
    public String extraBetter() {
        return "success";
    }

    @GetMapping("bucket/{id}")
    @RobinTopic("api-sensitive")
    public String bucket(@PathVariable String id) {
        return "bucket" + id;
    }
}
