package com.tmdb.dante666lcf.tmdb;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.tmdb.dante666lcf.tmdb.adapters.MoviesGenreAdapter;
import com.tmdb.dante666lcf.tmdb.models.Genres;
import com.tmdb.dante666lcf.tmdb.models.MoviesGenre;
import com.tmdb.dante666lcf.tmdb.retrofit.APIServices;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by mussa on 03.03.2018.
 */

public class MoviesGenreActivity extends AppCompatActivity {

    private List<Genres> genresList = new ArrayList<>();
    private List<MoviesGenre> moviesList = new ArrayList<>();
    private MoviesGenreAdapter moviesGenreAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private BroadcastReceiver createBroadcastReceiverMoviesGenre;
    private Toolbar toolbar;

    private boolean loading = true;
    private int previousTotal = 0;
    private int visibleThreshold = 5;
    private int nextPage = 1;

    private int genreId;
    private String genreString;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genre_movies);

        Intent intent = getIntent();
        genreId = intent.getIntExtra("genreId",1);
        genreString = intent.getStringExtra("genreString");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp));
        toolbar.setTitle(genreString);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (createBroadcastReceiverMoviesGenre == null) {
            createBroadcastReceiverMoviesGenre = broadcastReceiverMoviesGenre();
            LocalBroadcastManager.getInstance(App.getContext()).registerReceiver(createBroadcastReceiverMoviesGenre, new IntentFilter(BroadcastIntents.MOVIES_GENRE_REQUEST_OK));
        }

        APIServices.getGenreMovies(genreId, nextPage);

        Realm genresRealm = Realm.getDefaultInstance();
        RealmResults<Genres> realmGenresResults;
        realmGenresResults = genresRealm.where(Genres.class).findAll();
        genresList = (ArrayList<Genres>)genresRealm.copyFromRealm(realmGenresResults);
        genresRealm.close();

        Realm moviesListRealm = Realm.getDefaultInstance();
        RealmResults<MoviesGenre> realmResults;
        realmResults = moviesListRealm.where(MoviesGenre.class).findAll();
        moviesList = (ArrayList<MoviesGenre>)moviesListRealm.copyFromRealm(realmResults);
        moviesListRealm.close();

        moviesGenreAdapter = new MoviesGenreAdapter(moviesList, genresList);

        layoutManager = new LinearLayoutManager(App.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(moviesGenreAdapter);

        createScrollListener();
    }

    protected void updateMoviesGenre() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<MoviesGenre> realmResults;
        realmResults = realm.where(MoviesGenre.class).findAll();
        moviesList = (ArrayList<MoviesGenre>)realm.copyFromRealm(realmResults);
        moviesGenreAdapter.updateData(moviesList);
        realm.close();
    }

    private BroadcastReceiver broadcastReceiverMoviesGenre() {
        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateMoviesGenre();
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (createBroadcastReceiverMoviesGenre != null) {
            LocalBroadcastManager.getInstance(App.getContext()).unregisterReceiver(createBroadcastReceiverMoviesGenre);
            createBroadcastReceiverMoviesGenre = null;
        }

        Realm realm = Realm.getDefaultInstance();
        final RealmResults<MoviesGenre> results = realm.where(MoviesGenre.class).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                results.deleteAllFromRealm();
            }
        });
        realm.close();

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
                        APIServices.getGenreMovies(genreId, nextPage);
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
