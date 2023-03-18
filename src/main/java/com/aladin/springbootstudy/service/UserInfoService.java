package com.aladin.springbootstudy.service;

import com.aladin.springbootstudy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserInfoService {

    @Autowired
    UserRepository userRepository;

}
