package com.aladin.springbootstudy.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    //@RequestMapping(value="/hello")
    @GetMapping("/hello")
    public String hello() {
        return "Hello World";
    }

    @GetMapping(value="/variable1/{variable}")
    /**
     *  PathVariable : GET 형식의 요청에서 파라미터를 전달하기 위해 URL에 값을 담아
     *  요청하는 방법 {변수}의 이름과 메소드의 매개변수와 일치시켜야 함
     *  매개변수의 변수명을 일치시킬 수 없는 상황에는
     * @PathVariable("변수명")으로 선언한다.
     * */
    public String getVariable1(@PathVariable String variable) {
        return variable;
    }
}
