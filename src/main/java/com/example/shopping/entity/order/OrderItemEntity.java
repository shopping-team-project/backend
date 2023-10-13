package com.example.shopping.entity.order;

import com.example.shopping.domain.Item.ItemDTO;
import com.example.shopping.domain.order.OrderItemDTO;
import com.example.shopping.entity.item.ItemEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "orderitem")
@Getter
@ToString(exclude = "order")
@NoArgsConstructor
public class OrderItemEntity {

    @Id
    @GeneratedValue
    @Column(name="orderitem_id")
    private Long orderitemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private ItemEntity item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    @Column(name="item_buyer")
    private Long itemBuyer;
    @Column(name="item_seller")
    private Long itemSeller;

    int count;

    @Builder
    public OrderItemEntity(Long orderitemId, ItemEntity item, int count, Long itemBuyer, Long itemSeller, OrderEntity order) {
        this.orderitemId = orderitemId;
        this.item = item;
        this.count = count;
        this.order = order;
        this.itemBuyer = itemBuyer;
        this.itemSeller =itemSeller;
    }

    public OrderItemDTO toOrderItemDTO(){
        return OrderItemDTO.builder()
                .item(item.toItemInfoDTO())
                .itemAmount(this.count)
                .itemBuyer(this.itemBuyer)
                .itemSeller(this.itemSeller)
                .itemPrice(this.item.getPrice())
                .orderitemId(this.orderitemId)
                .itemId(this.item.getItemId())
                .orderDate(order==null? LocalDateTime.now():order.getOrderDate())
                .build();
    }

    public static OrderItemEntity setOrderItem(ItemEntity item, Long itemBuyer, Long itemSeller, int count){
        return OrderItemEntity.builder()
                .item(item)
                .itemBuyer(itemBuyer)
                .itemSeller(itemSeller)
                .count(count)
                .build();
    }



}
