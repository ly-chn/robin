package kim.nzxy.robin.autoconfigure;

import lombok.Data;

import java.time.Duration;

/**
 * robin验证器-基础策略配置类
 *
 * @author lyun-chn
 * @since 2022/8/31 15:50
 */
@Data
public class RobinValidatorBasicConfig {
    /**
     * 锁定时长
     */
    private Duration lockDuration;
    /**
     * 是否启用元数据压缩，防止缓存大key
     */
    private Boolean digest = false;
    /**
     * 优先级
     */
    private Integer precedence = 0;
    /**
     * 如果为true, 则表示所有方法均适用此拦截方式
     */
    private Boolean asDefault = false;
}
