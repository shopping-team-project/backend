package com.example.shopping.repository.order;

import com.example.shopping.domain.order.OrderItemDTO;
import com.example.shopping.entity.order.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemJpaRepository extends JpaRepository<OrderItemEntity, Long> {

    OrderItemEntity findByOrderOrderId(Long orderId);

    OrderItemEntity findByItemSeller(Long sellerId);
}
