package kim.nzxy.robin.metadata;

import kim.nzxy.robin.enums.RobinStrCodecEnum;
import kim.nzxy.robin.util.RobinUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

/**
 * Robin依赖的元数据
 *
 * @author ly-chn
 * @since 2022/8/29 12:10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RobinMetadata implements Serializable {
    /**
     * 数据主题，如"IP"/"Token"/"UserId"等
     */
    private String topic;
    /**
     * 数据值，如具体IP地址，Token值，用户Id值等
     */
    private String metadata;
    /**
     * 是否启用元数据压缩，防止缓存大key, 但是会有概率误杀, 且Robin不会缓存压缩前的原始值
     */
    private RobinStrCodecEnum codec;

    /**
     * @return topic是否有效
     */
    public boolean hasTopic() {
        return RobinUtil.isNotEmpty(topic);
    }

    /**
     * @return metadata值是否有效
     */
    public boolean hasMetadata() {
        return RobinUtil.isNotEmpty(metadata);
    }

    public String getMetadata() {
        if (codec == null) {
            return metadata;
        }
        return codec.encode(metadata);
    }

    public String getSourceMetadata() {
        return metadata;
    }
}
