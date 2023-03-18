package com.aladin.springbootstudy.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/auth/kakao") // infix = 공통 URL
public class LoginController {
    ////http://localhost:8080/auth/kakao/callback?code=y2t9ayG3Idre8Zpfei_i07S0yn2D4jAWGkA82VtWq8R1KvB0eqE28nsjpGt0DZ5Wznw0mwo9dZsAAAGG82rjuA

    @GetMapping(value="/callback")
    @ResponseBody
    public String kakaoCallback(@RequestParam(name="code") String code) {
        //@ResponseBody : Data를 리턴해주는 컨트롤러 함수

        //POST 방식으로 key=value 데이터를 요청(카카오 쪽으로)
        RestTemplate rt = new RestTemplate(); // http 요청 라이브러리
        HttpHeaders headers = new HttpHeaders();
        //POST요청 날릴 데이터가 key-value 형태임을 알리는 헤더 선언
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");


        return "카카오 로그인 완료 req param return code : " + code;
    }
}
