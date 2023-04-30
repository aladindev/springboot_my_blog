package com.aladin.springbootstudy.controller;

import com.aladin.springbootstudy.common.CommonFunction;
import com.aladin.springbootstudy.dto.TradeHistDto;
import com.aladin.springbootstudy.dto.UserExchngListDto;
import com.aladin.springbootstudy.service.TradeHistService;
import com.aladin.springbootstudy.service.UserInfoService;
import com.aladin.springbootstudy.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

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
            , Model model){

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

                // 당일 매매 일지
                List<TradeHistDto> tradeHistList = new ArrayList<>();

                // 리스트 별 보유 종목 Api 호출
                for(int i = 0 ; i < userExchngList.size() ; i++) {
                    userExchngList.get(i).setAccountsListFormDtoList(exchngApiRequest(userExchngList.get(i).getExchngCd()));

                    TradeHistDto tHDto = new TradeHistDto();
                    tHDto.setEmail(userExchngList.get(i).getEmail());
                    tHDto.setExchngCd(userExchngList.get(i).getExchngCd());
                    tHDto.setRgstrnDt(getDateFormat(getDate()));
                    tHDto = tradeHistService.selectTodayTradeHist(tHDto);

                    tHDto.setSrcUrl(userExchngList.get(i).getSrcUrl());

                    tradeHistList.add(tHDto);

                }
                model.addAttribute("userExchngList", userExchngList);

                if(tradeHistList != null && tradeHistList.size() > 0) {
                    model.addAttribute("tradeHistList", tradeHistList);
                }
            }
        } catch (Exception e) {
            log.error("listController exception " + e.getMessage());
        }
        return "list";
    }
}
