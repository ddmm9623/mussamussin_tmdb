package com.tmdb.dante666lcf.tmdb;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tmdb.dante666lcf.tmdb.fragments.GenresFragment;
import com.tmdb.dante666lcf.tmdb.fragments.MoviesFragment;
import com.tmdb.dante666lcf.tmdb.fragments.NowPlayingMoviesFragment;
import com.tmdb.dante666lcf.tmdb.models.Movies;

import java.util.ArrayList;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private ImageView imgNavHeaderBg, imgNavProfile;
    private TextView textNameNavHeader;

    private View navHeader;
    private ArrayList<Movies> nowPlayingMovies;
    private String urlNavHeaderBg, urlNavProfileImg;
    private Realm realm;

    public static int navItemIndex = 0;

    private static final String TAG_MOVIES = "movies";
    private static final String TAG_NOWPLAYING_MOVIES = "nowplaying_movies";
    private static final String TAG_GENRES = "genres";
    public static String CURRENT_TAG = TAG_NOWPLAYING_MOVIES;

    private String[] activityTitles;
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        urlNavHeaderBg = getString(R.string.url_nav_header_bg);
        urlNavProfileImg = getString(R.string.url_nav_profile_img);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mHandler = new Handler();

        nowPlayingMovies = new ArrayList<>();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        navHeader = navigationView.getHeaderView(0);
        imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        imgNavProfile = (ImageView) navHeader.findViewById(R.id.img_profile);
        textNameNavHeader = (TextView) navHeader.findViewById(R.id.nav_name_profile);
        

        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

        setUpNavigationView();
        loadNavHeader();


        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_NOWPLAYING_MOVIES;
            loadHomeFragment();
        }

    }

    private void setUpNavigationView() {

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.nav_nowplaying_movies:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_NOWPLAYING_MOVIES;
                        break;
                    case R.id.nav_movies:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_MOVIES;
                        break;
                    case R.id.nav_genres:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_GENRES;
                        break;
                    default:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_NOWPLAYING_MOVIES;
                }

                if (item.isChecked()) {
                    item.setChecked(false);
                } else {
                    item.setChecked(true);
                }
                item.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawer,
                toolbar,
                R.string.openDrawer,
                R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                supportInvalidateOptionsMenu();
//                Toast.makeText(MainActivity.this, getString(R.string.closeDrawer), Toast.LENGTH_SHORT).show();
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                supportInvalidateOptionsMenu();
//                Toast.makeText(MainActivity.this, getString(R.string.openDrawer), Toast.LENGTH_SHORT).show();
                super.onDrawerOpened(drawerView);
            }
        };

        actionBarDrawerToggle.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
        actionBarDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });
        drawer.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void loadHomeFragment() {
        selectNavMenu();

        setToolbarTitle();

        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();
            return;
        }

        Runnable mRunnable = new Runnable() {
            @Override
            public void run() {
                Fragment fragment = getFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame_layout, fragment, CURRENT_TAG);
                fragmentTransaction.commit();
            }
        };

        if (mRunnable != null) {
            mHandler.post(mRunnable);
        }

        drawer.closeDrawers();
    }

    private Fragment getFragment() {

        switch (navItemIndex) {
            case 0:
                return new NowPlayingMoviesFragment();
            case 1:
                return new MoviesFragment();
            case 2:
                return new GenresFragment();

            default:
                return new NowPlayingMoviesFragment();
        }
    }

    private void loadNavHeader() {
        textNameNavHeader.setText(getString(R.string.text_name_nav_header));

        Glide.with(this)
                .load(urlNavHeaderBg)
                .into(imgNavHeaderBg);


        Glide.with(this).load(urlNavProfileImg)
                .thumbnail(0.5f)
                .into(imgNavProfile);
    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_NOWPLAYING_MOVIES;
                loadHomeFragment();
                return;
            }
        }
        super.onBackPressed();
    }
}
