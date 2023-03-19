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

    public KakaoProfileEntity getOne(Long id) {

        Optional<KakaoProfileEntity> result = userRepository.findById(id);
        if(result.isPresent()) {
            return result.get();
        }else {
            return result.orElse(null);
        }
    }
}
