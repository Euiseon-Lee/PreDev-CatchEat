package com.ge.predevcatcheat.repository;

import com.ge.predevcatcheat.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    /** DAO, 데이터 접근 계층과 거의 유사
     *  JPA를 사용해 DB에서 데이터를 읽고 쓰는 역할 => 실제 DB와 상호작용하는 역할
     *  JpaRepository<User, Long>을 상속하면 자동으로 CRUD 기능 제공
     *  User 엔터티에 대한 CRUD(Create, Read, Update, Delete) 작업을 수행
     *  쿼리를 직접 작성하지 않아도 됨.
     */

    /** 1. JDBC + SQL
     *  SQL을 직접 작성 및 실행해야 함.
     *  ResultSet을 수동으로 맵핑 필요 >> ResultSet을 User 객체로 변환하는 작업 필요
     *  기본적으로 트랜잭션을 지원하지만, 직접 관리해야 함. (Connection.setAutoCommit(false) 등 사용)
     *  DB 변경 가능성이 적고, SQL 최적화가 중요한 경우 사용.
     *  SQL 작성 시 PreparedStatement를 사용하여 SQL Injection 방지 가능
     *  MyBatis 같은 ORM 프레임워크를 사용하면 SQL과 객체 맵핑을 보다 쉽게 할 수 있음.
     *
     *  2. JPA
     *  SQL 대신 Entity 객체를 통해 데이터 조작
     *  자동으로 ResultSet을 객체로 변환
     *  자동 트랜잭션 관리 (@Transactional 활용)
     *  객체 지향적인 방식으로 데이터베이스를 조작
     */

    /** DAO vs Repository
     *
     *  1. DAO (Data Access Object)
     *  - 데이터베이스와 직접 소통하는 객체 (CRUD 담당)
     *  - 개발자가 직접 SQL 작성하여 사용
     *  - JDBC, MyBatis (SQL Mapper)를 사용할 때 주로 사용됨.
     *  - Hibernate 같은 ORM을 사용할 수도 있지만, Hibernate는 JPA 구현체이므로 JPA와 함께 쓰는 것이 일반적임.
     *      == DAO를 사용할 때 Hibernate를 함께 사용할 수 있지만, Hibernate 자체는 DAO 패턴을 반드시 요구하지 않음.
     *  - 보통 @Repository 선언하여 Spring에서 관리됨.
     *
     *  2. Repository
     *  - DAO의 확장 개념으로, 데이터 접근을 추상화한 개념.
     *  - Spring Data JPA를 활용하여 SQL 없이 데이터 조작 가능.
     *  - JpaRepository 인터페이스를 상속하면 자동으로 구현체가 생성됨.
     *  - JPA, MyBatis 등 프레임워크에서 자동 처리되어 사용
     *  - Spring Data JPA는 JPA 인터페이스를 상속하여 자동으로 구현체를 생성하는데, 기본적으로 Hibernate가 사용됨.
     *      == Spring Data JPA를 사용하면 Hibernate를 직접 코드에서 다루지 않더라도 Hibernate가 백그라운드에서 동작
     *          , Hibernate 외에도 EclipseLink 같은 다른 JPA 구현체도 존재 (반드시 Hibernate가 필수는 아님)
     *  - 기본적인 CRUD 메서드를 제공하며, 메서드명을 분석하여 SQL을 자동 생성.
     *
     *  결론) DAO는 전통적인 DB 접근 방식이고, Repository는 JPA와 ORM에 최적화된 방식
     */

    /** JPA는 어떻게 동작하는가?
     *  Spring Data JPA는 메서드 이름을 분석해서 자동으로 SQL을 생성
     *
     *  1. 기본 패턴: findBy + [필드명]([파라미터]) == SELECT + [TABLE] + WHERE [CONDITIONALS]
     *  2. 다양한 조건
     *      1) AND / OR 연산: User findByUsernameAndEmail(String username, String email);
     *      2) 비교 연산 (LessThan, GreaterThan, Between): List<User> findByAgeGreaterThan(int age);
     *      3) LIKE 검색 (Containing, StartsWith, EndsWith)
     *      4) 정렬 -> OrderBy 뒤에 정렬 기준을 붙여서 사용 (OrderByUsernameAsc, OrderByAgeDesc 등)
     *      5) 페이징 처리 관련 메서드 (Pageable 객체를 파라미터로 사용 가능)
     *  3. 명명규칙이 복잡해지면 @Query("SELECT ~") 를 선언하여 JPQL 또는 Native Query를 직접 사용 가능
     *
     *  Q. 그럼 반환하는 객체를 테이블 명으로 맵핑하는가?
     *  A. JPA는 기본적으로 엔티티 클래스 이름을 테이블 이름으로 사용 (@Entity가 붙은 클래스는 기본적으로 클래스명을 테이블명으로 맵핑)
     *     내부 필드는 컬럼으로 기본 인식함.
     *     기본적으로 CamelCase 필드명은 Snake_case 컬럼명으로 변환됨 (Spring Boot 설정에 따라 달라질 수 있음)
     *
     *     테이블을 직접 지정하고 싶은 경우 클래스명 위에 @Table(name = "user_info")로 선언 가능
     *     컬럼 역시 필드 위에 @Column(name = "user_name")를 사용하여 직접 지정할 수 있음
     *     @Column(nullable = false, length = 50) 등의 추가 설정도 가능
     *     데이터베이스의 테이블 이름이 대소문자를 구분하는 경우 (@Table(name = "")로 명시하는 것이 안전)
     */
    User findByEmail(String email);
}
