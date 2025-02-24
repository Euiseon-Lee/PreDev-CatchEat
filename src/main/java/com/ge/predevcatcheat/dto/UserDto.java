package com.ge.predevcatcheat.dto;

import lombok.Getter;

@Getter
public class UserDto {
    /** DTO (Data Transfer Object)란?
     *  Client - Server 간 데이터 전송용 객체
     *  View, API 요청/응답 등 데이터 전송 용도
     *  비즈니스 로직 없이 단순한 데이터 전달 객체
     *  @Getter, @Setter, @NoArgsConstructor 주로 사용
     *  
     *  클라이언트에서 받은 요청을 가공하거나 응답 데이터 전달 >> 불필요한 데이터를 노출하지 않을 수 있음
     */

    private String email;
    private String userName;

    public UserDto(String email, String userName) {
        this.email = email;
        this.userName = userName;
    }

}
