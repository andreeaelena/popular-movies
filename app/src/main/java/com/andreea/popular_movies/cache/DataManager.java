package com.andreea.popular_movies.cache;

import com.andreea.popular_movies.BuildConfig;
import com.andreea.popular_movies.callback.MoviesCallback;
import com.andreea.popular_movies.model.Movie;
import com.andreea.popular_movies.model.MoviesResponse;
import com.andreea.popular_movies.network.MoviesApi;
import com.andreea.popular_movies.network.RetrofitClientInstance;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataManager {

    public enum SortBy {
        MOST_POPULAR,
        TOP_RATED
    }

    private static DataManager sInstance;

    private MoviesCallback mMoviesCallback;
    private int mCurrentPage = 0;
    private int mTotalPages;
    private List<Movie> mMovieList;

    private DataManager() {
        mMovieList = new ArrayList<>();
    }

    public static DataManager getInstance() {
        if (sInstance == null) {
            sInstance = new DataManager();
        }
        return sInstance;
    }

    /**
     * Uses a callback to return the popular movies data, as an object of type MoviesResponse.
     * The caller does not need to know if the object comes from the API or from a local cache.
     * @param page The page to request
     * @param callback The callback on which we will pass the MoviesResponse back to the caller
     */
    public void getMovies(SortBy sortBy, int page, boolean forced, MoviesCallback callback) {
        mMoviesCallback = callback;

        // If we already have the movies data, use the callback to return it,
        // otherwise make the API call to get the data form the appropriate endpoint,
        // and then store it locally and return it.
        if (!forced && mMovieList.size() > 0) {
            returnCachedMoviesData();
        } else {
            switch (sortBy) {
                case MOST_POPULAR:
                    requestMostPopularMoviesData(page);
                    break;
                case TOP_RATED:
                    requestTopRatedMoviesData(page);
                    break;
            }

        }
    }

    /**
     * Return the Movie object for the provided id.
     * @param id the movie id for which to return the object
     * @return a Movie object or null
     */
    public Movie getMovie(int id) {
        Movie movie = null;
        if (mMovieList != null) {
            for (Movie currentMovie : mMovieList) {
                if (id == currentMovie.getId()) {
                    movie = currentMovie;
                    break;
                }
            }
        }
        return movie;
    }

    /**
     * Returns the current page of movies
     */
    public int getCurrentPage() {
        return mCurrentPage;
    }

    /**
     * Return true if the previously requested page is the last one
     */
    public boolean isLastPage() {
        return mCurrentPage == mTotalPages;
    }

    private void returnCachedMoviesData() {
        // Use the MoviesCallback instance to pass the data back to the initial caller
        mMoviesCallback.onMoviesResponse(mMovieList);
    }

    private void requestMostPopularMoviesData(int page) {
        // Make the request to the /movie/popular endpoint
        MoviesApi moviesApi = RetrofitClientInstance.getInstance().create(MoviesApi.class);
        Call<MoviesResponse> popularMoviesCall = moviesApi.getPopularMovies(page, BuildConfig.THE_MOVIE_DB_API_KEY);
        popularMoviesCall.enqueue(new RetrofitCallback());
    }

    private void requestTopRatedMoviesData(int page) {
        // Make the request to the /movie/top_rated endpoint
        MoviesApi topRatedMovies = RetrofitClientInstance.getInstance().create(MoviesApi.class);
        Call<MoviesResponse> topRatedMoviesCall = topRatedMovies.getTopRatedMovies(page, BuildConfig.THE_MOVIE_DB_API_KEY);
        topRatedMoviesCall.enqueue(new RetrofitCallback());
    }

    /**
     * Class that implements the Retrofit Callback class and is used as a callback for both
     * the /movie/popular and /movie/top_rated endpoints.
     */
    class RetrofitCallback implements Callback<MoviesResponse> {
        @Override
        public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
            MoviesResponse moviesResponse = response.body();
            if (moviesResponse != null) {
                mCurrentPage = moviesResponse.getPage();
                mTotalPages = moviesResponse.getTotalPages();

                // If this is the first page it might mean that the user switched the sort order
                // so we need to clear the previously stored movies.
                if (mCurrentPage == 1) {
                    mMovieList.clear();
                }

                mMovieList.addAll(moviesResponse.getResults());
            }
            // Use the MoviesCallback instance to pass the data back to the initial caller
            mMoviesCallback.onMoviesResponse(mMovieList);
        }

        @Override
        public void onFailure(Call<MoviesResponse> call, Throwable t) {
            // Use the MoviesCallback instance to pass the failure reason to the
            // initial caller
            mMoviesCallback.onMoviesFailure(t);
        }
    }
}
