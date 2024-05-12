package com.aladin.springbootstudy.controller;

import com.aladin.springbootstudy.dto.USER_AUTH_DTO;
import com.aladin.springbootstudy.dto.USER_INFO_DTO;
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

import java.util.Date;
import java.util.HashMap;

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
    public String kakaoCallback(@RequestParam("code") String code) throws Exception {

        String kakaoAccessToken = OAuthKakaoService.getAccessTokenFromKakao(client_id, code);

        /** Kakao Auth */
        HashMap<String, Object> userInfoMap = new HashMap<>();
        userInfoMap = OAuthKakaoService.getUserInfo(kakaoAccessToken);
        String email = userInfoMap.get("email") != null ? userInfoMap.get("email").toString() : "";
        String encryptResult = encryptService.aesCBCEncode(email);

        if("".equals(email)) {
            throw new Exception();
        }

        /** DB Info */
        USER_INFO_DTO userInfoDto = new USER_INFO_DTO();
        userInfoDto.setSecretKey(encryptResult);
        userInfoDto = userService.getUserInfo(userInfoDto);

        if(userInfoDto == null) {
            userInfoDto = new USER_INFO_DTO();
            userInfoDto.setSecretKey(encryptResult);
            userInfoDto.setCreateDtm(new java.sql.Timestamp(new Date().getTime()));
            userInfoDto.setChangeDtm(new java.sql.Timestamp(new Date().getTime()));

            userService.insertUserInfo(userInfoDto);

            userInfoDto = userService.getUserInfo(userInfoDto);

            USER_AUTH_DTO userAuthDto = new USER_AUTH_DTO();
            userAuthDto.setUserId(userInfoDto.getUserId());
            userAuthDto.setAuthCd("00");
            userAuthDto.setAuthLevel("00");
            userAuthDto.setCreateDtm(new java.sql.Timestamp(new Date().getTime()));
            userAuthDto.setChangeDtm(new java.sql.Timestamp(new Date().getTime()));


        } else {
            return "/";
        }

        return null;
    }
}
