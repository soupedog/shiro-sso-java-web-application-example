package io.github.soupedog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * a-service 启动入口
 *
 * @author Xavier
 * @date 2024/9/18
 * @since 1.0
 */
@EnableFeignClients
@SpringBootApplication
public class A_Application {
    public static void main(String[] args) {
        SpringApplication.run(A_Application.class);
    }
}
