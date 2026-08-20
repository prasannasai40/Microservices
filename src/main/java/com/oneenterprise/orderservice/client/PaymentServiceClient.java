package com.oneenterprise.orderservice.client;

import com.oneenterprise.orderservice.config.CircuitBreaker;
import com.oneenterprise.orderservice.dto.PaymentResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class PaymentServiceClient {

    private final RestTemplate restTemplate;

    private final CircuitBreaker circuitBreaker;

    @Value("${payment-service.base-url}")
    private String paymentServiceBaseUrl;

    private final int maxRetries = 3;

    public PaymentServiceClient(
            RestTemplate restTemplate,
            CircuitBreaker circuitBreaker) {

        this.restTemplate = restTemplate;
        this.circuitBreaker = circuitBreaker;
    }

    public PaymentResponse processPayment(Integer orderId) {

        /*
         * CIRCUIT BREAKER
         */
        if (!circuitBreaker.allowRequest()) {

            return fallback(orderId);
        }

        Exception lastException = null;

        /*
         * RETRY
         */
        for (int attempt = 1;
             attempt <= maxRetries;
             attempt++) {

            try {

                String url =
                        paymentServiceBaseUrl
                        + "/api/payments/"
                        + orderId;

                PaymentResponse response =
                        restTemplate.getForObject(
                                url,
                                PaymentResponse.class
                        );

                /*
                 * SUCCESS
                 */
                circuitBreaker.recordSuccess();

                return response;

            } catch (RestClientException exception) {

                lastException = exception;

                System.out.println(
                        "Payment Service attempt "
                        + attempt
                        + " failed: "
                        + exception.getMessage()
                );

                if (attempt < maxRetries) {

                    try {

                        Thread.sleep(300);

                    } catch (InterruptedException interruptedException) {

                        Thread.currentThread().interrupt();

                        break;
                    }
                }
            }
        }

        /*
         * ALL RETRIES FAILED
         */
        circuitBreaker.recordFailure();

        System.out.println(
                "Payment Service failed after "
                + maxRetries
                + " attempts."
        );

        return fallback(orderId);
    }

    private PaymentResponse fallback(Integer orderId) {

        System.out.println(
                "Payment fallback executed for order: "
                + orderId
        );

        return new PaymentResponse(
                orderId,
                "UNAVAILABLE",
                "Payment Service is temporarily unavailable. Please try again later."
        );
    }

    public String getCircuitState() {

        return circuitBreaker.getState();
    }
}
