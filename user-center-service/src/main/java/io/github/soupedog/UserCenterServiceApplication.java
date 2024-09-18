package io.github.soupedog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * UserCenterService 启动入口
 *
 * @author Xavier
 * @date 2024/9/18
 * @since 1.0
 */
@EnableJpaAuditing
@EnableTransactionManagement
@SpringBootApplication
public class UserCenterServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserCenterServiceApplication.class);
    }
}
