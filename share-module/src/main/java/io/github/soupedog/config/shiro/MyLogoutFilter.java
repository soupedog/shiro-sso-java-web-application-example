package io.github.soupedog.config.shiro;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.LogoutFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 登出操作
 */
public class MyLogoutFilter extends LogoutFilter {

    @Override
    protected String getRedirectUrl(ServletRequest request, ServletResponse response, Subject subject) {
        // 要做自定义登出处理的话，可以在此处添加逻辑
        return super.getRedirectUrl(request, response, subject);
    }
}
