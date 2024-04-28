package com.aladin.springbootstudy.controller;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


@RestController
@RequestMapping("/") // 
public class MainController {

    private Logger logger = Logger.getLogger(MainController.class);
    // 로컬 서버 포트 변경
    @GetMapping(value="/index")
    public ModelAndView index() {
        logger.info("/index");
        ModelAndView mv = new ModelAndView("index");
        return mv;
    }

    // 로컬 서버 포트 변경
    @GetMapping(value="/")
    public ModelAndView main() {
        logger.info("/");
        ModelAndView mv = new ModelAndView("index");
        return mv;
    }
}