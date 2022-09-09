package kim.nzxy.robin.util;

/**
 * 普普通通的工具类
 *
 * @author xy
 * @since 2021/6/6
 */
public class RobinUtil {
    /**
     * @return 返回当前秒级时间戳
     */
    public static int now() {
        return (int) (System.currentTimeMillis() / 1000);
    }
}
