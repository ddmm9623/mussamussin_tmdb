package com.tmdb.dante666lcf.tmdb.retrofit;

import android.content.Intent;
import android.graphics.Movie;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tmdb.dante666lcf.tmdb.App;
import com.tmdb.dante666lcf.tmdb.BroadcastIntents;
import com.tmdb.dante666lcf.tmdb.models.Actors;
import com.tmdb.dante666lcf.tmdb.models.ActorsResponse;
import com.tmdb.dante666lcf.tmdb.models.Genres;
import com.tmdb.dante666lcf.tmdb.models.GenresResponse;
import com.tmdb.dante666lcf.tmdb.models.MoviesGenre;
import com.tmdb.dante666lcf.tmdb.models.MoviesGenreResponse;
import com.tmdb.dante666lcf.tmdb.models.MoviesResponse;
import com.tmdb.dante666lcf.tmdb.models.Movies;
import com.tmdb.dante666lcf.tmdb.models.PageMovieModel;
import com.tmdb.dante666lcf.tmdb.models.SimilarMovies;
import com.tmdb.dante666lcf.tmdb.models.SimilarMoviesResponse;
import com.tmdb.dante666lcf.tmdb.serializers.MovieGenresDeserializer;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mussa on 28.02.2018.
 */

public class TmdbRestClient {

    private Gson gson;
    private Retrofit retrofit;
    private TmdbApi tmdbApi;
    private OkHttpClient httpClient;
    private HttpLoggingInterceptor httpLoggingInterceptor;
    private GsonConverterFactory gsonConverterFactory;

    private String host = "https://api.themoviedb.org/3/";
    private String api_key = "02da584cad2ae31b564d940582770598";

    private static TmdbRestClient instance;

    public static synchronized TmdbRestClient getInstance() {
        if (instance == null) {
            instance = new TmdbRestClient();
        }
        return instance;
    }

    private  TmdbRestClient() {
        tmdbApi = builtClient(null);
    }

    private TmdbApi builtClient(List<Interceptor> httpInterceptors) {
        return buildClient(httpInterceptors, true);
    }

    private TmdbApi buildClient(List<Interceptor> httpInterceptors, boolean withApi) {
        if (gson == null) {
            gson = new GsonBuilder()
//                    .registerTypeAdapter(GenresResponse.class, new MovieGenresDeserializer())
                    .create();
            gsonConverterFactory = GsonConverterFactory.create(gson);
        }
        if (httpLoggingInterceptor == null) {
            httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        }
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder()
                .addInterceptor(httpLoggingInterceptor);

        if (httpInterceptors != null) {
            for (int i = 0; i < httpInterceptors.size(); i++) {
                builder.addInterceptor(httpInterceptors.get(i));
            }
        }
        httpClient = builder.build();

        String url = host;
        if (withApi) url = host;

        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(gsonConverterFactory)
                .client(httpClient)
                .build();

        return retrofit.create(TmdbApi.class);
    }

    public void getGenres() {

        tmdbApi.getGenres(api_key).enqueue(new Callback<GenresResponse>() {
            @Override
            public void onResponse(Call<GenresResponse> call, Response<GenresResponse> response) {

                if (response.isSuccessful()) {
                    List<Genres> genres = response.body().getGenres();
                    Realm mRealm = Realm.getDefaultInstance();
                    mRealm.beginTransaction();
                    mRealm.copyToRealmOrUpdate(genres);
                    mRealm.commitTransaction();
                    mRealm.close();

                    Log.e("getGenres", "||| isSuccessful");
                }
            }

            @Override
            public void onFailure(Call<GenresResponse> call, Throwable t) {
                Log.e("getGenres", t.getMessage());
            }
        });
    }

