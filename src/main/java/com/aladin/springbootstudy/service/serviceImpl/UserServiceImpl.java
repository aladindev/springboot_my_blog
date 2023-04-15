package com.aladin.springbootstudy.service.serviceImpl;

import com.aladin.springbootstudy.dto.UserExchngListDto;
import com.aladin.springbootstudy.repository.UserExchngListRepository;
import com.aladin.springbootstudy.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    UserExchngListRepository mapper;

    @Override
    public List<UserExchngListDto> getUserExchngList(@Param("email") String email) {
        return mapper.getUserExchngList(email);
    }
}
