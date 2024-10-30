package io.github.soupedog.config.shiro;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.soupedog.client.UserCenterClient;
import io.github.soupedog.domain.dto.LoginInInfo;
import io.github.soupedog.domain.dto.ServiceResponse;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class MyAccessControlFilter extends AccessControlFilter {
    private static final ObjectMapper mapper = new ObjectMapper();
    private UserCenterClient userCenterClient;

    public MyAccessControlFilter(UserCenterClient userCenterClient) {
        this.userCenterClient = userCenterClient;
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        // 是登录请求的话默认放行
        if (isLoginRequest(request, response)) {
            return true;
        }

        LoginInInfo loginInInfo = SSOUtil.getLoginInfo();
        if (loginInInfo == null) {
            return false;
        }
        boolean result = false;
        try {
            ResponseEntity<ServiceResponse<LoginInInfo>> validateResult = userCenterClient.validLoginInfo(loginInInfo.getCredentials());

            if (HttpStatus.OK.equals(validateResult.getStatusCode())) {
                Optional.of(validateResult)
                        .map(HttpEntity::getBody)
                        .map(ServiceResponse::getMain)
                        .ifPresent(loginResponse -> {
                            // 返回 null 代表无需更新用户信息
                            loginInInfo.setCredentials(loginResponse.getCredentials());
                            loginInInfo.setUser(loginResponse.getUser());
                            log.info("用户信息已更新");
                        });

                result = true;
            }

        } catch (Exception e) {
            log.warn("鉴权未通过，拒绝访问。", e);
        }

        if (!result) {
            // 鉴权失败意味着，旧的身份信息有问题，需要清理当前已登录信息
            SSOUtil.logout();
        }

        // 鉴权逻辑，不通过校验需返回 false，会触发 onAccessDenied 逻辑
        return result;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        if (isRedirectByFrontEnd(WebUtils.toHttp(request))) {
            // 如 ajax 等主页发起的请求无法让主页面直接跳转，需要前端自己进行页面跳转
            markAsClientEndNeedRedirect(WebUtils.toHttp(response));
        } else {
            WebUtils.issueRedirect(request, response, getLoginUrl());
        }
        // 继续处理请求时需要返回 true，而此处转发已中断请求处理故返回 false
        return false;
    }

    private boolean isRedirectByFrontEnd(HttpServletRequest request) {
        String contentType = request.getHeader(HttpHeaders.CONTENT_TYPE);
        // auth-no-redirect 是自定义和前端商量好，该键值存在时后端就不会自行转发的标记
        if (request.getHeader("auth-no-redirect") != null) {
            return true;
        } else if (StringUtils.hasLength(contentType)) {
            // 约定前后端分离， json 作为前后端数据交互接口的情况下，这种请求也是无法让主页面跳转的
            // (如有必要，可以继续判断是否为 ajax 等请求类型，此处示例没做演示 ———— 属于过时技术)
            return APPLICATION_JSON_VALUE.equals(contentType);
        } else {
            return false;
        }
    }

    private void markAsClientEndNeedRedirect(HttpServletResponse response) {
        // 与前端商量好的状态码
        response.setStatus(HttpStatus.FORBIDDEN.value());
        String responseBody = "{\"msg\":\"请自行跳转\"}";
        try (PrintWriter out = response.getWriter()) {
            out.append(responseBody);
        } catch (IOException e) {
            log.error(responseBody, e);
        }
    }
}
