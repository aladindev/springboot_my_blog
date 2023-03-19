package com.aladin.springbootstudy.entity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@NoArgsConstructor
@Table(name = "USER_INFO")
@Entity
public class KakaoProfileEntity {
    @Id
    @Column(name="id", unique = true, nullable = false)
    public Long id;
    @Column(name="nickname")
    public String nickname;
    @Column(name="connected_at")
    public String connected_at;
    @Column(name="email")
    public String email;
}