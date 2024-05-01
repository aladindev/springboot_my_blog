package com.aladin.springbootstudy.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginController {
    private Logger logger = Logger.getLogger(BoardController.class);

    @Value("#{oauth.client_id}")
    String client_id;
    @Value("#{oauth.redirect_url}")
    String redirect_url;

    @GetMapping(value="/kakao/get-url")
    public void redirectToKakao(HttpServletResponse response) {
        String authUrl = "https://kauth.kakao.com/oauth/authorize" +
                "?response_type=code" +
                "&client_id=" + client_id +
                "&redirect_uri=" + redirect_url;

        response.setHeader("Location", authUrl);
        response.setStatus(302);
    }


}
