package io.github.soupedog.controller;

import io.github.soupedog.domain.dto.LoginRequest;
import io.github.soupedog.domain.dto.LoginResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述
 *
 * @author Xavier
 * @date 2024/9/18
 * @since 1.0
 */
@RestController
public class UserController {
    @PostMapping(value = "/user/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        return LoginResponse.builder().token("test").build();
    }
}
