package com.example.shopping.domain.cart;

import com.example.shopping.entity.item.ItemEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor

public class CartDetailDTO {

    private Long cartItemId;

    private String itemName;

    private int price;

    private int count;

    private String imgUrl;

    public CartDetailDTO(Long cartItemId, String name, int price, int count, String imgUrl){
        this.cartItemId = cartItemId;
        this.itemName = name;
        this.price = price;
        this.count = count;
        this.imgUrl = imgUrl;
    }

}
