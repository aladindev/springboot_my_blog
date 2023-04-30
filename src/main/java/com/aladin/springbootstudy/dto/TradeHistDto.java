package com.aladin.springbootstudy.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class TradeHistDto {
    public String email;
    public Integer sn;
    public Date tradeDt;
    public String exchngCd;
    public String tokenName;
    public BigDecimal nowAmt;
    public BigDecimal totAmt;
    public String rgstrnDt;
    public String srcUrl;
}
