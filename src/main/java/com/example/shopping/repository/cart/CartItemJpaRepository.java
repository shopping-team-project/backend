package com.example.shopping.repository.cart;

import com.example.shopping.entity.cart.CartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemJpaRepository extends JpaRepository<CartItemEntity, Long> {
}
