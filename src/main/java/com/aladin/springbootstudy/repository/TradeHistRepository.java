package com.aladin.springbootstudy.repository;

import com.aladin.springbootstudy.dto.TradeHistDto;
import com.aladin.springbootstudy.dto.TradeHistTodayDto;
import com.aladin.springbootstudy.dto.UserExchngListDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface TradeHistRepository {
    //List<UserExchngListDto> getUserExchngList(@Param("email") String email);
    int insertTradeHist(TradeHistDto tradeHistDto);
    List<TradeHistDto> selectTradeHist(String email);
    int selectMaxSn(TradeHistDto tradeHistDto);
    List<TradeHistTodayDto> selectTodayTradeHist(TradeHistDto tradeHistDto);
    List<TradeHistTodayDto> selectYesterDayTradeHist(TradeHistDto tradeHistDto);
}
