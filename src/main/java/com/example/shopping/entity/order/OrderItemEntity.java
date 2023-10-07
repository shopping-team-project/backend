package com.example.shopping.entity.order;

import com.example.shopping.domain.order.OrderItemDTO;
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

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="order_id")
    private OrderEntity order;

    @Id
    @GeneratedValue
    @Column(name="orderitem_id")
    private Long orderitemId;

    @Column(name="item_price")
    private String itemPrice;

    @Column(name="item_amount")
    private Integer itemAmount;

    @Column(name="item_buyer")
    private Long itemBuyer;

    @Builder
    public OrderItemEntity(Long orderitemId, String itemPrice, Integer itemAmount, Long itemBuyer) {
        this.orderitemId = orderitemId;
        this.itemPrice = itemPrice;
        this.itemAmount = itemAmount;
        this.itemBuyer = itemBuyer;
    }

    public OrderItemDTO toDTO(){
        return OrderItemDTO.builder()
                .itemAmount(this.itemAmount)
                .itemBuyer(this.itemBuyer)
                .itemPrice(this.itemPrice)
                .orderitemId(this.orderitemId)
                .build();
    }



}
