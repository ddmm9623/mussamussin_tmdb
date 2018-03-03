package com.tmdb.dante666lcf.tmdb.serializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.tmdb.dante666lcf.tmdb.models.Genres;

import java.lang.reflect.Type;

/**
 * Created by mussa on 01.03.2018.
 */

public class MovieGenresDeserializer implements JsonDeserializer<Genres>, JsonSerializer<Genres> {
    @Override
    public Genres deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String genreName = json.getAsJsonPrimitive().getAsString();
        Genres genres = new Genres();
        genres.setName(genreName);
        return genres;
    }

    @Override
    public JsonElement serialize(Genres src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.getName());
    }
}
