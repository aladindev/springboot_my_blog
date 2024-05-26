package com.aladin.springbootstudy.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/board")
public class BoardController {
    private Logger logger = LoggerFactory.getLogger(BoardController.class);
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

    @GetMapping(value="/write") // editor
    public ModelAndView getWriteHtml() {
        ModelAndView mv = new ModelAndView("board/write");
        return mv;
    }

    @PostMapping(value="/write") // post write
    public void postWrite(@RequestParam("title") String title,
                          @RequestParam("editordata") String editorData) {

        logger.info("title >> " + title);
        logger.info("editorData >> " + editorData);

    }
}
