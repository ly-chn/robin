package kim.nzxy.robin.annotations;

/**
 * robin策略
 *
 * @author lyun-chn
 * @since 2022/9/8 10:45
 */
public @interface RobinStrategy {
    /**
     * @return 策略key
     */
    String key();
}
