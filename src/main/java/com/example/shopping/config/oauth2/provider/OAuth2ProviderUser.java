package com.example.shopping.config.oauth2.provider;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

// 여기는 소셜 로그인 정보를 담아둘 곳의 공통적인 요소들을 모아놓은 곳입니다.
// 소셜 로그인 즉, 구글, 네이버 등은 각자 다르므로 관리하기 쉽게 공통적인 요소는
// 모아놓고 코드의 중복화를 줄입니다.
public abstract class OAuth2ProviderUser implements OAuth2UserInfo{
    private Map<String, Object> attributes;
    private OAuth2User oAuth2User;
    private ClientRegistration clientRegistration;

    public OAuth2ProviderUser(
            Map<String, Object> attributes,
            OAuth2User oAuth2User,
            ClientRegistration clientRegistration
    ) {
        this.attributes = attributes;
        this.oAuth2User = oAuth2User;
        this.clientRegistration = clientRegistration;
    }

    @Override
    public String getProvider() {
        return clientRegistration.getRegistrationId();
    }

    @Override
    public String getEmail() {
        return (String) getAttributes().get("email");
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }
}
