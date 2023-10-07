package com.example.shopping.service.order;

import com.example.shopping.domain.order.OrderDTO;
import org.springframework.http.ResponseEntity;

public interface OrderService {

    //TODO - 주문이 들어갈 때 itemstatus, itemstock, memberpoint 작업필요
    public ResponseEntity<?> orderItem(OrderDTO order);

    public OrderDTO getByOrderAdmin(long id);

    public OrderDTO getByOrderMember(long id);

}
