package com.aladin.springbootstudy.common;

import com.aladin.springbootstudy.dto.AccountsListFormDto;
import com.aladin.springbootstudy.dto.BinanceAccountsDto;
import com.aladin.springbootstudy.dto.OAuthToken;
import com.aladin.springbootstudy.dto.UpbitAccountDto;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.binary.Hex;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.util.*;

public class CommonFunction implements CommonUtils{
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

    public static ResponseEntity<String> httpRequest(Map<String, String> headerMap, Map<String
                                , String> params, String url, HttpMethod type) {

        try {
            //POST 방식으로 key=value 데이터를 요청(카카오 쪽으로)
            RestTemplate rt = new RestTemplate(); // http 요청 라이브러리

            if(headerMap == null) {
                throw new Exception("헤더정보 누락");
            }

            //POST요청 날릴 데이터가 key-value 형태임을 알리는 HttpHeader 선언
            HttpHeaders headers = new HttpHeaders();
            Iterator<Map.Entry<String, String>> iter = headerMap.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, String> entry = iter.next();
                headers.add(entry.getKey(), entry.getValue());
            }

            MultiValueMap<String, String> mvm = new LinkedMultiValueMap<>();
            Iterator<Map.Entry<String, String>> iter2 = params.entrySet().iterator();
            while(iter2.hasNext()) {
                Map.Entry<String, String> entry = iter2.next();
                mvm.add(entry.getKey(), entry.getValue());
            }

            // HttpHeader와 HttpBody를 하나의 오브젝트에 담기
            // >> exchange()가 HttpEntity object를 매개변수로 받기 때문이다.
            HttpEntity<MultiValueMap<String, String>> httpEntity =
                    new HttpEntity<>(mvm, headers);

            // Http 요청하기 - Post 방식으로 그리고 response 변수의 응답을 받는다.
            // 제네릭 String 선언 -> 응답 데이터를 String 클래스로 받겠다.
            ResponseEntity<String> response = rt.exchange(
                    url
                    , type // Type
                    , httpEntity   // 토큰 요청 데이터
                    , String.class    // 응답받을 클래스타입입
            );
            return response;

        } catch (Exception e) {
            System.out.println("http request exception > " + e.getMessage());
            return null;
        }
    }

    public static OAuthToken getOAuthToken(ResponseEntity<String> response) {
        // ObjectMapper > json을 object로 변환 라이브러리
        // 파싱 시 반드시 멤버변수 변수명과 응답 json의 키값이 일치해야 정상적으로 매핑된다.
        ObjectMapper objectMapper = new ObjectMapper();
        OAuthToken oAuthToken = null;

        try {
            oAuthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
            return oAuthToken;
        } catch(Exception e) {
            System.out.println("oauthToken exception >>  " + e.getMessage());
            return null;
        }
    }

    public static ResponseEntity<String> getResponseEntity(HttpEntity httpEntity, String url) {
        //POST 방식으로 key=value 데이터를 요청(카카오 쪽으로)
        RestTemplate rt = new RestTemplate(); // http 요청 라이브러리

        ResponseEntity<String> response = rt.exchange(
                url
                , HttpMethod.POST // Type
                , httpEntity   // 토큰 요청 데이터
                , String.class    // 응답받을 클래스타입입
        );
        return response;
    }

    public BinanceAccountsDto binance_accounts_info() {


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
            //StringBuilder sb2 = new StringBuilder("<<<<<<<<<바이낸스 계좌 정보>>>>>>>>>>").append("<br>");

//            for (BinanceAccountsDto.Position position : positionList) {
//                if (!"0".equals(position.getInitialMargin())) {
//
//                    String uP = position.getUnrealizedProfit();
//                    double d = Double.parseDouble(uP) * 1300;
//                    String sf = String.format("%.0f", d);
//                    String ac = addComma(sf);
//
//                    sb2.append("보유 코인 : ");
//                    sb2.append(position.getSymbol()).append("(" + position.getLeverage() + "x)").append("<br>");
//                    sb2.append("평단가 : ").append(position.getEntryPrice()).append("<br>");
//                    sb2.append("USDT : ").append(roundUp(position.getInitialMargin())).append("<br>");
//                    sb2.append("손익 : ").append(roundUp(position.getUnrealizedProfit()))
//                            .append("usdt / ")
//                            .append(ac).append("won")
//                            .append("<br>");
//                    sb2.append("SIZE : ").append(roundUp(position.getNotional())).append("<br>");
//
//                    double pee = Double.parseDouble(position.getNotional());
//                    pee *= 0.0008;
//
//                    sb2.append("수수료 : ").append(String.format("%.2f", pee)).append("usdt").append("<br>");
//
//                    sb2.append("<br>");
//                    sb2.append("<br>");
//                }
//
//            }
            return binanceAccountsDto;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }

    }

    public List<UpbitAccountDto> upbit_accounts_info() {

        try {
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
            List<UpbitAccountDto> listUpbitAccountDto =
                    objectMapper.readValue(responseEntity.getBody(), new TypeReference<List<UpbitAccountDto>>() {} );
            return listUpbitAccountDto;

        } catch(Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
//        try {
//            listUpbitAccountDto = objectMapper.readValue(responseEntity.getBody(), new TypeReference<List<UpbitAccountDto>>() {} );
//
//            StringBuilder sb = null;
//            if(listUpbitAccountDto != null) {
//                sb = new StringBuilder("<<<<<<<<<<<< 업비트 자산 리스트 >>>>>>>>>>>" + "<br/>");
//            }
//
//            for(UpbitAccountDto upbitAccountDto : listUpbitAccountDto) {
//                if("KRW".equals(upbitAccountDto.getCurrency())) {
//                    sb.append("원화 : " + roundUp(upbitAccountDto.getBalance()) + "<br/>");
//                } else {
//                    Map<String, String> tickerHeader = new LinkedHashMap<>();
//                    tickerHeader.put("accept", "application/json");
//
//                    //markets=KRW-GRS"
//                    String url = upbit_get_ticker_url + "?markets=KRW-" + upbitAccountDto.getCurrency();
//                    String bal = upbitAccountDto.getBalance();
//                    String lock = upbitAccountDto.getLocked();
//                    Double coinCount = Double.parseDouble(bal) + Double.parseDouble(lock);
//
//                    //trade_price(종가:현재가)
//                    ResponseEntity<String> tickerEntity = httpRequest(
//                            tickerHeader
//                            , new HashMap<String, String>()
//                            , url
//                            , HttpMethod.GET);
//                    JSONArray jsonArray = new JSONArray(tickerEntity.getBody());
//                    JSONObject jsonObject = jsonArray.getJSONObject(0);
//
//                    Number trade_price = (Number) jsonObject.get("trade_price");
//                    String totAsset = roundUp(BigDecimal.valueOf(coinCount * trade_price.doubleValue()));
//
//                    sb.append(upbitAccountDto.getCurrency() + " : " + totAsset + "<br/>");
//                }
//            }
//            return listUpbitAccountDto;
//        }
//
//        catch (Exception e) {
//            System.out.println("" + e.getMessage());
//            return null;
//        }
    }

    public List<AccountsListFormDto> upbitDtoProcessor(List<UpbitAccountDto> upbitAccountsList) {

        try {
            List<AccountsListFormDto> accountsList = new ArrayList<>();

            for(UpbitAccountDto upbitAccountDto : upbitAccountsList) {
                AccountsListFormDto aLFDto = new AccountsListFormDto();
                if("KRW".equals(upbitAccountDto.getCurrency())) {
                    //sb.append("원화 : " + roundUp(upbitAccountDto.getBalance()) + "<br/>");
                    aLFDto.setTokenName("KRW");
                    aLFDto.setExchngCd("01");
                    aLFDto.setNowAmt(new BigDecimal(roundUp(upbitAccountDto.getBalance())));
                    aLFDto.setCoinAmount(0.0);

                    accountsList.add(aLFDto);

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

                    aLFDto.setTokenName(upbitAccountDto.getCurrency());
                    aLFDto.setExchngCd("01");
                    aLFDto.setNowAmt(new BigDecimal(totAsset));
                    aLFDto.setCoinAmount(coinCount);

                    accountsList.add(aLFDto);

                    //sb.append(upbitAccountDto.getCurrency() + " : " + totAsset + "<br/>");
                }
            }
            return accountsList;
        } catch (Exception e) {
            System.out.println("upbit dto processor exception " + e.getMessage());
            return null;
        }
    }

    protected List<AccountsListFormDto> exchngApiRequest(String exchngCd) {
        switch(exchngCd) {
            case "01" : //upbit
                return upbitDtoProcessor(upbit_accounts_info());
            case "02" : //binance
                break;
            default:
                break;
        }
        return null;
    }
}
