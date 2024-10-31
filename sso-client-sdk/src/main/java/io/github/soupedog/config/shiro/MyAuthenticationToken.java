package io.github.soupedog.config.shiro;

import io.github.soupedog.domain.dto.LoginInInfo;
import lombok.Getter;
import lombok.Setter;
import org.apache.shiro.authc.RememberMeAuthenticationToken;


public class MyAuthenticationToken implements RememberMeAuthenticationToken {
    @Getter
    @Setter
    private LoginInInfo loginInInfo;
    private final boolean rememberMe;

    public MyAuthenticationToken(LoginInInfo loginInInfo, boolean rememberMe) {
        this.loginInInfo = loginInInfo;
        this.rememberMe = rememberMe;
    }

    @Override
    public Object getPrincipal() {
        return loginInInfo.getUser();
    }

    @Override
    public Object getCredentials() {
        return loginInInfo.getCredentials();
    }

    @Override
    public boolean isRememberMe() {
        return rememberMe;
    }
}
