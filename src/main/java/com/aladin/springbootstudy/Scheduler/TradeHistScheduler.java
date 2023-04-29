package com.aladin.springbootstudy.Scheduler;

import com.aladin.springbootstudy.common.CommonFunction;
import com.aladin.springbootstudy.dto.AccountsListFormDto;
import com.aladin.springbootstudy.dto.TradeHistDto;
import com.aladin.springbootstudy.dto.UserExchngListDto;
import com.aladin.springbootstudy.repository.TradeHistRepository;
import com.aladin.springbootstudy.repository.UserExchngListRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class TradeHistScheduler extends CommonFunction {
    @Value("#{crypto.upbit_a_key}")
    String upbit_a_key;

    @Value("#{crypto.upbit_s_key}")
    String upbit_s_key;

    @Value("#{crypto.upbit_server_domain}")
    String upbit_server_domain;

    @Value("#{crypto.upbit_get_accounts_url}")
    String upbit_get_accounts_url;

    @Value("#{crypto.upbit_get_ticker_url}")
    String upbit_get_ticker_url;

    @Value("#{crypto.binance_a_key}")
    String binance_a_key;

    @Value("#{crypto.binance_s_key}")
    String binance_s_key;

    @Autowired
    UserExchngListRepository userExchngListRepository;

    @Autowired
    TradeHistRepository tradeHistRepository;

    //@Scheduled(cron = "0 0 0 * * *") /* 매일 0시 */
    @Scheduled(cron = "0 */5 * * * *") /* 5분 주기*/
    public void scheduleFixedDelayTask() throws InterruptedException {

        try {
            List<UserExchngListDto> userExchngListDtoList = userExchngListRepository.getUserEmailList();

            if (userExchngListDtoList != null && userExchngListDtoList.size() > 0) {
                for (UserExchngListDto uELDto : userExchngListDtoList) {
                    List<AccountsListFormDto> accountsListFormDto = exchngApiRequest(uELDto.getExchngCd());

                    for (AccountsListFormDto alFDto : accountsListFormDto) {
                        TradeHistDto tradeHistDto = new TradeHistDto();

                        tradeHistDto.setEmail(uELDto.getEmail());
                        tradeHistDto.setExchngCd(uELDto.getExchngCd());
                        tradeHistDto.setTokenName(alFDto.getTokenName());
                        tradeHistDto.setNowAmt(alFDto.getNowAmt().setScale(0, RoundingMode.DOWN));

                        int result = tradeHistRepository.insertTradeHist(tradeHistDto);

                    }
                }
            }
        } catch(Exception e) {
            log.error("trade hist scheduler exception > " + e.getMessage());
        }
    }
}
