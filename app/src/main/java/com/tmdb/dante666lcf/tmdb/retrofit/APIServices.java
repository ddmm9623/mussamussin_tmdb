package com.tmdb.dante666lcf.tmdb.retrofit;

import android.app.IntentService;
import android.content.Intent;
import android.os.Process;

import com.tmdb.dante666lcf.tmdb.App;

/**
 * Created by mussa on 28.02.2018.
 */

public class APIServices extends IntentService {

    private static final String EXTRA_PAGE = "com.tmdb.dante666lcf.tmdb.extra.EXTRA_PAGE";
    private static final String EXTRA_MOVIE_PAGE_ID = "com.tmdb.dante666lcf.tmdb.extra.EXTRA_MOVIE_PAGE_ID";
    private static final String EXTRA_MOVIE_ID_FOR_ACTORS = "com.tmdb.dante666lcf.tmdb.extra.EXTRA_MOVIE_ID_FOR_ACTORS";
    private static final String EXTRA_MOVIE_ID_FOR_SIMILAR = "com.tmdb.dante666lcf.tmdb.extra.EXTRA_MOVIE_ID_FOR_SIMILAR";
    private static final String EXTRA_GENRE_ID = "com.tmdb.dante666lcf.tmdb.extra.EXTRA_GENRE_ID";
    private static final String EXTRA_SEARCH_KEY = "com.tmdb.dante666lcf.tmdb.extra.EXTRA_SEARCH_KEY";
    private static final String ACTION_GET_NOW_PLAYING_MOVIES = "com.tmdb.dante666lcf.tmdb.action.ACTION_GET_NOW_PLAYING_MOVIES";
    private static final String ACTION_GET_MOVIES_GENRES = "com.tmdb.dante666lcf.tmdb.action.ACTION_GET_MOVIES_GENRES";
    private static final String ACTION_GET_POPULAR_MOVIES = "com.tmdb.dante666lcf.tmdb.action.ACTION_GET_POPULAR_MOVIES";
    private static final String ACTION_GET_UPCOMING_MOVIES = "com.tmdb.dante666lcf.tmdb.action.ACTION_GET_UPCOMING_MOVIES";
    private static final String ACTION_GET_MOVIE_PAGE_ID = "com.tmdb.dante666lcf.tmdb.action.ACTION_GET_MOVIE_PAGE_ID";
    private static final String ACTION_GET_GENRE_ID = "com.tmdb.dante666lcf.tmdb.action.ACTION_GET_GENRE_ID";
    private static final String ACTION_GET_MOVIE_ACTORS = "com.tmdb.dante666lcf.tmdb.action.ACTION_GET_MOVIE_ACTORS";
    private static final String ACTION_GET_SIMILAR_MOVIES = "com.tmdb.dante666lcf.tmdb.action.ACTION_GET_SIMILAR_MOVIES";
    private static final String ACTION_GET_SEARCH_MOVIES = "com.tmdb.dante666lcf.tmdb.action.ACTION_GET_SEARCH_MOVIES";


    public APIServices() {
        super("APIService");
    }

    public static void getNowPlayingMovies(int page) {
        Intent i = new Intent(App.getContext(), APIServices.class);
        i.setAction(ACTION_GET_NOW_PLAYING_MOVIES);
        i.putExtra(EXTRA_PAGE, page);
        App.getContext().startService(i);
    }

    public static void getGenres() {
        Intent i = new Intent(App.getContext(), APIServices.class);
        i.setAction(ACTION_GET_MOVIES_GENRES);
        App.getContext().startService(i);
    }

    public static void getMoviesPopular(int page) {
        Intent i = new Intent(App.getContext(), APIServices.class);
        i.setAction(ACTION_GET_POPULAR_MOVIES);
        i.putExtra(EXTRA_PAGE, page);
        App.getContext().startService(i);
    }

    public static void getMoviesUpcoming(int page) {
        Intent i = new Intent(App.getContext(), APIServices.class);
        i.setAction(ACTION_GET_UPCOMING_MOVIES);
        i.putExtra(EXTRA_PAGE, page);
        App.getContext().startService(i);
    }

