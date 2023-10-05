package com.example.shopping.config;

import com.example.shopping.entity.member.MemberEntity;
import com.example.shopping.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// 이 클래스의 기능은 로그인시 회원을 조회하고 찾아서 PrincipalDetails에 넘겨준다.
@Service
@Log4j2
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MemberEntity findMember = memberRepository.findByEmail(username);
        log.info("member : " + findMember);

        if(findMember == null) {
            throw new UsernameNotFoundException("해당 사용자가 없습니다.");
        } else {
            return new PrincipalDetails(findMember);
        }
    }
}
