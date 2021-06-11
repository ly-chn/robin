package kim.nzxy.robin.sample.web.common.validator;

import kim.nzxy.robin.validator.RobinValidator;
import kim.nzxy.robin.sample.web.common.exception.LyException;
import kim.nzxy.robin.sample.web.common.util.SpringContextUtil;
import org.springframework.stereotype.Component;

/**
 * 自定义策略示例-refer
 *
 * @author xy
 * @since 2021/6/9
 */
@Component
public class ReferValidator implements RobinValidator {
    @Override
    public void execute() {
        if (!SpringContextUtil.referer().contains("nzxy.kim")) {
            throw new LyException("你为什么不是来自 nzxy.kim? ");
        }
    }

    // @Override
    // public int getOrder() {
    //     return -1;
    // }
}
