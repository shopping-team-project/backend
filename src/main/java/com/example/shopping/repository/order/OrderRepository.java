package com.example.shopping.repository.order;

import com.example.shopping.domain.order.OrderDTO;
import java.util.*;

public interface OrderRepository {

    OrderDTO save(OrderDTO order);

    Optional<OrderDTO> findByOrderAdmin(String admin);

    Optional<OrderDTO> findByOrderMember(String mbrId);
}
