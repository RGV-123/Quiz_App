package com.quiz_app.service;

import com.quiz_app.model.User;
import com.quiz_app.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    // ========== SIGN UP ==========
    public ResponseEntity<Map<String, String>> registerUser(User user) {
        Map<String, String> response = new LinkedHashMap<>();

        if (userDao.existsByUsername(user.getUsername())) {
            response.put("status", "error");
            response.put("message", "Username already exists!");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        if (userDao.existsByEmail(user.getEmail())) {
            response.put("status", "error");
            response.put("message", "Email already registered!");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        if (user.getUsername() == null || user.getUsername().trim().isEmpty() ||
            user.getPassword() == null || user.getPassword().trim().isEmpty() ||
            user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            response.put("status", "error");
            response.put("message", "All fields are required!");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        if (user.getRole() == null || user.getRole().trim().isEmpty()) {
            user.setRole("USER");
        }

        userDao.save(user);

        response.put("status", "success");
        response.put("message", "User registered successfully!");
        response.put("username", user.getUsername());
        response.put("role", user.getRole());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // ========== SIGN IN ==========
    public ResponseEntity<Map<String, String>> loginUser(String username, String password) {
        Map<String, String> response = new LinkedHashMap<>();

        Optional<User> userOptional = userDao.findByUsername(username);

        if (userOptional.isEmpty()) {
            response.put("status", "error");
            response.put("message", "User not found!");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        User user = userOptional.get();

        if (!user.getPassword().equals(password)) {
            response.put("status", "error");
            response.put("message", "Wrong password!");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        response.put("status", "success");
        response.put("message", "Login successful!");
        response.put("username", user.getUsername());
        response.put("email", user.getEmail());
        response.put("role", user.getRole());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // ========== GET ALL USERS ==========
    public ResponseEntity<?> getAllUsers() {
        return new ResponseEntity<>(userDao.findAll(), HttpStatus.OK);
    }
}