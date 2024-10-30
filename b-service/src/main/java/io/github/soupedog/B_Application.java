package io.github.soupedog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * "http://localhost:8082/swagger-ui.html"
 * <p>
 * b-service 启动入口
 *
 * @author Xavier
 * @date 2024/9/18
 * @since 1.0
 */
@SpringBootApplication
public class B_Application {
    public static void main(String[] args) {
        SpringApplication.run(B_Application.class);
    }
}
