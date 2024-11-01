package io.github.soupedog.config.shiro;

import io.github.soupedog.domain.dto.LoginInInfo;
import io.github.soupedog.util.AESUtil;
import io.github.soupedog.util.JsonUtil;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.util.StringUtils;

/**
 * 用于接收远端前端页面传递的登录信息并在当前系统登陆
 */
public class MyLoginFilter extends AuthenticationFilter {
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        // 远端用户服务完成登录后会携带登录信息访问该过滤器指定的路径，进而把成功登陆的信息同步给接入了 SSO 的下游服务
        // 本地登录系统没保存过登录信息，故会被拒绝触发 onAccessDenied，该方法来从初始化远端用户服务返回的登录信息
        LoginInInfo loginInInfo = validateAndReadLoginInfo(request);

        if (loginInInfo == null) {
            SSOUtil.logout();
            // 登录信息有问题，让其重新登陆
            WebUtils.issueRedirect(request, response, getLoginUrl());
        } else {
            boolean rememberMe = StringUtils.hasText(WebUtils.getCleanParam(request, "rememberMe"));

            Subject subject = SecurityUtils.getSubject();
            MyAuthenticationToken token = new MyAuthenticationToken(loginInInfo, rememberMe);
            subject.login(token);

            // 跳转至主页面
            WebUtils.issueRedirect(request, response, getSuccessUrl());
        }
        // 中断当前请求，因为上方已手动处理成请求跳转
        return false;
    }

    protected LoginInInfo validateAndReadLoginInfo(ServletRequest request) {
        String loginInfoStringVal = WebUtils.getCleanParam(request, "info");
        loginInfoStringVal = AESUtil.decrypt(loginInfoStringVal);
        return JsonUtil.readAsObject(loginInfoStringVal, LoginInInfo.class);
    }
}
