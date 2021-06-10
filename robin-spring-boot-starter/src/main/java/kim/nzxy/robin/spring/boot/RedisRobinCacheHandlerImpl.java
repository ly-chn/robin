package kim.nzxy.robin.spring.boot;

import kim.nzxy.robin.config.Constant;
import kim.nzxy.robin.config.RobinManagement;
import kim.nzxy.robin.enums.RobinRuleEnum;
import kim.nzxy.robin.handler.RobinCacheHandler;
import kim.nzxy.robin.util.RobinUtil;
import lombok.val;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author xy
 * @since 2021/6/5
 */
@Component
public class RedisRobinCacheHandlerImpl implements RobinCacheHandler {
    private final StringRedisTemplate template;
    /**
     * key通配符
     */
    private final String asterisk = "*";

    public RedisRobinCacheHandlerImpl(StringRedisTemplate template) {
        this.template = template;
        this.clean();
    }


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

    public void clean() {
        new Thread(()->{
            // 可能存在注入顺序问题, 故而如此
            // todo: 寻找优雅的解决方案
            while (true) {
                try {
                    //noinspection ResultOfMethodCallIgnored
                    RobinManagement.getRobinProperties().getDetail().getCache().getCleanAt();
                    break;
                } catch (Exception ignored) {
                }
            }
            for (LocalTime localTime : RobinManagement.getRobinProperties().getDetail().getCache().getCleanAt()) {
                val targetTimestamp = localTime.atDate(LocalDate.now()).toEpochSecond(ZoneOffset.ofHours(8));
                val now = RobinUtil.now();

                ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
                long oneDay = 24 * 60 * 60;
                val delay = targetTimestamp > now ? targetTimestamp-now : targetTimestamp - now+oneDay;

                executor.scheduleAtFixedRate(
                        () -> {
                            cleanLock();
                            // todo cleanAccessRecord
                        },
                        delay,
                        oneDay,
                        TimeUnit.SECONDS);
            }
        }).start();
    }

    // todo: 方案不行, 不同enum过期时间机制不一样, 比如ip的过期时间实际上不止IP自己用, 或者说整个机制都不太好, 需要重新设计
    private void cleanAccessRecord(RobinRuleEnum type, int timestamp) {
        val keys = template.keys(Constant.recordPrefix + asterisk);
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

    private void cleanLock() {
        val keys = template.opsForZSet().rangeByScore(Constant.lockedPrefix, 0, RobinUtil.now());
        if (keys == null) {
            return;
        }
        template.opsForZSet().remove(Constant.lockedPrefix, keys.toArray());
    }
}
