// package kim.nzxy.robin.data.redis;
//
// import kim.nzxy.robin.metadata.RobinMetadata;
// import kim.nzxy.robin.util.RobinUtil;
// import lombok.RequiredArgsConstructor;
// import lombok.CustomLog;
// import org.springframework.core.io.ClassPathResource;
// import org.springframework.data.redis.core.StringRedisTemplate;
// import org.springframework.data.redis.core.script.DefaultRedisScript;
// import org.springframework.scripting.support.ResourceScriptSource;
// import org.springframework.stereotype.Component;
//
// import java.time.Duration;
// import java.util.*;
//
// /**
//  * @author lyun-chn
//  * @since 2021/6/5
//  */
// @Component
// @CustomLog
// @RequiredArgsConstructor
// public class RedisRobinCacheHandlerImpl {
//     private final StringRedisTemplate redisTemplate;
//     private static final DefaultRedisScript<Boolean> SUSTAIN_VISIT_LUA;
//     private static final DefaultRedisScript<Boolean> BUCKET_LUA;
//
//     static {
//         SUSTAIN_VISIT_LUA = loadLua("sustain-visit");
//         BUCKET_LUA = loadLua("bucket");
//     }
//
//     public static DefaultRedisScript<Boolean> loadLua(String filename) {
//         DefaultRedisScript<Boolean> script = new DefaultRedisScript<>();
//         script.setScriptSource(new ResourceScriptSource(new ClassPathResource("robin-lua/"+filename+".lua")));
//         script.setResultType(Boolean.class);
//         return script;
//     }
//
//     @Override
//     public boolean sustainVisit(RobinMetadata metadata, Duration timeFrameSize, Integer maxTimes) {
//         String key = Constant.SUSTAIN_VISIT_PREFIX + metadata.getTopic();
//         int currentTimeFrame = RobinUtil.currentTimeFrame(timeFrameSize);
//         return Boolean.TRUE.equals(redisTemplate.execute(SUSTAIN_VISIT_LUA,
//                 Collections.singletonList(key),
//                 metadata.getMetadata(),
//                 String.valueOf(currentTimeFrame),
//                 maxTimes.toString(),
//                 String.valueOf(Constant.SUSTAIN_VISIT_PRECISION),
//                 Boolean.toString(log.isDebugEnabled())
//         ));
//     }
//
//     @Override
//     public boolean bucket(RobinMetadata robinMetadata, Duration generationInterval, Integer tokenCount, Integer capacity) {
//         String key = Constant.BUCKET_PREFIX + robinMetadata.getTopic();
//         int currentTimeFrame = RobinUtil.currentTimeFrame(generationInterval);
//         return Boolean.TRUE.equals(redisTemplate.execute(BUCKET_LUA,
//                 Collections.singletonList(key),
//                 robinMetadata.getMetadata(),
//                 String.valueOf(currentTimeFrame),
//                 String.valueOf(capacity),
//                 String.valueOf(tokenCount),
//                 String.valueOf(Constant.BUCKET_PRECISION),
//                 Boolean.toString(log.isDebugEnabled())
//         ));
//     }
//
//
//     private void cleanSustainVisit() {
//         /*ZSetOperations<String, String> zSetOperations = redisTemplate.opsForZSet();
//         for (Map.Entry<String, Duration> entry : SUSTAIN_TOPIC_MAP.entrySet()) {
//             zSetOperations.removeRangeByScore(entry.getKey(), 0, RobinUtil.currentTimeFrame(entry.getValue()) - 1);
//         }*/
//     }
//
//     @Override
//     public void lock(RobinMetadata metadata, Duration lock) {
//         redisTemplate.opsForZSet().add(Constant.LOCKED_PREFIX + metadata.getTopic(), metadata.getMetadata(), lock.getSeconds() + RobinUtil.now());
//     }
//
//     @Override
//     public boolean locked(RobinMetadata metadata) {
//         Double score = redisTemplate.opsForZSet()
//                 .score(Constant.LOCKED_PREFIX + metadata.getTopic(), metadata.getMetadata());
//         return score != null && score > RobinUtil.now();
//     }
//
//     @Override
//     public void unlock(RobinMetadata metadata) {
//         if (metadata != null) {
//             redisTemplate.opsForZSet().remove(Constant.LOCKED_PREFIX + metadata.getTopic(), metadata.getMetadata());
//             return;
//         }
//         // todo: null for all
//     }
//
//     @Override
//     public void freshenUp() {
//         cleanSustainVisit();
//         log.debug("sustain visit record is cleaned");
//         cleanLock();
//         log.debug("locked record is cleaned");
//     }
//
//     private void cleanLock() {
//         Set<String> keys = redisTemplate.keys(Constant.LOCKED_PREFIX + "*");
//         if (keys != null) {
//             for (String topic : keys) {
//                 redisTemplate.opsForZSet().removeRange(topic, 0, RobinUtil.now());
//             }
//         }
//     }
//
//     interface Constant {
//         /**
//          * 缓存前缀
//          */
//         String CACHE_PREFIX = "robin:";
//         /**
//          * 锁定元数据
//          */
//         String LOCKED_PREFIX = CACHE_PREFIX + "lock:";
//         /**
//          * 持续访问缓存前缀
//          */
//         String SUSTAIN_VISIT_PREFIX = CACHE_PREFIX + "sustain:";
//         /**
//          * 持续访问记录最大记录数量
//          */
//         int SUSTAIN_VISIT_PRECISION = 100000;
//         /**
//          * 持续访问缓存前缀
//          */
//         String BUCKET_PREFIX = CACHE_PREFIX + "bucket:";
//         /**
//          * 持续访问记录最大记录数量
//          */
//         int BUCKET_PRECISION = 100000;
//     }
// }
