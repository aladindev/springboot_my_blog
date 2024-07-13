package com.aladin.springbootstudy.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Controller       
@RequestMapping("/board")
public class BoardController {

    @Value("${file_path_img}")
    private String filePathImg;

    private Logger logger = LoggerFactory.getLogger(BoardController.class);
    // 로컬 서버 포트 변경
    @GetMapping(value="/post")
    public ModelAndView post() {
        logger.warn("post controller ");
        ModelAndView mv = new ModelAndView("board/post");
        return mv;
    }

    @GetMapping(value="/category")
    public ModelAndView category() {  // kafka !!
        logger.warn("post controller ");
        ModelAndView mv = new ModelAndView("board/category");
        return mv;
    }

    @GetMapping(value="/write") // editor
    public ModelAndView getWriteHtml(HttpSession session) {
        Map<String, Boolean> resultMap = new HashMap<>();
        if(session.getAttribute("userInfoDto") != null) {
            resultMap.put("isLogin", true);
        } else {
            resultMap.put("isLogin", false);
            ModelAndView mv = new ModelAndView("/index");
            return mv;
        }

        ModelAndView mv = new ModelAndView("board/write");
        return mv;
    }

    @PostMapping(value="/write") // post write // serialization.... interface
    public void postWrite(@RequestParam("title") String title,
                          @RequestParam("editordata") String editorData,
                          @RequestParam("img") String[] imgs) {

        logger.info("title >> " + title);
        logger.info("editorData >> " + editorData);
        logger.info("imgs >> " + imgs);
        logger.info("filePathImg >> " + filePathImg);



        try {
            for(String base64Image : imgs) {
                // Base64 디코딩
                byte[] imageBytes = Base64.getDecoder().decode(base64Image);

                // 디코딩된 바이트 배열을 파일로 저장
                Path destinationFile = Paths.get(filePathImg, "filename.png"); // ㅎㅏ... bb  
                Files.write(destinationFile, imageBytes);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
