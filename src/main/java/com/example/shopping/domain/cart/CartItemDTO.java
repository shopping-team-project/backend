package com.example.shopping.domain.cart;

import com.example.shopping.entity.cart.CartItemEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
public class CartItemDTO {
    private Long cartitemId;
    private String itemName;
    private String itemPrice;
    private Integer itemAmount;
    private String itemStatus;
    private String itemPlace;

    @Builder
    public CartItemDTO(Long cartitemId, String itemName, String itemPrice, Integer itemAmount, String itemStatus, String itemPlace) {
        this.cartitemId = cartitemId;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemAmount = itemAmount;
        this.itemStatus = itemStatus;
        this.itemPlace = itemPlace;
    }

    /*
    public static CartItemDTO addCartItem(ItemDTO item){
        //item정보세팅
    }
    */

    public CartItemEntity toEntity(){
        return CartItemEntity.builder()
                .cartitemId(this.cartitemId)
                .itemAmount(this.itemAmount)
                .itemName(this.itemName)
                .itemPlace(this.itemPlace)
                .itemPrice(this.itemPrice)
                .itemStatus(this.itemStatus)
                .build();
    }


}
