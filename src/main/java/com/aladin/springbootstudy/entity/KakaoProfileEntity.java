package com.aladin.springbootstudy.entity;

import com.aladin.springbootstudy.common.CommonBuilder;
import com.aladin.springbootstudy.dto.KakaoProfileDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


// jpa -> mybatis
@Getter
@Table(name = "USER_INFO")
@Entity
public class KakaoProfileEntity {
    @Id
    @Column(name="id", unique = true, nullable = false)
    private Long id;
    @Column(name="nickname")
    private String nickname;
    @Column(name="connected_at")
    private String connected_at;
    @Column(name="email")
    private String email;
    @Column(name="role")
    private String role;
    @Column(name="app_key")
    private String appKey;

    private KakaoProfileEntity() {}
    /* builder pattern */
    private KakaoProfileEntity(KakaoBuilder builder) {
        this.id = builder.id;
        this.nickname = builder.nickname;
        this.connected_at = builder.connected_at;
        this.email = builder.email;
        this.role = builder.role;
        this.appKey = builder.appKey;
    }

    /* Kakao Profile builder class */
    public static class KakaoBuilder implements CommonBuilder<KakaoProfileEntity> {
        private Long id;
        private String nickname;
        private String connected_at;
        private String email;
        private String role;
        private String appKey;

        /* 생성자 */
        public KakaoBuilder(KakaoProfileDto kakaoProfileDto) {
            this.id = kakaoProfileDto.getId();
            this.nickname = kakaoProfileDto.getProperties().getNickname();
            this.connected_at = kakaoProfileDto.getConnected_at();
            this.email = kakaoProfileDto.getKakao_account().getEmail();
            this.role = kakaoProfileDto.getKakao_account().getRole();
            this.appKey = kakaoProfileDto.getKakao_account().getAppKey();
        }

        @Override
        public KakaoProfileEntity build() {
            return new KakaoProfileEntity(this);
        }
    }
}