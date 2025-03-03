CREATE TABLE user_info (
    user_id             BIGSERIAL PRIMARY KEY               -- 기본 키 (자동 증가)
    , provider          VARCHAR(20) NOT NULL                -- 로그인 제공자 (GOOGLE, KAKAO, APPLE, LOCAL)
    , provider_id       VARCHAR(100)                        -- SNS 로그인 사용자 고유 ID (LOCAL은 NULL)
    , email             VARCHAR(255) NOT NULL UNIQUE        -- 이메일 (로컬 사용자는 ID로 사용, SNS 로그인 사용자는 선택적)
    , password          VARCHAR(255)                        -- 비밀번호 (LOCAL 계정만 사용)
    , nickname          VARCHAR(50)                         -- 닉네임
    , profile_image_url VARCHAR(500)                        -- 프로필 이미지 URL
    , created_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- 생성 시간
    , updated_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- 수정 시간 >> 트리거로 업데이트

    , CONSTRAINT uq_provider UNIQUE (provider, provider_id)
);

-- updated_at 자동 갱신을 위한 트리거 함수 생성
CREATE FUNCTION update_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- 트리거 추가
CREATE TRIGGER update_user_info_updated_at
BEFORE UPDATE ON user_info
FOR EACH ROW
EXECUTE FUNCTION update_updated_at();
