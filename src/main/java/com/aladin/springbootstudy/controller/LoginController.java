package com.aladin.springbootstudy.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginController {
    private Logger logger = LoggerFactory.getLogger(BoardController.class);

    @Value("${oauth.client_id}")
    String client_id;
    @Value("${oauth.redirect_uri}")
    String redirect_uri;

    @GetMapping(value="/kakao")
    public Map<String, String> getKakaoConfig() {
        Map<String, String> kakaoConfig = new HashMap<>();
        kakaoConfig.put("client_id", client_id);
        kakaoConfig.put("redirect_uri", redirect_uri);

        // 클라이언트 ID와 콜백 URL을 포함하는 Map을 반환합니다.
        return kakaoConfig;
    }

    @GetMapping("/logout")
    public Map<String, String> logout(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);  // Session이 없으면 null return
        if(session != null) {
            session.invalidate();
        }
        Map<String, String> resMap = new HashMap<>();
        resMap.put("resultMsg", "정상적으로 로그아웃 되었습니다.");
        return resMap;
    }
}
