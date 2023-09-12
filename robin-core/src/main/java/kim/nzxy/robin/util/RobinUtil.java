package kim.nzxy.robin.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Array;
import java.time.Duration;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * 普普通通的工具类
 *
 * @author lyun-chn
 * @since 2021/6/6
 */
public class RobinUtil {
    /**
     * 2000年1月1日0时0分0秒的时间戳
     */
    private static final int BASE_TIME = 946656000;

    /**
     * @return 返回当前秒级时间戳
     */
    public static int now() {
        return (int) (System.currentTimeMillis() / 1000) - BASE_TIME;
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

    @Contract("null -> false")
    public static boolean isNotEmpty(@Nullable Object obj) {
        return !isEmpty(obj);
    }

    /**
     * 判断对象是否为空
     * @param obj 要判断的对象, 支持Optional
     * @return 为true表示对象为null或为空
     */
    @Contract("null -> true")
    public static boolean isEmpty(@Nullable Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj instanceof CharSequence) {
            return ((CharSequence) obj).length() == 0;
        }
        if (obj.getClass().isArray()) {
            return Array.getLength(obj) == 0;
        }
        if (obj instanceof Collection) {
            return ((Collection<?>) obj).isEmpty();
        }
        if (obj instanceof Map) {
            return ((Map<?, ?>) obj).isEmpty();
        }
        if (obj instanceof Optional) {
            return ((Optional<?>) obj).map(RobinUtil::isEmpty).orElse(true);
        }
        // else
        return false;
    }
}
