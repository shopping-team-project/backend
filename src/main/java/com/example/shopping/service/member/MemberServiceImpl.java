package com.example.shopping.service.member;

import com.example.shopping.config.jwt.JwtProvider;
import com.example.shopping.domain.jwt.TokenDTO;
import com.example.shopping.domain.member.MemberDTO;
import com.example.shopping.domain.member.ModifyDTO;
import com.example.shopping.domain.member.Role;
import com.example.shopping.entity.jwt.TokenEntity;
import com.example.shopping.entity.member.AddressEntity;
import com.example.shopping.entity.member.MemberEntity;
import com.example.shopping.repository.jwt.TokenRepository;
import com.example.shopping.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class MemberServiceImpl implements MemberService{
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final TokenRepository tokenRepository;

    // 회원가입
    @Override
    public ResponseEntity<?> signUp(MemberDTO memberDTO) {
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
                MemberEntity saveMember = memberRepository.save(member);

                MemberDTO coverMember = MemberDTO.toMemberDTO(saveMember);
                return ResponseEntity.ok().body(coverMember);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 회원 조회
    @Override
    public MemberDTO search(Long memberId) {
        MemberEntity member = memberRepository.findById(memberId)
                .orElseThrow(EntityNotFoundException::new);

        return MemberDTO.toMemberDTO(member);
    }

    // 회원 삭제
    @Override
    public String removeUser(Long memberId) {
        MemberEntity member = memberRepository.findById(memberId)
                .orElseThrow(EntityNotFoundException::new);

        if(member != null) {
            memberRepository.deleteByMemberId(memberId);
            return "회원 탈퇴 완료";
        } else {
            return "유저가 등록되어 있지 않습니다.";
        }
    }

    // 로그인
    @Override
    public ResponseEntity<?> login(String memberEmail, String memberPw){
        try {
            MemberEntity findUser = memberRepository.findByEmail(memberEmail);
            log.info("user : " + findUser);

            if(findUser != null) {
                // DB에 넣어져 있는 비밀번호는 암호화가 되어 있어서 비교하는 기능을 사용해야 합니다.
                // 사용자가 입력한 패스워드를 암호화하여 사용자 정보와 비교
                if(passwordEncoder.matches(memberPw, findUser.getMemberPw())) {
                    Authentication authentication = new UsernamePasswordAuthenticationToken(memberEmail, memberPw);
                    log.info("authentication : " + authentication);
                    List<GrantedAuthority> authoritiesForUser = getAuthoritiesForUser(findUser);

                    // JWT 생성
                    TokenDTO token = jwtProvider.createToken(authentication, authoritiesForUser);
                    TokenEntity findToken = tokenRepository.findByMemberEmail(token.getMemberEmail());

                    if(findToken == null) {
                        log.info("발급한 토큰이 없습니다. 새로운 토큰을 발급합니다.");
                        TokenEntity tokenEntity = TokenEntity.tokenEntity(token);
                        tokenRepository.save(tokenEntity);
                    } else {
                        log.info("이미 발급한 토큰이 있습니다. 토큰을 업데이트합니다.");
                        token = TokenDTO.builder()
                                .id(findToken.getId())
                                .grantType(token.getGrantType())
                                .accessToken(token.getAccessToken())
                                .accessTokenTime(token.getAccessTokenTime())
                                .refreshToken(token.getRefreshToken())
                                .refreshTokenTime(token.getRefreshTokenTime())
                                .memberEmail(token.getMemberEmail())
                                .build();
                        TokenEntity tokenEntity = TokenEntity.tokenEntity(token);
                        tokenRepository.save(tokenEntity);
                    }
                    return ResponseEntity.ok().body(token);
                } else {
                    return ResponseEntity.badRequest().build();
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("유저가 없습니다.");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    // 회원의 권한을 GrantedAuthority타입으로 반환하는 메소드
    private List<GrantedAuthority> getAuthoritiesForUser(MemberEntity member) {
        Role memberRole = member.getMemberRole();
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + memberRole.name()));
        log.info("role : " + authorities);
        return authorities;
    }

    // 회원정보 수정
    @Override
    public ResponseEntity<?> updateUser(ModifyDTO modifyDTO, String memberEmail) {
        try {
            MemberEntity findUser = memberRepository.findByEmail(memberEmail);
            log.info("user : " + findUser);

            if(findUser != null) {
                findUser = MemberEntity.builder()
                        .memberId(findUser.getMemberId())
                        .email(findUser.getEmail())
                        .memberPw(passwordEncoder.encode(modifyDTO.getMemberPw()))
                        .nickName(modifyDTO.getNickName())
                        .memberRole(findUser.getMemberRole())
                        .memberPoint(findUser.getMemberPoint())
                        .address(AddressEntity.builder()
                                .memberAddr(modifyDTO.getMemberAddress().getMemberAddr())
                                .memberAddrDetail(modifyDTO.getMemberAddress().getMemberAddrDetail())
                                .memberAddrEtc(modifyDTO.getMemberAddress().getMemberAddrEtc())
                                .build()).build();

                MemberEntity updateUser = memberRepository.save(findUser);
                MemberDTO toMemberDTO = MemberDTO.toMemberDTO(updateUser);
                return ResponseEntity.ok().body(toMemberDTO);
            } else {
                throw new EntityNotFoundException();
            }
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("회원 정보가 없습니다.");
        }
    }

    // 중복체크
    @Override
    public String emailCheck(String email) {
        MemberEntity findEmail = memberRepository.findByEmail(email);
        if(findEmail == null) {
            return "회원가입이 가능한 이메일입니다.";
        } else {
            return "이미 가입한 이메일이 있습니다.";
        }
    }
}
