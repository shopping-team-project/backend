package com.example.shopping.config.oauth2;

import com.example.shopping.entity.jwt.TokenEntity;
import com.example.shopping.entity.member.MemberEntity;
import com.example.shopping.repository.jwt.TokenRepository;
import com.example.shopping.repository.member.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final MemberRepository memberRepository;
    private final TokenRepository tokenRepository;
    // Jackson ObjectMapper를 주입합니다.
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        try {
            log.info("OAuth2 Login 성공!");
            String email = authentication.getName();
            log.info("email : " + email);
            TokenEntity findToken = tokenRepository.findByMemberEmail(email);
            log.info("token : " + findToken);
            MemberEntity findUser = memberRepository.findByEmail(email);
            // 헤더에 담아준다.
            response.addHeader("email", findToken.getMemberEmail());

            // 바디에 담아준다.
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("providerId", findUser.getProviderId());
            responseBody.put("accessToken", findToken.getAccessToken());
            responseBody.put("refreshToken", findToken.getRefreshToken());
            responseBody.put("email", findToken.getMemberEmail());

            // JSON 응답 전송
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(responseBody));
        } catch (Exception e) {
            // 예외가 발생하면 클라이언트에게 오류 응답을 반환
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("OAuth 2.0 로그인 성공 후 오류 발생: " + e.getMessage());
            response.getWriter().flush();
        }
    }
}
