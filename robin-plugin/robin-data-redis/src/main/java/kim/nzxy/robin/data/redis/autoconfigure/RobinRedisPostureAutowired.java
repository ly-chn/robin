package kim.nzxy.robin.data.redis.autoconfigure;

import kim.nzxy.robin.data.redis.RobinRedisConfig;
import kim.nzxy.robin.data.redis.action.BucketPosture;
import kim.nzxy.robin.data.redis.action.SustainVisitPosture;
import kim.nzxy.robin.factory.RobinPostureFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author ly-chn
 */
public class RobinRedisPostureAutowired {
    @Bean
    @ConfigurationProperties("robin.redis")
    public RobinRedisConfig robinRedisPosture() {
        return new RobinRedisConfig();
    }

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public void define(StringRedisTemplate redisTemplate, RobinRedisConfig robinRedisConfig) {
        RobinRedisManage.setStringRedisTemplate(redisTemplate);
        RobinRedisManage.setRobinRedisConfig(robinRedisConfig);
        RobinPostureFactory.register(new BucketPosture());
        RobinPostureFactory.register(new SustainVisitPosture());
    }
}
