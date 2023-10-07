package com.example.shopping.entity.cart;

import com.example.shopping.domain.cart.CartDTO;
import com.example.shopping.entity.Base.BaseTimeEntity;
import com.example.shopping.entity.member.MemberEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Entity(name = "cart")
@Getter
@ToString
@NoArgsConstructor
public class CartEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name="cart_id")
    private Long cartId;

    @OneToMany(mappedBy="cart")
    private List<CartItemEntity> cartItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity member;

    @Builder
    public CartEntity(Long cartId, List<CartItemEntity> cartItem, MemberEntity member) {
        this.cartId = cartId;
        this.cartItem = cartItem;
        this.member = member;
    }

    public CartDTO toDTO(){
        return CartDTO.builder()
                .cartId(this.cartId)
                .cartItem(this.cartItem.stream()
                        .map(CartItemEntity::toDTO)
                        .collect(Collectors.toList()))

                .build();
    }

}
