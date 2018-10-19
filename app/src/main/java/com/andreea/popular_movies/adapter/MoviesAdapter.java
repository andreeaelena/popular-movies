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

public class MoviesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int ITEM_TYPE_LOADING = -1;
    public static final int ITEM_TYPE_MOVIE = 1;

    private List<Movie> mMovieList = new ArrayList<>();
    private OnRecyclerViewItemClickListener mOnItemClickListener;
    private boolean mShouldShowLoadingView;

    /**
     * The MoviesAdapter used by the RecyclerView
     * @param onItemClickListener The item click listener that is injected into the adapter
     */
    public MoviesAdapter(OnRecyclerViewItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder holder;

        if (viewType == ITEM_TYPE_MOVIE) {
            View movieView = inflater.inflate(R.layout.movie_item, parent, false);
            holder = new MovieViewHolder(movieView);
        } else {
            View loadingView = inflater.inflate(R.layout.loading_item, parent, false);
            holder = new LoadingViewHolder(loadingView);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(final @NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == ITEM_TYPE_MOVIE) {
            Movie currentMovie = mMovieList.get(position);
            MovieViewHolder movieHolder = (MovieViewHolder) holder;
            ImageView posterView = movieHolder.mMoviePoster;
            // Set the click listener on the view
            posterView.setOnClickListener(new OnItemClickListener(mOnItemClickListener, currentMovie));
            // Download the image using Picasso
            Picasso.get()
                    .load(currentMovie.computeFinalPosterUrl())
                    .placeholder(R.drawable.image_placeholder)
                    .into(posterView);
        }
    }

    @Override
    public int getItemCount() {
        return mMovieList.size() + (mShouldShowLoadingView ? 1 : 0);
    }

    @Override
    public int getItemViewType(int position) {
        return (position == mMovieList.size() && mShouldShowLoadingView)
                ? ITEM_TYPE_LOADING
                : ITEM_TYPE_MOVIE;
    }

    /**
     * Sets a movie list that should be displayed inside the RecyclerView.
     */
    public void setMoviesData(List<Movie> movieList, boolean shouldShowLoadingView) {
        mShouldShowLoadingView = shouldShowLoadingView;
        mMovieList.clear();
        mMovieList.addAll(movieList);
    }

    public void clearMoviesData() {
        mMovieList.clear();
    }

    /**
     * ViewHolder class that holds a reference to the poster ImageView.
     */
    static class MovieViewHolder extends RecyclerView.ViewHolder {
        private ImageView mMoviePoster;

        MovieViewHolder(View itemView) {
            super(itemView);
            mMoviePoster = itemView.findViewById(R.id.movie_poster);
        }
    }

    /**
     * ViewHolder class that holds a reference to the loading view used for pagination.
     */
    static class LoadingViewHolder extends RecyclerView.ViewHolder {

        LoadingViewHolder(View itemView) {
            super(itemView);
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
