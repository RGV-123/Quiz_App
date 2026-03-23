package com.quiz_app.controller;

import com.quiz_app.model.User;
import com.quiz_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private UserService userService;

    // SIGN UP
    @PostMapping("signup")
    public ResponseEntity<Map<String, String>> signUp(@RequestBody User user) {
        return userService.registerUser(user);
    }

    // SIGN IN
    @PostMapping("signin")
    public ResponseEntity<Map<String, String>> signIn(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");
        return userService.loginUser(username, password);
    }

    // GET ALL USERS (Admin)
    @GetMapping("users")
    public ResponseEntity<?> getAllUsers() {
        return userService.getAllUsers();
    }
}
