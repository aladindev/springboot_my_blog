package com.aladin.springbootstudy.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


@RestController
@RequestMapping("/") // 
public class MainController {

    private Logger logger = LoggerFactory.getLogger(MainController.class);
    // 로컬 서버 포트 변경
    @GetMapping(value="/index")
    public ModelAndView index() {
        logger.debug("/index");
        ModelAndView mv = new ModelAndView("index");
        return mv;
    }

    // 로컬 서버 포트 변경
    @GetMapping(value="/")
    public ModelAndView main() {
        logger.debug("/");
        ModelAndView mv = new ModelAndView("index");
        return mv;
    }
}