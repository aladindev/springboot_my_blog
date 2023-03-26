package com.aladin.springbootstudy.controller;

import com.aladin.springbootstudy.common.CommonCode;
import com.aladin.springbootstudy.dto.UpbitAccountDto;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import static com.aladin.springbootstudy.common.CommonFunction.*;

@RestController
@RequestMapping("/list") // infix = 공통 URL
public class ListController implements CommonCode {

    @Value("#{crypto.upbit_a_key}")
    String upbit_a_key;

    @Value("#{crypto.upbit_s_key}")
    String upbit_s_key;

    @Value("#{crypto.upbit_server_url}")
    String upbit_server_url;

    @GetMapping(value="/upbit-account")
    @ResponseBody
    public String upbitAccountList() {

        return upbitGetAccount();
    }

    public String upbitGetAccount() {

        String aKey = upbit_a_key;
        String sKey = upbit_s_key;
        String serverUrl = upbit_server_url;

        Algorithm algorithm = Algorithm.HMAC256(sKey);
        String jwtToken = JWT.create()
                .withClaim("access_key", aKey)
                .withClaim("nonce", UUID.randomUUID().toString())
                .sign(algorithm);

        String authenticationToken = "Bearer " + jwtToken;

        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(serverUrl + "/v1/accounts");
            request.setHeader("Content-Type", "application/json");
            request.addHeader("Authorization", authenticationToken);

            HttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();

            String result = EntityUtils.toString(entity, "UTF-8");
            System.out.println("result >>  " + result);
            return result;

        } catch (IOException e) {
            e.printStackTrace();
            return "잘못된 요청입니다.";
        }
    }
}
