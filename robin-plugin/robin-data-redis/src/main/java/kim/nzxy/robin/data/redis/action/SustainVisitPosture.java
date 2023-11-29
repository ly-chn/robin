package kim.nzxy.robin.data.redis.action;

import kim.nzxy.robin.data.redis.util.RobinLuaUtil;
import kim.nzxy.robin.factory.RobinEffortFactory;
import kim.nzxy.robin.metadata.RobinMetadata;
import kim.nzxy.robin.posture.RobinPosture;
import kim.nzxy.robin.posture.config.BuiltInEffort;
import kim.nzxy.robin.posture.config.BuiltInEffortConstant;
import kim.nzxy.robin.util.RobinUtil;
import lombok.CustomLog;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * 持续访问控制
 *
 * @author ly-chn
 * @since 2022/9/1 9:00
 */
@RobinPosture.PostureConfig(key = BuiltInEffort.Fields.sustain)
@CustomLog
public class SustainVisitPosture extends AbstractRobinRedisPosture {
    private static final DefaultRedisScript<Boolean> SUSTAIN_VISIT_LUA = RobinLuaUtil.loadBool("sustain-visit");
    private static final DefaultRedisScript<Boolean> SUSTAIN_VISIT_CLEAN_LUA = RobinLuaUtil.loadBool("sustain-visit-clean");


    @Override
    public boolean handler(RobinMetadata robinMetadata) {
        BuiltInEffort.SustainVisit effort = getExpandEffort(robinMetadata.getTopic());
        String key = Constant.SUSTAIN_VISIT_PREFIX + robinMetadata.getTopic();
        int currentTimeFrame = RobinUtil.currentTimeFrame(effort.getTimeFrameSize());
        return Boolean.TRUE.equals(getStringRedisTemplate().execute(SUSTAIN_VISIT_LUA,
                Collections.singletonList(key),
                robinMetadata.getMetadata(),
                String.valueOf(currentTimeFrame),
                effort.getMaxTimes().toString(),
                String.valueOf(BuiltInEffortConstant.SUSTAIN_VISIT_PRECISION),
                Boolean.toString(log.isDebugEnabled())
        ));
    }

    @Override
    public void freshenUp() {
        Set<String> topicSet = RobinEffortFactory.getTopicByKey(BuiltInEffort.Fields.sustain);
        if (topicSet.isEmpty()) {
            return;
        }
        List<String> keys = new ArrayList<>();
        List<String> values = new ArrayList<>();
        topicSet.forEach(topic -> {
            keys.add(topic);
            BuiltInEffort.SustainVisit sustainVisit = getExpandEffort(topic);
            int currentTimeFrame = RobinUtil.currentTimeFrame(sustainVisit.getTimeFrameSize());
            values.add(String.valueOf(currentTimeFrame));
        });
        getStringRedisTemplate().execute(SUSTAIN_VISIT_CLEAN_LUA, keys, values.toArray());
    }

    interface Constant {
        /**
         * 缓存前缀
         */
        String CACHE_PREFIX = "robin:";
        /**
         * 持续访问缓存前缀
         */
        String SUSTAIN_VISIT_PREFIX = CACHE_PREFIX + "sustain:";
    }
}
