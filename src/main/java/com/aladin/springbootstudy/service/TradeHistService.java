package com.aladin.springbootstudy.service;

import com.aladin.springbootstudy.dto.TradeHistDto;
import com.aladin.springbootstudy.dto.TradeHistTodayDto;

import java.util.List;

public interface TradeHistService {
//    public List<TradeHistDto> getTradeHistToday(@Param("email") String email);
    public List<TradeHistTodayDto> selectTodayTradeHist(TradeHistDto tradeHistDto);
}
