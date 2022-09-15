package kim.nzxy.robin.sample.web.controller;

import kim.nzxy.robin.annotations.RobinTopic;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lyun-chn
 * @since 2021/6/5
 */
@RestController
public class TestController {
    @RequestMapping
    @RobinTopic("ip-b")
    @RobinTopic("ip-c")
    public String test() {
        return "success";
    }
}
