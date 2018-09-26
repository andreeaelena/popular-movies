package com.andreea.popular_movies;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.andreea.popular_movies.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder> {

    private List<Movie> mMovieList = new ArrayList<>();
    private OnRecyclerViewItemClickListener mOnItemClickListener;

    /**
     * The MoviesAdapter used by the RecyclerView
     * @param onItemClickListener The item click listener that is injected into the adapter
     */
    MoviesAdapter(OnRecyclerViewItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
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
        Movie currentMovie = mMovieList.get(position);
        View view = holder.mMoviePoster;

        // Set the click listener on the view
        view.setOnClickListener(new OnItemClickListener(mOnItemClickListener, currentMovie));

        // Download the image using Picasso
        Picasso.get()
                .load(currentMovie.computeFinalUrl())
                .into(holder.mMoviePoster);

    }

    @Override
    public int getItemCount() {
        return mMovieList.size();
    }

    public void setMoviesData(List<Movie> movieList) {
        mMovieList.clear();
        mMovieList.addAll(movieList);
    }

    static class MoviesViewHolder extends RecyclerView.ViewHolder {
        private ImageView mMoviePoster;

        MoviesViewHolder(ImageView moviePoster) {
            super(moviePoster);
            mMoviePoster = moviePoster;
        }
    }

    class OnItemClickListener implements View.OnClickListener {
        private OnRecyclerViewItemClickListener mOnItemClickListener;
        private Movie mMovieData;

        OnItemClickListener(OnRecyclerViewItemClickListener onItemClickListener, Movie movieData) {
            mOnItemClickListener = onItemClickListener;
            mMovieData = movieData;
        }

        @Override
        public void onClick(View view) {
            mOnItemClickListener.onClick(view, mMovieData.getId());
        }
    }
}
