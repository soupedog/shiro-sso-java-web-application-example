package io.github.soupedog.controller;

import io.github.soupedog.domain.dto.LoginInfo;
import io.github.soupedog.domain.dto.LoginRequest;
import io.github.soupedog.domain.dto.LoginResponse;
import io.github.soupedog.domain.dto.ServiceResponse;
import io.github.soupedog.domain.dto.UserDTO;
import io.github.soupedog.domain.po.User;
import io.github.soupedog.repository.UserDao;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static io.github.soupedog.domain.constant.AccountConstant.ALGORITHM_NAME;
import static io.github.soupedog.domain.constant.AccountConstant.HASH_ITERATIONS;
import static io.github.soupedog.domain.constant.AccountConstant.SALT;

/**
 * 描述
 *
 * @author Xavier
 * @date 2024/9/18
 * @since 1.0
 */
@ControllerAdvice
@RestController
public class UserController {
    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ServiceResponse<?> throwableHandler(Throwable e) {
        return ServiceResponse.builder().msg(e.getMessage()).build();
    }

    @Autowired
    private UserDao userDao;

    // 若干分钟的毫秒数
    private long min = 1000 * 60 * 5;

    @PostMapping("/user/check")
    public ServiceResponse<LoginResponse> validLoginInfo(@RequestBody LoginInfo loginInfo) {
        User user = userDao.findOne(Example.of(User.builder().name(loginInfo.getName()).build())).orElseThrow(() -> new RuntimeException("账号密码有误或不存在"));

        boolean isTokenActive = System.currentTimeMillis() < user.getLastUpdateTs() + min;

        boolean needRefresh = false;

        boolean isLoginActive = false;

        if (isTokenActive) {
            if (user.getToken().equals(loginInfo.getToken())) {
                isLoginActive = true;
            } else {
                needRefresh = true;
            }
        } else {
            needRefresh = true;
        }

        if (needRefresh && user.getRefreshKey().equals(loginInfo.getRefreshKey())) {
            String token = UUID.randomUUID().toString().replace("-", "");
            String refreshKey = UUID.randomUUID().toString().replace("-", "");

            user.setToken(token);
            user.setRefreshKey(refreshKey);
            // 更新用户信息
            userDao.save(user);

            return ServiceResponse.<LoginResponse>builder()
                    .main(LoginResponse.builder()
                            .token(token)
                            .refreshKey(refreshKey)
                            .user(
                                    UserDTO.builder()
                                            .uid(user.getUid())
                                            .name(user.getName())
                                            .userSex(user.getUserSex())
                                            .createTs(user.getCreateTs())
                                            .lastUpdateTs(user.getLastUpdateTs())
                                            .build()
                            )
                            .build())
                    .build();
        }

        if (isLoginActive) {
            return null;
        }
        throw new RuntimeException("请重新登陆");
    }

    @PostMapping(value = "/user/login")
    public ServiceResponse<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        User user = userDao.findOne(Example.of(User.builder().name(loginRequest.getName()).build())).orElseThrow(() -> new RuntimeException("账号密码有误或不存在"));
        SimpleHash simpleHash = new SimpleHash(ALGORITHM_NAME, loginRequest.getPw(), SALT, HASH_ITERATIONS);
        if (!simpleHash.toHex().equals(user.getPassword())) {
            throw new RuntimeException("账号密码有误或不存在");
        }

        String token = UUID.randomUUID().toString().replace("-", "");
        String refreshKey = UUID.randomUUID().toString().replace("-", "");

        user.setToken(token);
        user.setRefreshKey(refreshKey);
        // 更新用户信息
        userDao.save(user);

        return ServiceResponse.<LoginResponse>builder()
                .main(LoginResponse.builder()
                        .token(token)
                        .refreshKey(refreshKey)
                        .user(
                                UserDTO.builder()
                                        .uid(user.getUid())
                                        .name(user.getName())
                                        .userSex(user.getUserSex())
                                        .createTs(user.getCreateTs())
                                        .lastUpdateTs(user.getLastUpdateTs())
                                        .build()
                        )
                        .build())
                .build();
    }
}
