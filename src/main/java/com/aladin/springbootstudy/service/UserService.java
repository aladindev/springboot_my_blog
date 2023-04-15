package com.aladin.springbootstudy.service;

import com.aladin.springbootstudy.dto.UserExchngListDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserService {
    public List<UserExchngListDto> getUserExchngList(@Param("email") String email);
}
