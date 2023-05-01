package com.aladin.springbootstudy.controller;

import com.aladin.springbootstudy.common.CommonFunction;
import com.aladin.springbootstudy.dto.TradeHistDto;
import com.aladin.springbootstudy.dto.TradeHistTodayDto;
import com.aladin.springbootstudy.dto.UserExchngListDto;
import com.aladin.springbootstudy.service.TradeHistService;
import com.aladin.springbootstudy.service.UserInfoService;
import com.aladin.springbootstudy.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/accounts") // infix = 공통 URL
public class ListController extends CommonFunction {

    @Autowired
    UserInfoService userInfoService; // JPA

    @Autowired
    UserService userService;

    @Autowired
    TradeHistService tradeHistService;

    @Value("#{kakao.app_key}")
    String appKey;

    @GetMapping(value="/list")
    public String accountsList(
              @SessionAttribute(name = "session_key", required = false) String session_key
            , @SessionAttribute(name = "email", required = false) String email
            , @SessionAttribute(name = "app_key", required = false) String appKey
            , HttpServletResponse response
            , Model model) throws IOException {

        try {
            if("".equals(session_key) || session_key == null || appKey == null || !appKey.equals(this.appKey)) {
                response.setContentType("text/html; charset=UTF-8");
                PrintWriter out = response.getWriter();
                out.println("<script>alert('로그인 세션 정보가 없습니다. 로그인 후 이용 바랍니다.'); location.href='/api/v1/get-api/login';</script>");
                out.flush();
                return null;
            } else {
                // 이용중인 거래소 리스트
                List<UserExchngListDto> userExchngList = userService.getUserExchngList(email);

                for(int i = 0 ; i < userExchngList.size() ; i++) {
                    userExchngList.get(i).setAccountsListFormDtoList(exchngApiRequest(userExchngList.get(i).getExchngCd()));
                }
                model.addAttribute("userExchngList", userExchngList);

                // 당일 매매 일지
                List<TradeHistTodayDto> tradeHistTodayDtoList = new ArrayList<>();

                // 리스트 별 보유 종목 Api 호출
                BigDecimal startAmtSum = BigDecimal.ZERO;
                BigDecimal nowAmtSum = BigDecimal.ZERO;
                BigDecimal diffAmtSum = BigDecimal.ZERO;

                TradeHistDto tHDto = new TradeHistDto();
                tHDto.setEmail(email);
                tHDto.setRgstrnDt(getDateFormat(getDate()));

                tradeHistTodayDtoList = tradeHistService.selectTodayTradeHist(tHDto);

                /* 총 합계 매매일지 */
                if(tradeHistTodayDtoList != null && tradeHistTodayDtoList.size() > 0) {

                    BigDecimal percent = BigDecimal.ZERO;

                    for(TradeHistTodayDto tradeHistTodayDto : tradeHistTodayDtoList) {
                        percent = tradeHistTodayDto.getNowAmt().subtract(tradeHistTodayDto.getStartAmt())
                                .divide(tradeHistTodayDto.getStartAmt(), 2, BigDecimal.ROUND_CEILING);
                        tradeHistTodayDto.setPercent(percent);

                        startAmtSum = startAmtSum.add(tradeHistTodayDto.getStartAmt());
                        nowAmtSum = nowAmtSum.add(tradeHistTodayDto.getNowAmt());
                        diffAmtSum = diffAmtSum.add(tradeHistTodayDto.getDiffAmt());
                    }

                    TradeHistTodayDto sumDto = new TradeHistTodayDto();
                    sumDto.setStartAmt(startAmtSum);
                    sumDto.setNowAmt(nowAmtSum);
                    sumDto.setDiffAmt(diffAmtSum);
                    sumDto.setSrcUrl("/img/sigma.png");
                    percent = nowAmtSum.subtract(startAmtSum)
                            .divide(startAmtSum, 2, BigDecimal.ROUND_CEILING);
                    sumDto.setPercent(percent);

                    tradeHistTodayDtoList.add(sumDto);

                    model.addAttribute("tradeHistTodayList", tradeHistTodayDtoList);
                }
            }
        } catch (Exception e) {
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<script>alert('로그인 세션 정보가 없습니다. 로그인 후 이용 바랍니다.'); location.href='/api/v1/get-api/login';</script>");
            out.flush();
        }
        return "list";
    }


    @ResponseBody
    @GetMapping("/tab_change")
    public List<TradeHistTodayDto> tabChange(
              @SessionAttribute(name = "session_key", required = false) String session_key
            , @SessionAttribute(name = "email", required = false) String email
            , @SessionAttribute(name = "app_key", required = false) String appKey
            , @RequestParam Map<String, Object> tabMap
            , HttpServletResponse response) throws IOException {

        try {
            if("".equals(session_key) || session_key == null || appKey == null || !appKey.equals(this.appKey)) {
                response.setContentType("text/html; charset=UTF-8");
                PrintWriter out = response.getWriter();
                out.println("<script>alert('로그인 세션 정보가 없습니다. 로그인 후 이용 바랍니다.'); location.href='/api/v1/get-api/login';</script>");
                out.flush();
                return null;
            } else {
                String tabId = (String)tabMap.get("tabId");

                switch (tabId) {
                    case "tab1" :
                        TradeHistDto tradeHistDto = new TradeHistDto();
                        tradeHistDto.setEmail(email);
                        tradeHistDto.setRgstrnDt(getDateFormat(getDate()));

                        List<TradeHistTodayDto> resultList = tradeHistService.selectTodayTradeHist(tradeHistDto);
                        return resultList;

                    case "tab2" :
                        break;
                    case "tab3" :
                        break;
                    default :
                        break;
                }

            }
        } catch(Exception e) {
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<script>alert('로그인 세션 정보가 없습니다. 로그인 후 이용 바랍니다.'); location.href='/api/v1/get-api/login';</script>");
            out.flush();
        }
        return null;
    }
}
