package kim.nzxy.robin.config;

import lombok.Data;

/**
 * Robin依赖的元数据
 *
 * @author lyun-chn
 * @since 2022/8/29 12:10
 */
@Data
public class RobinMetadata {
    /**
     * 数据主题，如"IP"/"Token"/"UserId"等
     */
    private String topic;
    /**
     * 数据值，如具体IP地址，Token值，用户Id值等
     */
    private String value;
}
