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

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

@Component
public class UserServiceClient {

    private final RestTemplate restTemplate;

    @Value("${user-service.base-url}")
    private String userServiceBaseUrl;

    public UserServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Calls User Service to retrieve a user.
     *
     * Resilience:
     * 1. Retry
     * 2. Circuit Breaker
     * 3. Timeout through RestTemplate configuration
     */
    @Retry(name = "userService")
    @CircuitBreaker(
            name = "userService",
            fallbackMethod = "userServiceFallback"
    )
    public UserResponse getUserById(Integer userId) {

        String url =
                userServiceBaseUrl
                + "/api/users/"
                + userId;

        try {

            return restTemplate.getForObject(
                    url,
                    UserResponse.class
            );

        } catch (HttpClientErrorException exception) {

            /*
             * If the user does not exist, this is not a
             * temporary infrastructure failure.
             */
            if (exception.getStatusCode() == HttpStatus.NOT_FOUND) {

                throw new OrderNotFoundException(
                        "User not found with id: " + userId
                );
            }

            throw new UserServiceUnavailableException(
                    "User Service returned an error"
            );

        } catch (RestClientException exception) {

            /*
             * Covers:
             * - Connection failure
             * - Timeout
             * - Other REST communication errors
             */
            throw new UserServiceUnavailableException(
                    "User Service is unavailable"
            );
        }
    }

    /**
     * Circuit breaker fallback.
     */
    public UserResponse userServiceFallback(
            Integer userId,
            Throwable throwable) {

        throw new UserServiceUnavailableException(
                "User Service is temporarily unavailable. "
                + "Please try again later."
        );
    }
}