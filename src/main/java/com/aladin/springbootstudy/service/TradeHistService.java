package com.aladin.springbootstudy.service;

import com.aladin.springbootstudy.dto.TradeHistDto;
import com.aladin.springbootstudy.dto.UserExchngListDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TradeHistService {
//    public List<TradeHistDto> getTradeHistToday(@Param("email") String email);
    public TradeHistDto selectTodayTradeHist(TradeHistDto tradeHistDto);
}
