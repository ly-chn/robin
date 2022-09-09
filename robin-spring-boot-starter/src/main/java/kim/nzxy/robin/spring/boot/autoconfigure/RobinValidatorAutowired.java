package kim.nzxy.robin.spring.boot.autoconfigure;

import kim.nzxy.robin.factory.RobinEffortFactory;
import kim.nzxy.robin.factory.RobinPostureFactory;
import kim.nzxy.robin.posture.RobinPosture;
import kim.nzxy.robin.posture.bucket.BucketEffort;
import kim.nzxy.robin.posture.sutain.visit.SustainVisitEffort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * 验证器自动注入
 *
 * @author lyun-chn
 * @since 2022/9/1 9:07
 */
@SuppressWarnings({"ConfigurationProperties", "AlibabaCommentsMustBeJavadocFormat"})
@Component
@Slf4j
public class RobinValidatorAutowired {

    /**
     * 用户自定义验证策略
     */
    @Autowired
    public void define(ApplicationContext applicationContext) {
        for (RobinPosture validator : applicationContext.getBeansOfType(RobinPosture.class).values()) {
            RobinPostureFactory.register(validator);
        }
    }

    //<editor-fold desc="持续访问校验">
    @ConfigurationProperties(prefix = "robin.validator")
    @Bean
    public SustainVisitEffort sustain() {
        return new SustainVisitEffort();
    }

    @Autowired
    public void sustain(SustainVisitEffort config) {
        RobinEffortFactory.register("sustain", config.getSustain());
    }
    //</editor-fold>

    //<editor-fold desc="令牌桶校验">
    @ConfigurationProperties(prefix = "robin.validator")
    @Bean
    public BucketEffort bucket() {
        return new BucketEffort();
    }

    @Autowired
    public void bucket(BucketEffort config) {
        RobinEffortFactory.register("bucket", config.getBucket());
    }
    //</editor-fold>
}
