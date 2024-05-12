package com.aladin.springbootstudy.mapper;

import com.aladin.springbootstudy.dto.DTO_USER_INFO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public class JoinDao {
    public interface UserMapper {
        @Select("SELECT SECRET_KEY FROM USER_INFO WHERE USER_ID WHERE SECRET_KEY = #{dtoUserInfo.secretKey}")
        DTO_USER_INFO findOne(@Param("dtoUserInfo") DTO_USER_INFO dtoUserInfo);
    }
}