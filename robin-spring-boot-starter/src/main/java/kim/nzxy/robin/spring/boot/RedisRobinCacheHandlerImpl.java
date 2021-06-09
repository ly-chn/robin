package kim.nzxy.robin.spring.boot;

import kim.nzxy.robin.config.Constant;
import kim.nzxy.robin.enums.RobinRuleEnum;
import kim.nzxy.robin.exception.RobinBuiltinException;
import kim.nzxy.robin.handler.RobinCacheHandler;
import kim.nzxy.robin.util.RobinUtil;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class RedisRobinCacheHandlerImpl implements RobinCacheHandler {
    private final StringRedisTemplate template;
    /**
     * key通配符
     */
    private final String asterisk = "*";

    @Override
    public void accessRecord(RobinRuleEnum type, String target, int timestamp) {
        template.opsForList().rightPushAll(Constant.recordPrefix + type + target, String.valueOf(timestamp));
    }

    @Override
    public List<Integer> accessRecord(RobinRuleEnum type, String target) {
        val stringList = template.opsForList().range(Constant.recordPrefix + type + target, 0, -1);
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

    @Override
    public void lock(RobinRuleEnum type, String target, Duration unlock) {
        template.opsForZSet().add(Constant.lockedPrefix + type, target, unlock.getSeconds() + RobinUtil.now());
    }

    @Override
    public boolean lock(RobinRuleEnum type, String target) {
        val score = template.opsForZSet().score(Constant.lockedPrefix + type, target);
        if (score == null) {
            return false;
        }
        return score > RobinUtil.now();
    }

    @Override
    public void unlock(RobinRuleEnum type, String target) {
        template.opsForZSet().remove(Constant.lockedPrefix + type, target);
    }

    @Override
    public void cleanAccessRecord(RobinRuleEnum type, int timestamp) {
        val keys = template.keys(Constant.recordPrefix + type + asterisk);
        if (keys == null) {
            return;
        }
        val now = RobinUtil.now();
        // todo: 肯定有更好的方式吧, 这也太麻烦了
        for (String key : keys) {
            val range = template.opsForList().range(key, 0, -1);
            // 理论上来说不可能为 null 避免 IDE 检查
            if (range == null) {
                continue;
            }
            val count = range.stream().filter(item -> Integer.parseInt(item) < now).count();
            for (long i = 0; i < count; i++) {
                template.opsForList().leftPop(key);
            }
        }
    }

    @Override
    public void cleanLock() {
        val keys = template.opsForZSet().rangeByScore(Constant.lockedPrefix, 0, RobinUtil.now());
        if (keys == null) {
            return;
        }
        template.opsForZSet().remove(Constant.lockedPrefix, keys.toArray());
    }
}
