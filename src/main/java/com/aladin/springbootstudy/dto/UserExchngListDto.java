package com.aladin.springbootstudy.dto;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.util.List;

@Data
@Alias("UserExchngListDto")
public class UserExchngListDto {
    public String email;
    public String exchngCd;
    public String cdNm;
    public String srcUrl;
    public List<AccountsListFormDto> accountsListFormDtoList;
}

