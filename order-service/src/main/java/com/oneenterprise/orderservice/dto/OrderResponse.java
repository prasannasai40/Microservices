package com.oneenterprise.orderservice.dto;

public class OrderResponse {

    private Integer orderId;
    private Integer userId;
    private String userName;
    private String userEmail;

    public OrderResponse() {
    }

    public OrderResponse(Integer orderId, Integer userId,
                         String userName, String userEmail) {
        this.orderId = orderId;
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}