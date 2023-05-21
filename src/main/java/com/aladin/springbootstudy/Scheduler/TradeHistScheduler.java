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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

    @Value("#{kakao.email}")
    String email;

    @Autowired
    UserExchngListRepository userExchngListRepository;

    @Autowired
    TradeHistRepository tradeHistRepository;

    //@Scheduled(cron = "0 0 0 * * *") /* 매일 0시 */
    //@Scheduled(cron = "0 0 0/1 * * *") /* 1시간 주기*/
    @Scheduled(cron = "0 * * * * *") /* 1분 주기*/
    public void scheduleFixedDelayTask() throws InterruptedException {

        try {

            List<UserExchngListDto> userExchngListDtoList = userExchngListRepository.getUserEmailList();

            if (userExchngListDtoList != null && userExchngListDtoList.size() > 0) {

                Date date = getDate();
                String yyyyMMdd = getDateFormat(date);

                for (UserExchngListDto uELDto : userExchngListDtoList) {
                    List<AccountsListFormDto> accountsListFormDto = exchngApiRequest(uELDto.getExchngCd());

                    TradeHistDto tradeHistDto1 = new TradeHistDto();
                    tradeHistDto1.setEmail(email);
                    tradeHistDto1.setRgstrnDt(yyyyMMdd);
                    tradeHistDto1.setExchngCd(uELDto.getExchngCd());

                    int maxSn = tradeHistRepository.selectMaxSn(tradeHistDto1);

                    for (AccountsListFormDto alFDto : accountsListFormDto) {
                        TradeHistDto tradeHistDto = new TradeHistDto();

                        tradeHistDto.setEmail(email);
                        tradeHistDto.setTradeDt(date);
                        tradeHistDto.setSn(maxSn+1);
                        tradeHistDto.setExchngCd(uELDto.getExchngCd());
                        tradeHistDto.setTokenName(alFDto.getTokenName());
                        tradeHistDto.setNowAmt(alFDto.getNowAmt().setScale(0, RoundingMode.DOWN));
                        tradeHistDto.setRgstrnDt(yyyyMMdd);

                        int result = tradeHistRepository.insertTradeHist(tradeHistDto);

                    }
                }
            }
        } catch(Exception e) {
            log.error("trade hist scheduler exception > " + e);
        }
    }
}
