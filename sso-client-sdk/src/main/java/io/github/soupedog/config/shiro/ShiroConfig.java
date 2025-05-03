package io.github.soupedog.config.shiro;

import io.github.soupedog.client.UserCenterClient;
import jakarta.servlet.Filter;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.AbstractShiroWebFilterConfiguration;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.config.ShiroFilterConfiguration;
import org.apache.shiro.web.filter.mgt.DefaultFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Configuration
public class ShiroConfig {
    @Value("${user-center.back-url}")
    private String backUrl;
    @Value("#{'${user-center.api.url.prefix}'+'/login?back_url='+'${user-center.back-url}'}")
    private String logoutRedirectUrl;
    @Value("${shiro.local.logout.path}")
    private String localLogoutPath;

    /**
     * @see AbstractShiroWebFilterConfiguration#shiroFilterFactoryBean()
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(
            UserCenterClient userCenterClient,
            SecurityManager securityManager,
            @Autowired(required = false) ShiroFilterConfiguration shiroFilterConfiguration,
            ShiroFilterChainDefinition shiroFilterChainDefinition,
            Map<String, Filter> filterMap
    ) {
        ShiroFilterFactoryBean filterFactoryBean = new ShiroFilterFactoryBean();
        filterFactoryBean.setLoginUrl("/login");
        filterFactoryBean.setSuccessUrl("/");
        filterFactoryBean.setUnauthorizedUrl(null);

        filterFactoryBean.setSecurityManager(securityManager);
        filterFactoryBean.setShiroFilterConfiguration(shiroFilterConfiguration == null ? new ShiroFilterConfiguration() : shiroFilterConfiguration);
        filterFactoryBean.setGlobalFilters(Collections.singletonList(DefaultFilter.invalidRequest.name()));
        filterFactoryBean.setFilterChainDefinitionMap(shiroFilterChainDefinition.getFilterChainMap());
        MyAccessControlFilter myAccessControlFilter = new MyAccessControlFilter(localLogoutPath, userCenterClient);

        MyLoginFilter myLoginFilter = new MyLoginFilter();

        // 添加自定义的 Filter
        filterMap.put("myAccessControl", myAccessControlFilter);
        filterMap.put("myFormAuthentication", myLoginFilter);
        filterMap.put("myLogout", new MyLogoutFilter(logoutRedirectUrl));

        filterFactoryBean.setFilters(filterMap);

        return filterFactoryBean;
    }


    /**
     * @see org.apache.shiro.spring.boot.autoconfigure.ShiroAutoConfiguration#securityManager(List)
     */
    @Bean
    public Realm myRealm() {
        MyRealm result = new MyRealm();
        result.setCachingEnabled(false);
        //        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
        //        // 此处演示
        //        matcher.setHashAlgorithmName(ALGORITHM_NAME);
        //        // 重复循环 HASH_ITERATIONS 次 hash 做混淆(上一轮 hash 的出参进行下一轮 hash 的入参)
        //        matcher.setHashIterations(HASH_ITERATIONS);

        MyMatcher matcher = new MyMatcher();
        result.setCredentialsMatcher(matcher);
        return result;
    }

    /**
     * https://shiro.apache.org/web.html#default_filters
     * <p>
     * 原生提供的 definition 和 Filter 对照表，同一 path 下多次配置是取并集
     */
    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
        //        // logged in users with the 'admin' role
        //        chainDefinition.addPathDefinition("/admin/**", "authc, roles[admin]");
        //
        //        // logged in users with the 'document:read' permission
        //        chainDefinition.addPathDefinition("/docs/**", "authc, perms[document:read]");
        //
        //        // all other paths require a logged in user
        //        chainDefinition.addPathDefinition("/**", "authc");

        // user 开头的接口无需鉴权
        chainDefinition.addPathDefinition("/user/check", "anon");
        // 登出
        chainDefinition.addPathDefinition(localLogoutPath, "myLogout");
        // file-entity、time-entity 需要基本的登录
        chainDefinition.addPathDefinition("/file-entity/**", "authc");
        chainDefinition.addPathDefinition("/time-entity/**", "authc");
        chainDefinition.addPathDefinition("/sso-login", "myFormAuthentication");

        // 所有请求都需要经过
        chainDefinition.addPathDefinition("/api/**", "myAccessControl,perms,roles");

        return chainDefinition;
    }
}
