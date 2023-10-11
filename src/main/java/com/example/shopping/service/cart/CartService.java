package com.example.shopping.service.cart;

import com.example.shopping.domain.Item.ItemDTO;
import com.example.shopping.domain.cart.CartDTO;
import com.example.shopping.domain.cart.CartItemDTO;
import com.example.shopping.domain.order.OrderDTO;

import org.springframework.http.ResponseEntity;
import java.util.*;

public interface CartService {

    CartDTO addCart(CartDTO cart, ItemDTO item);

    CartDTO getByMemberId(long mbrId);
}
