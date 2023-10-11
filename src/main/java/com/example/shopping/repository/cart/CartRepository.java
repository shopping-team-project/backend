package com.example.shopping.repository.cart;

import com.example.shopping.domain.cart.CartDTO;
import com.example.shopping.entity.member.MemberEntity;
import org.springframework.stereotype.Repository;
import java.util.*;

public interface CartRepository {

    CartDTO save(CartDTO cart);

    //Optional<CartDTO> findByMemberId(long mbrId);
}
