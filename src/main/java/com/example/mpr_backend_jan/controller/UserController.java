package com.example.mpr_backend_jan.controller;

import com.example.mpr_backend_jan.model.User;
import com.example.mpr_backend_jan.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Registration endpoint
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        User savedUser = userService.registerUser(user);
        // avoid returning password
        savedUser.setPassword(null);
        return ResponseEntity.ok(savedUser);
    }
}
