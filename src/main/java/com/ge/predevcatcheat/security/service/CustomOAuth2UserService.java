package com.ge.predevcatcheat.security.service;

import com.ge.predevcatcheat.entity.User;
import com.ge.predevcatcheat.repository.UserRepository;
import com.ge.predevcatcheat.security.model.CustomOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    /** CustomOAuth2UserService의 역할
     *  OAuth2 로그인 처리를 담당하는 서비스.
     *  Spring Security에서 인증(Authentication) 처리의 핵심 역할 수행.
     *  OAuth2UserService를 구현해서 사용자 정보를 로드하고, 인증 객체로 변환하는 역할을 수행.
     */

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {     //Java에서는 생성자의 이름이 클래스 이름과 동일해야 한다.
        this.userRepository = userRepository;
    }

    /**
     * loadUser() 메서드에서 OAuth2 Provider(Google, Kakao)에서 제공하는 사용자 정보를 받아옴.
     * 사용자 이메일을 기준으로 DB에서 기존 회원을 찾음.
     *  1. 기존 회원이면 → 정보 업데이트 가능.
     *  2. 신규 회원이면 → 자동 회원가입 진행 후 저장.
     * CustomOAuth2User 객체를 반환하여 Spring Security가 사용할 수 있도록 변환.
     *
     * @param userRequest OAuth2 로그인 요청
     * @return OAuth2User 객체
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // OAuth2 Provider 정보 (Google, Kakao 등)
        String provider = userRequest.getClientRegistration().getRegistrationId();
        String email = oAuth2User.getAttribute("email");

        Optional<User> userOptional = userRepository.findByEmail(email);

        User user;
        if (userOptional.isPresent()) {
            // 기존 회원 -> 업데이트
            user = userOptional.get();
        } else {
            // 신규 회원 -> 회원가입 처리
            user = new User();
            user.setEmail(email);
            user.setProvider(provider);
            userRepository.save(user);
        }

        // CustomOAuth2User로 변환하여 반환
        return new CustomOAuth2User(user, oAuth2User.getAttributes());
    }
}
