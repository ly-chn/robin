package kim.nzxy.robin.util;

import java.time.Duration;

/**
 * 时间窗口相关工具类
 *
 * @author xuyf
 * @since 2022/8/30 11:45
 */
public class RobinTimeFrameUtil {
    /**
     * 计算当前时间是第几个时间窗口（根据基准时间）
     *
     * @param frameSize 时间窗口大小
     * @return 此时此刻是第n个时间窗口（根据基准时间）
     */
    public static int currentTimeFrame(Duration frameSize) {
        long seconds = frameSize.getSeconds();
        // todo： 基准时间
        return Math.toIntExact(RobinUtil.now() / seconds);
    }
}
