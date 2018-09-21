package com.andreea.popular_movies;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.andreea.popular_movies.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder> {

    private List<Movie> mMovieList = new ArrayList<>();

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
        Movie currentMovie = mMovieList.get(position);
        Picasso.get()
                .load(currentMovie.computeFinalUrl())
                .into(holder.mMoviePoster);

    }

    @Override
    public int getItemCount() {
        return mMovieList.size();
    }

    public void setMoviesData(List<Movie> movieList) {
        mMovieList.addAll(movieList);
    }

    public static class MoviesViewHolder extends RecyclerView.ViewHolder {
        private ImageView mMoviePoster;

        public MoviesViewHolder(ImageView moviePoster) {
            super(moviePoster);
            mMoviePoster = moviePoster;
        }
    }
}
