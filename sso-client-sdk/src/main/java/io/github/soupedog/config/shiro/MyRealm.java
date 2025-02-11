package io.github.soupedog.config.shiro;

import io.github.soupedog.domain.dto.LoginInInfo;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;


public class MyRealm extends AuthorizingRealm {

    /**
     * loginInInfo 即 {@link AuthenticationToken#getPrincipal()}
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        LoginInInfo loginInInfo = (LoginInInfo) principalCollection.getPrimaryPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addRole("admin");
        info.addStringPermission("create");
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        MyAuthenticationToken myAuthenticationToken = getActualToken(authenticationToken);

        Object principal = authenticationToken.getPrincipal();

        if (principal == null) {
            throw new RuntimeException("未登录");
        }

        return new SimpleAuthenticationInfo(
                myAuthenticationToken.getLoginInInfo(),
                myAuthenticationToken.getCredentials(),
                getName());
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof MyAuthenticationToken;
    }

    protected MyAuthenticationToken getActualToken(AuthenticationToken authenticationToken) {
        return (MyAuthenticationToken) authenticationToken;
    }
}
