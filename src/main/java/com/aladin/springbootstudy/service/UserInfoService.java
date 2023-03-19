package com.aladin.springbootstudy.service;

import com.aladin.springbootstudy.entity.KakaoProfileEntity;
import com.aladin.springbootstudy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserInfoService {

    @Autowired
    UserRepository userRepository;

    public List<KakaoProfileEntity> getList(KakaoProfileEntity kakaoProfileEntity) {
        return null;
    }

    public Optional<KakaoProfileEntity> getOne(Long id) {

        System.out.println(userRepository.findById(id));
        return userRepository.findById(id);
    }
}
