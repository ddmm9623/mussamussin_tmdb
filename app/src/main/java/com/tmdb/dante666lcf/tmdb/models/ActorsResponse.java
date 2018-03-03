package com.tmdb.dante666lcf.tmdb.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mussa on 03.03.2018.
 */

public class ActorsResponse {

    @SerializedName("cast")
    private List<Actors> actors;

    public List<Actors> getActors() {
        return actors;
    }

    public void setActors(List<Actors> actors) {
        this.actors = actors;
    }
}
