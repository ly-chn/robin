package kim.nzxy.robin.posture.config;

/**
 * 内置一些常量
 * @author  ly-chn
 */
public interface BuiltInEffortConstant {
    /**
     * 持续访问记录最大记录数量
     */
    int SUSTAIN_VISIT_PRECISION = 100000;
    /**
     * 持续访问记录步长
     */
    double SUSTAIN_VISIT_STEP = 1.0d / SUSTAIN_VISIT_PRECISION;
    /**
     * 令牌桶最大容量, 令牌桶精度
     */
    int BUCKET_PRECISION = 100000;
    /**
     * 令牌桶步长, 用于小数位
     */
    double BUCKET_STEP = 1.0d / SUSTAIN_VISIT_PRECISION;
}
