package com.aladin.springbootstudy.controller;

import com.aladin.springbootstudy.common.EncryptModule;
import com.aladin.springbootstudy.dto.KakaoProfileDto;
import com.aladin.springbootstudy.dto.OAuthToken;
import com.aladin.springbootstudy.entity.KakaoProfileEntity;
import com.aladin.springbootstudy.service.UserInfoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth/kakao") // infix = 공통 URL
public class LoginController {

    @Autowired
    UserInfoService userInfoService;
    @Autowired
    EncryptModule encryptModule;

    @Value("#{kakao.client_id}")
    private String client_id;

    @GetMapping(value="/callback")
    @ResponseBody
    public String kakaoCallback(@RequestParam(name="code") String code) {
        //@ResponseBody : Data를 리턴해주는 컨트롤러 함수

        try {

            Map<String, String> headers = new LinkedHashMap<>();
            headers.put("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
            Map<String, String> params = new LinkedHashMap<>();
            params.put("grant_type", "authorization_code");
            params.put("client_id", client_id);
            params.put("redirect_uri", "http://localhost:8080/auth/kakao/callback");
            params.put("code", code);

            // httpRequest(headers, params,);

            // ObjectMapper > json을 object로 변환 라이브러리
            // 파싱 시 반드시 멤버변수의 변수명과 응답 json의 key값이 일치해야 한다!!
            ObjectMapper objectMapper = new ObjectMapper();
            OAuthToken oauthToken = null;
            try {
                oauthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
            } catch (Exception e) {
                System.out.println("oauthTokent exception > " + e.getMessage());
            }

            // 로직 분리는 나중에..

            {
                //POST 방식으로 key=value 데이터를 요청(카카오 쪽으로)
                RestTemplate rt2 = new RestTemplate(); // http 요청 라이브러리

                //POST요청 날릴 데이터가 key-value 형태임을 알리는 HttpHeader 선언
                HttpHeaders headers2 = new HttpHeaders();
                headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
                headers.add("Authorization", "Bearer " + oauthToken.getAccess_token());

                // HttpHeader와 HttpBody를 하나의 오브젝트에 담기
                // >> exchange()가 HttpEntity object를 매개변수로 받기 때문이다.
                HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest =
                        new HttpEntity<>(params, headers);

                // Http 요청하기 - Post 방식으로 그리고 response 변수의 응답을 받는다.
                // 제네릭 String 선언 -> 응답 데이터를 String 클래스로 받겠다.
                ResponseEntity<String> response2 = rt2.exchange(
                        "https://kapi.kakao.com/v2/user/me"
                        , HttpMethod.POST // Type
                        , kakaoProfileRequest  // profile 요청 데이터
                        , String.class    // 응답받을 클래스타입입
                );

                ObjectMapper objectMapper2 = new ObjectMapper();
                KakaoProfileDto kakaoProfileDto = null;
                try {

                    // entity builder pattern
                    kakaoProfileDto = objectMapper.readValue(response2.getBody(), KakaoProfileDto.class);
                    System.out.println("kakaoProfile dto > > " + kakaoProfileDto);

                    KakaoProfileEntity entity = new KakaoProfileEntity.KakaoBuilder(kakaoProfileDto).build();

                    KakaoProfileEntity entity2 = null;
                    entity2 = userInfoService.getOne(kakaoProfileDto.getId());

                    if(entity2 != null) {
                        String email = kakaoProfileDto.getKakao_account().getEmail();
                        if(encryptModule.encrypt(email).equals(entity2.getEmail())) {
                            return "카카오 로그인 완료 req param return code : " + code;
                        }

                    }

                } catch (Exception e) {
                    System.out.println("kakaoProfile exception > " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return "등록되지 않은 사용자 입니다.";
    }


    public void httpRequest(Map<String, String> headerMap, Map<String, String> params, String url) {

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

            if(params == null) {
                throw new Exception("파라미터정보 누락");
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
                    , HttpMethod.POST // Type
                    , httpEntity   // 토큰 요청 데이터
                    , String.class    // 응답받을 클래스타입입
            );

            // ObjectMapper > json을 object로 변환 라이브러리
            // 파싱 시 반드시 멤버변수의 변수명과 응답 json의 key값이 일치해야 한다!!
            ObjectMapper objectMapper = new ObjectMapper();
            OAuthToken oauthToken = null;
            try {
                oauthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
            } catch (Exception e) {
                System.out.println("oauthTokent exception > " + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("http request exception > " + e.getMessage());
        }
    }
}
