package com.ge.predevcatcheat.security.service;

import com.ge.predevcatcheat.entity.User;
import com.ge.predevcatcheat.repository.UserRepository;
import com.ge.predevcatcheat.security.model.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    /** CustomOAuth2UserService의 역할
     *  일반 로그인(이메일+비밀번호) 처리를 담당하는 서비스
     *  Spring Security에서 인증(Authentication) 처리의 핵심 역할 수행.
     *  UserDetailsService를 구현해서 사용자 정보를 로드하고, 인증 객체로 변환하는 역할을 수행.
     */
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * loadUserByUsername(String email) 메서드를 통해 DB에서 이메일을 기준으로 사용자 조회.
     * 사용자가 존재하면 CustomUserDetails 객체로 변환하여 반환.
     * Spring Security가 비밀번호 검증을 수행하도록 도움.
     *
     * @param email 사용자의 이메일
     * @return UserDetails 객체
     * @throws UsernameNotFoundException 사용자가 존재하지 않으면 예외 발생
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // findByEmail은 Optional을 반환하므로 isPresent()로 확인
        Optional<User> userOptional = userRepository.findByEmail(email);

        User user = userOptional.orElseThrow(() ->
            new UsernameNotFoundException("User not found with email: " + email));

        return new CustomUserDetails(user); // UserDetails 구현체 반환
    }
}
