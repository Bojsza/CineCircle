package com.example.cinecircle.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

public class Movie {
    private int id;
    private String title;
    private String overview;

    @JsonProperty("poster_path")
    private String posterPath;

    @JsonProperty("vote_average")
    private double voteAverage;

    // Új mezők
    private List<Genre> genres = new ArrayList<>();
    private List<String> actors = new ArrayList<>();
    private String releaseYear; // Add this
    private int runtime; // Add this
    
    // Getter és setter metódusok
    
    public String getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(String releaseYear) {
        this.releaseYear = releaseYear;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    // Segédmetódusok a poszter URL létrehozásához
    public String getPosterUrl() {
        return posterPath != null ? "https://image.tmdb.org/t/p/w500" + posterPath : "";
    }

    // Új metódusok a műfajok és szereplők hozzáadásához
    public void addGenre(Genre genre) {
        this.genres.add(genre);
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void addActor(String actor) {
        this.actors.add(actor);
    }

    public List<String> getActors() {
        return actors;
    }
}
