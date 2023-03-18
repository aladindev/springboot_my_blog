package com.aladin.springbootstudy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/")
public class ThymeleafController {

    //@RequestMapping(value="/hello")
    @GetMapping("user-login")
    public String hello() {
        return "login";
    }
}
