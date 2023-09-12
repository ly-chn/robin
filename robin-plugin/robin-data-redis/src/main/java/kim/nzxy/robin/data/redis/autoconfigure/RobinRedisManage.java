package kim.nzxy.robin.data.redis.autoconfigure;

import kim.nzxy.robin.data.redis.RobinRedisConfig;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * redis配置管理中心
 *
 * @author ly-chn
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RobinRedisManage {
    @Setter
    @Getter
    private static StringRedisTemplate stringRedisTemplate;
    @Getter
    @Setter
    private static RobinRedisConfig robinRedisConfig;
}
