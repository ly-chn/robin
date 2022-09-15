package kim.nzxy.robin.annotations;

import java.lang.annotation.*;

/**
 * 跳过robin检测
 *
 * @author lyun-chn
 * @since 2022/9/8 10:13
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface RobinSkip {
}
