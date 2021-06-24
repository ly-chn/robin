package kim.nzxy.robin.sample.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.DigestUtils;

@SpringBootApplication
public class RobinSpringBootSampleWebApplication {
    public static void main(String[] args) {
        DigestUtils.md5DigestAsHex("原串".getBytes());
        SpringApplication.run(RobinSpringBootSampleWebApplication.class, args);
    }
}
