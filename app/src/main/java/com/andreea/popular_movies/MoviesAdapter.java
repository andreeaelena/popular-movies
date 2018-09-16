package com.andreea.popular_movies;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder> {

    public MoviesAdapter() {
    }

    @NonNull
    @Override
    public MoviesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ImageView moviePoster = (ImageView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_item, parent, false);
        MoviesViewHolder vh = new MoviesViewHolder(moviePoster);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesViewHolder holder, int position) {
        Picasso.get()
                .load("")
                .into(holder.mMoviePoster);

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class MoviesViewHolder extends RecyclerView.ViewHolder {
        private ImageView mMoviePoster;

        public MoviesViewHolder(ImageView moviePoster) {
            super(moviePoster);
            mMoviePoster = moviePoster;
        }
    }
}
