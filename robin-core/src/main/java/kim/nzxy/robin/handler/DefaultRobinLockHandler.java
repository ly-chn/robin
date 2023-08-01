package kim.nzxy.robin.handler;


import kim.nzxy.robin.metadata.RobinMetadata;
import kim.nzxy.robin.util.RobinUtil;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认lock实现
 *
 * @author ly-chn
 */
public class DefaultRobinLockHandler implements RobinLockHandler {

    /**
     * 封禁缓存, 格式: {topic: {metadata: 解禁时间时间戳(秒级)}}
     */
    public static final Map<String, ConcurrentHashMap<String, Integer>> LOCK_CACHE_MAP = new ConcurrentHashMap<>();

    @Override
    public void lock(RobinMetadata metadata, Duration lock) {
        if (lock.getSeconds() == 0) {
            return;
        }
        ConcurrentHashMap<String, Integer> topicMap = LOCK_CACHE_MAP.get(metadata.getTopic());
        if (!LOCK_CACHE_MAP.containsKey(metadata.getTopic())) {
            topicMap = new ConcurrentHashMap<>();
            LOCK_CACHE_MAP.put(metadata.getTopic(), topicMap);
        }
        topicMap.put(metadata.getMetadata(), Math.toIntExact(lock.getSeconds() + RobinUtil.now()));
    }

    @Override
    public boolean locked(RobinMetadata metadata) {
        if (!LOCK_CACHE_MAP.containsKey(metadata.getTopic())) {
            return false;
        }
        ConcurrentHashMap<String, Integer> topicMap = LOCK_CACHE_MAP.get(metadata.getTopic());
        Integer score = topicMap.get(metadata.getMetadata());
        return score == null || score > RobinUtil.now();
    }

    @Override
    public void unlock(RobinMetadata metadata) {
        if (metadata == null || metadata.getTopic() == null) {
            LOCK_CACHE_MAP.clear();
            return;
        }
        if (!LOCK_CACHE_MAP.containsKey(metadata.getTopic())) {
            return;
        }
        if (metadata.getMetadata() == null) {
            LOCK_CACHE_MAP.get(metadata.getTopic()).clear();
            return;
        }
        LOCK_CACHE_MAP.get(metadata.getTopic()).remove(metadata.getMetadata());
    }


    @Override
    public void freshenUp() {
        if (LOCK_CACHE_MAP.isEmpty()) {
            return;
        }
        int now = RobinUtil.now();
        for (ConcurrentHashMap<String, Integer> topicMap : LOCK_CACHE_MAP.values()) {
            for (String metadata : topicMap.keySet()) {
                Integer lockTo = topicMap.get(metadata);
                if (lockTo == null || lockTo < now) {
                    topicMap.remove(metadata);
                }
            }
        }
    }

}
