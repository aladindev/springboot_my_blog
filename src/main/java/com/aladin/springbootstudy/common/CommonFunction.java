package com.aladin.springbootstudy.common;

import com.aladin.springbootstudy.dto.OAuthToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Iterator;
import java.util.Map;

public class CommonFunction {
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
}
