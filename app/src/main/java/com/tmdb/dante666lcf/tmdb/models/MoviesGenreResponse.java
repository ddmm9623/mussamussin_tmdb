package com.tmdb.dante666lcf.tmdb.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mussa on 03.03.2018.
 */

public class MoviesGenreResponse {

    @SerializedName("results")
    private List<MoviesGenre> moviesGenreResponse;

    public List<MoviesGenre> getMoviesGenreResponse() {
        return moviesGenreResponse;
    }

    public void setMoviesGenreResponse(List<MoviesGenre> moviesGenreResponse) {
        this.moviesGenreResponse = moviesGenreResponse;
    }
}
