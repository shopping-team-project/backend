package com.example.shopping.domain.cart;

import com.example.shopping.domain.member.MemberDTO;
import com.example.shopping.entity.cart.CartEntity;
import com.example.shopping.entity.member.MemberEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class CartDTO {

    @Schema(description = "장바구니번호")
    private Long cartId;

    @Schema(description = "구매자아이디")
    private Long memberId;

    @Schema(description = "구매자닉네임")
    private Long memberNick;

    private List<CartItemDTO> cartItem = new ArrayList<>();

    @Builder
    public CartDTO(Long cartId, Long memberId, List<CartItemDTO> cartItem) {
        this.cartId = cartId;
        this.memberId = memberId;
        this.cartItem = cartItem;
    }

    public CartEntity toEntity(MemberEntity member){
        return CartEntity.builder()
                .cartId(this.cartId)
                .member(member)
                .cartItem(this.cartItem.stream()
                        .map(CartItemDTO::toEntity).collect(Collectors.toList()))
                .build();

    }

}
