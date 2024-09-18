package io.github.soupedog.config.shiro;

import io.github.soupedog.domain.dto.LoginResponse;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.lang.util.ByteSource;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;


public class MyRealm extends AuthorizingRealm {
    public static final String ALGORITHM_NAME = "SHA-256";
    public static final ByteSource SALT = ByteSource.Util.bytes("AAAA");
    public static final Integer HASH_ITERATIONS = 2;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        AuthenticationInfo result = null;
        Object principal = authenticationToken.getPrincipal();
        String uid = principal.toString();

        if (uid != null) {
            // 用 uid 从数据库查出密码
            String pw = "c1a818a88adb66de711940dae5d22a2213c5353b72bf1b0c776626b8f3739087";

            result = new SimpleAuthenticationInfo(
                    LoginResponse.builder().token("test").build(),
                    pw,
                    SALT,
                    getName()
            );
        }

        return result;
    }
}
