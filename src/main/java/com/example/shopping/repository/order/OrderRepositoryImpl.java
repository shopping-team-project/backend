package com.example.shopping.repository.order;

import com.example.shopping.domain.order.OrderDTO;
import com.example.shopping.domain.order.OrderItemDTO;
import com.example.shopping.entity.order.OrderEntity;
import com.example.shopping.entity.order.OrderItemEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository{

    @Autowired
    private final OrderJpaRepository orderJpaRepository;

    @Override
    public OrderDTO save(OrderDTO order) {

        OrderEntity savedOrder = orderJpaRepository.save(order.toEntity());
        return savedOrder.toDTO();
    }

    @Override
    public List<OrderDTO> findByOrderAdmin(Long memberId) {
        List<OrderEntity> adminOrder = orderJpaRepository.findByOrderAdmin(memberId);
        return adminOrder.stream().map(OrderEntity::toDTO).collect(Collectors.toList());
    }


    @Override
    public List<OrderDTO> findByOrderMember(Long memberId) {
        List<OrderEntity> memberOrder = orderJpaRepository.findByOrderMember(memberId);
        return memberOrder.stream().map(OrderEntity::toDTO).collect(Collectors.toList());
    }

}
