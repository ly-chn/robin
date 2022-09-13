package kim.nzxy.robin.sample.web.common.runner;

import kim.nzxy.robin.factory.RobinMetadataFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * robin元数据注册
 *
 * @author ly-chn
 * @since 2022/9/8 17:07
 */
@Component
public class RobinMetadataRegisterRunner implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        RobinMetadataFactory.register("ip-1", () -> {
            return "";
        });
    }
}
