package com.example.shopping.controller;

import com.example.shopping.domain.member.MemberDTO;
import com.example.shopping.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
// @Slf4j를 사용하지 않고 Log4j2를 사용하는 이유는
// 기능면에서 더 좋기 때문입니다.
@Log4j2
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;


    // 회원가입
    @PostMapping("/api/v1/users/")
    // BindingResult 타입의 매개변수를 지정하면 BindingResult 매개 변수가 입력값 검증 예외를 처리한다.
    public ResponseEntity<?> join(@Validated @RequestBody MemberDTO member,
                                  BindingResult result) throws Exception {
        // 입력값 검증 예외가 발생하면 예외 메시지를 응답한다.
        if(result.hasErrors()) {
            log.info("BindingResult error : " + result.hasErrors());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getClass().getSimpleName());
        }

        try {
            ResponseEntity<?> signUp = memberService.signUp(member);
            return ResponseEntity.ok().body(signUp);
        } catch (Exception e) {
            log.error("예외 : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
