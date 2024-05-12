package com.aladin.springbootstudy.mapper;

import com.aladin.springbootstudy.dto.USER_INFO_DTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("SELECT SECRET_KEY FROM USER_INFO WHERE SECRET_KEY = #{dtoUserInfo.secretKey}")
    USER_INFO_DTO findOne(@Param("dtoUserInfo") USER_INFO_DTO dtoUserInfo);
}