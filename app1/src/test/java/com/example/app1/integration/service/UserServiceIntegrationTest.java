package com.example.app1.integration.service;

import com.example.app1.integration.annotation.IT;
import com.example.app1.service.CompanyService;
import com.example.app1.service.UserService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@IT
@RequiredArgsConstructor
public class UserServiceIntegrationTest {

    private static final Long USER_ID = 1L;

    private final UserService userService;

    @Test
    void findById() {
        String company = userService.findById(USER_ID);

        Assertions.assertEquals("user_1", company);
    }
}
