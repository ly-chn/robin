package kim.nzxy.robin.enums;

/**
 * 缓存类型 (没有使用RuleEnum是因为考虑到可能会有不同的规则之间进行复用缓存)
 * 暂时先不用吧, 复用就复用吧
 * 暂定改为map，key为类型， value为提供值。
 * 每个策略提供读取依赖key， 读取依赖时间周期属性
 *
 * @author lyun-chn
 * @since 2021/6/9
 */
public enum RobinCacheEnum {

}
