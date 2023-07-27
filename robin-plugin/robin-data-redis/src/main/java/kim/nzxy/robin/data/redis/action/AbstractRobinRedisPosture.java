package kim.nzxy.robin.data.redis.action;

import kim.nzxy.robin.data.redis.autoconfigure.RobinRedisManage;
import kim.nzxy.robin.posture.RobinPosture;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author ly-chn
 */
public abstract class AbstractRobinRedisPosture implements RobinPosture {
    public StringRedisTemplate getStringRedisTemplate() {
        return RobinRedisManage.getStringRedisTemplate();
    }
}
