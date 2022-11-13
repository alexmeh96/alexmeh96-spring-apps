package com.example.app2.integration.service;

import com.example.app2.integration.annotation.IT;
import com.example.app2.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@IT
@RequiredArgsConstructor
public class CompanyServiceIntegrationTest {

    private static final Long COMPANY_ID = 1L;

    private final CompanyService companyService;

    @Test
    void findById() {
        String company = companyService.findById(COMPANY_ID);

        Assertions.assertEquals("company_1", company);
    }
}
