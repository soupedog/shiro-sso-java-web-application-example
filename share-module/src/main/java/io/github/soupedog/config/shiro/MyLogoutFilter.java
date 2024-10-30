package io.github.soupedog.config.shiro;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.LogoutFilter;


/**
 * 登出操作
 */
public class MyLogoutFilter extends LogoutFilter {

    @Override
    protected String getRedirectUrl(ServletRequest request, ServletResponse response, Subject subject) {
        SSOUtil.logout();

        // 要做自定义登出处理的话，可以在此处添加逻辑
        return super.getRedirectUrl(request, response, subject);
    }
}
