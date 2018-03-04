package com.tmdb.dante666lcf.tmdb.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mussa on 04.03.2018.
 */

public class SimilarMoviesResponse {

    @SerializedName("results")
    private List<SimilarMovies> similarMoviesList;


    public List<SimilarMovies> getSimilarMoviesList() {
        return similarMoviesList;
    }

    public void setSimilarMoviesList(List<SimilarMovies> similarMoviesList) {
        this.similarMoviesList = similarMoviesList;
    }
}
