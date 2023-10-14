package com.example.shopping.domain.member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

// 주소 정보를 따로 뺀 이유는 객체지향에 맞게 관리하기 편하도록 따로 빼놓음
@ToString
@Getter
@NoArgsConstructor
public class AddressDTO {
    @Schema(description = "우편번호")
    private String memberAddr;
    @Schema(description = "주소")
    private String memberAddrDetail;
    @Schema(description = "상세 주소")
    private String memberAddrEtc;

    @Builder
    public AddressDTO(String memberAddr, String memberAddrDetail, String memberAddrEtc) {
        this.memberAddr = memberAddr;
        this.memberAddrDetail = memberAddrDetail;
        this.memberAddrEtc = memberAddrEtc;
    }
}
