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
 ├── common/
 │    ├── enums/
 │    │    ├── UserRole.java        # 사용자의 권한(Role) 정보를 정의하는 상수 값 (선택 사항)
 │
 ├── config/                        # 애플리케이션 전반 설정 (보안 포함)
 │    ├── SecurityConfig.java       # Spring Security 설정 (인증/인가, 필터 설정)
 │    ├── OAuth2Config.java         # OAuth2 설정 (필요한 경우)
 │    ├── WebConfig.java            # 정적 자원, CORS, View Resolver 설정
 │
 ├── controller/
 │    ├── HomeController.java       # 메인 페이지 컨트롤러 (Thymeleaf)
 │    ├── AuthController.java       # 로그인/회원가입 관련 컨트롤러
 │
 ├── dto/                           # 데이터 전송 객체 (클라이언트와 데이터 교환)
 │    ├── UserDto.java
 │
 ├── entity/                        # JPA 엔터티 클래스 (DB 매핑)
 │    ├── User.java
 │
 ├── exception/
 │    ├── GlobalExceptionHandler.java           # 전역 예외 처리 (@ControllerAdvice)
 │
 ├── repository/                                # 데이터베이스 접근 계층 (JPA Repository)
 │    ├── UserRepository.java
 │
 ├── security/                                  # 인증 & 보안 관련 패키지
 │    ├── config/                     
 │    │    ├── JwtTokenProvider.java            # JWT 토큰 생성 및 검증
 │    │    ├── JwtAuthenticationFilter.java     # JWT 인증 필터
 │    │
 │    ├── service/                              # 인증 서비스 계층
 │    │    ├── CustomUserDetailsService.java    # UserDetailsService 구현
 │    │    ├── CustomOAuth2UserService.java     # OAuth2UserService 구현
 │    │
 │    ├── model/
 │    │    ├── CustomUserDetails.java           # UserDetails 구현체
 │    │    ├── OAuth2UserInfo.java              # SNS 유저 정보 추상 클래스
 │    │    ├── GoogleUserInfo.java              # Google 로그인 정보
 │    │    ├── KakaoUserInfo.java               # Kakao 로그인 정보
 │
 ├── service/                        # 비즈니스 로직 (서비스 계층)
 │    ├── IUserService.java
 │    ├── UserServiceImpl.java
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
 ├── PreDevCatchEatApplication.java  # Spring Boot 메인 실행 클래스

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

---

## 5️⃣ 헷갈리는 개념 정리
### **UserRole (Enum)**
- 사용자의 권한(Role) 정보를 정의하는 상수 값
- Enum을 사용하면 허용된 값만 사용할 수 있어 실수를 방지가능
- 예를 들어, "ADMIN", "USER" 같은 권한을 문자열이 아니라 타입 안정적인 Enum으로 관리.
```
public enum UserRole {
    USER,   // 일반 사용자
    ADMIN   // 관리자
}
```
- 왜 Enum을 써야 할까?
  - 고정된 값이므로 실수 방지 (오타 방지)
  - 가독성이 높아짐 (UserRole.USER vs "user")
  -  DB에도 그대로 저장 가능 (@Enumerated(EnumType.STRING))

### **User (Entity)**
- DB 테이블과 매핑되는 클래스 (JPA 엔터티)
- 데이터베이스에서 데이터를 가져오고, 저장할 때 사용하는 핵심 객체.
- JPA를 사용해 자동으로 DB와 매핑되며, Repository를 통해 CRUD를 수행.
```
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;

    @Enumerated(EnumType.STRING)  // UserRole Enum을 DB에 문자열로 저장
    private UserRole role;

    // Getter & Setter
}
```
- 주요 역할
  - DB와 직접 연결 (JPA에서 자동으로 테이블 매핑)
  - UserRepository를 통해 CRUD 수행

### **UserDto (DTO - Data Transfer Object)**
- 데이터 전송을 위한 객체 (클라이언트 ↔ 서버)
- DB와 직접 연결되는 User 엔터티는 보안 문제나 DTO 변환이 필요한 경우에 DTO 사용.
- 클라이언트가 필요한 데이터만 포함해서 보낼 수도 있고, 보안을 위해 민감한 데이터를 제외할 수도 있음.
```
public class UserDto {
    private String email;
    private String role;  // 문자열로 변환하여 전달

    public UserDto(User user) {
        this.email = user.getEmail();
        this.role = user.getRole().name(); // Enum -> String 변환
    }

    // Getter & Setter
}
```
- 주요 역할
  - 클라이언트와 서버 간 데이터 전송
  - 엔터티와 분리하여 보안 및 확장성 확보

### **UserRepository**
- DB에 접근하는 인터페이스 (Spring Data JPA)
- JpaRepository<User, Long>을 상속하면 기본적인 CRUD 메서드가 자동 생성.
- SQL을 직접 작성하지 않고도, 메서드 이름만으로 조회 가능.
```
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
```
- 주요 역할
  - DB에서 User 엔터티 조회/저장/삭제
  - JPA를 사용하여 SQL 없이 쿼리 실행