package com.aladin.springbootstudy.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class TradeHistTodayDto {
    String exchngCd;
    BigDecimal startAmt;
    BigDecimal nowAmt;
    BigDecimal diffAmt;
    String srcUrl;
    BigDecimal percent;
}
