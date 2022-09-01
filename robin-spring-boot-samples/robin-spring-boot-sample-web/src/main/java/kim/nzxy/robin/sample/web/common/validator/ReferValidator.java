package kim.nzxy.robin.sample.web.common.validator;

import kim.nzxy.robin.autoconfigure.RobinBasicStrategy;
import kim.nzxy.robin.validator.RobinValidator;
import org.springframework.stereotype.Component;

/**
 * 自定义策略示例-refer
 *
 * @author xy
 * @since 2021/6/9
 */
@Component
@RobinValidator.WithConfig(key = "refer")
public class ReferValidator implements RobinValidator {

    @Override
    public void preHandle(String topic, String metadata, RobinBasicStrategy basicConfig, Object validatorConfig) {
        // if (!SpringContextUtil.referer().contains("nzxy.kim")) {
        //     throw new LyException("你为什么不是来自 nzxy.kim? ");
        // }
    }
}
