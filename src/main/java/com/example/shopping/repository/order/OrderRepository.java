package com.example.shopping.repository.order;

import com.example.shopping.domain.order.OrderDTO;
import com.example.shopping.domain.order.OrderItemDTO;

import java.util.*;

public interface OrderRepository {

    OrderDTO save(OrderDTO order);

    List<OrderDTO> findByOrderAdmin(Long memberId);

    List<OrderDTO> findByOrderMember(Long memberId);

}
