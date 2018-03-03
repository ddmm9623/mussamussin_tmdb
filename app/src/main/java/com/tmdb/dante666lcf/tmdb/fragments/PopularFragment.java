package com.tmdb.dante666lcf.tmdb.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tmdb.dante666lcf.tmdb.App;
import com.tmdb.dante666lcf.tmdb.BroadcastIntents;
import com.tmdb.dante666lcf.tmdb.R;
import com.tmdb.dante666lcf.tmdb.adapters.MoviesAdapter;
import com.tmdb.dante666lcf.tmdb.models.Genres;
import com.tmdb.dante666lcf.tmdb.models.Movies;
import com.tmdb.dante666lcf.tmdb.retrofit.APIServices;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by mussa on 01.03.2018.
 */

public class PopularFragment extends Fragment {

    public PopularFragment(){

    }

    public static PopularFragment newInstance() {

        Bundle args = new Bundle();

        PopularFragment fragment = new PopularFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private List<Genres> genresList = new ArrayList<>();
    private List<Movies> moviesList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MoviesAdapter moviesAdapter;
    private BroadcastReceiver createBroadcastReceiverPopularMovies;
    private LinearLayoutManager layoutManager;

    private boolean loading = true;
    private int previousTotal = 0;
    private int visibleThreshold = 5;
    private int nextPage = 1;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_movies_fragment);

        Realm genresRealm = Realm.getDefaultInstance();
        RealmResults<Genres> realmGenresResults;
        realmGenresResults = genresRealm.where(Genres.class).findAll();
        genresList = (ArrayList<Genres>)genresRealm.copyFromRealm(realmGenresResults);
        genresRealm.close();

        Realm realm = Realm.getDefaultInstance();
        RealmResults<Movies> realmResults;
        realmResults = realm.where(Movies.class).findAll();
        moviesList = (ArrayList<Movies>)realm.copyFromRealm(realmResults);
        realm.close();

        moviesAdapter = new MoviesAdapter(moviesList, genresList);

        layoutManager = new LinearLayoutManager(App.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(moviesAdapter);

        createScrollListener();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (createBroadcastReceiverPopularMovies == null) {
            createBroadcastReceiverPopularMovies = broadcastReceiverNowPlayingMovies();
            LocalBroadcastManager.getInstance(App.getContext()).registerReceiver(createBroadcastReceiverPopularMovies, new IntentFilter(BroadcastIntents.POPULAR_MOVIES_REQUEST_OK));
        }

        APIServices.getMoviesPopular(nextPage);
    }

    protected void updatePopularMoviesList() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Movies> realmResults;
        realmResults = realm.where(Movies.class).findAll();
        moviesList = (ArrayList<Movies>)realm.copyFromRealm(realmResults);
        moviesAdapter.updateData(moviesList);
        realm.close();
    }

    private BroadcastReceiver broadcastReceiverNowPlayingMovies() {
        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updatePopularMoviesList();
            }
        };
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (createBroadcastReceiverPopularMovies != null) {
            LocalBroadcastManager.getInstance(App.getContext()).unregisterReceiver(createBroadcastReceiverPopularMovies);
            createBroadcastReceiverPopularMovies = null;
        }

        Realm realm = Realm.getDefaultInstance();
        final RealmResults<Movies> results = realm.where(Movies.class).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                results.deleteAllFromRealm();
            }
        });
        realm.close();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.popular_fragment, container, false);
    }

    protected void createScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {

                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if (totalItemCount > previousTotal) {
                            loading = false;
                            previousTotal = totalItemCount;
                        }
                    }

                    if (!loading && (totalItemCount - visibleItemCount)
                            <= (firstVisibleItem + visibleThreshold)) {
                        nextPage++;
                        APIServices.getMoviesPopular(nextPage);
                        loading = true;
                    }
                }
                if (dy < 0) {
                }

                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

}
