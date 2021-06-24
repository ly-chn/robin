package kim.nzxy.robin.handler;

/**
 * 上下文管理器
 *
 * @author xy
 * @since 2021/6/8
 */
public interface RobinContextHandler {

    /**
     * @return 当前访问者IP
     */
    String ip();

    /**
     * @return 当前访问资源
     */
    String uri();

    /**
     * @return 当前ua
     */
    String ua();
}
