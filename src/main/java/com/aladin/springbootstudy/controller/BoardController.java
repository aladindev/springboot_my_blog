package com.aladin.springbootstudy.controller;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/board")
public class BoardController {
    private Logger logger = Logger.getLogger(BoardController.class);
    // 로컬 서버 포트 변경
    @GetMapping(value="/post")
    public ModelAndView post() {
        logger.warn("post controller ");
        ModelAndView mv = new ModelAndView("board/post");
        return mv;
    }

    @GetMapping(value="/category")
    public ModelAndView category() {
        logger.warn("post controller ");
        ModelAndView mv = new ModelAndView("board/category");
        return mv;
    }

    @GetMapping(value="/about")
    public ModelAndView about() {
        logger.warn("post controller ");
        ModelAndView mv = new ModelAndView("board/about");
        return mv;
    }
}
