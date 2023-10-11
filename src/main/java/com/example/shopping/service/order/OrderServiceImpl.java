package com.example.shopping.service.order;

import com.example.shopping.domain.Item.ItemSellStatus;
import com.example.shopping.domain.order.OrderDTO;
import com.example.shopping.domain.order.OrderItemDTO;
import com.example.shopping.domain.order.OrderMainDTO;
import com.example.shopping.entity.item.ItemEntity;
import com.example.shopping.entity.member.MemberEntity;
import com.example.shopping.entity.order.OrderItemEntity;
import com.example.shopping.repository.item.ItemRepository;
import com.example.shopping.repository.member.MemberRepository;
import com.example.shopping.repository.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements  OrderService{

    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public OrderDTO orderItem(OrderMainDTO order, String adminEmail){

        //Order Table Insert
        Long memberId = memberRepository.findByEmail(order.getMbrEmail()).getMemberId();
        Long adminId = memberRepository.findByEmail(adminEmail).getMemberId();

        ItemEntity item = itemRepository.findById(order.getItemId()).orElseThrow();

        List<OrderItemDTO> itemList = new ArrayList<>();

        OrderItemEntity orderItem = OrderItemEntity.setOrderItem(item, memberId, order.getCount());
        itemList.add(orderItem.toOrderItemDTO());

        OrderDTO orderInfo = OrderDTO.createOrder(adminId, memberId, itemList);
        orderRepository.save(orderInfo);

        //Member, Item테이블 변경
        MemberEntity member = memberRepository.findByEmail(order.getMbrEmail());

        ItemEntity updateItem = itemRepository.findById(order.getItemId()).orElseThrow();
        updateItem.itemSell(order.getCount(), ItemSellStatus.SOLD_OUT);
        itemRepository.save(updateItem);

        return orderInfo;
    }



}
