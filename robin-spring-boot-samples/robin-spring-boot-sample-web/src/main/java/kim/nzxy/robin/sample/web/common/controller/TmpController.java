package kim.nzxy.robin.sample.web.common.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xy
 * @since 2021/6/5
 */
@RestController
public class TmpController {
    @RequestMapping
    public String tmp() {
        return "success";
    }
}
