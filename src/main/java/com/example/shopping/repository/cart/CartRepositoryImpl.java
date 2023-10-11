package com.example.shopping.repository.cart;

import com.example.shopping.domain.cart.CartDTO;
import com.example.shopping.entity.member.MemberEntity;
import com.example.shopping.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CartRepositoryImpl implements CartRepository{

    @Autowired
    private final CartJpaRepository cartJpaRepository;
    @Autowired
    private final MemberRepository memberRepository;

    //CartEntity에러때문에 주석처리
    @Override
    public CartDTO save(CartDTO cart) {
        //TODO - 삭제 수정 추가 전부 영속성 이용하여 작업
        //MemberEntity mem = memberRepository.findByEmail(cart.getMember().getEmail());

        //return cartJpaRepository.save(cart.toEntity(mem)).toDTO();
        return null;
    }

    /*
    @Override
    public Optional<CartDTO> findByMemberId(long mbrId) {
        return cartJpaRepository.findByMemberId(mbrId);
    }

     */
}
