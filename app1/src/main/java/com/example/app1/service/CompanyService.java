package com.example.app1.service;

import org.springframework.stereotype.Service;

@Service
public class CompanyService {

    public String findById(Long id) {
        return "company_" + id;
    }
}
