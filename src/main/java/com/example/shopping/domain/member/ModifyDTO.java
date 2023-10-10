package com.example.shopping.domain.member;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@ToString
@Getter
@NoArgsConstructor
public class ModifyDTO {
    @Schema(description = "회원 이름")
    @NotNull(message = "이름은 필수 입력입니다.")
    private String memberName;

    @Schema(description = "닉네임")
    @NotNull(message = "닉네임은 필수 입력입니다.")
    private String nickName;

    @Schema(description = "회원 비밀번호")
    private String memberPw;

    @Schema(description = "회원 주소")
    private AddressDTO memberAddress;

    @Builder
    public ModifyDTO(String memberName, String nickName, String memberPw, AddressDTO memberAddress) {
        this.memberName = memberName;
        this.nickName = nickName;
        this.memberPw = memberPw;
        this.memberAddress = memberAddress;
    }
}
