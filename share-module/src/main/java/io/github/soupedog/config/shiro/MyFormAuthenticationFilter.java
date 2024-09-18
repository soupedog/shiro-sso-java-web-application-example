package io.github.soupedog.config.shiro;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

/**
 * 登录操作
 */
public class MyFormAuthenticationFilter extends FormAuthenticationFilter {
    @Override
    public String getLoginUrl() {
        return "www.baidu.com";
    }
}
