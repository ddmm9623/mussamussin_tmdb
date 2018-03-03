package com.tmdb.dante666lcf.tmdb.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tmdb.dante666lcf.tmdb.App;
import com.tmdb.dante666lcf.tmdb.R;
import com.tmdb.dante666lcf.tmdb.models.Actors;

import java.util.List;

/**
 * Created by mussa on 03.03.2018.
 */

public class ActorsAdapter extends RecyclerView.Adapter<ActorsAdapter.MyViewHolder> {

    private List<Actors> actorsList;

    public ActorsAdapter(List<Actors> actorsList) {
        this.actorsList = actorsList;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie_actor, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Actors actors = actorsList.get(position);

        if (actors.getProfilePath() != null) {
            Glide.with(App.getContext())
                    .load(App.getContext().getString(R.string.url_image) + actors.getProfilePath())
                    .into(holder.mImageView);
        } else {
            Glide.with(App.getContext())
                    .load("https://www.bigmouthvoices.com/profile_picture/large/default-profile_picture.jpg")
                    .into(holder.mImageView);
        }

        if (actors.getCharacter() != null) {
            holder.mCharacter.setText(actors.getCharacter());
        }
        if (actors.getName() != null) {
            holder.mName.setText(actors.getName());
        }
    }

    @Override
    public int getItemCount() {
        return actorsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImageView;
        private TextView mCharacter, mName;
        public MyViewHolder(View itemView) {
            super(itemView);

            mImageView = (ImageView) itemView.findViewById(R.id.movie_actor_imageview);
            mCharacter = (TextView) itemView.findViewById(R.id.movie_actor_textview_character);
            mName = (TextView) itemView.findViewById(R.id.movie_actor_textview_name);
        }
    }
}
