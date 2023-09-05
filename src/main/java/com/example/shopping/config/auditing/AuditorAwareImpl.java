package com.example.shopping.config.auditing;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        // 인증된 회원은 SecurityContextHolder에 등록되는데
        // 그것을 불러오는 것이다.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = "";

        if(authentication != null) {
            // 현재 로그인한 사용자의 정보를 조회하여 사용자의 이름을 등록자와 수정자로 지정
            email = authentication.getName();
        }
        return Optional.of(email);
    }
}
