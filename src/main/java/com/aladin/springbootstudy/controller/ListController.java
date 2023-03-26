package com.aladin.springbootstudy.controller;

import com.aladin.springbootstudy.common.CommonCode;
import com.aladin.springbootstudy.dto.KakaoProfileDto;
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
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;

import static com.aladin.springbootstudy.common.CommonFunction.*;

@RestController
@RequestMapping("/list") // infix = 공통 URL
public class ListController implements CommonCode {

    @Value("#{crypto.upbit_a_key}")
    String upbit_a_key;

    @Value("#{crypto.upbit_s_key}")
    String upbit_s_key;

    @Value("#{crypto.upbit_server_domain}")
    String upbit_server_domain;

    @Value("#{crypto.upbit_get_accounts_url}")
    String upbit_get_accounts_url;

    @GetMapping(value="/upbit-account")
    @ResponseBody
    public String upbitAccountList() {

        return upbitGetAccount();
    }

    public String upbitGetAccount() {

        Algorithm algorithm = Algorithm.HMAC256(upbit_s_key);
        String jwtToken = JWT.create()
                .withClaim("access_key", upbit_a_key)
                .withClaim("nonce", UUID.randomUUID().toString())
                .sign(algorithm);

        String authenticationToken = "Bearer " + jwtToken;

        //            HttpClient client = HttpClientBuilder.create().build();
//            HttpGet request = new HttpGet(upbit_get_accounts_url);
//            request.setHeader("Content-Type", "application/json");
//            request.addHeader("Authorization", authenticationToken);
//
//            HttpResponse response = client.execute(request);
//            HttpEntity entity = response.getEntity();
//
//            String result = EntityUtils.toString(entity, "UTF-8");
//
//            System.out.println("result >>  " + result);
//            return result;
        Map<String, String> accountsHeader = new LinkedHashMap<>();
        accountsHeader.put("Content-Type", "application/json");
        accountsHeader.put("Authorization", authenticationToken);

        ResponseEntity<String> responseEntity = httpRequest(accountsHeader, new HashMap<String, String>()
                                        , upbit_get_accounts_url, HttpMethod.GET);

        System.out.println("우선 response entity > " + responseEntity.getBody().toString());

        ObjectMapper objectMapper = new ObjectMapper();
        List<UpbitAccountDto> listUpbitAccountDto = new ArrayList<>();
        try {
            listUpbitAccountDto = objectMapper.readValue(responseEntity.getBody(), new TypeReference<List<UpbitAccountDto>>() {} );
        } catch (Exception e) {
            System.out.println("" + e.getMessage());
        }
        return "성공";

    }
}
