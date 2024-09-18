package io.github.soupedog.client;

import io.github.soupedog.config.FeignConfig;
import io.github.soupedog.domain.dto.LoginRequest;
import io.github.soupedog.domain.dto.LoginResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 此处给了 configuration 属性就是对当前 Feign 指定配置项，如果不传默认是 {@link org.springframework.cloud.openfeign.FeignClientsConfiguration}
 */
@FeignClient(name = "userCenterFeign", configuration = FeignConfig.class, url = "${user-center.api.url.prefix}")
public interface UserCenterClient {

    @PostMapping(value = "${user-center.api.login.path}")
    LoginResponse login(@RequestBody LoginRequest loginRequest);
}