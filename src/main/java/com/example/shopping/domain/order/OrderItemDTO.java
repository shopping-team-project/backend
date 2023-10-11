package com.example.shopping.domain.order;

import com.example.shopping.entity.order.OrderItemEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.*;

@ToString
@Getter
@NoArgsConstructor
public class OrderItemDTO {
    @Schema(description = "주문결제일자")
    private LocalDateTime orderDate;
    private Long orderitemId;
    private int itemPrice;
    private Integer itemAmount;
    private Long itemBuyer;
    private Long itemId;

    @Builder
    public OrderItemDTO(Long orderitemId, int itemPrice, Integer itemAmount, Long itemBuyer) {
        this.orderitemId = orderitemId;
        this.itemPrice = itemPrice;
        this.itemAmount = itemAmount;
        this.itemBuyer = itemBuyer;
    }

    public OrderItemEntity toEntity() {
        return OrderItemEntity.builder()
                .count(this.itemAmount)
                .itemBuyer(this.itemBuyer)
                .orderitemId(this.orderitemId)
                .build();
    }

}
