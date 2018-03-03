package com.tmdb.dante666lcf.tmdb.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mussa on 28.02.2018.
 */

public class MoviesResponse {
    @SerializedName("results")
    private List<Movies> moviesResponses;


    public List<Movies> getMoviesResponses() {
        return moviesResponses;
    }

    public void getMoviesResponses(List<Movies> nowPlayingMoviesResponses) {
        this.moviesResponses = nowPlayingMoviesResponses;
    }
}
