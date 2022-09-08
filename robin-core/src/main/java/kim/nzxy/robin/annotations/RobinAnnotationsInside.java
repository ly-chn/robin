package kim.nzxy.robin.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * robin元注解
 *
 * @author lyun-chn
 * @since 2022/9/8 10:44
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RobinAnnotationsInside {
}
