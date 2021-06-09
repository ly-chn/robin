package kim.nzxy.robin.config;

import kim.nzxy.robin.autoconfigure.RobinProperties;
import kim.nzxy.robin.enums.RobinBuiltinErrEnum;
import kim.nzxy.robin.exception.RobinBuiltinException;
import kim.nzxy.robin.handler.RobinCacheHandler;
import kim.nzxy.robin.handler.RobinContextHandler;
import lombok.Getter;
import lombok.Setter;

/**
 * @author xy
 * @since 2021/6/4
 */
public class RobinManagement {
    @Getter
    @Setter
    private static RobinProperties robinProperties;
    @Setter
    private static RobinCacheHandler cacheHandler;
    @Getter
    @Setter
    private static RobinContextHandler contextHandler;

    public static RobinCacheHandler getCacheHandler() {
        if (cacheHandler == null) {
            throw new RobinBuiltinException(RobinBuiltinErrEnum.SOMETHING_NOT_REGISTER_YET);
            // todo: default CacheHandler
        }
        return cacheHandler;
    }
}
