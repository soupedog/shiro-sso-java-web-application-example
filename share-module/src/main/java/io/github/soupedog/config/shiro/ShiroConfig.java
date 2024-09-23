package io.github.soupedog.config.shiro;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.AbstractShiroWebFilterConfiguration;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.config.ShiroFilterConfiguration;
import org.apache.shiro.web.filter.mgt.DefaultFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static io.github.soupedog.domain.constant.AccountConstant.ALGORITHM_NAME;
import static io.github.soupedog.domain.constant.AccountConstant.HASH_ITERATIONS;


@Configuration
public class ShiroConfig {
    /**
     * @see AbstractShiroWebFilterConfiguration#shiroFilterFactoryBean()
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(
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

        // 添加自定义的 Filter
        filterMap.put("myAccessControl", new MyAccessControlFilter());
        filterMap.put("myFormAuthentication", new MyFormAuthenticationFilter());
        filterMap.put("myLogout", new MyLogoutFilter());

        filterFactoryBean.setFilters(filterMap);

        return filterFactoryBean;
    }


    /**
     * @see org.apache.shiro.spring.boot.autoconfigure.ShiroAutoConfiguration#securityManager(List)
     */
    @Bean
    public Realm myRealm() {
        MyRealm result = new MyRealm();
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
        // 此处演示
        matcher.setHashAlgorithmName(ALGORITHM_NAME);
        // 重复循环 HASH_ITERATIONS 次 hash 做混淆(上一轮 hash 的出参进行下一轮 hash 的入参)
        matcher.setHashIterations(HASH_ITERATIONS);
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
        chainDefinition.addPathDefinition("/user/**", "anon");
        // file-entity、time-entity 需要基本的登录
        chainDefinition.addPathDefinition("/file-entity/**", "authc");
        chainDefinition.addPathDefinition("/time-entity/**", "authc");

        // 所有请求都需要经过
//        chainDefinition.addPathDefinition("/api/**", "myAccessControl,perms,roles");

        return chainDefinition;
    }
}
