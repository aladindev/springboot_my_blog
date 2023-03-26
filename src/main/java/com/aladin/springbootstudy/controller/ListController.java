package com.aladin.springbootstudy.controller;

import com.aladin.springbootstudy.common.CommonCode;
import com.aladin.springbootstudy.dto.UpbitAccountDto;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.*;

import static com.aladin.springbootstudy.common.CommonFunction.httpRequest;

@RestController
@RequestMapping("/api/v1") // infix = 공통 URL
public class ListController implements CommonCode {

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

    @GetMapping(value="/accounts")
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

        Map<String, String> accountsHeader = new LinkedHashMap<>();
        accountsHeader.put("Content-Type", "application/json");
        accountsHeader.put("Authorization", authenticationToken);

        ResponseEntity<String> responseEntity = httpRequest(accountsHeader, new HashMap<String, String>()
                                        , upbit_get_accounts_url, HttpMethod.GET);

        ObjectMapper objectMapper = new ObjectMapper();
        List<UpbitAccountDto> listUpbitAccountDto = new ArrayList<>();
        try {
            listUpbitAccountDto = objectMapper.readValue(responseEntity.getBody(), new TypeReference<List<UpbitAccountDto>>() {} );

            StringBuilder sb = null;
            if(listUpbitAccountDto != null) {
                sb = new StringBuilder("<<<<<<<<<<<< 업비트 자산 리스트 >>>>>>>>>>>" + "<br/>");
            }

            for(UpbitAccountDto upbitAccountDto : listUpbitAccountDto) {
                if("KRW".equals(upbitAccountDto.getCurrency())) {
                    sb.append("원화 : " + roundUp(upbitAccountDto.getBalance()) + "<br/>");
                } else {
                    Map<String, String> tickerHeader = new LinkedHashMap<>();
                    tickerHeader.put("accept", "application/json");

                    //markets=KRW-GRS"
                    String url = upbit_get_ticker_url + "?markets=KRW-" + upbitAccountDto.getCurrency();
                    String bal = upbitAccountDto.getBalance();
                    String lock = upbitAccountDto.getLocked();
                    Double coinCount = Double.parseDouble(bal) + Double.parseDouble(lock);
                    System.out.println("coinCount > " + coinCount);

                    //trade_price(종가:현재가)
                    ResponseEntity<String> tickerEntity = httpRequest(
                            tickerHeader
                            , new HashMap<String, String>()
                            , url
                            , HttpMethod.GET);
                    JSONArray jsonArray = new JSONArray(tickerEntity.getBody());
                    JSONObject jsonObject = jsonArray.getJSONObject(0);

                    Number trade_price = (Number) jsonObject.get("trade_price");

                    BigDecimal totAsset = BigDecimal.valueOf(coinCount * trade_price.doubleValue());
                    totAsset = roundUp(totAsset);
                    System.out.println("totAsset >> " + totAsset);
                    sb.append(upbitAccountDto.getCurrency() + " : " + totAsset + "<br/>");
                }
            }
            return sb.toString();
        } catch (Exception e) {
            System.out.println("" + e.getMessage());
            return "비정상 요청";
        }
    }

    public BigDecimal roundUp(String asset) {
        return new BigDecimal(asset).setScale(0, BigDecimal.ROUND_UP);
    }
    public BigDecimal roundUp(Double asset) {
        return new BigDecimal(asset).setScale(0, BigDecimal.ROUND_UP);
    }
    public BigDecimal roundUp(BigDecimal asset) {
        return asset.setScale(0, BigDecimal.ROUND_UP);
    }
}
