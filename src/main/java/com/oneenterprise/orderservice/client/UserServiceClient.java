package com.oneenterprise.orderservice.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import com.oneenterprise.orderservice.dto.UserResponse;
import com.oneenterprise.orderservice.exception.UserServiceUnavailableException;

@Component
public class UserServiceClient {

    private final RestClient restClient;

    public UserServiceClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public UserResponse getUserById(Integer userId) {

        try {
            return restClient.get()
                    .uri("/api/users/{id}", userId)
                    .retrieve()
                    .body(UserResponse.class);

        } catch (RestClientException exception) {

            throw new UserServiceUnavailableException(
                    "User Service is unavailable"
            );
        }
    }
}