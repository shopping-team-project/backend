package com.example.shopping.controller.order;

import com.example.shopping.domain.order.OrderDTO;
import com.example.shopping.domain.order.OrderItemDTO;
import com.example.shopping.domain.order.OrderMainDTO;
import com.example.shopping.service.order.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    @Autowired
    private final OrderService orderService;

    @PostMapping(value = "/orderItem")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "상품주문", description = "상품을 주문하는 API입니다.")
    public ResponseEntity<?> order(@RequestBody OrderMainDTO order, BindingResult result
                                     ,@AuthenticationPrincipal UserDetails userDetails
    ) {
        OrderDTO orderItem;

        try {
            if (result.hasErrors()) {
                log.error("bindingResult error : " + result.hasErrors());
                return ResponseEntity.badRequest().body(result.getClass().getSimpleName());
            }

            //String email = userDetails.getUsername();
            orderItem = orderService.orderItem(order, "admin123@test.com");

        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok().body(orderItem);
    }


    @GetMapping(value = "/{findEmail}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @Operation(summary = "주문내역조회", description = "주문내역을 조회하는 API입니다.")
    public ResponseEntity<?> getOrders(@PathVariable String findEmail
                                        ,@AuthenticationPrincipal UserDetails userDetails) {
        List<OrderItemDTO> orders = new ArrayList<>();
        try {
            orders = orderService.getOrders(findEmail);

        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().body(orders);
    }

}