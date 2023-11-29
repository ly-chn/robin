package kim.nzxy.robin.autoconfigure;

import kim.nzxy.robin.enums.RobinStrCodecEnum;
import lombok.Data;

import java.time.Duration;

/**
 * robin验证器-基础策略配置类
 *
 * @author ly-chn
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
     * todo: 支持多个拼接
     */
    private String metadataHandler;
    /**
     * 锁定时长
     */
    private Duration lockDuration = Duration.ofMinutes(1);
    /**
     * 是否启用元数据压缩，防止缓存大key, Robin不会缓存压缩前的原始值
     */
    private RobinStrCodecEnum digest;
    /**
     * 优先级
     */
    private Integer precedence = 0;
    /**
     * 如果为true, 则表示所有方法均适用此拦截方式
     */
    private Boolean asDefault = false;
}
