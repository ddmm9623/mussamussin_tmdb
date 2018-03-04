package com.tmdb.dante666lcf.tmdb.adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tmdb.dante666lcf.tmdb.App;
import com.tmdb.dante666lcf.tmdb.MoviePageActivity;
import com.tmdb.dante666lcf.tmdb.R;
import com.tmdb.dante666lcf.tmdb.models.Genres;
import com.tmdb.dante666lcf.tmdb.models.MoviesGenre;
import com.tmdb.dante666lcf.tmdb.retrofit.APIServices;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by mussa on 01.03.2018.
 */

public class MoviesGenreAdapter extends RecyclerView.Adapter<MoviesGenreAdapter.MyViewHolder> {

    private List<MoviesGenre> moviesList;
    private List<Genres> genresList;

    public MoviesGenreAdapter(List<MoviesGenre> moviesList, List<Genres> genresList) {
        this.moviesList = moviesList;
        this.genresList = genresList;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new MyViewHolder(view);
    }

    public void updateData(List<MoviesGenre> moviesUpdate) {
        this.moviesList = moviesUpdate;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final MoviesGenre moviePosition = moviesList.get(position);

        holder.mMovieTitle.setText(moviePosition.getTitle());
        holder.mMovieRate.setText(moviePosition.getVoteAverage() + " / 10");
        holder.mReleaseDate.setText(moviePosition.getReleaseDate());

        if (moviePosition.getPoster_path() != null) {
            Glide.with(App.getContext())
                    .load(App.getContext().getString(R.string.url_image) + moviePosition.getPoster_path())
                    .into(holder.mMovieImageView);
        } else if (moviePosition.getBackdropPath() != null) {
            Glide.with(App.getContext())
                    .load(App.getContext().getString(R.string.url_image) + moviePosition.getBackdropPath())
                    .into(holder.mMovieImageView);
        } else {
            Glide.with(App.getContext())
                    .load("http://www.valuewalk.com/wp-content/uploads/2017/04/no-thumbnail.png")
                    .into(holder.mMovieImageView);
        }


        if (!moviePosition.getGenreIdsModels().isEmpty()) {
            ArrayList<String> genresArrayList = new ArrayList<>();
            String list = "";
            for (int i = 0; i < moviePosition.getGenreIdsModels().size(); i++) {
                for (int j = 0; j < genresList.size(); j++) {
                    if (moviePosition.getGenreIdsModels().get(i) == genresList.get(j).getId()) {
                        if (i == moviePosition.getGenreIdsModels().size()-1) {
                            genresArrayList.add(genresList.get(j).getName() + ".");
                        } else {
                            genresArrayList.add(genresList.get(j).getName() + ",");
                        }
                    }
                }
            }
            for (String s : genresArrayList)
            {
                list += s + "\t";
            }

            holder.mMovieGenre.setText(list);
        } else {
            holder.mMovieGenre.setText(R.string.no_genre);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                APIServices.getMoviePage(moviePosition.getId());
                Intent intent = new Intent(App.getContext(), MoviePageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("movieId", moviePosition.getId());
                App.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mMovieTitle, mMovieGenre, mMovieRate, mReleaseDate;
        public ImageView mMovieImageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            mMovieTitle = (TextView) itemView.findViewById(R.id.item_movie_title);
            mMovieGenre = (TextView) itemView.findViewById(R.id.item_movie_genre);
            mMovieRate = (TextView) itemView.findViewById(R.id.item_movie_rating);
            mMovieImageView = (ImageView) itemView.findViewById(R.id.item_movie_imageview);
            mReleaseDate = (TextView) itemView.findViewById(R.id.item_movie_release_date);
        }
    }
}
