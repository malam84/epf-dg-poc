package com.rh.poc.api;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ExternalApiService {

    private final RestTemplate restTemplate = new RestTemplate();
    
    // Mock API URL (Replace with actual external API)
    private static final String EXTERNAL_API_URL = "https://jsonplaceholder.typicode.com/posts/1";

    @Cacheable(value = "mockedApiResponse", key = "#id")
    public String getExternalApiResponse(String id) {
        System.out.println("Fetching data from external API...");
        return restTemplate.getForObject(EXTERNAL_API_URL, String.class);
    }
}