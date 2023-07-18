package kim.nzxy.robin.sample.web.common.validator;

import kim.nzxy.robin.autoconfigure.RobinEffortBasic;
import kim.nzxy.robin.config.RobinMetadata;
import kim.nzxy.robin.posture.RobinPosture;
import kim.nzxy.robin.sample.web.common.exception.LyException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

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
    public boolean handler(RobinMetadata robinMetadata) {
        boolean fromNzxy = StringUtils.substringMatch(robinMetadata.getMetadata(), 0, "nzxy.kim");
        if (!fromNzxy) {
            throw new LyException("你为什么不是来自 nzxy.kim? ");
        }
        return false;
    }
}
