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
import android.util.Log;
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

public class UpcomingFragment extends Fragment {

    private BroadcastReceiver createBroadcastReceiverUpcomingMovies;
    private MoviesAdapter moviesAdapter;
    private RecyclerView recyclerView;
    private List<Genres> genresList = new ArrayList<>();
    private List<Movies> moviesList = new ArrayList<>();
    private LinearLayoutManager layoutManager;

    private boolean loading = true;
    private int previousTotal = 0;
    private int visibleThreshold = 5;
    private int nextPage = 1;

    public UpcomingFragment() {

    }

    public static UpcomingFragment newInstance() {

        Bundle args = new Bundle();

        UpcomingFragment fragment = new UpcomingFragment();
        fragment.setArguments(args);
        return fragment;
    }

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
        if (createBroadcastReceiverUpcomingMovies == null) {
            createBroadcastReceiverUpcomingMovies = broadcastReceiverUpcomingMovies();
            LocalBroadcastManager.getInstance(App.getContext()).registerReceiver(createBroadcastReceiverUpcomingMovies, new IntentFilter(BroadcastIntents.UPCOMING_MOVIES_REQUEST_OK));
        }

        APIServices.getMoviesUpcoming(nextPage);
    }

    protected void updateUpcomingMoviesList() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Movies> realmResults;
        realmResults = realm.where(Movies.class).findAll();
        moviesList = (ArrayList<Movies>)realm.copyFromRealm(realmResults);
        moviesAdapter.updateData(moviesList);
        realm.close();
    }

    private BroadcastReceiver broadcastReceiverUpcomingMovies() {
        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateUpcomingMoviesList();
            }
        };
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (createBroadcastReceiverUpcomingMovies != null) {
            LocalBroadcastManager.getInstance(App.getContext()).unregisterReceiver(createBroadcastReceiverUpcomingMovies);
            createBroadcastReceiverUpcomingMovies = null;
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

        return inflater.inflate(R.layout.upcoming_fragment, container, false);
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
                        APIServices.getMoviesUpcoming(nextPage);
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
