package com.example.shopping.controller;

import com.example.shopping.domain.member.AddressDTO;
import com.example.shopping.domain.member.MemberDTO;
import com.example.shopping.domain.member.Role;
import com.example.shopping.service.member.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@Log4j2
@AutoConfigureMockMvc
@Transactional
class MemberControllerTest {

    // 테스트를 더 격리시키고 제어 가능하게 만들기 위한 것입니다.
    // 이를 통해 테스트를 더 간단하고 효율적으로 수행할 수 있습니다.
    @Autowired
    private MockMvc mockMvc;
    // MemberService를 Mock으로 대체
    @MockBean
    private MemberService memberService;

    @Test
    @DisplayName("회원가입 테스트")
    void signUpTest() throws Exception {
        String email = "zxzz12@naver.com";
        String password = "zxzz12";
        String name = "tester";
        String nickName = "CH";
        Role role = Role.USER;
        String addr = "서울시 구로구";
        String addrDetail = "101-12";
        String addrEtc = "101";

        MemberDTO memberDTO = MemberDTO.builder()
                .memberPw(password)
                .email(email)
                .memberName(name)
                .nickName(nickName)
                .memberRole(role)
                .memberAddress(AddressDTO.builder()
                        .memberAddr(addr)
                        .memberAddrDetail(addrDetail)
                        .memberAddrEtc(addrEtc)
                        .build()).build();

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(memberDTO);

        // POST 요청을 보내고 응답을 확인
        mockMvc.perform(post("/api/v1/users/")
                .contentType(MediaType.APPLICATION_JSON)
                // MemberDTO를 JSON 문자열로 변환
                .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(content().string("회원가입 성공"));

    }
}