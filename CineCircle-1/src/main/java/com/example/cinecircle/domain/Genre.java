package com.example.cinecircle.domain;

public class Genre {
    private int id;  // Műfaj ID
    private String name;  // Műfaj neve

    // Getterek és setterek
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
