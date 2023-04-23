package com.aladin.springbootstudy.service.serviceImpl;

import com.aladin.springbootstudy.dto.TradeHistDto;
import com.aladin.springbootstudy.dto.UserExchngListDto;
import com.aladin.springbootstudy.repository.TradeHistRepository;
import com.aladin.springbootstudy.repository.UserExchngListRepository;
import com.aladin.springbootstudy.service.TradeHistService;
import com.aladin.springbootstudy.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TradeHistServiceImpl implements TradeHistService {

    @Autowired
    TradeHistRepository mapper;

    @Override
    public List<TradeHistDto> getTradeHistToday(@Param("email") String email) {
        return mapper.selectTradeHist(email);
    }
}
