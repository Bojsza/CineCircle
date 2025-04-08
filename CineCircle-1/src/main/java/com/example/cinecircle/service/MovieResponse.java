package com.example.cinecircle.service;

import java.util.List;

import com.example.cinecircle.domain.Movie;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieResponse {
    private List<Movie> results;
    private int page;
    private int total_pages;
    private int total_results;

    // getters and setters
}