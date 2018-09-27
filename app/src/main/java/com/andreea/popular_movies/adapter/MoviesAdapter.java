package com.andreea.popular_movies.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.andreea.popular_movies.callback.OnRecyclerViewItemClickListener;
import com.andreea.popular_movies.R;
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
    public MoviesAdapter(OnRecyclerViewItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public MoviesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_item, parent, false);
        MoviesViewHolder viewHolder = new MoviesViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final @NonNull MoviesViewHolder holder, int position) {
        Movie currentMovie = mMovieList.get(position);
        View view = holder.mMoviePoster;

        // Set the click listener on the view
        view.setOnClickListener(new OnItemClickListener(mOnItemClickListener, currentMovie));

        // Download the image using Picasso
        Picasso.get()
                .load(currentMovie.computeFinalPosterUrl())
                .into(holder.mMoviePoster);
    }

    @Override
    public int getItemCount() {
        return mMovieList.size();
    }

    /**
     * Sets a movie list that should be displayed inside the RecyclerView.
     */
    public void setMoviesData(List<Movie> movieList) {
        mMovieList.clear();
        mMovieList.addAll(movieList);
    }

    /**
     * ViewHolder class that holds a reference to the poster ImageView.
     */
    static class MoviesViewHolder extends RecyclerView.ViewHolder {
        private ImageView mMoviePoster;

        MoviesViewHolder(View view) {
            super(view);
            mMoviePoster = view.findViewById(R.id.movie_poster);
        }
    }

    /**
     * Class that implements View.OnClickListener and is used to listen for item click events.
     */
    class OnItemClickListener implements View.OnClickListener {
        private OnRecyclerViewItemClickListener mOnItemClickListener;
        private Movie mMovieData;

        OnItemClickListener(OnRecyclerViewItemClickListener onItemClickListener, Movie movieData) {
            mOnItemClickListener = onItemClickListener;
            mMovieData = movieData;
        }

        @Override
        public void onClick(View view) {
            // On click we forward the event to the OnRecyclerViewItemClickListener instance
            mOnItemClickListener.onClick(view, mMovieData.getId());
        }
    }
}
