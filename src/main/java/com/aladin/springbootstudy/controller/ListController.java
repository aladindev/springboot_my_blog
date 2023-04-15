package com.aladin.springbootstudy.controller;

import com.aladin.springbootstudy.common.CommonFunction;
import com.aladin.springbootstudy.dto.AccountsListFormDto;
import com.aladin.springbootstudy.dto.UpbitAccountDto;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@RequestMapping("/api/v1") // infix = 공통 URL
public class ListController extends CommonFunction{

    @GetMapping(value="/accounts")
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
                writer.flush();
                Thread.sleep(2000);
            }
            writer.close();
        }
    }
}
