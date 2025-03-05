package com.ge.predevcatcheat.security.model;

import com.ge.predevcatcheat.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
public class CustomUserDetails implements UserDetails {

    /** CustomUserDetails는 왜 필요한가?
     *
     *  Spring Security의 UsernamePasswordAuthenticationFilter에서 사용됨.
     *  사용자 정보를 DB에서 가져와 Spring Security에서 사용할 수 있도록 변환하는 역할을 수행.
     *  일반적으로 User 엔티티 자체를 그대로 사용하면 보안 관련 추가 기능을 넣기 어렵기 때문에 사용!
     *
     *  주요 역할
     *  1. `UserDetailsService`에서 `loadUserByUsername()` 호출 시 User 엔티티를 `UserDetails` 형태로 변환하여 반환.
     *  2. 이메일 & 비밀번호 기반 로그인 처리.
     *  3. Spring Security가 요구하는 계정 상태(활성화, 만료, 잠김 등) 체크 기능 제공.
     *  4. `getAuthorities()`를 통해 사용자 권한(Role) 관리 가능. (현재 `ROLE_USER`로 설정됨)
     *  5. `getUser()`를 통해 원본 User 엔티티를 반환하여 추가적인 정보 활용 가능.
     */

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 사용자의 권한(Role)을 설정 (예: ROLE_USER, ROLE_ADMIN 등)
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}