package com.aladin.springbootstudy.controller;

import com.aladin.springbootstudy.common.CommonFunction;
import com.aladin.springbootstudy.dto.AccountsListFormDto;
import com.aladin.springbootstudy.dto.UpbitAccountDto;
import com.aladin.springbootstudy.dto.UserExchngListDto;
import com.aladin.springbootstudy.service.UserInfoService;
import com.aladin.springbootstudy.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@RestController
@RequestMapping("/api/v3/sse") // infix = 공통 URL
public class SseController extends CommonFunction {

    //이벤트 발행을 비동기로 처리하기 위해 별도 스레드를 통해 처리함
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    @GetMapping(value="/subscribe", produces = "text/event-stream")
    public SseEmitter subscribe(@RequestParam(value = "exchngCd", required = false) Set<String> exchngCdSet) throws IOException {
        final SseEmitter emitter = new SseEmitter();

        // String 배열로 받아도 되지만 굳이 Set으로 받는다.
        Iterator<String> iterator = exchngCdSet.iterator();
        String[] exchngCdArr = exchngCdSet != null && exchngCdSet.size() > 0 ? new String[exchngCdSet.size()] : null;
        int idx = 0;
        while(iterator.hasNext()) exchngCdArr[idx++] = iterator.next();

        // 이전 금액을 담을 Map 선언 key:exchngCd val: Map<String, BigDecimal>(key:coin token / val : amt)
        Map<String, Map<String, BigDecimal>> bfAmtMap = new HashMap<>();
        for(String exchngCd : exchngCdArr) {
            bfAmtMap.put(exchngCd, new HashMap<>());
        }

        // 503 에러 방지를 위한 최초 연결 시 더미 데이터 전송
        emitter.send(SseEmitter.event()
                .name("connect")
                .data("connected!"));

        executorService.execute(() -> {
            try {
                while(true) {

                    List<AccountsListFormDto> listAccounts = new ArrayList<>();
                    for(String s : exchngCdArr) {
                        listAccounts.addAll(exchngApiRequest(s));
                    }

                    emitter.send(listAccounts);

                    Thread.sleep(1000);
                }
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        });
        return emitter;

    }


    public void upbitAccountList(@SessionAttribute(name = "session_key", required = false) String session_key
                                   , HttpServletResponse response) throws IOException, InterruptedException {

        HttpHeaders headers = new HttpHeaders();
        if("".equals(session_key) || session_key == null) {
            response.sendRedirect("/api/v1/get-api/login");

        } else {
            //upbit
            List<UpbitAccountDto> upbitAccountDtoList = upbit_accounts_info();
            List<AccountsListFormDto> UpbitAccountsListForm = upbitDtoProcessor(upbitAccountDtoList);

            Writer writer = response.getWriter();
            for(AccountsListFormDto alFDto : UpbitAccountsListForm) {
                writer.write(alFDto.toString());
                writer.write("\n");
                writer.flush();
                Thread.sleep(2000);
            }
            writer.close();
        }
    }
}
