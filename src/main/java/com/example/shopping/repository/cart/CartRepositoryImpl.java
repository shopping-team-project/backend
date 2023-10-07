package com.example.shopping.repository.cart;

import com.example.shopping.domain.cart.CartDTO;
import com.example.shopping.entity.member.MemberEntity;
import com.example.shopping.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CartRepositoryImpl implements CartRepository{

    private final CartJpaRepository cartJpaRepository;
    private final MemberRepository memberRepository;

    @Override
    public CartDTO save(long id, CartDTO cart) {
        //TODO - 삭제 수정 추가 전부 영속성 이용하여 작업
        Optional<MemberEntity> mem = memberRepository.findById(id);

        if(mem.isPresent()){
            return cartJpaRepository.save(cart.toEntity(mem.get())).toDTO();
        }
        else{
            return null;
        }
    }

    @Override
    public Optional<CartDTO> findByMemberId(long mbrId) {
        return cartJpaRepository.findByMeberId(mbrId);
    }
}
