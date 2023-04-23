package com.aladin.springbootstudy.controller;

import com.aladin.springbootstudy.common.CommonFunction;
import com.aladin.springbootstudy.dto.AccountsListFormDto;
import com.aladin.springbootstudy.dto.TradeHistDto;
import com.aladin.springbootstudy.dto.UpbitAccountDto;
import com.aladin.springbootstudy.dto.UserExchngListDto;
import com.aladin.springbootstudy.service.TradeHistService;
import com.aladin.springbootstudy.service.UserInfoService;
import com.aladin.springbootstudy.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/accounts") // infix = 공통 URL
public class ListController extends CommonFunction{

    @Autowired
    UserInfoService userInfoService; // JPA

    @Autowired
    UserService userService;

    @Autowired
    TradeHistService tradeHistService;

    @GetMapping(value="/list")
    public String accountsList(
              @SessionAttribute(name = "session_key", required = false) String session_key
            , @SessionAttribute(name = "email", required = false) String email
            , HttpServletResponse response
            , Model model){

        try {
            if("".equals(session_key) || session_key == null) {
                response.setContentType("text/html; charset=UTF-8");
                PrintWriter out = response.getWriter();
                out.println("<script>alert('로그인 세션 정보가 없습니다. 로그인 후 이용 바랍니다.'); location.href='/api/v1/get-api/login';</script>");
                out.flush();
            } else {
                // 이용중인 거래소 리스트
                List<UserExchngListDto> userExchngList = userService.getUserExchngList(email);

                // 리스트 별 보유 종목 Api 호출
                for(int i = 0 ; i < userExchngList.size() ; i++) {
                    userExchngList.get(i).setAccountsListFormDtoList(exchngApiRequest(userExchngList.get(i).getExchngCd()));
                }
                model.addAttribute("userExchngList", userExchngList);


                // 당일 매매 일지
                List<TradeHistDto> tradeHistDto = new ArrayList<>();
                tradeHistDto = tradeHistService.getTradeHistToday(email);

                if(tradeHistDto != null && tradeHistDto.size() > 0) {
                    model.addAttribute("tradeHist", tradeHistDto);
                }
            }
        } catch (Exception e) {
            log.error("listController exception " + e.getMessage());
        }
        return "list";
    }
    @GetMapping(value="/accounts-info")
    public void upbitAccountList(@SessionAttribute(name = "session_key", required = false) String session_key
                                   , HttpServletResponse response) throws IOException, InterruptedException {

        HttpHeaders headers = new HttpHeaders();
        if("".equals(session_key) || session_key == null) {
            response.sendRedirect("/api/v1/get-api/login");

        } else {
            //upbit
            List<UpbitAccountDto> upbitAccountDtoList = upbit_accounts_info();
            List<AccountsListFormDto> UpbitAccountsListForm = upbitDtoProcessor(upbitAccountDtoList);

            Writer writer = response.getWriter();
            for(AccountsListFormDto alFDto : UpbitAccountsListForm) {
                writer.write(alFDto.toString());
                writer.write("\n");
                writer.flush();
                Thread.sleep(2000);
            }
            writer.close();
        }
    }
}
