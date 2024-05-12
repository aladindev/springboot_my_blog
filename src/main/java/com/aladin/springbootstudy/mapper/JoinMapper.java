package com.aladin.springbootstudy.mapper;

import com.aladin.springbootstudy.dto.USER_INFO_DTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface JoinMapper {
    @Insert("INSERT USER_INFO(USER_ID, SECRET_KEY, CREATE_DTM, CHANGE_DTM)" +
            "VALUES(#{dtoUserInfo.userId}, #{dtoUserInfo.secretKey}, #{dtoUserInfo.createDtm}, #{dtoUserInfo.changeDtm}")
    int insertUser(@Param("dtoUserInfo") USER_INFO_DTO dtoUserInfo);
}
