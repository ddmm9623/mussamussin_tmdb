package com.tmdb.dante666lcf.tmdb.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tmdb.dante666lcf.tmdb.App;
import com.tmdb.dante666lcf.tmdb.R;
import com.tmdb.dante666lcf.tmdb.adapters.GenresAdapter;
import com.tmdb.dante666lcf.tmdb.models.Genres;
import com.tmdb.dante666lcf.tmdb.retrofit.APIServices;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by mussa on 01.03.2018.
 */

public class GenresFragment extends Fragment {

    private List<Genres> genresList = new ArrayList<>();
    private GenresAdapter genresAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;

    public GenresFragment(){

    }

    public static GenresFragment newInstance() {
        Bundle args = new Bundle();
        GenresFragment fragment = new GenresFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        Realm genresRealm = Realm.getDefaultInstance();
        RealmResults<Genres> realmGenresResults;
        realmGenresResults = genresRealm.where(Genres.class).findAll();
        genresList = (ArrayList<Genres>)genresRealm.copyFromRealm(realmGenresResults);
        genresRealm.close();

        genresAdapter = new GenresAdapter(genresList);

        linearLayoutManager = new LinearLayoutManager(App.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(genresAdapter);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.content_main, container, false);
    }


}
