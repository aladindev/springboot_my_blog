package com.aladin.springbootstudy.controller;

import org.springframework.web.bind.annotation.*;

@RestController
public class X2 {

    //@RequestMapping(value="/hello")
    @GetMapping("/hello")
    public String hello() {
        return "Hello World";
    }
}
