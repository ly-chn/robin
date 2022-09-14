package kim.nzxy.robin.sample.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lyun-chn
 * @since 2021/6/5
 */
@RestController
public class TestController {
    @RequestMapping
    public String test() {
        return "success";
    }
}
