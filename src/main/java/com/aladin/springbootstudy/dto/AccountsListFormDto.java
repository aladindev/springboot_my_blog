package com.aladin.springbootstudy.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountsListFormDto {

    public BigDecimal nowAmt;
    public String exchngCd;
    public String tokenName;
    public Double coinAmount;

}

