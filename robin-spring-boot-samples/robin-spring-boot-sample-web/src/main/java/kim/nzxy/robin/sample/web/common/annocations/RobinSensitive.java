package kim.nzxy.robin.sample.web.common.annocations;

import kim.nzxy.robin.annotations.RobinTopic;

import java.lang.annotation.*;

/**
 * 表示将被robin视为敏感接口
 *
 * @author xuyf
 * @since 2022/9/19 17:04
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@RobinTopic("ip-sensitive")
public @interface RobinSensitive {
}
