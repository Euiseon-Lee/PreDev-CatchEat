package com.ge.predevcatcheat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

/**
 * Spring Security 설정 클래스
 *
 * - 인증 및 인가(SecurityFilterChain)를 설정하는 역할을 한다.
 * - CSRF 보호 비활성화 (Spring Security 6.1 기준 최신 방식 적용)
 * - 특정 URL에 대한 접근 권한 설정
 * - JWT 또는 OAuth2를 사용하는 경우 추가 설정 필요
 */
@Configuration
public class SecurityConfig {

    /**
     * SecurityFilterChain을 정의하는 Bean
     *
     * - 모든 요청에 대해 기본적으로 인증을 요구하지만, 특정 경로는 허용 가능
     * - CSRF 보호는 비활성화 (JWT 또는 OAuth2 인증 시 필요 없음)
     * - 세션 정책을 STATELESS로 설정하여 세션을 사용하지 않음 (JWT 인증 시 필수)
     *
     * CSRF란?
     * Cross-Site Request Forgery, 사이트 간 요청 위조
     * 사용자가 본인의 의도와 상관없이 공격자가 만든 요청을 실행하게 만드는 웹 보안 공격 기법
     * Spring Security는 기본적으로 POST, PUT, DELETE 요청 시 CSRF 토큰이 필요하도록 설정됨.
     *  1. 사용자가 로그인하면 서버는 CSRF 토큰을 발급하여 클라이언트에게 전달.
     *  2. 이후 클라이언트가 POST/PUT/DELETE 요청을 보낼 때 해당 토큰을 함께 전송해야만 요청이 정상 처리됨.
     *  3. 만약 CSRF 토큰이 없거나 올바르지 않다면 요청이 거부됨.
     * CSRF 보호는 세션 기반 인증(쿠키를 사용하는 방식)에서는 필수지만, JWT, OAuth2 같은 토큰 기반 인증을 사용할 경우 불필요하기 때문에 비활성화하는 것.
     * CSRF는 쿠키 기반 세션 인증에서 발생하는 문제라서, 세션을 사용하지 않는다면 의미가 없음.
     *
     * @param http HttpSecurity 객체
     * @return SecurityFilterChain
     * @throws Exception HttpSecurity 설정 과정에서 예외 발생 시 처리
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)                                  // CSRF 보호 비활성화 (Spring Security 6.1 기준 최신 방식)
            .authorizeHttpRequests(auth -> auth     // authorizeHttpRequest()는 특정 URL 패턴 별 접근 권한을 설정함
                .requestMatchers("/", "/auth/**", "/public/**").permitAll()  // 특정 경로는 인증 없이 접근 가능
                .anyRequest().authenticated()                                       // 그 외 모든 요청은 인증 필요
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)             // JWT 사용 시 세션 비활성화 (세션을 사용하지 않고, JWT 인증을 사용할 경우 필수 설정)
            )
            .formLogin(withDefaults())                                              // 기본 로그인 폼 활성화 (OAuth2 사용 시 비활성화 가능 / JWT를 사용할 경우, 기본 로그인 폼을 비활성화하는 것이 일반적 / loginPage("/login")으로 커스텀 가능)
            .logout(withDefaults());                                                // 기본 로그아웃 기능 활성화

        return http.build();
    }
}
