package com.oneenterprise.paymentservice.controller;

import com.oneenterprise.paymentservice.dto.PaymentResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @GetMapping("/{orderId}")
    public ResponseEntity<PaymentResponse> processPayment(
            @PathVariable Integer orderId,
            @RequestParam(defaultValue = "normal") String mode)
            throws InterruptedException {

        if ("slow".equalsIgnoreCase(mode)) {

            Thread.sleep(5000);

            return ResponseEntity.ok(
                    new PaymentResponse(
                            orderId,
                            "SUCCESS",
                            "Payment completed after delay"
                    )
            );
        }

        if ("fail".equalsIgnoreCase(mode)) {

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            new PaymentResponse(
                                    orderId,
                                    "FAILED",
                                    "Payment service deliberately failed"
                            )
                    );
        }

        return ResponseEntity.ok(
                new PaymentResponse(
                        orderId,
                        "SUCCESS",
                        "Payment completed successfully"
                )
        );
    }
}