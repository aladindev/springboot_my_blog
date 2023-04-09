package com.aladin.springbootstudy.controller;

import com.aladin.springbootstudy.common.CommonCode;
import com.aladin.springbootstudy.common.EncryptModule;
import com.aladin.springbootstudy.common.SessionManager;
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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

import static com.aladin.springbootstudy.common.CommonFunction.getOAuthToken;
import static com.aladin.springbootstudy.common.CommonFunction.httpRequest;

@RestController
@RequestMapping("/auth/kakao") // infix = 공통 URL
public class LoginController implements CommonCode {

    @Autowired
    UserInfoService userInfoService;
    @Autowired
    EncryptModule encryptModule;
    @Autowired
    SessionManager sessionManager;

    @Value("#{kakao.client_id}")
    private String client_id;
    @Value("#{encrypt.session_key}")
    private String session_key;

    @GetMapping(value="/callback")
    public ModelAndView kakaoCallback(@RequestParam(name="code") String code, HttpServletRequest request, HttpServletResponse response) {
        //@ResponseBody : Data를 리턴해주는 컨트롤러 함수

        // 로그인 세션
        HttpSession session = request.getSession();

        ModelAndView mv = null;
        try {

            Map<String, String> oauthHeaders = new LinkedHashMap<>();
            oauthHeaders.put("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

            Map<String, String> params = new LinkedHashMap<>();
            params.put("grant_type", "authorization_code");
            params.put("client_id", client_id);
            params.put("redirect_uri", "http://localhost:8080/auth/kakao/callback");
            params.put("code", code);

            ResponseEntity<String> oAuthResponse =  httpRequest(oauthHeaders, params, OAUTH_TOKEN_URL, HttpMethod.POST);

            // ObjectMapper > json을 object로 변환 라이브러리
            // 파싱 시 반드시 멤버변수의 변수명과 응답 json의 key값이 일치해야 한다!!
            OAuthToken oAuthToken = getOAuthToken(oAuthResponse);

            Map<String, String> getUserInfoHeaders = new LinkedHashMap<>();
            getUserInfoHeaders.put("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
            getUserInfoHeaders.put("Authorization", "Bearer " + oAuthToken.getAccess_token());

            ResponseEntity<String> userInfoResponse = httpRequest(getUserInfoHeaders, params, GET_USER_INFO_URL, HttpMethod.POST);

            ObjectMapper objectMapper = new ObjectMapper();
            KakaoProfileDto kakaoProfileDto = null;
            try {

                // entity builder pattern
                kakaoProfileDto = objectMapper.readValue(userInfoResponse.getBody(), KakaoProfileDto.class);
                System.out.println("kakaoProfile dto > > " + kakaoProfileDto);

                KakaoProfileEntity entity = new KakaoProfileEntity.KakaoBuilder(kakaoProfileDto).build();

                entity = userInfoService.getOne(kakaoProfileDto.getId());

                if(entity != null) {
                    String email = kakaoProfileDto.getKakao_account().getEmail();
                    /* 세션 등록 */
                    session.setAttribute("session_key", UUID.randomUUID().toString());

                    System.out.println("encryptModule.encrypt(email) >> " + encryptModule.encrypt(email));
                    if(encryptModule.encrypt(email).equals(entity.getEmail())) {
                        System.out.println("인증성공 list redirect");
                        RedirectView rv = new RedirectView("/api/v1/accounts");
                        return new ModelAndView(rv);
                    }

                }

            } catch (Exception e) {
                System.out.println("kakaoProfile exception > " + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new ModelAndView("error");
    }



}
