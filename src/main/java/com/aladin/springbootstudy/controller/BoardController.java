package com.aladin.springbootstudy.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;

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
    public Map<String, Object> writePost(@SessionAttribute(name = "userId", required = false) String userId) {
        Map<String, Object> resultMap = new HashMap<>();
        if(userId != null && !"".equals(userId)) {
            resultMap.put("isLogin", true);
        } else {
            resultMap.put("isLogin", false);
        }
        return resultMap;
    }
}
