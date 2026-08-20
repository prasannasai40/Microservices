package com.oneenterprise.userservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oneenterprise.userservice.dto.UserResponse;
import com.oneenterprise.userservice.service.UserService;

import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(
            @PathVariable
            @Positive(message = "User ID must be greater than zero")
            Integer id) {

        UserResponse user = userService.getUserById(id);

        return ResponseEntity.ok(user);
    }
}
