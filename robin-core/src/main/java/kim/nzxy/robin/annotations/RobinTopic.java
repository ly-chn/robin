package kim.nzxy.robin.annotations;

import org.intellij.lang.annotations.Language;

import java.lang.annotation.*;

/**
 * RobinTopic
 *
 * @author ly-chn
 * @since 2022/9/14 16:57
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Repeatable(RobinTopicCollector.class)
public @interface RobinTopic {
    /**
     * @return 适用的robin策略
     */
    String value();


    /**
     * todo: 支持直接使用固定元数据, 以及SpEL
     * @return 固定元数据
     */
    @Language("SpEL")
    String metadata() default "";
}
