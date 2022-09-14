package kim.nzxy.robin.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * RobinTopic
 *
 * @author xuyf
 * @since 2022/9/14 16:57
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface RobinTopic {
    String value();
}
