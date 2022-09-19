package kim.nzxy.robin.sample.web.controller;

import kim.nzxy.robin.annotations.RobinSkip;
import kim.nzxy.robin.annotations.RobinTopic;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 简单使用
 * @author lyun-chn
 * @since 2021/6/5
 */
@RestController
@RequestMapping("sample")
public class SimpleController {
    @RequestMapping("simple")
    public String test() {
        return "success";
    }
    @RequestMapping("skip")
    @RobinSkip
    public String skip() {
        return "success";
    }
    @RequestMapping("extra")
    @RobinTopic("ip-b")
    public String extra() {
        return "success";
    }
}