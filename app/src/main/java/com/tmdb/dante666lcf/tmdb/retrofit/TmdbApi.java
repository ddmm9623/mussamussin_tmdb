package com.tmdb.dante666lcf.tmdb.retrofit;

import com.tmdb.dante666lcf.tmdb.models.ActorsResponse;
import com.tmdb.dante666lcf.tmdb.models.GenresResponse;
import com.tmdb.dante666lcf.tmdb.models.MoviesGenreResponse;
import com.tmdb.dante666lcf.tmdb.models.MoviesResponse;
import com.tmdb.dante666lcf.tmdb.models.PageMovieModel;
import com.tmdb.dante666lcf.tmdb.models.SimilarMoviesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by mussa on 28.02.2018.
 */

public interface TmdbApi {

    @GET("movie/now_playing")
    Call<MoviesResponse> getNowPlayingMovies( @Query("api_key") String api_key,
                                              @Query("page") int page);

    @GET("genre/movie/list")
    Call<GenresResponse> getGenres (@Query("api_key") String api_key);

    @GET("movie/popular")
    Call<MoviesResponse> getMoviesPopular (@Query("api_key") String api_key,
                                           @Query("page") int page);

    @GET("movie/upcoming")
    Call<MoviesResponse> getMoviesUpcoming (@Query("api_key") String api_key,
                                            @Query("page") int page);

    @GET("movie/{movie_id}")
    Call<PageMovieModel> getMoviePage (@Path("movie_id") int movieId, @Query("api_key") String api_key);

    @GET("genre/{genre_id}/movies")
    Call<MoviesGenreResponse> getGenreMovies (@Path("genre_id") int genreId,
                                              @Query("api_key") String api_key,
                                              @Query("page") int page);

    @GET("movie/{movie_id}/credits")
    Call<ActorsResponse> getMovieActors (@Path("movie_id") int movieId,
                                         @Query("api_key") String api_key);

    @GET("movie/{movie_id}/similar")
    Call<SimilarMoviesResponse> getSimilarMovies (@Path("movie_id") int movieId,
                                                  @Query("api_key") String api_key);

    @GET("search/movie")
    Call<MoviesGenreResponse> getSearchMovie (@Query("api_key") String api_key,
                                         @Query("query") String searchKey,
                                         @Query("page") int page);

}
