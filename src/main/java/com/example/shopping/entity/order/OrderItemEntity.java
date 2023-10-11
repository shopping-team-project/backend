package com.example.shopping.entity.order;

import com.example.shopping.domain.order.OrderItemDTO;
import com.example.shopping.entity.item.ItemEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity(name = "orderitem")
@Getter
@ToString
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

    private Long itemBuyer;

    int count;

    @Builder
    public OrderItemEntity(Long orderitemId, OrderEntity order, ItemEntity item, int count, Long itemBuyer) {
        this.orderitemId = orderitemId;
        this.item = item;
        this.count = count;
        this.order = order;
        this.itemBuyer = itemBuyer;
    }

    public OrderItemDTO toOrderItemDTO(){
        return OrderItemDTO.builder()
                .itemAmount(this.count)
                .itemBuyer(this.itemBuyer)
                .itemPrice(this.item.getPrice())
                .orderitemId(this.orderitemId)
                .build();
    }

    public static OrderItemEntity setOrderItem(ItemEntity item, Long itemBuyer, int count){
        return OrderItemEntity.builder()
                .item(item)
                .itemBuyer(itemBuyer)
                .count(count)
                .build();
    }

}
