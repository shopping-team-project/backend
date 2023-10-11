package com.example.shopping.entity.cart;

import com.example.shopping.domain.cart.CartDTO;
import com.example.shopping.domain.cart.CartItemDTO;
import com.example.shopping.entity.Base.BaseTimeEntity;
import com.example.shopping.entity.item.ItemEntity;
import com.example.shopping.entity.member.MemberEntity;
import com.example.shopping.entity.order.OrderEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity(name = "cartitem")
@Getter
@ToString
@NoArgsConstructor
public class CartItemEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name="cartitem_id")
    private Long cartitemId;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="cart_id")
    private CartEntity cart;

    @Column(name="cart_count")
    private int count;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private ItemEntity item;

    @Builder
    public CartItemEntity(Long cartitemId, CartEntity cart, ItemEntity item, int count) {
        this.cartitemId = cartitemId;
        this.cart =cart;
        this.item = item;
        this.count = count;
    }

    public CartItemDTO toDTO(){
        return CartItemDTO.builder()
                .itemId(this.item.getItemId())
                .count(this.count)
                .build();
    }

    public static CartItemEntity setCartItem(CartEntity cart, ItemEntity item, int count){
        return CartItemEntity.builder()
                .cart(cart)
                .item(item)
                .count(count)
                .build();
    }

    public void addCount(int count){
        this.count += count;
    }

    public void updateCount(int count){
        this.count = count;
    }

}
