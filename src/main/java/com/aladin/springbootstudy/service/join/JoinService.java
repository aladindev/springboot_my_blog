package com.aladin.springbootstudy.service.join;

import com.aladin.springbootstudy.dto.USER_INFO_DTO;
import com.aladin.springbootstudy.mapper.JoinMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JoinService {

    @Autowired
    JoinMapper joinMapper;
    public int joinUser(USER_INFO_DTO userInfoDto) {
        return joinMapper.insertOne(userInfoDto);
    }
}
