package kim.nzxy.robin.spring.boot;

import kim.nzxy.robin.config.Constant;
import kim.nzxy.robin.handler.RobinCacheHandler;
import kim.nzxy.robin.util.RobinUtil;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xy
 * @since 2021/6/5
 */
@Component
@AllArgsConstructor
public class RedisRobinCacheHandlerImpl implements RobinCacheHandler {
    private final StringRedisTemplate template;

    @Override
    public void ipAccessRecord(String ip, int timestamp) {
        template.opsForList().rightPushAll(Constant.ipPrefix + ip, String.valueOf(timestamp));
    }

    @Override
    public List<Integer> ipAccessRecord(String ip) {
        val stringList = template.opsForList().range(Constant.ipPrefix + ip, 0, -1);
        if (stringList != null) {
            return stringList.stream().map(Integer::valueOf).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public void lockIp(String ip, Duration unlock) {
        template.opsForZSet().add(Constant.ipLockedPrefix, ip, unlock.getSeconds() + RobinUtil.now());
    }

    @Override
    public boolean lockIp(String ip) {
        val score = template.opsForZSet().score(Constant.ipLockedPrefix, ip);
        if (score == null) {
            return false;
        }
        return score > RobinUtil.now();
    }
}
