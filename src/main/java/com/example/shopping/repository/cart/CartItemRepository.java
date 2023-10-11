package com.example.shopping.repository.cart;

import com.example.shopping.domain.cart.CartItemDTO;
import com.example.shopping.entity.cart.CartItemEntity;

public interface CartItemRepository {

    CartItemDTO save(CartItemDTO cartItem);

}
