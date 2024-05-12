package com.aladin.springbootstudy.controller;

import com.aladin.springbootstudy.dto.USER_INFO_DTO;
import com.aladin.springbootstudy.mapper.UserMapper;
import com.aladin.springbootstudy.service.encrypt.EncryptService;
import com.aladin.springbootstudy.service.oauth.OAuthKakaoService;
import com.aladin.springbootstudy.service.user.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/oauth")
public class OAuthController {
    private Logger logger = Logger.getLogger(BoardController.class);

    @Autowired
    OAuthKakaoService OAuthKakaoService;

    @Autowired
    EncryptService encryptService;

    @Autowired
    UserService userService;

    @Value("#{oauth.client_id}")
    String client_id;

    @GetMapping(value="/kakao/callback")
    public Map<String, String> kakaoCallback(@RequestParam("code") String code) throws Exception {

        String kakaoAccessToken = OAuthKakaoService.getAccessTokenFromKakao(client_id, code);

        HashMap<String, Object> userInfoMap = new HashMap<>();
        userInfoMap = OAuthKakaoService.getUserInfo(kakaoAccessToken);

        String email = userInfoMap.get("email") != null ? userInfoMap.get("email").toString() : "";

        String encryptResult = encryptService.aesCBCEncode(email);

        USER_INFO_DTO userInfoDto = new USER_INFO_DTO();
        userInfoDto.se
        userService.getUserInfo()

        return null;
    }
}
