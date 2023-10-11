package com.example.shopping.domain.order;

import com.example.shopping.entity.order.OrderEntity;
import com.example.shopping.entity.order.OrderItemEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ToString
@Getter
@NoArgsConstructor
public class OrderDTO {

    @Schema(description = "주문결제번호")
    private Long orderId;

    private LocalDateTime orderDate;

    @Schema(description = "주문결제자")
    private Long orderAdmin;

    @Schema(description = "구매자닉네임")
    private Long orderMember;

    private List<OrderItemDTO> orderItem;

    @Builder
    public OrderDTO(Long orderId, LocalDateTime orderDate, Long orderAdmin, Long orderMember, List<OrderItemDTO> orderItem) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.orderAdmin = orderAdmin;
        this.orderMember = orderMember;
        this.orderItem = orderItem;
    }

    public static OrderDTO createOrder(Long orderAdmin, Long orderMember, List<OrderItemDTO> orderItemList) {
        return OrderDTO.builder()
                .orderDate(LocalDateTime.now())
                .orderAdmin(orderAdmin)
                .orderMember(orderMember)
                .orderItem(orderItemList)
                .build();
    }

    public OrderEntity toEntity(){
        return OrderEntity.builder()
                .orderDate(this.orderDate)
                .orderAdmin(this.orderAdmin)
                .orderMember(this.orderMember)
                .orderId(this.orderId)
                .orderItem(this.orderItem.stream()
                        .map(OrderItemDTO::toEntity).collect(Collectors.toList()))
                .build();
    }

}
