package com.example.app2.service;

import org.springframework.stereotype.Service;

@Service
public class CompanyService {

    public String findById(Long id) {
        return "company_" + id;
    }
}
