package com.example.shopping.repository.order;

import com.example.shopping.domain.order.OrderDTO;
import com.example.shopping.domain.order.OrderItemDTO;
import com.example.shopping.entity.order.OrderEntity;
import com.example.shopping.entity.order.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface OrderJpaRepository extends JpaRepository<OrderEntity, Long> {

    List<OrderEntity> findByOrderAdmin(Long memberId);

    List<OrderEntity> findByOrderMember(Long memberId);

}
