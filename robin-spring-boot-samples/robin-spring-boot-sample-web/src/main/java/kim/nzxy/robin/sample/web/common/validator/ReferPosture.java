package kim.nzxy.robin.sample.web.common.validator;

import kim.nzxy.robin.autoconfigure.RobinEffortBasic;
import kim.nzxy.robin.posture.RobinPosture;
import org.springframework.stereotype.Component;

/**
 * 自定义策略示例-refer
 *
 * @author xy
 * @since 2021/6/9
 */
@Component
@RobinPosture.PostureConfig(key = "refer")
public class ReferPosture implements RobinPosture {

    @Override
    public void preHandle(String topic, String metadata, RobinEffortBasic basicConfig, Object validatorConfig) {
        // if (!SpringContextUtil.referer().contains("nzxy.kim")) {
        //     throw new LyException("你为什么不是来自 nzxy.kim? ");
        // }
    }
}
