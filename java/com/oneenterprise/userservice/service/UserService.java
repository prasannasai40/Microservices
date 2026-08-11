package com.oneenterprise.userservice.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.oneenterprise.userservice.dto.UserResponse;
import com.oneenterprise.userservice.exception.UserNotFoundException;

@Service
public class UserService {

    private final Map<Integer, UserResponse> users = new HashMap<>();

    public UserService() {

        users.put(1, new UserResponse(
                1,
                "Sai",
                "sai@example.com"
        ));

        users.put(2, new UserResponse(
                2,
                "Rahul",
                "rahul@example.com"
        ));

        users.put(3, new UserResponse(
                3,
                "Priya",
                "priya@example.com"
        ));
    }

    public UserResponse getUserById(Integer id) {

        UserResponse user = users.get(id);

        if (user == null) {
            throw new UserNotFoundException(
                    "User not found with id: " + id
            );
        }

        return user;
    }
}