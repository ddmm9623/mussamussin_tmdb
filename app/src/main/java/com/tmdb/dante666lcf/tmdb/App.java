package com.tmdb.dante666lcf.tmdb;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Movie;

import com.tmdb.dante666lcf.tmdb.models.Movies;
import com.tmdb.dante666lcf.tmdb.retrofit.APIServices;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by mussa on 28.02.2018.
 */

public class App extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this.getApplicationContext();

        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .schemaVersion(1) // Must be bumped when the schema changes
//                .migration( migration ) // Migration to run instead of throwing an exception
                //          .deleteRealmIfMigrationNeeded()
                .build();
        Realm.compactRealm( realmConfiguration );
        Realm.setDefaultConfiguration(realmConfiguration);

        Realm realm = Realm.getDefaultInstance();
        final RealmResults<Movies> results = realm.where(Movies.class).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                results.deleteAllFromRealm();
            }
        });
        realm.close();


        APIServices.getGenres();
    }

    public static Context getContext() {
        return context;
    }
}
