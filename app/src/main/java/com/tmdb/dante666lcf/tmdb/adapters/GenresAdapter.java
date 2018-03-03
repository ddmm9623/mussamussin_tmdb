package com.tmdb.dante666lcf.tmdb.adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tmdb.dante666lcf.tmdb.App;
import com.tmdb.dante666lcf.tmdb.MoviePageActivity;
import com.tmdb.dante666lcf.tmdb.MoviesGenreActivity;
import com.tmdb.dante666lcf.tmdb.R;
import com.tmdb.dante666lcf.tmdb.models.Genres;

import java.util.List;

/**
 * Created by mussa on 01.03.2018.
 */

public class GenresAdapter extends RecyclerView.Adapter<GenresAdapter.MyViewHolder> {

    private List<Genres> genresList;

    public GenresAdapter(List<Genres> genresList) {
        this.genresList = genresList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_genre, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Genres genres = genresList.get(position);

        holder.mGenreTitle.setText(genres.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(App.getContext(), MoviesGenreActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("genreId", genres.getId());
                intent.putExtra("genreString", genres.getName());
                App.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return genresList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mGenreTitle;
        public MyViewHolder(View itemView) {
            super(itemView);
            mGenreTitle = (TextView) itemView.findViewById(R.id.item_genre_title);
        }
    }
}
