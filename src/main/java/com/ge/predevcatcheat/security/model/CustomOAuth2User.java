package com.ge.predevcatcheat.security.model;

import com.ge.predevcatcheat.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {

    /** CustomOAuth2User가 왜 필요한가?
     *
     *  OAuth2User 인터페이스를 구현하여 Google, Kakao 같은 소셜 로그인에 대응.
     *  OAuth2UserService (CustomOAuth2UserService)에서 소셜 로그인 성공 후 OAuth2User 타입으로 변환하여 Spring Security에서 사용 가능하도록 함.
     *  OAuth2에서 받은 SNS 사용자 정보를 매핑하고, 추가적인 커스텀 데이터를 저장할 수 있음.
     *
     *  주요 역할
     *  1. OAuth2 Provider에서 제공하는 사용자 정보를 `OAuth2User` 객체로 변환.
     *  2. getAttributes()를 통해 원본 OAuth2 사용자 데이터 제공.
     *  3. getName()은 OAuth2 Provider에서 제공하는 고유 ID(예: Google: "sub", Kakao: "id")를 반환.
     *  4. getEmail()을 추가하여 사용자 이메일을 쉽게 가져올 수 있도록 함.
     *  5. getProvider()를 통해 어떤 소셜 로그인(Google, Kakao 등)을 사용했는지 확인 가능.
     */

    private final User user;
    private Map<String, Object> attributes = Map.of();

    public CustomOAuth2User(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = this.attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getName() {
        // OAuth2 Provider별로 ID 필드가 다를 수 있음.
        // Google의 경우 "sub", Kakao의 경우 "id"가 고유 ID.
        return attributes.getOrDefault("sub", user.getEmail()).toString();
    }

    public String getEmail() {
        return user.getEmail();
    }

    public String getProvider() {
        return user.getProvider(); // Google, Kakao 등 Provider 정보 반환
    }
}
