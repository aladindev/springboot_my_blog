package com.aladin.springbootstudy.controller;

import com.aladin.springbootstudy.service.KakaoService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/oauth")
public class OAuthController {
    private Logger logger = Logger.getLogger(BoardController.class);

    @Autowired
    KakaoService kakaoService;

    @Value("#{oauth.client_id}")
    String client_id;
    @Value("#{oauth.redirect_uri}")
    String redirect_uri;

    @GetMapping(value="/kakao/callback")
    public Map<String, String> kakaoCallback(@RequestParam("code") String code) throws IOException {

        String kakaoAccessToken = kakaoService.getAccessTokenFromKakao(client_id, code);
        logger.info("kakaoAccessToken > " + kakaoAccessToken);
        return null;
    }
}
