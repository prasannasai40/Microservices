package com.oneenterprise.orderservice.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.oneenterprise.orderservice.dto.UserResponse;
import com.oneenterprise.orderservice.exception.OrderNotFoundException;
import com.oneenterprise.orderservice.exception.UserServiceUnavailableException;

@Component
public class UserServiceClient {

    private final RestTemplate restTemplate;

    @Value("${user-service.base-url}")
    private String userServiceBaseUrl;

    public UserServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public UserResponse getUserById(Integer userId) {

        String url = userServiceBaseUrl + "/api/users/" + userId;

        try {

            return restTemplate.getForObject(
                    url,
                    UserResponse.class
            );

        } catch (HttpClientErrorException exception) {

            if (exception.getStatusCode() == HttpStatus.NOT_FOUND) {

                throw new OrderNotFoundException(
                        "User not found with id: " + userId
                );
            }

            throw new UserServiceUnavailableException(
                    "User Service returned an error"
            );

        } catch (RestClientException exception) {

            throw new UserServiceUnavailableException(
                    "User Service is unavailable"
            );
        }
    }
}