package com.oneenterprise.orderservice.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.oneenterprise.orderservice.client.PaymentServiceClient;
import com.oneenterprise.orderservice.client.UserServiceClient;
import com.oneenterprise.orderservice.dto.OrderResponse;
import com.oneenterprise.orderservice.dto.PaymentResponse;
import com.oneenterprise.orderservice.dto.UserResponse;
import com.oneenterprise.orderservice.exception.OrderNotFoundException;

@Service
public class OrderService {

    private final UserServiceClient userServiceClient;

    private final PaymentServiceClient paymentServiceClient;

    private final Map<Integer, Integer> orders =
            new HashMap<>();

    public OrderService(
            UserServiceClient userServiceClient,
            PaymentServiceClient paymentServiceClient) {

        this.userServiceClient = userServiceClient;

        this.paymentServiceClient =
                paymentServiceClient;

        orders.put(101, 1);
        orders.put(102, 2);
        orders.put(103, 3);
    }

    public OrderResponse getOrderById(Integer orderId) {

        Integer userId = orders.get(orderId);

        if (userId == null) {

            throw new OrderNotFoundException(
                    "Order not found with id: "
                    + orderId
            );
        }

        UserResponse user =
                userServiceClient.getUserById(userId);

        return new OrderResponse(
                orderId,
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public PaymentResponse processPayment(
            Integer orderId) {

        Integer userId = orders.get(orderId);

        if (userId == null) {

            throw new OrderNotFoundException(
                    "Order not found with id: "
                    + orderId
            );
        }

        return paymentServiceClient
                .processPayment(orderId);
    }

    public String getPaymentCircuitState() {

        return paymentServiceClient
                .getCircuitState();
    }
}