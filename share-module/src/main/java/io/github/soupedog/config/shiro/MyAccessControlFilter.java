package io.github.soupedog.config.shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class MyAccessControlFilter extends AccessControlFilter {
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        // 是登录请求的话默认放行
        if (isLoginRequest(request, response)) {
            return true;
        }

        // 鉴权逻辑，不通过校验需返回 false，会触发 onAccessDenied 逻辑
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        if (isRedirectByFrontEnd(WebUtils.toHttp(request))) {
            // 如 ajax 等主页发起的请求无法让主页面直接跳转，需要前端自己进行页面跳转
            markAsClientEndNeedRedirect(WebUtils.toHttp(response));
        } else {
            WebUtils.issueRedirect(request, response, "www.baidu.com");
        }
        // 继续处理请求 需要返回 true，此处转发已中断请求处理故返回 false
        return false;
    }

    private boolean isRedirectByFrontEnd(HttpServletRequest request) {
        String contentType = request.getHeader(HttpHeaders.CONTENT_TYPE);
        // auth-no-redirect 是自定义和前端商量好，该键值存在时后端就不会自行转发的标记
        if (request.getHeader("auth-no-redirect") != null || StringUtils.hasLength(contentType)) {
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
