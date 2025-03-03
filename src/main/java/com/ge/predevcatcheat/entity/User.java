package com.ge.predevcatcheat.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Entity
@Getter
@Setter
@Table(name="user_info")
public class User {

    /** Entity란?
     *  DB 테이블과 1 대 1로 맵핑되는 클래스. == 실제 데이터
     *  JPA를 이용해 DB와 직접 CRUD 수행
     *  보통 @Table, @Id, @Column 등을 포함
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)     // DB의 AUTO_INCREMENT
    private int userId; // 내부적으로 사용하는 기본 키

    @Column(nullable = false, length = 20)
    private String provider;                                // "LOCAL", "GOOGLE", "KAKAO", "APPLE"

    @Column(nullable = true, length = 100, unique = true)
    private String providerId;                              // SNS 로그인 ID (LOCAL 사용자는 NULL)

    @Column(nullable = true, length = 255, unique = true)
    private String email;                                   // LOCAL 로그인 ID (SNS 사용자는 선택 사항)

    private String password;                                // LOCAL 사용자만 사용

    private String nickname;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;
}
