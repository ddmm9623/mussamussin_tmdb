package com.tmdb.dante666lcf.tmdb.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mussa on 01.03.2018.
 */

public class GenresResponse {

    @SerializedName("genres")
    private List<Genres> genres;

    public List<Genres> getGenres() {
        return genres;
    }

    public void setGenres(List<Genres> genres) {
        this.genres = genres;
    }
}
