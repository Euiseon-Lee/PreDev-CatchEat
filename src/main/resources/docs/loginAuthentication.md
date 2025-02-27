# 🔐 로그인/회원가입 & 인증 구조
### 250227.이의선.수정

## 1️⃣ 사용자 로그인 요청 흐름

```
  ├──> (1) Spring Security Filter Chain
        │  
        ├──> (2) [기본 로그인] UsernamePasswordAuthenticationFilter (아이디/비밀번호)
        │       ├──> AuthenticationManager
        │       ├──> UserDetailsService (DB 조회)
        │       └──> 인증 완료 → SecurityContext에 저장
        │
        ├──> (3) [OAuth2 로그인] OAuth2LoginAuthenticationFilter
                ├──> OAuth2UserService (OAuth2 공급자에서 사용자 정보 가져옴)
                └──> 인증 완료 → SecurityContext에 저장
```

---

## 2️⃣ 프로젝트 로그인 관련 패키지 구조

```
src/main/java/com/ge/predevcatcheat
 ├── config/                        # 설정 관련 (Spring Security 포함)
 │    ├── SecurityConfig.java       # Spring Security 설정 클래스
 │    ├── OAuth2Config.java         # OAuth2 설정 (필요한 경우)
 │    ├── WebConfig.java            # 정적 자원 및 View Resolver 설정
 │
 ├── controller/
 │    ├── HomeController.java       # 메인 페이지 컨트롤러 (Thymeleaf)
 │    ├── AuthController.java       # 로그인/회원가입 관련 컨트롤러
 │
 ├── dto/
 │    ├── UserDto.java
 │
 ├── entity/
 │    ├── User.java
 │
 ├── repository/
 │    ├── UserRepository.java
 │
 ├── security/                                  # 인증 관련 패키지
 │    ├── service/
 │    │    ├── CustomUserDetailsService.java    # UserDetailsService 구현
 │    │    ├── CustomOAuth2UserService.java     # OAuth2UserService 구현
 │    │
 │    ├── model/
 │    │    ├── CustomUserDetails.java           # UserDetails 구현체
 │    │    ├── OAuth2UserInfo.java              # SNS 유저 정보 추상 클래스
 │    │    ├── GoogleUserInfo.java              # Google 로그인 정보
 │    │    ├── KakaoUserInfo.java               # Kakao 로그인 정보
 │    │
 │    ├── jwt/                                  # JWT 사용 시 추가 (선택)
 │    │    ├── JwtTokenProvider.java            # JWT 토큰 생성 및 검증
 │    │    ├── JwtAuthenticationFilter.java     # JWT 필터
 │
 ├── service/
 │    ├── IUserService.java
 │    ├── UserServiceImplementation.java
 │
 ├── view/                           # Thymeleaf 템플릿 폴더 (resources/templates/)
 │    ├── layout/                    # 공통 레이아웃 파일
 │    │    ├── header.html           # 헤더
 │    │    ├── footer.html           # 푸터
 │    │    ├── main.html             # 메인 레이아웃
 │    │
 │    ├── auth/                      # 로그인/회원가입 관련 뷰
 │    │    ├── login.html            # 로그인 페이지
 │    │    ├── register.html         # 회원가입 페이지
 │    │
 │    ├── home.html                  # 메인 페이지
 │
 ├── static/                         # 정적 자원 폴더 (resources/static/)
 │    ├── css/                       # CSS 파일
 │    ├── js/                        # JS 파일
 │    ├── images/                    # 이미지 파일
 │
 ├── PreDevCatchEatApplication.java

```

---

## 3️⃣ 인증 관련 주요 클래스 설명

### 🔹 `SecurityConfig.java`
- Spring Security의 전반적인 설정을 담당.
- 인증, 인가, CORS 정책 등을 정의.

### 🔹 `CustomUserDetailsService.java`
- `UserDetailsService`를 구현하여 데이터베이스에서 사용자를 조회하는 역할.

### 🔹 `CustomOAuth2UserService.java`
- `OAuth2UserService`를 구현하여 OAuth2 로그인 처리.

### 🔹 `JwtTokenProvider.java` (JWT 사용 시)
- JWT를 생성하고 검증하는 역할.

### 🔹 `JwtAuthenticationFilter.java` (JWT 사용 시)
- JWT를 이용한 인증 필터.

---

## 4️⃣ 로그인 API 엔드포인트

| 요청 방식 | 엔드포인트 | 설명 |
|-----------|-----------|--------------------|
| `POST` | `/auth/login` | 일반 로그인 |
| `GET` | `/oauth2/authorize/google` | Google 로그인 |
| `GET` | `/oauth2/authorize/kakao` | Kakao 로그인 |

