package com.aladin.springbootstudy.service.session;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Service
public class SessionService {

    public void addSession(HttpServletRequest httpServletRequest, String userId) {
        // 세션을 생성하기 전에 기존의 세션 파기
        httpServletRequest.getSession().invalidate();
        HttpSession session = httpServletRequest.getSession(true);  // Session이 없으면 생성
        session.setAttribute("userId", userId);
        session.setMaxInactiveInterval(600); // Session이 10분동안 유지
    }
}
