package io.github.soupedog.controller;

import io.github.soupedog.domain.dto.LoginInInfo;
import io.github.soupedog.domain.dto.LoginRequest;
import io.github.soupedog.domain.dto.ServiceResponse;
import io.github.soupedog.domain.dto.UserCredentials;
import io.github.soupedog.domain.dto.UserDTO;
import io.github.soupedog.domain.po.User;
import io.github.soupedog.repository.UserDao;
import io.github.soupedog.util.AESUtil;
import io.github.soupedog.util.JsonUtil;
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

    /**
     * 后端间直连，无需密文传输
     */
    @PostMapping("/user/check")
    public ServiceResponse<LoginInInfo> validLoginInfo(@RequestBody UserCredentials userCredentials) {
        User user = userDao.findOne(Example.of(User.builder().uno(userCredentials.getUno()).build())).orElseThrow(() -> new RuntimeException("账号密码有误或不存在"));

        boolean isTokenActive = System.currentTimeMillis() < user.getTokenETS();

        boolean needRefresh = false;

        boolean isLoginActive = false;

        if (isTokenActive) {
            if (user.getToken().equals(userCredentials.getToken())) {
                isLoginActive = true;
            }
        } else {
            needRefresh = true;
        }

        if (needRefresh && user.getRefreshKey().equals(userCredentials.getRefreshKey())) {
            String token = UUID.randomUUID().toString().replace("-", "");
            String refreshKey = UUID.randomUUID().toString().replace("-", "");

            user.setToken(token);
            user.setRefreshKey(refreshKey);
            user.setTokenETS(System.currentTimeMillis() + min);
            // 更新用户信息
            userDao.save(user);

            LoginInInfo loginInInfo = LoginInInfo.builder()
                    .credentials(UserCredentials.builder()
                            .uno(user.getUno())
                            .token(user.getToken())
                            .tokenETS(user.getTokenETS())
                            .refreshKey(user.getRefreshKey())
                            .build()
                    )
                    .user(UserDTO.builder()
                            .uno(user.getUno())
                            .name(user.getName())
                            .userType(user.getUserType())
                            .createTs(user.getCreateTs())
                            .lastUpdateTs(user.getLastUpdateTs())
                            .build()
                    ).build();

            return ServiceResponse.<LoginInInfo>builder()
                    .main(loginInInfo)
                    .build();
        }

        if (isLoginActive) {
            return null;
        }
        throw new RuntimeException("请重新登陆");
    }

    @PostMapping(value = "/user/login")
    public ServiceResponse<String> login(@RequestBody LoginRequest loginRequest) {
        User user = userDao.findOne(Example.of(User.builder().account(loginRequest.getAccount()).build()))
                .orElseThrow(() -> new RuntimeException("账号密码有误或不存在"));

        String encryptPW = AESUtil.encrypt(loginRequest.getPassword());

        if (!encryptPW.equals(user.getPassword())) {
            throw new RuntimeException("账号密码有误或不存在");
        }

        String token = UUID.randomUUID().toString().replace("-", "");
        String refreshKey = UUID.randomUUID().toString().replace("-", "");

        user.setToken(token);
        user.setRefreshKey(refreshKey);
        user.setTokenETS(System.currentTimeMillis() + min);
        // 更新用户信息
        userDao.save(user);

        LoginInInfo loginInInfo = LoginInInfo.builder()
                .credentials(UserCredentials.builder()
                        .uno(user.getUno())
                        .token(user.getToken())
                        .tokenETS(user.getTokenETS())
                        .refreshKey(user.getRefreshKey())
                        .build()
                )
                .user(UserDTO.builder()
                        .uno(user.getUno())
                        .name(user.getName())
                        .userType(user.getUserType())
                        .createTs(user.getCreateTs())
                        .lastUpdateTs(user.getLastUpdateTs())
                        .build()
                ).build();

        String json = JsonUtil.formatAsString(loginInInfo);
        // 经前端跳转间接进行后端服务间的信息传递，需要密文传输
        // Base64 默认规范有换行约束、且部分字符与 URL 字符冲突，故 AES 中使用的 Base64.getUrlEncoder()
        String afterEncrypt = AESUtil.encrypt(json);

        return ServiceResponse.<String>builder()
                .main(afterEncrypt)
                .build();
    }
}
