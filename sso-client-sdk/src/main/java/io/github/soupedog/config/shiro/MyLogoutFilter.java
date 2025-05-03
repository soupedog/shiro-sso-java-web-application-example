package io.github.soupedog.config.shiro;

import org.apache.shiro.web.filter.authc.LogoutFilter;


/**
 * 登出操作逻辑
 */
public class MyLogoutFilter extends LogoutFilter {
    private String actualRedirectUrl;

    public MyLogoutFilter(String actualRedirectUrl) {
        this.actualRedirectUrl = actualRedirectUrl;
    }

    @Override
    public String getRedirectUrl() {
        // 要做自定义登出处理的话，可以在此处添加逻辑

        SSOUtil.logout();
        return actualRedirectUrl;
    }
}
