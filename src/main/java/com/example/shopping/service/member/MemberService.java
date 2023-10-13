package com.example.shopping.service.member;

import com.example.shopping.domain.member.MemberDTO;
import com.example.shopping.domain.member.ModifyDTO;
import org.springframework.http.ResponseEntity;

public interface MemberService {
    // 회원가입
    ResponseEntity<?> signUp(MemberDTO memberDTO);
    // 회원조회
    MemberDTO search(Long memberId);
    // 회원삭제
    String removeUser(Long memberId, String email);
    // 로그인
    ResponseEntity<?> login(String memberEmail, String memberPw);
    // 회원수정
    ResponseEntity<?> updateUser(Long memberId, ModifyDTO modifyDTO, String memberEmail);
    // 이메일 체크
    String emailCheck(String email);
    // 닉네임 체크
    String nickNameCheck(String nickName);
}
