package com.aladin.springbootstudy.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/api/v1/get-api") // infix = 공통 URL
public class MainController {

    @GetMapping(value="/login")
    public ModelAndView login() {
        ModelAndView mv = new ModelAndView("login");
        return mv;
    }
}
