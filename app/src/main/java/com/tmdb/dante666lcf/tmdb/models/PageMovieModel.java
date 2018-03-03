package com.tmdb.dante666lcf.tmdb.models;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by mussa on 02.03.2018.
 */

public class PageMovieModel extends RealmObject {

    @PrimaryKey
    private int id;
    @SerializedName("backdrop_path")
    private String backdropPath;
    @SerializedName("poster_path")
    private String posterPath;
    @SerializedName("genres")
    private RealmList<Genres> genres;
    @SerializedName("overview")
    private String overview;
    @SerializedName("runtime")
    private int runtime;
    @SerializedName("title")
    private String title;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public RealmList<Genres> getGenres() {
        return genres;
    }

    public void setGenres(RealmList<Genres> genres) {
        this.genres = genres;
    }
}
