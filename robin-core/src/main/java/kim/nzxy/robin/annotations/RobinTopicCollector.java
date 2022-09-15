package kim.nzxy.robin.annotations;

import java.lang.annotation.*;

/**
 * @author xuyf
 * @see RobinTopic
 * @since 2022/9/15 8:44
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface RobinTopicCollector {
    RobinTopic[] value();
}
