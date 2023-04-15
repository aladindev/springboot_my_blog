package com.aladin.springbootstudy.controller;

import com.aladin.springbootstudy.common.CommonFunction;
import com.aladin.springbootstudy.common.CommonUtils;
import com.aladin.springbootstudy.dto.BinanceAccountsDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.binary.Hex;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1") // infix = 공통 URL
public class ListController {



    @GetMapping(value="/accounts")
    public void upbitAccountList(@SessionAttribute(name = "session_key", required = false) String session_key
                                   , HttpServletResponse response) throws IOException, InterruptedException, NoSuchAlgorithmException, InvalidKeyException, ParseException {

        HttpHeaders headers = new HttpHeaders();
//        if("".equals(session_key) || session_key == null) {
//            response.sendRedirect("/api/v1/get-api/login");
//            //return null;
//        } else {
//            StringBuilder sb = new StringBuilder(binance_accounts_info());
//            Writer writer = response.getWriter();
//            for (int i = 0; i < 4; i++) {
//                writer.write(binance_accounts_info() + "\n\n");
//                writer.flush(); // 꼭 flush 해주어야 한다.
//                Thread.sleep(2000);
//            }
//            writer.close();
//        }



//        sb.append("<br><br>");
//        sb.append(upbitGetAccount());
//        return sb.toString();


    }




}
