package io.github.soupedog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * b-service 启动入口
 *
 * @author Xavier
 * @date 2024/9/18
 * @since 1.0
 */
@EnableFeignClients
@SpringBootApplication
public class B_Application {
    public static void main(String[] args) {
        SpringApplication.run(B_Application.class);
    }
}
