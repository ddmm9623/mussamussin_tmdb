package com.tmdb.dante666lcf.tmdb.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tmdb.dante666lcf.tmdb.App;
import com.tmdb.dante666lcf.tmdb.MoviePageActivity;
import com.tmdb.dante666lcf.tmdb.R;
import com.tmdb.dante666lcf.tmdb.models.Actors;
import com.tmdb.dante666lcf.tmdb.models.PageMovieModel;
import com.tmdb.dante666lcf.tmdb.models.SimilarMovies;

import java.util.List;

import io.realm.Realm;

/**
 * Created by mussa on 04.03.2018.
 */

public class SimilarMoviesAdapter extends RecyclerView.Adapter<SimilarMoviesAdapter.MyViewHolder> {

    private List<SimilarMovies> similarMoviesList;
    private Context context;

    public SimilarMoviesAdapter(List<SimilarMovies> similarMoviesList, Context context) {
        this.similarMoviesList = similarMoviesList;
        this.context = context;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_similar_movies, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final SimilarMovies similarMovies = similarMoviesList.get(position);

        if(similarMovies.getTitle() != null) {
            holder.mTitle.setText(similarMovies.getTitle());
        } else {
            holder.mTitle.setText(App.getContext().getString(R.string.error));
        }
        if(similarMovies.getReleaseDate() != null) {
            holder.mDate.setText(similarMovies.getReleaseDate());
        } else {
            holder.mDate.setText(App.getContext().getString(R.string.no_date_released));
        }

        if (similarMovies.getPosterPath() != null) {
            Glide.with(App.getContext())
                    .load(App.getContext().getString(R.string.url_image) + similarMovies.getPosterPath())
                    .into(holder.mImageView);
        } else if (similarMovies.getBackdropPath() != null) {
            Glide.with(App.getContext())
                    .load(App.getContext().getString(R.string.url_image) + similarMovies.getBackdropPath())
                    .into(holder.mImageView);
        } else {
            Glide.with(App.getContext())
                    .load("http://www.valuewalk.com/wp-content/uploads/2017/04/no-thumbnail.png")
                    .into(holder.mImageView);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Realm realm = Realm.getDefaultInstance();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.delete(PageMovieModel.class);
                        realm.delete(Actors.class);
                        realm.delete(SimilarMovies.class);
                    }
                });
                realm.close();

                Intent intent = new Intent(context, MoviePageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("movieId", similarMovies.getId());
                ((Activity)context).finish();
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return similarMoviesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImageView;
        private TextView mTitle, mDate;

        public MyViewHolder(View itemView) {
            super(itemView);

            mImageView = (ImageView)itemView.findViewById(R.id.similar_movie_imageview);
            mTitle = (TextView) itemView.findViewById(R.id.similar_movie_title);
            mDate = (TextView) itemView.findViewById(R.id.similar_movie_date);
        }
    }
}
