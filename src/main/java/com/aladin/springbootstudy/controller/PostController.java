package com.aladin.springbootstudy.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@RequestMapping("/api/v1/post-api")
/**
 *  Post API : 리소스를 추가하기 위해 사용되는 API
 * @PostMapping : POST API를 제작하기 위해 사용되는 어노테이션(Annotation)
 * @RequestMapping + POST METHOD 조합
 * 일반적으로 추가하고자 하는 Resource를 http body에 추가하여 서버에 요청
 * 그렇기 때문에 @RequestBody를 이용하여 body에 담겨있는 값을 받아야 함.
 * */
public class PostController {

    @PostMapping(value="/member")
    public String postMember(@RequestBody Map<String, Object> postData) {
        StringBuilder sb = new StringBuilder();

        postData.entrySet().forEach(map -> {
            sb.append(map.getKey() + " : " + map.getValue() + " \n");
        });

        return sb.toString();
    }

}
