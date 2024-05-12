package com.aladin.springbootstudy.service.user;

import com.aladin.springbootstudy.dto.USER_INFO_DTO;
import com.aladin.springbootstudy.mapper.UserMapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private Logger logger = Logger.getLogger(UserService.class);

    @Autowired
    UserMapper userMapper;

    public USER_INFO_DTO getUserInfo(USER_INFO_DTO userInfoDto) {
        return userMapper.findOne(userInfoDto);
    }
}
