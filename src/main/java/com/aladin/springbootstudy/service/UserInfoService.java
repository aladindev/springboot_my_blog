package com.aladin.springbootstudy.service;

import com.aladin.springbootstudy.entity.KakaoProfileEntity;
import com.aladin.springbootstudy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserInfoService {

    @Autowired
    UserRepository userRepository;

    public List<KakaoProfileEntity> list(KakaoProfileEntity kakaoProfileEntity) {
        return null;
    }
}
