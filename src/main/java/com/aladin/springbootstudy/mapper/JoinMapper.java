package com.aladin.springbootstudy.mapper;

import com.aladin.springbootstudy.dto.USER_INFO_DTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface JoinMapper {
    @Insert("INSERT INTO USER_INFO(USER_ID, SECRET_KEY, CREATE_DTM, CHANGE_DTM)" +
            "VALUES(LPAD(USER_INFO_SEQ.NEXTVAL, 6, '0'), #{dtoUserInfo.secretKey}, #{dtoUserInfo.createDtm}, #{dtoUserInfo.changeDtm})")
    int insertOne(@Param("dtoUserInfo") USER_INFO_DTO dtoUserInfo);
}
