package com.aladin.springbootstudy.dto;
import lombok.Data;

@Data
public class UpbitAccountDto {

    public String currency;
    public String balance;
    public String locked;
    public String avg_buy_price;
    public Boolean avg_buy_price_modified;
    public String unit_currency;

}

