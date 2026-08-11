package com.oneenterprise.orderservice.controller;

import jakarta.validation.constraints.Positive;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oneenterprise.orderservice.dto.OrderResponse;
import com.oneenterprise.orderservice.service.OrderService;

@RestController
@RequestMapping("/api/orders")
@Validated
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(
            @PathVariable
            @Positive(message = "Order ID must be greater than zero")
            Integer orderId) {

        OrderResponse order = orderService.getOrderById(orderId);

        return ResponseEntity.ok(order);
    }
}