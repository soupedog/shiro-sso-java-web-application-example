package io.github.soupedog.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述
 *
 * @author Xavier
 * @date 2024/9/19
 * @since 1.0
 */
@CrossOrigin("*")
@RestController
public class BController {
    @GetMapping(value = "/api/b/needlogin")
    public String loginResponse() {
        return "允许访问";
    }
}
