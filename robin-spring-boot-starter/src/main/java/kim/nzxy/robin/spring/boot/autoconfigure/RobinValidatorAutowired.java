package kim.nzxy.robin.spring.boot.autoconfigure;

import kim.nzxy.robin.factory.RobinEffortFactory;
import kim.nzxy.robin.factory.RobinMetadataFactory;
import kim.nzxy.robin.factory.RobinPostureFactory;
import kim.nzxy.robin.handler.RobinMetadataHandler;
import kim.nzxy.robin.posture.RobinPosture;
import kim.nzxy.robin.posture.config.BuiltInEffort;
import lombok.CustomLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * 验证器自动注入
 *
 * @author ly-chn
 * @since 2022/9/1 9:07
 */
@SuppressWarnings({"AlibabaCommentsMustBeJavadocFormat"})
@CustomLog
public class RobinValidatorAutowired {

    /**
     * 用户自定义验证策略
     */
    @Autowired
    public void define(ApplicationContext applicationContext) {
        applicationContext.getBeansOfType(RobinPosture.class).values().forEach(RobinPostureFactory::register);
        applicationContext.getBeansOfType(RobinMetadataHandler.class).forEach(RobinMetadataFactory::register);
    }

    //<editor-fold desc="持续访问校验">
    @ConfigurationProperties(prefix = "robin.validator")
    @Bean
    public BuiltInEffort sustain() {
        return new BuiltInEffort();
    }

    @Autowired
    public void sustain(BuiltInEffort config) {
        RobinEffortFactory.register(BuiltInEffort.Fields.sustain, config.getSustain());
        RobinEffortFactory.register(BuiltInEffort.Fields.bucket, config.getBucket());
    }
    //</editor-fold>
}
