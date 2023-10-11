package com.example.shopping.repository.order;

import com.example.shopping.domain.order.OrderDTO;
import java.util.*;

public interface OrderRepository {

    OrderDTO save(OrderDTO order);

}
