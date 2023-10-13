package com.example.shopping.repository.order;

import com.example.shopping.domain.order.OrderDTO;
import com.example.shopping.domain.order.OrderItemDTO;
import java.util.*;

public interface OrderItemRepository {

    OrderItemDTO save(OrderItemDTO orderItem, OrderDTO order);

    OrderItemDTO findByOrderOrderId(Long orderId);

    //OrderItemDTO findByOrderId(Long orderId);

    OrderItemDTO findByItemSeller(Long memberId);
}
