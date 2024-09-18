package io.github.soupedog.controller;

import io.github.soupedog.client.UserCenterClient;
import io.github.soupedog.domain.dto.LoginRequest;
import io.github.soupedog.domain.dto.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private UserCenterClient userCenterClient;

    @GetMapping(value = "/api/a/needlogin")
    public LoginResponse loginResponse() {
        return userCenterClient.login(LoginRequest.builder().uid("1").pw("1").build());
    }
}