    public void getMoviesUpcoming(int page) {

        tmdbApi.getMoviesUpcoming(api_key, page).enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {

                if (response.isSuccessful()) {

                    List<Movies> upcomingMovies = response.body().getMoviesResponses();
                    Realm mRealm = Realm.getDefaultInstance();
                    mRealm.beginTransaction();
                    mRealm.copyToRealmOrUpdate(upcomingMovies);
                    mRealm.commitTransaction();
                    mRealm.close();

                    Intent intent = new Intent(BroadcastIntents.UPCOMING_MOVIES_REQUEST_OK);
                    LocalBroadcastManager.getInstance(App.getContext()).sendBroadcast(intent);

                    Log.e("getMoviesUpcoming", "||| isSuccessful");

                } else {
                    Log.e("getMoviesUpcoming", "||| ERROR");
                }
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                Log.e("getMoviesUpcoming", "onFailure");
            }
        });

    }
    public void getMoviesPopular(int page) {

        tmdbApi.getMoviesPopular(api_key, page).enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {

                if(response.isSuccessful()) {

                    List<Movies> popularMovies = response.body().getMoviesResponses();
                    Realm mRealm = Realm.getDefaultInstance();
                    mRealm.beginTransaction();
                    mRealm.copyToRealmOrUpdate(popularMovies);
                    mRealm.commitTransaction();
                    mRealm.close();

                    Intent intent = new Intent(BroadcastIntents.POPULAR_MOVIES_REQUEST_OK);
                    LocalBroadcastManager.getInstance(App.getContext()).sendBroadcast(intent);

                    Log.e("getMoviesPopular", "||| isSuccessful");

                } else {
                    Log.e("getMoviesPopular", "||| ERROR");
                }
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                Log.e("getMoviesPopular", "onFailure");
            }
        });
    }

    public void getGenreMovies(final int genreId, int page) {

        tmdbApi.getGenreMovies(genreId, api_key, page).enqueue(new Callback<MoviesGenreResponse>() {
            @Override
            public void onResponse(Call<MoviesGenreResponse> call, Response<MoviesGenreResponse> response) {
                if(response.isSuccessful()) {

                    List<MoviesGenre> popularMovies = response.body().getMoviesGenreResponse();
                    Realm mRealm = Realm.getDefaultInstance();
                    mRealm.beginTransaction();
                    mRealm.copyToRealmOrUpdate(popularMovies);
                    mRealm.commitTransaction();
                    mRealm.close();

                    Intent intent = new Intent(BroadcastIntents.MOVIES_GENRE_REQUEST_OK);
                    LocalBroadcastManager.getInstance(App.getContext()).sendBroadcast(intent);

                } else {

                }
            }

            @Override
            public void onFailure(Call<MoviesGenreResponse> call, Throwable t) {

            }
        });
    }
    public void getMoviePage(final int movieId) {

        tmdbApi.getMoviePage(movieId, api_key).enqueue(new Callback<PageMovieModel>() {
            @Override
            public void onResponse(Call<PageMovieModel> call, Response<PageMovieModel> response) {
                if(response.isSuccessful()) {
                    PageMovieModel pageMovie = response.body();
                    Realm mRealm = Realm.getDefaultInstance();
                    mRealm.beginTransaction();
                    mRealm.copyToRealmOrUpdate(pageMovie);
                    mRealm.commitTransaction();
                    mRealm.close();

                    Intent intent = new Intent(BroadcastIntents.MOVIE_PAGE_REQUEST_OK);
                    LocalBroadcastManager.getInstance(App.getContext()).sendBroadcast(intent);

                } else {

                }
            }

            @Override
            public void onFailure(Call<PageMovieModel> call, Throwable t) {

            }
        });
    }

    public void getMovieActors(final int movieId) {

        tmdbApi.getMovieActors(movieId, api_key).enqueue(new Callback<ActorsResponse>() {
            @Override
            public void onResponse(Call<ActorsResponse> call, Response<ActorsResponse> response) {

                if (response.isSuccessful()) {
                    List<Actors> actorsList = response.body().getActors();
                    Realm mRealm = Realm.getDefaultInstance();
                    mRealm.beginTransaction();
                    mRealm.copyToRealmOrUpdate(actorsList);
                    mRealm.commitTransaction();
                    mRealm.close();

                    Intent intent = new Intent(BroadcastIntents.MOVIE_ACTORS_REQUEST_OK);
                    LocalBroadcastManager.getInstance(App.getContext()).sendBroadcast(intent);
                }
            }

            @Override
            public void onFailure(Call<ActorsResponse> call, Throwable t) {

            }
        });
    }

    public void getSimilarMovies(final int movieId) {

        tmdbApi.getSimilarMovies(movieId, api_key).enqueue(new Callback<SimilarMoviesResponse>() {
            @Override
            public void onResponse(Call<SimilarMoviesResponse> call, Response<SimilarMoviesResponse> response) {

                if(response.isSuccessful()) {

                    List<SimilarMovies> similarMoviesList = response.body().getSimilarMoviesList();
                    Realm realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(similarMoviesList);
                    realm.commitTransaction();
                    realm.close();

                    Intent intent = new Intent(BroadcastIntents.SIMILAR_MOVIES_REQUEST_OK);
                    LocalBroadcastManager.getInstance(App.getContext()).sendBroadcast(intent);
                }
            }

            @Override
            public void onFailure(Call<SimilarMoviesResponse> call, Throwable t) {

            }
        });
    }

    public void getSearchMovie(String searchKey, int page) {

        tmdbApi.getSearchMovie(api_key, searchKey, page).enqueue(new Callback<MoviesGenreResponse>() {
            @Override
            public void onResponse(Call<MoviesGenreResponse> call, Response<MoviesGenreResponse> response) {

                if (response.isSuccessful()) {

                    List<MoviesGenre> searchMovie = response.body().getMoviesGenreResponse();
                    Realm mRealm = Realm.getDefaultInstance();
                    mRealm.beginTransaction();
                    mRealm.copyToRealmOrUpdate(searchMovie);
                    mRealm.commitTransaction();
                    mRealm.close();

                    Intent intent = new Intent(BroadcastIntents.SEARCH_MOVIE_REQUEST_OK);
                    LocalBroadcastManager.getInstance(App.getContext()).sendBroadcast(intent);
                }
            }

            @Override
            public void onFailure(Call<MoviesGenreResponse> call, Throwable t) {

            }
        });

    }

    public void getNowPlayingMovies(int page) {

        tmdbApi.getNowPlayingMovies(api_key, page).enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {

                if(response.isSuccessful()) {

                    List<Movies> nowPlayingMoviesResponses = response.body().getMoviesResponses();
                    Realm mRealm = Realm.getDefaultInstance();
                    mRealm.beginTransaction();
                    mRealm.copyToRealmOrUpdate(nowPlayingMoviesResponses);
                    mRealm.commitTransaction();
                    mRealm.close();

                    Intent intent = new Intent(BroadcastIntents.NOWPLAYING_MOVIES_REQUEST_OK);
                    LocalBroadcastManager.getInstance(App.getContext()).sendBroadcast(intent);

                    Log.e("getNowPlayingMovies", "||| isSuccessful");

                } else {
                    Log.e("getNowPlayingMovies", "||| ERROR");
                }
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                Log.e("getNowPlayingMovies", "onFailure");
            }
        });

    }

}
