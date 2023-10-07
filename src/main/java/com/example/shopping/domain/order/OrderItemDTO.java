package com.example.shopping.domain.order;

import com.example.shopping.entity.order.OrderItemEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;

@ToString
@Getter
@NoArgsConstructor
public class OrderItemDTO {
    private Long orderitemId;
    private String itemPrice;
    private Integer itemAmount;
    private Long itemBuyer;

    @Builder
    public OrderItemDTO(Long orderitemId, String itemPrice, Integer itemAmount, Long itemBuyer) {
        this.orderitemId = orderitemId;
        this.itemPrice = itemPrice;
        this.itemAmount = itemAmount;
        this.itemBuyer = itemBuyer;
    }

    public OrderItemEntity toEntity() {
        return OrderItemEntity.builder()
                .itemAmount(this.itemAmount)
                .itemBuyer(this.itemBuyer)
                .itemPrice(this.itemPrice)
                .orderitemId(this.orderitemId)
                .build();
    }
}
