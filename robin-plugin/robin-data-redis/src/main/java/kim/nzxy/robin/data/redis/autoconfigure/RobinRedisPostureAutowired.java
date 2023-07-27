package kim.nzxy.robin.data.redis.autoconfigure;

import cn.dev33.satoken.dao.SaTokenDao;
import kim.nzxy.robin.data.redis.RobinRedisConfig;
import kim.nzxy.robin.data.redis.action.BucketPosture;
import kim.nzxy.robin.data.redis.action.SustainVisitPosture;
import kim.nzxy.robin.enums.RobinExceptionEnum;
import kim.nzxy.robin.exception.RobinException;
import kim.nzxy.robin.factory.RobinPostureFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.lang.reflect.Field;

/**
 * @author ly-chn
 */
@AutoConfigureAfter(name = "cn.dev33.satoken.dao.alone.SaAloneRedisInject")
@Slf4j
public class RobinRedisPostureAutowired {
    @Bean
    @ConfigurationProperties("robin.redis")
    public RobinRedisConfig robinRedisPosture() {
        System.out.println("\n\n\nhi\n\n\n");
        return new RobinRedisConfig();
    }

    @Autowired
    @ConditionalOnMissingBean(name = "cn.dev33.satoken.dao.alone.SaAloneRedisInject")
    public void define(StringRedisTemplate redisTemplate, RobinRedisConfig robinRedisConfig) {
        if (robinRedisConfig.getExtendsSaToken()) {
            log.warn("无法找到sa-token-alone-redis配置, 使用默认配置");
        }
        RobinRedisManage.setStringRedisTemplate(redisTemplate);
    }

    @ConditionalOnBean(name = "cn.dev33.satoken.dao.alone.SaAloneRedisInject")
    @Autowired
    public void define(@Autowired(required = false) SaTokenDao saTokenDao,
                   @Autowired(required = false) StringRedisTemplate stringRedisTemplate,
                   @Autowired RobinRedisConfig robinRedisConfig) {
        RobinRedisManage.setRobinRedisConfig(robinRedisConfig);
        // 即便引入了sa-token-alone-redis, 也未必使用redis配置, saTokenDao依然存在null的可能
        if (saTokenDao != null || robinRedisConfig.getExtendsSaToken()) {
            try {
                Field field = saTokenDao.getClass().getField("stringRedisTemplate");
                Object o = field.get(saTokenDao);
                if (o instanceof StringRedisTemplate) {
                    RobinRedisManage.setStringRedisTemplate((StringRedisTemplate) o);
                }
                return;
            } catch (NoSuchFieldException | IllegalAccessException e) {
                log.error("配置失败, 无法找到redis配置");
                throw new RobinException.Panic(RobinExceptionEnum.Panic.ConfigParamVerifyFailed);
            }
        }
        RobinRedisManage.setStringRedisTemplate(stringRedisTemplate);
    }

    @Autowired
    public void define(RobinRedisConfig robinRedisConfig) {
        RobinRedisManage.setRobinRedisConfig(robinRedisConfig);
        RobinPostureFactory.register(new BucketPosture());
        RobinPostureFactory.register(new SustainVisitPosture());
    }
}
