package com.example.shopping.config.oauth2;

import com.example.shopping.config.PrincipalDetails;
import com.example.shopping.config.jwt.JwtProvider;
import com.example.shopping.config.oauth2.provider.GoogleUser;
import com.example.shopping.config.oauth2.provider.NaverUser;
import com.example.shopping.config.oauth2.provider.OAuth2UserInfo;
import com.example.shopping.domain.jwt.TokenDTO;
import com.example.shopping.domain.member.Role;
import com.example.shopping.entity.jwt.TokenEntity;
import com.example.shopping.entity.member.MemberEntity;
import com.example.shopping.repository.jwt.TokenRepository;
import com.example.shopping.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

// 소셜 로그인하면 사용자 정보를 가지고 온다.
// 가져온 정보와 PrincipalDetails 객체를 생성합니다.
@Service
@Log4j2
@RequiredArgsConstructor
public class PrincipalOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;
    private final TokenRepository tokenRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // userRequest.getClientRegistration()은 인증 및 인가된 사용자 정보를 가져오는
        // Spring Security에서 제공하는 메서드입니다.
        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        log.info("clientRegistration : " + clientRegistration);
        // 소셜 로그인 accessToken
        String socialAccessToken = userRequest.getAccessToken().getTokenValue();
        log.info("소셜 로그인 accessToken : " + socialAccessToken);

        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService =
                new DefaultOAuth2UserService();
        log.info("oAuth2UserService : " + oAuth2UserService);

        // 소셜 로그인한 유저정보를 가져온다.
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);
        log.info("oAuth2User : " + oAuth2User);
        log.info("getAttribute : " + oAuth2User.getAttributes());

        // 회원가입 강제 진행
        OAuth2UserInfo oAuth2UserInfo = null;
        String registrationId = clientRegistration.getRegistrationId();
        log.info("registrationId : " + registrationId);

        if(registrationId.equals("google")) {
            log.info("구글 로그인");
            oAuth2UserInfo = new GoogleUser(oAuth2User, clientRegistration);
        } else  if(registrationId.equals("naver")) {
            log.info("네이버 로그인");
            oAuth2UserInfo = new NaverUser(oAuth2User, clientRegistration);
        } else {
            log.error("지원하지 않는 소셜 로그인입니다.");
        }

        // 사용자가 로그인한 소셜 서비스를 가지고 옵니다.
        // 예시) google or naver 같은 값을 가질 수 있다.
        String provider = oAuth2UserInfo.getProvider();
        // 사용자의 소셜 서비스(provider)에서 발급된 고유한 식별자를 가져옵니다.
        // 이 값은 해당 소셜 서비스에서 유니크한 사용자를 식별하는 용도로 사용됩니다.
        String providerId = oAuth2UserInfo.getProviderId();
        String name = oAuth2UserInfo.getName();
        // 사용자의 이메일 주소를 가지고 옵니다.
        // 소셜 서비스에서 제공하는 이메일 정보를 사용합니다.
        String email = oAuth2UserInfo.getEmail();
        // 소셜 로그인의 경우 무조건 USER 등급으로 고정이다.
        Role role = Role.USER;

        MemberEntity findUser = memberRepository.findByEmail(email);

        if(findUser == null) {
            log.info("소셜 로그인이 최초입니다.");
            log.info("소셜 로그인 자동 회원가입을 진행합니다.");

            findUser = MemberEntity.builder()
                    .email(email)
                    .memberName(name)
                    .provider(provider)
                    .providerId(providerId)
                    .memberRole(role)
                    .nickName(name)
                    .build();

            log.info("member : " + findUser);
            memberRepository.save(findUser);
        } else {
            log.info("로그인을 이미 한적이 있습니다.");
        }
        List<GrantedAuthority> authorities = getAuthoritiesForUser(findUser);
        TokenDTO tokenForOAuth2 = jwtProvider.createTokenForOAuth2(email, authorities);
        TokenEntity findToken = tokenRepository.findByMemberEmail(tokenForOAuth2.getMemberEmail());

        TokenEntity saveToken = null;
        if(findToken == null) {
            TokenEntity tokenEntity = TokenEntity.tokenEntity(tokenForOAuth2);
            saveToken = tokenRepository.save(tokenEntity);
            log.info("token : " + saveToken);
        } else {
            tokenForOAuth2 = TokenDTO.builder()
                    .id(findToken.getId())
                    .grantType(tokenForOAuth2.getGrantType())
                    .accessToken(tokenForOAuth2.getAccessToken())
                    .accessTokenTime(tokenForOAuth2.getAccessTokenTime())
                    .refreshToken(tokenForOAuth2.getRefreshToken())
                    .refreshTokenTime(tokenForOAuth2.getRefreshTokenTime())
                    .memberEmail(tokenForOAuth2.getMemberEmail())
                    .build();
            TokenEntity tokenEntity = TokenEntity.tokenEntity(tokenForOAuth2);
            saveToken = tokenRepository.save(tokenEntity);
            log.info("token : " + saveToken);
        }

        // 토큰이 제대로 되어 있나 검증
        if(StringUtils.hasText(saveToken.getAccessToken())
                && jwtProvider.validateToken(saveToken.getAccessToken())) {
            Authentication authentication = jwtProvider.getAuthentication(saveToken.getAccessToken());
            log.info("authentication : " + authentication);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetails userDetails = new User(email, "", authorities);
            log.info("userDetails : " + userDetails);
            Authentication authentication1 =
                    new UsernamePasswordAuthenticationToken(userDetails, authorities);
            log.info("authentication1 : " + authentication1);
            SecurityContextHolder.getContext().setAuthentication(authentication1);
        } else {
            log.info("검증 실패");
        }
        // attributes가 있는 생성자를 사용하여 PrincipalDetails 객체 생성
        // 소셜 로그인인 경우에는 attributes도 함께 가지고 있는 PrincipalDetails 객체를 생성하게 됩니다.
        PrincipalDetails principalDetails = new PrincipalDetails(findUser, oAuth2User.getAttributes());
        log.info("principalDetails : " + principalDetails);
        return principalDetails;
    }
    private List<GrantedAuthority> getAuthoritiesForUser(MemberEntity findUser) {
        Role role = findUser.getMemberRole();

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role.name()));
        log.info("권한 : " + role.name());
        return authorities;
    }
}
