package com.aladin.springbootstudy.controller;

import com.aladin.springbootstudy.dto.KakaoProfileDto;
import com.aladin.springbootstudy.dto.OAuthToken;
import com.aladin.springbootstudy.entity.KakaoProfileEntity;
import com.aladin.springbootstudy.service.UserInfoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/auth/kakao") // infix = 공통 URL
public class LoginController {

    @Autowired
    UserInfoService userInfoService;

    @Value("#{kakao.client_id}")
    private String client_id;

    @GetMapping(value="/callback")
    @ResponseBody
    public String kakaoCallback(@RequestParam(name="code") String code) {
        //@ResponseBody : Data를 리턴해주는 컨트롤러 함수

        try {

            //POST 방식으로 key=value 데이터를 요청(카카오 쪽으로)
            RestTemplate rt = new RestTemplate(); // http 요청 라이브러리

            //POST요청 날릴 데이터가 key-value 형태임을 알리는 HttpHeader 선언
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "authorization_code");
            params.add("client_id", client_id);
            params.add("redirect_uri", "http://localhost:8080/auth/kakao/callback");
            params.add("code", code);

            // HttpHeader와 HttpBody를 하나의 오브젝트에 담기
            // >> exchange()가 HttpEntity object를 매개변수로 받기 때문이다.
            HttpEntity<MultiValueMap<String, String>> kakaoTokenReq =
                    new HttpEntity<>(params, headers);

            // Http 요청하기 - Post 방식으로 그리고 response 변수의 응답을 받는다.
            // 제네릭 String 선언 -> 응답 데이터를 String 클래스로 받겠다.
            ResponseEntity<String> response = rt.exchange(
                    "https://kauth.kakao.com/oauth/token"
                    , HttpMethod.POST // Type
                    , kakaoTokenReq   // 토큰 요청 데이터
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
                    kakaoProfileDto = objectMapper.readValue(response2.getBody(), KakaoProfileDto.class);
                    System.out.println("kakaoProfile dto > > " + kakaoProfileDto);

                    KakaoProfileEntity entity = new KakaoProfileEntity.KakaoBuilder(kakaoProfileDto).build();
                    System.out.println("build entity >>  " + entity);

                } catch (Exception e) {
                    System.out.println("kakaoProfile exception > " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return "카카오 로그인 완료 req param return code : " + code;
    }
}
