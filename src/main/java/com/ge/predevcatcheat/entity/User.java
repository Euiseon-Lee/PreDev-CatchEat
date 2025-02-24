package com.ge.predevcatcheat.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="t_user_info")
public class User {

    /** Entity란?
     *  DB 테이블과 1 대 1로 맵핑되는 클래스. == 실제 데이터
     *  JPA를 이용해 DB와 직접 CRUD 수행
     *  보통 @Table, @Id, @Column 등을 포함
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)     // DB의 AUTO_INCREMENT
    private int userNo;

    private String email;
    private String userName;
    private String password;
}
