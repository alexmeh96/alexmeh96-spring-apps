package com.example.app1.integration;

import org.springframework.boot.test.context.TestConfiguration;

// нужно для кэширования контекста, при добавлении в него моков
@TestConfiguration
public class TestApplicationRunner {
}
