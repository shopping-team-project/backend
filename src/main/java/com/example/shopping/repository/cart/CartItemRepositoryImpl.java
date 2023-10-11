package com.example.shopping.repository.cart;

import com.example.shopping.domain.cart.CartItemDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CartItemRepositoryImpl implements CartItemRepository{

    @Autowired
    private final CartJpaRepository cartJpaRepository;
    @Autowired
    private final CartItemJpaRepository cartItemJpaRepository;

    @Override
    public CartItemDTO save(CartItemDTO cartItem) {
        return null;
    }
}