    public static void getMoviePage(int movieId) {
        Intent i = new Intent(App.getContext(), APIServices.class);
        i.setAction(ACTION_GET_MOVIE_PAGE_ID);
        i.putExtra(EXTRA_MOVIE_PAGE_ID, movieId);
        App.getContext().startService(i);
    }

    public static void getGenreMovies(int genreId, int page) {
        Intent i = new Intent(App.getContext(), APIServices.class);
        i.setAction(ACTION_GET_GENRE_ID);
        i.putExtra(EXTRA_PAGE, page);
        i.putExtra(EXTRA_GENRE_ID, genreId);
        App.getContext().startService(i);
    }

    public static void getMovieActors(int movieId) {
        Intent i = new Intent(App.getContext(), APIServices.class);
        i.setAction(ACTION_GET_MOVIE_ACTORS);
        i.putExtra(EXTRA_MOVIE_ID_FOR_ACTORS, movieId);
        App.getContext().startService(i);
    }

    public static void getSimilarMovies(int movieId) {
        Intent i = new Intent(App.getContext(), APIServices.class);
        i.setAction(ACTION_GET_SIMILAR_MOVIES);
        i.putExtra(EXTRA_MOVIE_ID_FOR_SIMILAR, movieId);
        App.getContext().startService(i);
    }

    public static void getSearchMovie(String searchKey, int page) {
        Intent i = new Intent(App.getContext(), APIServices.class);
        i.setAction(ACTION_GET_SEARCH_MOVIES);
        i.putExtra(EXTRA_SEARCH_KEY, searchKey);
        i.putExtra(EXTRA_PAGE, page);
        App.getContext().startService(i);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

        if (intent != null) {
            final String action = intent.getAction();

            if (action.equals(ACTION_GET_NOW_PLAYING_MOVIES)) {
                int page = intent.getIntExtra(EXTRA_PAGE, 1);
                TmdbRestClient.getInstance().getNowPlayingMovies(page);
            } else if (action.equals(ACTION_GET_MOVIES_GENRES)) {
                TmdbRestClient.getInstance().getGenres();
            } else if (action.equals(ACTION_GET_POPULAR_MOVIES)) {
                int page = intent.getIntExtra(EXTRA_PAGE, 1);
                TmdbRestClient.getInstance().getMoviesPopular(page);
            } else if (action.equals(ACTION_GET_UPCOMING_MOVIES)) {
                int page = intent.getIntExtra(EXTRA_PAGE, 1);
                TmdbRestClient.getInstance().getMoviesUpcoming(page);
            } else if (action.equals(ACTION_GET_MOVIE_PAGE_ID)) {
                int movieId = intent.getIntExtra(EXTRA_MOVIE_PAGE_ID, 1);
                TmdbRestClient.getInstance().getMoviePage(movieId);
            } else if (action.equals(ACTION_GET_GENRE_ID)) {
                int genreId = intent.getIntExtra(EXTRA_GENRE_ID, 1);
                int page = intent.getIntExtra(EXTRA_PAGE, 1);
                TmdbRestClient.getInstance().getGenreMovies(genreId, page);
            } else if (action.equals(ACTION_GET_MOVIE_ACTORS)){
                int movieId = intent.getIntExtra(EXTRA_MOVIE_ID_FOR_ACTORS, 1);
                TmdbRestClient.getInstance().getMovieActors(movieId);
            } else if(action.equals(ACTION_GET_SIMILAR_MOVIES)) {
                int movieId = intent.getIntExtra(EXTRA_MOVIE_ID_FOR_SIMILAR, 1);
                TmdbRestClient.getInstance().getSimilarMovies(movieId);
            } else if(action.equals(ACTION_GET_SEARCH_MOVIES)) {
                String searchkey = intent.getStringExtra(EXTRA_SEARCH_KEY);
                int page = intent.getIntExtra(EXTRA_PAGE, 1);
                TmdbRestClient.getInstance().getSearchMovie(searchkey, page);
            }
        }
    }
}
