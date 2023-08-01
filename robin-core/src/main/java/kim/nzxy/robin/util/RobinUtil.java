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
     * 计算当前时间是第几个时间窗口
     *
     * @param frameSize 时间窗口大小
     * @return 此时此刻是第n个时间窗口
     */
    public static int currentTimeFrame(Duration frameSize) {
        long seconds = frameSize.getSeconds();
        return Math.toIntExact(RobinUtil.now() / seconds);
    }

    /**
     * 计算指定时间窗口的结束时间
     *
     * @param timeframe 指定时间窗口
     * @param frameSize 时间窗口大小
     * @return 时间窗口的结束时间
     */
    public static int timeframeEndTime(int timeframe, Duration frameSize) {
        long seconds = frameSize.getSeconds();
        return (int) (timeframe * seconds);
    }

    /**
     * 将整数部分和小数部分组装成一个小数
     * @param integerPart 整数部分
     * @param decimalPart 小数部分
     * @param precision 小数精度,表示小数点后面保留位数
     * @return 组装后的小数
     */
    public static double assembleDecimal(int integerPart, int decimalPart, int precision) {
        return integerPart + (decimalPart * Math.pow(10, -precision));
    }

    /**
     * 从一个小数中解析出整数部分和小数部分
     * @param result 需要解析的小数
     * @param precision 原始的小数精度
     * @return 整数数组,第一个元素是整数部分,第二个元素是小数部分
     */
    public static int[] disassembleDecimal(double result, int precision) {
        int integerPart = (int)result;

        double decimalPart = result - integerPart;
        decimalPart = decimalPart * Math.pow(10, precision);
        int decimalPartInt = (int)decimalPart;

        return new int[]{integerPart, decimalPartInt};
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
