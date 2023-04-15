package com.aladin.springbootstudy.dto;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("UserExchngListDto")
public class UserExchngListDto {
    public String exchngCd;
}

