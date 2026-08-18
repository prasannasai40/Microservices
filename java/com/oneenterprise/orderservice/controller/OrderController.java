package com.oneenterprise.orderservice.controller;

import jakarta.validation.constraints.Positive;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.oneenterprise.orderservice.dto.OrderResponse;
import com.oneenterprise.orderservice.dto.PaymentResponse;
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

        OrderResponse order =
                orderService.getOrderById(orderId);

        return ResponseEntity.ok(order);
    }

    @GetMapping("/{orderId}/payment")
    public ResponseEntity<PaymentResponse> processPayment(
            @PathVariable
            @Positive(message = "Order ID must be greater than zero")
            Integer orderId) {

        PaymentResponse response =
                orderService.processPayment(orderId);

        if ("UNAVAILABLE".equals(
                response.getStatus())) {

            return ResponseEntity
                    .status(503)
                    .body(response);
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/payment/circuit-state")
    public ResponseEntity<String> getCircuitState() {

        return ResponseEntity.ok(
                orderService.getPaymentCircuitState()
        );
    }
}