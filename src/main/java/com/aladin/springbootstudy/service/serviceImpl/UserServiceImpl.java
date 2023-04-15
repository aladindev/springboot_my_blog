package com.aladin.springbootstudy.service.serviceImpl;

import com.aladin.springbootstudy.dto.UserExchngListDto;
import com.aladin.springbootstudy.repository.UserExchngListRepository;
import com.aladin.springbootstudy.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserExchngListRepository mapper;

    @Override
    public List<UserExchngListDto> getUserExchngList(@Param("eamil") String email) {
        return mapper.getUserExchngList();
    }
}
