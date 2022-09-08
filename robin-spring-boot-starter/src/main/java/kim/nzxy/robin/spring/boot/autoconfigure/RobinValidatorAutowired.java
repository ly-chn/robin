package kim.nzxy.robin.spring.boot.autoconfigure;

import kim.nzxy.robin.config.RobinManagement;
import kim.nzxy.robin.factory.RobinValidFactory;
import kim.nzxy.robin.factory.RobinValidatorConfigFactory;
import kim.nzxy.robin.validator.RobinValidator;
import kim.nzxy.robin.validator.bucket.BucketValidatorConfig;
import kim.nzxy.robin.validator.sutain.visit.SustainVisitValidatorConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Map;

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
        for (RobinValidator validator : applicationContext.getBeansOfType(RobinValidator.class).values()) {
            RobinValidFactory.register(validator);
        }
    }

    //<editor-fold desc="持续访问校验">
    @ConfigurationProperties(prefix = "robin.validator")
    @Bean
    public SustainVisitValidatorConfig sustainVisitValidatorConfig() {
        return new SustainVisitValidatorConfig();
    }

    @Autowired
    public void sustainVisitValidatorConfig(SustainVisitValidatorConfig config) {
        RobinValidatorConfigFactory.registerValidatorConfig(config.getSustain());
    }
    //</editor-fold>

    //<editor-fold desc="令牌桶校验">
    @ConfigurationProperties(prefix = "robin.validator")
    @Bean
    public BucketValidatorConfig bucketValidatorConfig() {
        return new BucketValidatorConfig();
    }

    @Autowired
    public void bucketValidatorConfig(BucketValidatorConfig config) {
        RobinValidatorConfigFactory.registerValidatorConfig(config.getBucket());
    }
    //</editor-fold>
}
