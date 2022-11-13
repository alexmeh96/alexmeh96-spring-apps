package com.example.app2.integration.database.repository;

import com.example.app2.entity.User;
import com.example.app2.integration.IntegrationBaseTest;
import com.example.app2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@RequiredArgsConstructor
public class UserRepositoryTest extends IntegrationBaseTest {

    private final UserRepository userRepository;

    @Test
    void findById() {
        User user = userRepository.getById(1L);
        assertNotNull(user);
    }

}
