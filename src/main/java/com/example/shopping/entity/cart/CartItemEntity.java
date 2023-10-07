package com.example.shopping.entity.cart;

import com.example.shopping.domain.cart.CartDTO;
import com.example.shopping.domain.cart.CartItemDTO;
import com.example.shopping.entity.Base.BaseTimeEntity;
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

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="cart_id")
    private CartEntity cart;

    @Id
    @GeneratedValue
    @Column(name="cartitem_id")
    private Long cartitemId;

    @Column(name="item_name")
    private String itemName;

    @Column(name="item_price")
    private String itemPrice;

    @Column(name="item_amount")
    private Integer itemAmount;

    @Column(name="item_status")
    private String itemStatus;

    @Column(name="item_place")
    private String itemPlace;

    @Builder
    public CartItemEntity(Long cartitemId, String itemName, String itemPrice, Integer itemAmount, String itemStatus, String itemPlace) {
        this.cartitemId = cartitemId;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemAmount = itemAmount;
        this.itemStatus = itemStatus;
        this.itemPlace = itemPlace;
    }

    public CartItemDTO toDTO(){
        return CartItemDTO.builder()
                .cartitemId(this.cartitemId)
                .itemAmount(this.itemAmount)
                .itemName(this.itemName)
                .itemPlace(this.itemPlace)
                .itemPrice(this.itemPrice)
                .itemStatus(this.itemStatus)
                .build();
    }
}
