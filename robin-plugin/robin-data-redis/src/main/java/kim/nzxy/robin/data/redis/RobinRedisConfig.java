package kim.nzxy.robin.data.redis;

import lombok.Data;

/**
 * redis配置
 *
 * @author ly-chn
 */
@Data
public class RobinRedisConfig {
    /**
     * 继承sa-token的redis配置, 即: 使用sa-token所用的redis数据源, 支持sa-token-alone-redis插件
     */
    private Boolean extendsSaToken = false;
}
