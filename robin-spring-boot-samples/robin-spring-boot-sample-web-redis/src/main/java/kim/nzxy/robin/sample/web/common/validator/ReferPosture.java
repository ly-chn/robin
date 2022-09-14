package kim.nzxy.robin.sample.web.common.validator;

import kim.nzxy.robin.config.RobinMetadata;
import kim.nzxy.robin.posture.RobinPosture;
import org.springframework.stereotype.Component;

/**
 * 自定义策略示例-refer
 *
 * @author lyun-chn
 * @since 2021/6/9
 */
@Component
@RobinPosture.PostureConfig(key = "refer")
public class ReferPosture implements RobinPosture {

    @Override
    public boolean preHandle(RobinMetadata robinMetadata) {
        // Object expandEffort = getExpandEffort(robinMetadata.getTopic());
        // if (!SpringContextUtil.referer().contains("nzxy.kim")) {
        //     throw new LyException("你为什么不是来自 nzxy.kim? ");
        // }
        return false;
    }
}
