package com.aladin.springbootstudy.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/login")
public class LoginController {
    private Logger logger = Logger.getLogger(BoardController.class);

    @Value("#{oauth.restapi_key}")
    String restapi_key;
    @Value("#{oauth.redirect_url}")
    String redirect_url;

    @GetMapping(value="/kakao")
    public ModelAndView post() {

        return null;
    }
}
