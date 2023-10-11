package com.example.shopping.service.cart;

import com.example.shopping.domain.cart.CartDTO;
import com.example.shopping.domain.cart.CartDetailDTO;
import com.example.shopping.domain.cart.CartItemDTO;
import com.example.shopping.entity.cart.CartEntity;
import com.example.shopping.entity.cart.CartItemEntity;
import com.example.shopping.entity.item.ItemEntity;
import com.example.shopping.entity.member.MemberEntity;
import com.example.shopping.repository.cart.CartItemRepository;
import com.example.shopping.repository.cart.CartRepository;
import com.example.shopping.repository.item.ItemRepository;
import com.example.shopping.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
@Service
@RequiredArgsConstructor
public class CartServiceImpl{

    @Autowired
    private final ItemRepository itemRepository;
    @Autowired
    private final CartRepository cartRepository;
    @Autowired
    private final CartItemRepository cartItemRepository;
    @Autowired
    private final MemberRepository memberRepository;

    /* CartEntity쪽 에러때문에 주석처리
    public CartDTO addCart(CartItemDTO cartItem, String mbrEmail) {

        CartDTO newCart = new CartDTO();
        CartItemEntity itemDetail = new CartItemEntity();

        MemberEntity member = memberRepository.findByEmail(mbrEmail);

        ItemEntity item = itemRepository.findById(cartItem.getItemId()).orElseThrow();


        Optional<CartDTO> cart = cartRepository.findByMemberId(member.getMemberId());

        if (cart.isPresent()) {
            //장바구니 아이템 체크
            CartItemDTO savedCartItem = cartItemRepository.findByCartIdAndItemId(newCart.getCartId(), cartItem.getItemId());
            CartEntity cartEntity = cart.get().toEntity(member);
            //기존에 있는 아이템 추가 시
            if (savedCartItem != null) {
                //수량증가
                savedCartItem.addCount(cartItem.getCount());
            } else {
                //아니라면 CartItem Insert
                itemDetail = CartItemEntity.setCartItem(cartEntity, item, cartItem.getCount());
                cartItemRepository.save(itemDetail.toDTO());
            }
        }
        //기존에 생성된 장바구니 없다면 생성
        else {
            newCart = cartRepository.save(newCart.createCart(member));

            //CartItem Insert
            itemDetail = CartItemEntity.setCartItem(newCart.toEntity(member), item, cartItem.getCount());
            cartItemRepository.save(itemDetail.toDTO());
        }


        return itemDetail.getCart().toDTO();
    }

     */
}
