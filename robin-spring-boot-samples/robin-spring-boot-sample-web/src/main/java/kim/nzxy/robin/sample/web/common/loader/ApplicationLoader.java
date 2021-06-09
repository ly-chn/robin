package kim.nzxy.robin.sample.web.common.loader;

import kim.nzxy.robin.factory.RobinValidFactory;
import kim.nzxy.robin.sample.web.common.validator.ReferValidatorImpl;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 项目启动后执行
 *
 * @author xy
 * @since 2020/9/30 20:36
 */
@Component
@AllArgsConstructor
public class ApplicationLoader implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments arg) throws Exception {
        /*
         * todo: 应该会有更好的办法来实现, 这种方式貌似不太好
         * 目前能考虑到的几种情况就是在config中使用完整类路径, 完整包名对eclipse非常不友好
         * 或者只写包名, 找实现类/子类, 通过反射找到策略, IDE会提示这类没人用
         * 其次是使用注解+包名, 也不太友好, 注解可以让IDE忽略`no usage`的提醒, 但是只有这一个功能太鸡肋, 或许可以加上order...来解除这个疑惑
         * 总归来说使用包名就不太友好
         */
        RobinValidFactory.register(new ReferValidatorImpl());
    }
}