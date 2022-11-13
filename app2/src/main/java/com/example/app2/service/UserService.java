package com.example.app2.service;

import org.springframework.stereotype.Service;

@Service
public class UserService {

    public String findById(Long id) {
        return "user_" + id;
    }
}
