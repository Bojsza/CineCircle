package com.example.cinecircle.service;

import com.example.cinecircle.config.TmdbConfig;
import com.example.cinecircle.domain.Genre;
import com.example.cinecircle.domain.Movie;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class TmdbService {

    private final TmdbConfig tmdbConfig;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private static final Logger logger = LoggerFactory.getLogger(TmdbService.class);

    public TmdbService(TmdbConfig tmdbConfig, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.tmdbConfig = tmdbConfig;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    // Get list of popular movies
    public List<Movie> getPopularMovies() {
        String url = UriComponentsBuilder.fromHttpUrl(tmdbConfig.getApiUrl() + "/movie/popular")
                .queryParam("api_key", tmdbConfig.getApiKey())
                .queryParam("language", "en-US")
                .toUriString();

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("API request failed with status: " + response.getStatusCode());
            }

            String jsonResponse = response.getBody();
            JsonNode root = objectMapper.readTree(jsonResponse);
            JsonNode results = root.path("results");

            List<Movie> movies = new ArrayList<>();
            for (JsonNode node : results) {
                Movie movie = objectMapper.treeToValue(node, Movie.class);
                movies.add(movie);
            }

            return movies;
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch popular movies", e);
        }
    }

    // Search movies
    public List<Movie> searchMovies(String query) {
        String url = UriComponentsBuilder.fromHttpUrl(tmdbConfig.getApiUrl() + "/search/movie")
                .queryParam("api_key", tmdbConfig.getApiKey())
                .queryParam("query", query) // Passing the query string here
                .queryParam("language", "en-US")
                .toUriString();

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("API request failed with status: " + response.getStatusCode());
            }

            String jsonResponse = response.getBody();
            JsonNode root = objectMapper.readTree(jsonResponse);
            JsonNode results = root.path("results");

            List<Movie> movies = new ArrayList<>();
            for (JsonNode node : results) {
                Movie movie = objectMapper.treeToValue(node, Movie.class);
                movies.add(movie);
            }

            return movies;
        } catch (Exception e) {
            throw new RuntimeException("Failed to search movies", e);
        }
    }

    // Get movie details by ID
    public Movie getMovieDetails(int id) {
        String url = UriComponentsBuilder.fromHttpUrl(tmdbConfig.getApiUrl() + "/movie/" + id)
                .queryParam("api_key", tmdbConfig.getApiKey())
                .queryParam("language", "en-US")
                .toUriString();

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            logger.info("API Response: {}", response.getBody());
            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("API request failed: " + response.getStatusCode());
            }

            String jsonResponse = response.getBody();
            JsonNode root = objectMapper.readTree(jsonResponse);

            Movie movie = objectMapper.treeToValue(root, Movie.class);

            // Műfajok kezelése
            JsonNode genresNode = root.path("genres");
            for (JsonNode genreNode : genresNode) {
                Genre genre = new Genre();
                genre.setName(genreNode.path("name").asText());
                movie.addGenre(genre);
            }

            // Release year (Kiadasi év)
            String releaseDate = root.path("release_date").asText();
            if (releaseDate != null && !releaseDate.isEmpty()) {
                movie.setReleaseYear(releaseDate.split("-")[0]); // Extract year from release date (YYYY-MM-DD)
            }

            // Runtime (Futásidő)
            movie.setRuntime(root.path("runtime").asInt());

            // Szereplők lekérése
            String castUrl = UriComponentsBuilder.fromHttpUrl(tmdbConfig.getApiUrl() + "/movie/" + id + "/credits")
                    .queryParam("api_key", tmdbConfig.getApiKey())
                    .toUriString();

            ResponseEntity<String> castResponse = restTemplate.getForEntity(castUrl, String.class);
            if (castResponse.getStatusCode().is2xxSuccessful()) {
                JsonNode castRoot = objectMapper.readTree(castResponse.getBody());
                JsonNode cast = castRoot.path("cast");

                int count = 0;
                for (JsonNode actorNode : cast) {
                    if (count >= 15) break;
                    String actorName = actorNode.path("name").asText();
                    movie.addActor(actorName);
                    count++;
                }
            }
            return movie;
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch movie details", e);
        }
    }


}

