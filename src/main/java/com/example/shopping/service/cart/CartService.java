package com.example.shopping.service.cart;

import com.example.shopping.domain.Item.ItemDTO;
import com.example.shopping.domain.cart.CartDTO;
import com.example.shopping.domain.cart.CartItemDTO;
import com.example.shopping.domain.order.OrderDTO;

import org.springframework.http.ResponseEntity;
import java.util.*;

public interface CartService {

    public ResponseEntity<?> addCart(CartDTO cart, ItemDTO item);

    public ResponseEntity<?> deleteCart(long id, CartDTO cart);

    public ResponseEntity<?> deleteAllCart(long id);

    public ResponseEntity<?> updateCart(long id, CartItemDTO cartItem);

    //TODO - 장바구니 주문하기 : itemstatus만 작업
    public ResponseEntity<?> orderCart(long itemId);

    public CartDTO getByMemberId(long mbrId);
}
