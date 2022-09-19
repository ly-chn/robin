package kim.nzxy.robin.annotations;

import java.lang.annotation.*;

/**
 * RobinTopic
 *
 * @author xuyf
 * @since 2022/9/14 16:57
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Repeatable(RobinTopicCollector.class)
public @interface RobinTopic {
    String value();
}