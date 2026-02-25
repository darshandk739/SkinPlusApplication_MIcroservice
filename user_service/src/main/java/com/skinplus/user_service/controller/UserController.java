package com.skinplus.user_service.controller;

import org.springframework.web.bind.annotation.*;
import com.skinplus.user_service.entity.User;
import com.skinplus.user_service.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public String test() {
        return "JWT WORKING";
    }

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return service.register(user);
    }
}