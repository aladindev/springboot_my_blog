package com.aladin.springbootstudy.controller;

import com.aladin.springbootstudy.dto.MemberDTO;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/get-api")
public class GetController {

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


    @GetMapping(value="/request1")
    /**
     * @RequestParam
     * GET형식의 요청에서 쿼리 문자열을 전달하기 위해 사용되는 방법
     * '?'를 기준으로 우측에 key=value의 형태로 전달되며,
     * 복수 형태로 전달될 경우 &를 구분자로 사용함.
     * 쿼리스트링!
     * */
    public String getRequestParam1(@RequestParam String name,
                                   @RequestParam String email,
                                   @RequestParam String organization) {
        return name + " / " + email + " / " + organization;
    }

    @GetMapping(value="/request2")
    /**
     * @RequestParam
     * 어떤 GET 요청 값이 들어올지 모를 경우 Map<String, String>으로
     * 전달받는다.
     * Object의 Null Check에 유의해야겠지?
     * */
    public String getRequestParam2(@RequestParam Map<String, String> params) {

        StringBuilder sb = new StringBuilder();

        params.entrySet().forEach(map -> {
            sb.append(map.getKey() + " : " + map.getValue() + "\n");
        });

        return sb.toString();
    }

    @GetMapping(value="/request3")
    /**
     * @RequestParam
     * key와 value가 정해져있지만, 받아야할 파라미터가 많을 경우
     * DTO 객체를 사용한 방식
     * */
    public String getRequestParam3(@RequestParam MemberDTO memberDto) {

        StringBuilder sb = new StringBuilder();

        sb.append(memberDto.getName());

        return sb.toString();
    }
}
