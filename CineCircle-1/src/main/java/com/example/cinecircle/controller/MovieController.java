package com.example.cinecircle.controller;

import com.example.cinecircle.domain.Movie;
import com.example.cinecircle.service.TmdbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class MovieController {

    private final TmdbService tmdbService;

    @Autowired
    public MovieController(TmdbService tmdbService) {
        this.tmdbService = tmdbService;
    }

    // Homepage: List popular movies
    @GetMapping("/")
    public String showHomePage(Model model) {
        System.out.println("Controller: Fetching movies..."); // Debug message
        List<Movie> movies = tmdbService.getPopularMovies();
        System.out.println("Controller: Movies fetched - " + (movies != null ? movies.size() + " items" : "NULL")); // Debug
        model.addAttribute("movies", movies);
        return "index";
    }

    // Movie detail page
    @GetMapping("/movie/{id}")
    public String showMovieDetail(@PathVariable int id, Model model) {
        System.out.println("Controller: Fetching movie details for movie ID: " + id);
        Movie movie = tmdbService.getMovieDetails(id);
        model.addAttribute("movie", movie);
        return "movieDetail";
    }

    // Search movies
    @GetMapping("/movies/search")
    public String searchMovies(@RequestParam("query") String query, Model model) {
        System.out.println("Controller: Searching for movies with query: " + query);
        List<Movie> searchResults = tmdbService.searchMovies(query); // Search method in TmdbService
        model.addAttribute("movies", searchResults);
        model.addAttribute("query", query); // Adding query to display on the page
        return "searchResults"; // Redirect to a new page for search results
    }
}

