package io.github.soupedog.config.shiro;

import io.github.soupedog.domain.dto.UserCredentials;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;

@Slf4j
public class MyMatcher extends SimpleCredentialsMatcher {

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        UserCredentials credentialsInToken = (UserCredentials) token.getCredentials();
        UserCredentials credentialsInInfo = (UserCredentials) info.getCredentials();

        boolean result = credentialsInToken.getUno().equals(credentialsInInfo.getUno())
                && credentialsInToken.getToken().equals(credentialsInInfo.getToken());

        log.info("credentialsInToken.equals(credentialsInInfo)=={}", result);

        return result;
    }

}
