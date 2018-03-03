package com.tmdb.dante666lcf.tmdb.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.tmdb.dante666lcf.tmdb.App;
import com.tmdb.dante666lcf.tmdb.R;
import com.tmdb.dante666lcf.tmdb.retrofit.APIServices;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mussa on 01.03.2018.
 */

public class MoviesFragment extends Fragment {

    private TabLayout tabLayout;
    private FrameLayout frameLayout;
    private Fragment fragment = null;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    public MoviesFragment() {

    }

    public static MoviesFragment newInstance() {

        Bundle args = new Bundle();

        MoviesFragment fragment = new MoviesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        frameLayout=(FrameLayout)view.findViewById(R.id.movie_framelayout);

        fragment = new PopularFragment();
        fragmentManager = getActivity().getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.movie_framelayout, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        fragment = new PopularFragment();
                        break;
                    case 1:
                        fragment = new UpcomingFragment();
                        break;
                }
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.movie_framelayout, fragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.content_movie_fragment, container, false);
    }

}
