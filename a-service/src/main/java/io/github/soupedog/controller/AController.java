package io.github.soupedog.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述
 *
 * @author Xavier
 * @date 2024/9/19
 * @since 1.0
 */
@RestController
public class AController {
    @GetMapping(value = "/api/a/needlogin")
    public String loginResponse() {
        return "允许访问";
    }
}
