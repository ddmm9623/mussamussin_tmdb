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
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tmdb.dante666lcf.tmdb.adapters.ActorsAdapter;
import com.tmdb.dante666lcf.tmdb.models.Actors;
import com.tmdb.dante666lcf.tmdb.models.Genres;
import com.tmdb.dante666lcf.tmdb.models.MoviesGenre;
import com.tmdb.dante666lcf.tmdb.models.PageMovieModel;
import com.tmdb.dante666lcf.tmdb.retrofit.APIServices;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by mussa on 02.03.2018.
 */

public class MoviePageActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView mDescription, mGenres, mRuntime;
    private Toolbar toolbar;
    private PageMovieModel pageMovieModel;
    private List<Actors> actorsList = new ArrayList<>();
    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerViewActors;
    private ActorsAdapter actorsAdapter;

    private BroadcastReceiver createBroadcastReceiverMoviePage;
    private BroadcastReceiver createBroadcastReceiverMovieActors;

    private int movieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_movie);
        Intent intent = getIntent();
        movieId = intent.getIntExtra("movieId", 1);

        recyclerViewActors = (RecyclerView) findViewById(R.id.recycler_view_actors_movie_page);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        imageView = (ImageView) findViewById(R.id.movie_page_imageview);
        mDescription = (TextView) findViewById(R.id.movie_page_text_descritpion);
        mGenres = (TextView) findViewById(R.id.movie_page_text_genres);
        mRuntime = (TextView) findViewById(R.id.movie_page_runtime);

        if (createBroadcastReceiverMoviePage == null) {
            createBroadcastReceiverMoviePage = broadcastReceiverMoviePage();
            LocalBroadcastManager.getInstance(App.getContext()).registerReceiver(createBroadcastReceiverMoviePage, new IntentFilter(BroadcastIntents.MOVIE_PAGE_REQUEST_OK));
        }

        if (createBroadcastReceiverMovieActors == null) {
            createBroadcastReceiverMovieActors = broadcastReceiverMovieActors();
            LocalBroadcastManager.getInstance(App.getContext()).registerReceiver(createBroadcastReceiverMovieActors, new IntentFilter(BroadcastIntents.MOVIE_ACTORS_REQUEST_OK));
        }


        APIServices.getMoviePage(movieId);
        APIServices.getMovieActors(movieId);

    }

    protected void getInfoMovieActors() {
        Realm actorsListRealm = Realm.getDefaultInstance();
        RealmResults<Actors> realmGenresResults = actorsListRealm.where(Actors.class).findAll();
        actorsList = (ArrayList<Actors>)actorsListRealm.copyFromRealm(realmGenresResults);
        actorsListRealm.close();

        actorsAdapter = new ActorsAdapter(actorsList);

        layoutManager = new LinearLayoutManager(App.getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewActors.setLayoutManager(layoutManager);
        recyclerViewActors.setItemAnimator(new DefaultItemAnimator());
        recyclerViewActors.setAdapter(actorsAdapter);

    }
    protected void getInfoMovieFromRealm() {
        Realm realm = Realm.getDefaultInstance();
        pageMovieModel = realm.where(PageMovieModel.class).findFirst();
        pageMovieModel = realm.copyFromRealm(pageMovieModel);
        realm.close();

        toolbar.setTitle(pageMovieModel.getTitle());
        mDescription.setText(pageMovieModel.getOverview());
        mRuntime.setText(String.valueOf(pageMovieModel.getRuntime()));
        
        ArrayList<String> genresList = new ArrayList<>();
        String list = "";

        for (int i = 0; i <pageMovieModel.getGenres().size(); i++) {
            if (i == pageMovieModel.getGenres().size()-1) {
                genresList.add(pageMovieModel.getGenres().get(i).getName() + ".");
            } else {
                genresList.add(pageMovieModel.getGenres().get(i).getName() + ",");
            }
        }

        for (String s : genresList)
        {
            list += s + "\t";
        }
        mGenres.setText(list);

        Glide.with(App.getContext())
                .load(getString(R.string.url_image) + pageMovieModel.getBackdropPath())
                .into(imageView);
    }

    private BroadcastReceiver broadcastReceiverMoviePage() {
        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                getInfoMovieFromRealm();
            }
        };
    }

    private BroadcastReceiver broadcastReceiverMovieActors() {
        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                getInfoMovieActors();
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (createBroadcastReceiverMoviePage != null) {
            LocalBroadcastManager.getInstance(App.getContext()).unregisterReceiver(createBroadcastReceiverMoviePage);
            createBroadcastReceiverMoviePage = null;
        }

        if (createBroadcastReceiverMovieActors != null) {
            LocalBroadcastManager.getInstance(App.getContext()).unregisterReceiver(createBroadcastReceiverMovieActors);
            createBroadcastReceiverMovieActors = null;
        }

        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.delete(PageMovieModel.class);
                realm.delete(Actors.class);
            }
        });
        realm.close();
    }
}

