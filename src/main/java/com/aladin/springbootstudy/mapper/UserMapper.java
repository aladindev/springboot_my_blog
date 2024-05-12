package com.aladin.springbootstudy.mapper;

import com.aladin.springbootstudy.dto.USER_AUTH_DTO;
import com.aladin.springbootstudy.dto.USER_INFO_DTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("SELECT USER_ID AS userId FROM USER_INFO WHERE SECRET_KEY = #{dtoUserInfo.secretKey}")
    USER_INFO_DTO findUserInfo(@Param("dtoUserInfo") USER_INFO_DTO dtoUserInfo);

    @Insert("INSERT INTO USER_INFO(USER_ID, SECRET_KEY, CREATE_DTM, CHANGE_DTM)" +
            "VALUES(LPAD(USER_INFO_SEQ.NEXTVAL, 6, '0'), #{dtoUserInfo.secretKey}, #{dtoUserInfo.createDtm}, #{dtoUserInfo.changeDtm})")
    int insertUserInfo(@Param("dtoUserInfo") USER_INFO_DTO dtoUserInfo);

    @Insert("INSERT INTO USER_INFO(USER_ID, AUTH_CD, AUTH_LEVEL, CRATE_DTM, CHANGE_DTM)" +
            "VALUES(#{userAuthDto.userId}, #{userAuthDto.authCd}, #{userAuthDto.authLevel}, #{userAuthDto.createDtm}, #{userAuthDto.changeDtm})")
    int insertUserAuth(@Param("userAuthDto") USER_AUTH_DTO userAuthDto);
}