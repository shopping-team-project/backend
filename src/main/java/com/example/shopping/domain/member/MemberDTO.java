package com.example.shopping.domain.member;

import com.example.shopping.entity.member.MemberEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@ToString
@Getter
@NoArgsConstructor
public class MemberDTO {
    @Schema(description = "유저 번호", example = "1", required = true)
    private Long memberId;

    @Schema(description = "이메일", example = "abc@gmail.com", required = true)
    @NotNull(message = "이메일은 필수 입력입니다.")
    @Pattern(regexp = "^(?:\\w+\\.?)*\\w+@(?:\\w+\\.)+\\w+$", message = "이메일 형식이 올바르지 않습니다.")
    @Email(message = "이메일 형식에 맞지 않습니다.")
    private String email;

    @Schema(description = "회원 이름")
    @NotNull(message = "이름은 필수 입력입니다.")
    private String memberName;

    @Schema(description = "닉네임")
    @NotNull(message = "닉네임은 필수 입력입니다.")
    private String nickName;

    @Schema(description = "회원 비밀번호")
    private String memberPw;

    @Schema(description = "회원 권한")
    @NotNull(message = "권한정보는 필수 입력입니다.")
    private Role memberRole;

    @Schema(description = "회원 주소")
    private AddressDTO memberAddress;

    @Schema(description = "소셜 로그인")
    private String provider;        // ex) google

    @Schema(description = "소셜 로그인 식별 아이디")
    private String providerId;

    @Schema(description = "포인트")
    private String memberPoint;

    @Builder
    public MemberDTO(Long memberId,
                     String email,
                     String memberName,
                     String nickName,
                     String memberPw,
                     Role memberRole,
                     AddressDTO memberAddress,
                     String provider,
                     String providerId,
                     String memberPoint) {
        this.memberId = memberId;
        this.email = email;
        this.memberName = memberName;
        this.nickName = nickName;
        this.memberPw = memberPw;
        this.memberRole = memberRole;
        this.memberAddress = memberAddress;
        this.provider = provider;
        this.providerId = providerId;
        this.memberPoint = memberPoint;
    }

    public static MemberDTO toMemberDTO(MemberEntity member) {
        return MemberDTO.builder()
                .memberId(member.getMemberId())
                .email(member.getEmail())
                .memberPw(member.getMemberPw())
                .nickName(member.getNickName())
                .memberName(member.getMemberName())
                .memberRole(member.getMemberRole())
                .memberPoint(member.getMemberPoint())
                .memberAddress(AddressDTO.builder()
                        .memberAddr(member.getAddress().getMemberAddr() == null
                                ? null : member.getAddress().getMemberAddr())
                        .memberAddrDetail(member.getAddress().getMemberAddrDetail() == null
                                ? null : member.getAddress().getMemberAddrDetail())
                        .memberAddrEtc(member.getAddress().getMemberAddrEtc() == null
                                ? null : member.getAddress().getMemberAddrEtc())
                        .build()).build();
    }
}
