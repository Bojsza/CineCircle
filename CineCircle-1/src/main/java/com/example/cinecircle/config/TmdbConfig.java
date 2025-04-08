package com.example.cinecircle.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TmdbConfig {
    @Value("${tmdb.api.key}")
    private String apiKey;
    
    @Value("${tmdb.api.url}")
    private String apiUrl;
    
    public String getApiKey() {
        System.out.println("Config: API Key - " + (apiKey != null ? "LOADED" : "MISSING")); // Debug
        return apiKey;
    }
    
    public String getApiUrl() {
        return apiUrl;
    }
}