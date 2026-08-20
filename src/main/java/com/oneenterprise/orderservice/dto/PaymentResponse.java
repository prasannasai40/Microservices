package com.oneenterprise.orderservice.dto;

public class PaymentResponse {

    private Integer orderId;
    private String status;
    private String message;

    public PaymentResponse() {
    }

    public PaymentResponse(
            Integer orderId,
            String status,
            String message) {

        this.orderId = orderId;
        this.status = status;
        this.message = message;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}