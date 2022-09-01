package kim.nzxy.robin.autoconfigure;

import kim.nzxy.robin.handler.RobinMetadataHandler;
import lombok.Data;

import java.time.Duration;

/**
 * robin验证器-基础策略配置类
 *
 * @author xuyf
 * @since 2022/8/31 15:50
 */
@Data
public class RobinBasicStrategy {
    /**
     * 锁定时长
     */
    private Duration lockDuration;
    /**
     * 元数据读取函数
     */
    private RobinMetadataHandler metadataHandler;
    /**
     * 是否启用元数据压缩，防止缓存大key
     */
    private Boolean digest;
    /**
     * 优先级
     */
    private Integer precedence;
}
