package io.github.soupedog.client;

import io.github.soupedog.domain.dto.LoginInInfo;
import io.github.soupedog.domain.dto.ServiceResponse;
import io.github.soupedog.domain.dto.UserCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
public class UserCenterClient {
    private RestTemplate restTemplate;
    private ParameterizedTypeReference<ServiceResponse<LoginInInfo>> typeRef = new ParameterizedTypeReference<>() {
    };

    @Value("#{'${user-center.api.url.prefix}' + '${user-center.api.check.path}'}")
    private String checkUrl;

    public UserCenterClient() {
        restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) throws IOException {
                return false;
            }

            @Override
            public void handleError(ClientHttpResponse response) throws IOException {

            }
        });
    }

    public ResponseEntity<ServiceResponse<LoginInInfo>> validLoginInfo(UserCredentials userCredentials) {
        String url = UriComponentsBuilder
                .fromUriString(checkUrl)
                // ``encode()`` 默认标记 utf8 编码
                .encode()
                .build()
                .toUriString();

        HttpEntity<UserCredentials> httpEntity = new HttpEntity<>(userCredentials, new HttpHeaders());

        return restTemplate.exchange(url, HttpMethod.POST, httpEntity, typeRef);
    }
}