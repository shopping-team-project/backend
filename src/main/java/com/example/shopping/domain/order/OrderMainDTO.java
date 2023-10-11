package com.example.shopping.domain.order;

import lombok.Getter;

import javax.validation.constraints.Min;

@Getter
public class OrderMainDTO {
    private Long itemId;

    @Min(value = 1, message = "최소 주문 수량은 1개 입니다.")
    private int count;

    private String mbrEmail;

}
