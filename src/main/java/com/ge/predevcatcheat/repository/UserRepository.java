package com.ge.predevcatcheat.repository;

import com.ge.predevcatcheat.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    /** 1. JDBC + SQL
     *  ResultSet을 수동으로 맵핑 필요 >> ResultSet을 User 객체로 변환하는 작업 필요
     *  트랜잭션 관리가 필요, SQL을 직접 작성해야 함.
     *  DB 변경 가능성이 적고, SQL 최적화가 중요한 경우 사용.
     *
     *  2. JPA
     *  SQL 대신 Entity 객체를 통해 데이터 조작
     *  자동으로 ResultSet을 객체로 변환
     *  자동 트랜잭션 관리 (@Transactional)
     *  객체 지향적인 방식으로 데이터베이스를 조작
     */

    User findByEmail(String email);
}
