package com.example.shopping.service.cart;

import com.example.shopping.repository.cart.CartRepository;
import com.example.shopping.repository.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartServiceImpl{

    private final ItemRepository itemRepository;
    private final CartRepository cartRepository;

}
