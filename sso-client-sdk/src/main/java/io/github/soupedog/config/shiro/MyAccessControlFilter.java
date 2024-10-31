package io.github.soupedog.config.shiro;

import io.github.soupedog.client.UserCenterClient;
import io.github.soupedog.domain.dto.LoginInInfo;
import io.github.soupedog.domain.dto.ServiceResponse;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.PathMatchingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.PrintWriter;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class MyAccessControlFilter extends PathMatchingFilter {
    private String loginUrl;
    /**
     * 实际上仅作为 loginUrl 的 queryString
     */
    private String backUrl;
    private UserCenterClient userCenterClient;

    public MyAccessControlFilter(String loginUrl, String backUrl, UserCenterClient userCenterClient) {
        this.loginUrl = loginUrl;
        this.backUrl = backUrl;
        this.userCenterClient = userCenterClient;
    }

    @Override
    protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        // 请求是否要继续处理(如请求已被提前转发、已被提前填充返回值则不应继续处理)
        return loginCheck(request, response);
    }

    protected boolean loginCheck(ServletRequest request, ServletResponse response) throws IOException {
        boolean loginSuccess = false;
        boolean needRedirectToLoginPage = true;

        LoginInInfo loginInInfo = SSOUtil.getLoginInfo();
        String newLoginInfo = null;
        if (loginInInfo != null) {
            try {
                ResponseEntity<ServiceResponse<String>> validateResult = userCenterClient.validLoginInfo(loginInInfo.getCredentials());
                if (HttpStatus.OK.equals(validateResult.getStatusCode())) {
                    loginSuccess = true;
                    if (validateResult.getBody() != null && validateResult.getBody().getMain() == null) {
                        // 登陆信息无误且无需刷新身份信息
                        needRedirectToLoginPage = false;
                    } else {
                        // 身份过期了，但服务端已自动根据 refreshKey 刷新登陆信息，需要重新在前端缓存
                        newLoginInfo = validateResult.getBody().getMain();
                    }
                }
            } catch (Exception e) {
                log.warn("鉴权失败，拒绝访问。", e);
            }
        }

        if (needRedirectToLoginPage) {
            if (isRedirectByFrontEnd(WebUtils.toHttp(request))) {
                // 如 ajax 等主页发起的请求无法让主页面直接跳转，需要前端自己进行页面跳转
                markAsClientEndNeedRedirect(WebUtils.toHttp(response), newLoginInfo);
            } else {
                WebUtils.issueRedirect(request, response, backUrl);
            }
        }

        // 仅登陆信息无误且未发生请求转发的请求允许返回 true 继续处理
        return loginSuccess && !needRedirectToLoginPage;
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

    private void markAsClientEndNeedRedirect(HttpServletResponse response, String newLoginInfo) {
        // 与前端商量好的状态码
        response.setStatus(HttpStatus.FORBIDDEN.value());
        String responseBody = newLoginInfo == null ? "{\"msg\":\"请前往登录页进行登陆\"}" :
                "{\"msg\":\"请前往登陆页刷新缓存\",\"main\":\"" + newLoginInfo + "\"}";
        try (PrintWriter out = response.getWriter()) {
            out.append(responseBody);
        } catch (IOException e) {
            log.error(responseBody, e);
        }
    }
}
