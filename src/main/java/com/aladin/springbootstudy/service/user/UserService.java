package com.aladin.springbootstudy.service.user;

import com.aladin.springbootstudy.dto.USER_AUTH_DTO;
import com.aladin.springbootstudy.dto.USER_INFO_DTO;
import com.aladin.springbootstudy.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    UserMapper userMapper;

    public USER_INFO_DTO getUserInfo(USER_INFO_DTO userInfoDto) {
        return userMapper.findUserInfo(userInfoDto);
    }

    public int insertUserInfo(USER_INFO_DTO userInfoDto) {
        return userMapper.insertUserInfo(userInfoDto);
    }

    public int insertUserAuth(USER_AUTH_DTO userAuthDto) {
        return userMapper.insertUserAuth(userAuthDto);
    }
}
