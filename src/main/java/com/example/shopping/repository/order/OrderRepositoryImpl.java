package com.example.shopping.repository.order;

import com.example.shopping.domain.order.OrderDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository{

    private final OrderJpaRepository orderJpaRepository;

    @Override
    public OrderDTO save(OrderDTO order) {

        return orderJpaRepository.save(order.toEntity()).toDTO();
    }

    @Override
    public Optional<OrderDTO> findByOrderAdmin(String admin) {
        return orderJpaRepository.findByOrderAdmin(admin);
    }

    @Override
    public Optional<OrderDTO> findByOrderMember(String mbrId) {
        return orderJpaRepository.findByOrderMember(mbrId);
    }
}
