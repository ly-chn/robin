package kim.nzxy.robin.metadata;

/**
 * Robin依赖的元数据
 *
 * @author lyun-chn
 * @since 2022/8/29 12:10
 */
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

    public RobinMetadata(String topic, String metadata, Boolean digest) {
        this.topic = topic;
        this.metadata = metadata;
        this.digest = digest;
    }

    public String getTopic() {
        return this.topic;
    }

    public String getMetadata() {
        return this.metadata;
    }

    public Boolean getDigest() {
        return this.digest;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public void setDigest(Boolean digest) {
        this.digest = digest;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof RobinMetadata)) return false;
        final RobinMetadata other = (RobinMetadata) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$topic = this.getTopic();
        final Object other$topic = other.getTopic();
        if (this$topic == null ? other$topic != null : !this$topic.equals(other$topic)) return false;
        final Object this$metadata = this.getMetadata();
        final Object other$metadata = other.getMetadata();
        if (this$metadata == null ? other$metadata != null : !this$metadata.equals(other$metadata)) return false;
        final Object this$digest = this.getDigest();
        final Object other$digest = other.getDigest();
        if (this$digest == null ? other$digest != null : !this$digest.equals(other$digest)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof RobinMetadata;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $topic = this.getTopic();
        result = result * PRIME + ($topic == null ? 43 : $topic.hashCode());
        final Object $metadata = this.getMetadata();
        result = result * PRIME + ($metadata == null ? 43 : $metadata.hashCode());
        final Object $digest = this.getDigest();
        result = result * PRIME + ($digest == null ? 43 : $digest.hashCode());
        return result;
    }

    public String toString() {
        return "RobinMetadata(" + this.getTopic() + ", " + this.getMetadata() + ", " + this.getDigest() + ")";
    }
}
