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
}
