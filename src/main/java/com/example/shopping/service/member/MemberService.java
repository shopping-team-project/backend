package com.example.shopping.service.member;

import com.example.shopping.domain.member.MemberDTO;
import com.example.shopping.entity.member.AddressEntity;
import com.example.shopping.entity.member.MemberEntity;
import com.example.shopping.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    public ResponseEntity<?> signUp(MemberDTO memberDTO) throws Exception {
        try {
            MemberEntity findEmail = memberRepository.findByEmail(memberDTO.getEmail());

            if(findEmail != null) {
                return ResponseEntity.badRequest().body("이미 가입된 회원입니다.");
            } else {
                // 아이디가 없다면 DB에 등록해줍니다.
                MemberEntity member = MemberEntity.builder()
                        .email(memberDTO.getEmail())
                        .memberPw(passwordEncoder.encode(memberDTO.getMemberPw()))
                        .memberName(memberDTO.getMemberName())
                        .nickName(memberDTO.getNickName())
                        .memberRole(memberDTO.getMemberRole())
                        .address(AddressEntity.builder()
                                .memberAddr(memberDTO.getMemberAddress().getMemberAddr())
                                .memberAddrDetail(memberDTO.getMemberAddress().getMemberAddrDetail())
                                .memberAddrEtc(memberDTO.getMemberAddress().getMemberAddrEtc())
                                .build()).build();

                log.info("member in service : " + member);
                memberRepository.save(member);

                MemberDTO coverMember = MemberDTO.toMemberDTO(member);
                return ResponseEntity.ok().body(coverMember);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
