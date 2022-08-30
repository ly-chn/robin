package kim.nzxy.robin.validator;

import kim.nzxy.robin.config.RobinManagement;
import kim.nzxy.robin.enums.RobinRuleEnum;
import kim.nzxy.robin.util.RobinAssert;
import kim.nzxy.robin.util.RobinUtil;
import lombok.val;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * IP 持续访问控制
 *
 * @author xy
 * @since 2021/6/15
 */
public class ContinuousVisitValidator implements RobinValidator {
    /**
     * todo: 放到配置类，统一管理
     * 用于计算时间窗口的基础时间
     */
    private final LocalDateTime baseTime = LocalDate.ofYearDay(2021, 1).atStartOfDay();

    @Override
    public void execute() {
        val cacheHandler = RobinManagement.getCacheHandler();
        val ip = RobinManagement.getContextHandler().ip();
        val properties = RobinManagement.getRobinProperties().getContinuousVisit();
        val durationSeconds = properties.getDuration().getSeconds();
        val times = properties.getTimes();
        // todo: 取第一个，再取list的大小即可，没必要全取出来
        // 断言已被禁用
        RobinAssert.assertLocked(RobinRuleEnum.CONTINUOUS_VISIT, ip);
        val accessRecord = cacheHandler.getAccessRecord(RobinRuleEnum.CONTINUOUS_VISIT, ip, times - 1);
        // 当前时间窗口结束时间
        val timeFrameEndSeconds = calcTimeFrameEndSeconds(durationSeconds);
        val expire = timeFrameEndSeconds + ((int) durationSeconds * times);
        RobinAssert.assertRobinException(
                accessRecord.size() == times - 1
                        && accessRecord.get(0) == expire,
                RobinRuleEnum.CONTINUOUS_VISIT,
                ip,
                () -> cacheHandler.lock(RobinRuleEnum.CONTINUOUS_VISIT, ip, properties.getUnlock()));
        if (!accessRecord.contains(expire)) {
            cacheHandler.accessRecord(RobinRuleEnum.CONTINUOUS_VISIT, ip, expire);
        }
    }

    /**
     * 计算当前时间窗口结束时间
     *
     * @return 时间窗口结束时间
     */
    private int calcTimeFrameEndSeconds(Long duration) {
        val now = RobinUtil.now();
        val baseSeconds = baseTime.toInstant(ZoneOffset.ofHours(8)).toEpochMilli() / 100;
        return (int) ((((now - baseSeconds) / duration) + 1) * duration + baseSeconds);
    }
}
