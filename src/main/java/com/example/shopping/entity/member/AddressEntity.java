package com.example.shopping.entity.member;

import lombok.*;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;


// 임베디드 타입을 사용하려면 넣어야한다.
@Embeddable
@ToString
@Getter
@NoArgsConstructor
public class AddressEntity {
    private String memberAddr;
    private String memberAddrDetail;
    private String memberAddrEtc;

    @Builder
    public AddressEntity(String memberAddr, String memberAddrDetail, String memberAddrEtc) {
        this.memberAddr = memberAddr;
        this.memberAddrDetail = memberAddrDetail;
        this.memberAddrEtc = memberAddrEtc;
    }
}