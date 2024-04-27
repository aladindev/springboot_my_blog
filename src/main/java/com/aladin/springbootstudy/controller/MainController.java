package com.aladin.springbootstudy.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

//@RestController
//@RequestMapping("/api/v1/get-api") // infix = 공통 URL
//public class MainController {
//
//    @GetMapping(value="/login")
//    public ModelAndView login() {
//        // 클라우드 변경 Oracle -> AWS
//        ModelAndView mv = new ModelAndView("login");
//        return mv;
//    }
//}

@RestController
@RequestMapping("/") //
public class MainController {

    // 로컬 서버 포트 변경
    @GetMapping(value="/index")
    public ModelAndView login() {
        // 클라우드 변경 Oracle -> AWS
        ModelAndView mv = new ModelAndView("login");
        return mv;
    }
}