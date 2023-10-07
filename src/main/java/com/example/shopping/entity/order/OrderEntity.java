package com.example.shopping.entity.order;

import com.example.shopping.domain.order.OrderDTO;
import com.example.shopping.entity.Base.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Entity(name = "order")
@Getter
@ToString
@NoArgsConstructor
public class OrderEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name="order_id")
    private Long orderId;

    @Column(name="order_date")
    private LocalDateTime orderDate;

    @Column(name="order_admin")
    private Long orderAdmin;

    @Column(name="order_member")
    private Long orderMember;

    @OneToMany(mappedBy="order")
    private List<OrderItemEntity> orderItem;

    @Builder

    public OrderEntity(Long orderId, LocalDateTime orderDate, Long orderAdmin, Long orderMember, List<OrderItemEntity> orderItem) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.orderAdmin = orderAdmin;
        this.orderMember = orderMember;
        this.orderItem = orderItem;
    }

    public OrderDTO toDTO(){
        return OrderDTO.builder()
                .orderId(this.orderId)
                .orderDate(this.orderDate)
                .orderAdmin(this.orderAdmin)
                .orderItem(this.orderItem.stream()
                        .map(OrderItemEntity::toDTO).collect(Collectors.toList()))
                .build();
    }
}
