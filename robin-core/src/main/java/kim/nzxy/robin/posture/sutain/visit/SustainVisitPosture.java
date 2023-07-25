package kim.nzxy.robin.posture.sutain.visit;

import kim.nzxy.robin.config.RobinManagement;
import kim.nzxy.robin.metadata.RobinMetadata;
import kim.nzxy.robin.posture.BuiltInEffort;
import kim.nzxy.robin.posture.RobinPosture;
import lombok.extern.slf4j.Slf4j;

/**
 * 持续访问控制
 *
 * @author lyun-chn
 * @since 2022/9/1 9:00
 */
@RobinPosture.PostureConfig(key = "sustain")
@Slf4j
public class SustainVisitPosture implements RobinPosture {
    @Override
    public boolean handler(RobinMetadata robinMetadata) {
        BuiltInEffort.SustainVisit expandEffort = getExpandEffort(robinMetadata.getTopic());
        if (expandEffort.getMaxTimes() < 1) {
            log.debug("直接禁止访问: {}", robinMetadata);
            return false;
        }
        return RobinManagement.getCacheHandler()
                .sustainVisit(robinMetadata, expandEffort.getTimeFrameSize(), expandEffort.getMaxTimes());
    }
}
