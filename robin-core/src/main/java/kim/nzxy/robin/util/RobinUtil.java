package kim.nzxy.robin.util;

import java.time.Duration;
import java.util.Objects;

/**
 * 普普通通的工具类
 *
 * @author lyun-chn
 * @since 2021/6/6
 */
public class RobinUtil {
    /**
     * @return 返回当前秒级时间戳
     */
    public static int now() {
        return (int) (System.currentTimeMillis() / 1000);
    }

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

    public static <T> boolean contains(T[] arr, T target) {
        for (T t : arr) {
            if (Objects.equals(t, target)) {
                return true;
            }
        }
        return false;
    }
}
