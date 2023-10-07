package com.example.shopping.repository.cart;

import com.example.shopping.domain.cart.CartDTO;
import org.springframework.stereotype.Repository;
import java.util.*;

public interface CartRepository {

    CartDTO save(long id, CartDTO cart);

    Optional<CartDTO> findByMemberId(long mbrId);
}
