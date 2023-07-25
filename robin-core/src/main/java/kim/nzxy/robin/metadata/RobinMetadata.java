package kim.nzxy.robin.metadata;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Robin依赖的元数据
 *
 * @author lyun-chn
 * @since 2022/8/29 12:10
 */
@Data
@AllArgsConstructor
public class RobinMetadata {
    /**
     * 数据主题，如"IP"/"Token"/"UserId"等
     */
    private String topic;
    /**
     * 数据值，如具体IP地址，Token值，用户Id值等
     */
    private String metadata;
    /**
     * 是否启用元数据压缩，防止缓存大key, 但是会有概率误杀, 且Robin不会缓存压缩前的数据
     */
    private Boolean digest;
}
