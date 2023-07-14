package kim.nzxy.robin.posture.sutain.visit;

import kim.nzxy.robin.config.RobinManagement;
import kim.nzxy.robin.config.RobinMetadata;
import kim.nzxy.robin.posture.BuiltInEffort;
import kim.nzxy.robin.posture.RobinPosture;

/**
 * 持续访问控制
 *
 * @author lyun-chn
 * @since 2022/9/1 9:00
 */
@RobinPosture.PostureConfig(key = "sustain")
public class SustainVisitPosture implements RobinPosture {
    @Override
    public boolean handler(RobinMetadata robinMetadata) {
        BuiltInEffort.SustainVisit expandEffort = getExpandEffort(robinMetadata.getTopic());
        return RobinManagement.getCacheHandler().sustainVisit(robinMetadata, expandEffort.getTimeFrameSize()) <= expandEffort.getMaxTimes();
    }
}
