package kim.nzxy.robin.autoconfigure;

import lombok.Data;
import lombok.NonNull;

import java.time.Duration;

/**
 * robin验证器-基础策略配置类
 *
 * @author lyun-chn
 * @since 2022/8/31 15:50
 */
@Data
public class RobinEffortBasic {
    /**
     * 配置主题, key, 全局唯一
     */
    private String topic;
    /**
     * 是否启用
     */
    private Boolean enabled = Boolean.TRUE;
    /**
     * 元数据处理器
     */
    private String metadataHandler;
    /**
     * 锁定时长
     */
    private Duration lockDuration = Duration.ofMinutes(1);
    /**
     * 是否启用元数据压缩，防止缓存大key, 但是Robin不会缓存压缩前的数据
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
