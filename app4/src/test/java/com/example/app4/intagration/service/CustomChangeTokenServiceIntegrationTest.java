package com.example.app4.intagration.service;

import com.example.app4.intagration.annotation.IT;
import com.example.app4.service.ApiService;
import com.example.app4.service.CustomChangeTokenService;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@IT
public class CustomChangeTokenServiceIntegrationTest {

    @Autowired
    private CustomChangeTokenService tokenService;

    public static MockWebServer mockServer;

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("api.baseUrl", () -> mockServer.url("/").toString());
    }

    @BeforeAll
    static void setUp() throws IOException {
        mockServer = new MockWebServer();
        mockServer.start();

        mockServer.enqueue(new MockResponse()
                .setBody("CUSTOM_TOKEN")
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockServer.shutdown();
    }

    @Test
    void test1() throws InterruptedException {
        mockServer.enqueue(new MockResponse()
                .setBody("Hello world")
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE));

        String fromApi = tokenService.getFromApi().block();
        assertEquals("Hello world", fromApi);

        int requestCount = mockServer.getRequestCount();
        assertEquals(2, requestCount);

        RecordedRequest recordedRequest1 = mockServer.takeRequest();
        assertEquals("POST", recordedRequest1.getMethod());
        assertEquals("/token", recordedRequest1.getPath());

        RecordedRequest recordedRequest2 = mockServer.takeRequest();
        assertEquals("GET", recordedRequest2.getMethod());
        assertEquals("/", recordedRequest2.getPath());
    }

    @Test
    void test2() throws InterruptedException {
        mockServer.enqueue(new MockResponse()
                .setBody("Hello world from test2")
                .addHeader("Content-Type", "application/json"));

        String fromApi = tokenService.getFromApi().block();
        assertEquals("Hello world from test2", fromApi);

        RecordedRequest recordedRequest2 = mockServer.takeRequest();
        assertEquals("GET", recordedRequest2.getMethod());
        assertEquals("/", recordedRequest2.getPath());
    }
}
