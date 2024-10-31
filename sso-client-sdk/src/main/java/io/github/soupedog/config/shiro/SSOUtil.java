package io.github.soupedog.config.shiro;

import io.github.soupedog.domain.dto.LoginInInfo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

public class SSOUtil {
    public static LoginInInfo getLoginInfo() {
        Subject subject = SecurityUtils.getSubject();
        return (LoginInInfo) subject.getPrincipal();
    }

    public static void logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
    }
}
