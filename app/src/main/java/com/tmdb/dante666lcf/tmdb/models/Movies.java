package com.tmdb.dante666lcf.tmdb.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by mussa on 28.02.2018.
 */

public class Movies extends RealmObject implements Serializable {

    @SerializedName("id")
    @PrimaryKey
    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("overview")
    private String overview;
    @SerializedName("poster_path")
    private String poster_path;
    @SerializedName("genre_ids")
    private RealmList<Integer> genreIdsModels;
    @SerializedName("backdrop_path")
    private String backdropPath;
    @SerializedName("vote_average")
    private String voteAverage;
    private String popularity;
    @SerializedName("release_date")
    private String releaseDate;


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

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public RealmList<Integer> getGenreIdsModels() {
        return genreIdsModels;
    }

    public void setGenreIdsModels(RealmList<Integer> genreIdsModels) {
        this.genreIdsModels = genreIdsModels;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
}
