package com.aladin.springbootstudy.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginController {
    private Logger logger = Logger.getLogger(BoardController.class);

    @Value("#{oauth.client_id}")
    String client_id;
    @Value("#{oauth.redirect_uri}")
    String redirect_uri;

    @GetMapping(value="/kakao")
    public Map<String, String> getKakaoConfig() {
        Map<String, String> kakaoConfig = new HashMap<>();
        kakaoConfig.put("client_id", client_id);
        kakaoConfig.put("redirect_uri", redirect_uri);

        // 클라이언트 ID와 콜백 URL을 포함하는 Map을 반환합니다.
        return kakaoConfig;
    }
}
