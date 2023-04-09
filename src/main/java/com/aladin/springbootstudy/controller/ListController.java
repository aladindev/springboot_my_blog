package com.aladin.springbootstudy.controller;

import com.aladin.springbootstudy.common.CommonCode;
import com.aladin.springbootstudy.common.CommonFunction;
import com.aladin.springbootstudy.dto.BinanceAccountsDto;
import com.aladin.springbootstudy.dto.UpbitAccountDto;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.binary.Hex;
import org.apache.tomcat.util.json.ParseException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
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
import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
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

    @Value("#{crypto.binance_a_key}")
    String binance_a_key;

    @Value("#{crypto.binance_s_key}")
    String binance_s_key;

    @GetMapping(value="/accounts")
    public String upbitAccountList(@SessionAttribute(name = "session_key", required = false) String session_key
                                   , HttpServletResponse response) throws IOException, InterruptedException, NoSuchAlgorithmException, InvalidKeyException, ParseException {

        HttpHeaders headers = new HttpHeaders();
        if("".equals(session_key) || session_key == null) {
            response.sendRedirect("/api/v1/get-api/login");
            return null;
        }

        StringBuilder sb = new StringBuilder(binance_accounts_info());
        sb.append("<br><br>");
        sb.append(upbitGetAccount());

        return sb.toString();
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

                    //trade_price(종가:현재가)
                    ResponseEntity<String> tickerEntity = httpRequest(
                            tickerHeader
                            , new HashMap<String, String>()
                            , url
                            , HttpMethod.GET);
                    JSONArray jsonArray = new JSONArray(tickerEntity.getBody());
                    JSONObject jsonObject = jsonArray.getJSONObject(0);

                    Number trade_price = (Number) jsonObject.get("trade_price");
                    String totAsset = roundUp(BigDecimal.valueOf(coinCount * trade_price.doubleValue()));

                    sb.append(upbitAccountDto.getCurrency() + " : " + totAsset + "<br/>");
                }
            }
            return sb.toString();
        } catch (Exception e) {
            System.out.println("" + e.getMessage());
            return "비정상 요청";
        }
    }

    public String binance_accounts_info() {


        try {

            Map<String, String> header = new HashMap<>();
            header.put("Content-Type", "application/json");
            header.put("X-MBX-APIKEY", binance_a_key);

            Map<String, String> params = new HashMap<>();

            String timestamp = Long.toString(System.currentTimeMillis());
            String queryString = "timestamp=" + timestamp;

            Mac hmacSha256 = Mac.getInstance("HmacSHA256");
            SecretKeySpec secKey = new SecretKeySpec(binance_s_key.getBytes(), "HmacSHA256");
            hmacSha256.init(secKey);

            String actualSign = new String(Hex.encodeHex(hmacSha256.doFinal(queryString.getBytes())));
            queryString += "&signature=" + actualSign;

            String serverUrl = "https://fapi.binance.com";
            StringBuilder sb = new StringBuilder(serverUrl);
            sb.append("/fapi/v2/account?");
            sb.append(queryString);
            serverUrl = sb.toString();

            ResponseEntity<String> binanceResponseEntity = CommonFunction.httpRequest(header, params, serverUrl, HttpMethod.GET);

            ObjectMapper objectMapper = new ObjectMapper();
            BinanceAccountsDto binanceAccountsDto = null;
            binanceAccountsDto = objectMapper.readValue(binanceResponseEntity.getBody(), BinanceAccountsDto.class);

            List<BinanceAccountsDto.Position> positionList = binanceAccountsDto.getPositions();
            StringBuilder sb2 = new StringBuilder("<<<<<<<<<바이낸스 계좌 정보>>>>>>>>>>").append("<br>");

            for (BinanceAccountsDto.Position position : positionList) {
                if (!"0".equals(position.getInitialMargin())) {

                    String uP = position.getUnrealizedProfit();
                    double d = Double.parseDouble(uP) * 1300;
                    String sf = String.format("%.0f", d);
                    String ac = addComma(sf);

                    sb2.append("보유 코인 : ");
                    sb2.append(position.getSymbol()).append("(" + position.getLeverage() + "x)").append("<br>");
                    sb2.append("평단가 : ").append(position.getEntryPrice()).append("<br>");
                    sb2.append("USDT : ").append(roundUp(position.getInitialMargin())).append("<br>");
                    sb2.append("손익 : ").append(roundUp(position.getUnrealizedProfit()))
                            .append("usdt / ")
                            .append(ac).append("won")
                            .append("<br>");
                    sb2.append("SIZE : ").append(roundUp(position.getNotional())).append("<br>");

                    double pee = Double.parseDouble(position.getNotional());
                    pee *= 0.0008;

                    sb2.append("수수료 : ").append(String.format("%.2f", pee)).append("usdt").append("<br>");

                    sb2.append("<br>");
                    sb2.append("<br>");
                }

            }
            return sb2.toString();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "error";
        }

    }

    public String roundUp(String asset) {
        return new BigDecimal(asset).setScale(3, BigDecimal.ROUND_UP).toString();
    }
    public String roundUp(Double asset) {
        return new BigDecimal(asset).setScale(3, BigDecimal.ROUND_UP).toString();
    }
    public String roundUp(BigDecimal asset) {
        return asset.setScale(3, BigDecimal.ROUND_UP).toString();
    }

    public String addComma(String asset) {

        boolean minus = false;
        if(asset.indexOf("-") > -1) {
            minus = true;
            asset = asset.substring(1);
        }

        StringBuilder sb = new StringBuilder(asset).reverse();
        String str = sb.toString();
        sb.setLength(0);
        for(int i = 0 ; i < str.length() ; i++) {
            if(i == 0) {
                sb.append(str.charAt(i));
            } else {
                if(i%3==0) sb.append("," + str.charAt(i));
                else sb.append(str.charAt(i));
            }
        }
        return minus ? "-" + sb.reverse().toString() : sb.reverse().toString();
    }
}
