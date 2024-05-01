package com.aladin.springbootstudy.controller;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/join")
public class JoinController {
    private Logger logger = Logger.getLogger(BoardController.class);
    // 로컬 서버 포트 변경
    @GetMapping(value="/kakao")
    public ModelAndView post() {
        ModelAndView mv = new ModelAndView("board/post");
        return mv;
    }
}
