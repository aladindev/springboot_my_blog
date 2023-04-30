package com.aladin.springbootstudy.dto;
import com.aladin.springbootstudy.entity.KakaoProfileEntity;
import lombok.Data;

@Data
public class KakaoProfileDto {
    public Long id;
    public String connected_at;
    public Properties properties;
    public KakaoAccount kakao_account;

    @Data
    public static class KakaoAccount {
        public Boolean profile_nickname_needs_agreement;
        public Profile profile;
        public Boolean has_email;
        public Boolean email_needs_agreement;
        public Boolean is_email_valid;
        public Boolean is_email_verified;
        public String email;
        public String role;
        public String appKey;
    }

    @Data
    public static class Profile {
        public String nickname;
    }
    @Data
    public static class Properties {
        public String nickname;
    }

}

