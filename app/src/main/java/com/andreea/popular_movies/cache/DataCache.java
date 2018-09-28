package com.andreea.popular_movies.cache;

import com.andreea.popular_movies.BuildConfig;
import com.andreea.popular_movies.callback.PopularMoviesCallback;
import com.andreea.popular_movies.model.Movie;
import com.andreea.popular_movies.model.PopularMoviesResponse;
import com.andreea.popular_movies.network.PopularMovies;
import com.andreea.popular_movies.network.RetrofitClientInstance;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataCache {

    private static DataCache sInstance;

    private PopularMoviesCallback mPopularMoviesCallback;
    private int mCurrentPage = 0;
    private int mTotalPages;
    private List<Movie> mMovieList;

    private DataCache() {
        mMovieList = new ArrayList<>();
    }

    public static DataCache getInstance() {
        if (sInstance == null) {
            sInstance = new DataCache();
        }
        return sInstance;
    }

    /**
     * Uses a callback to return the popular movies data, as an object of type PopularMoviesResponse.
     * The caller does not need to know if the object comes from the API or from a local cache.
     * @param page The page to request
     * @param callback The callback on which we will pass the PopularMoviesResponse back to the caller
     */
    public void getPopularMovies(int page, boolean forced, PopularMoviesCallback callback) {
        mPopularMoviesCallback = callback;

        // If we already have the popular movies data, use the callback to return it,
        // otherwise make the API call to get the data, and then store it locally and return it.
        if (!forced && mMovieList.size() > 0) {
            returnCachedPopularMoviesData();
        } else {
            requestPopularMoviesData(page);
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

    private void returnCachedPopularMoviesData() {
        // Use the PopularMoviesCallback instance to pass the data back to the initial caller
        mPopularMoviesCallback.onMoviesResponse(mMovieList);
    }

    private void requestPopularMoviesData(int page) {
        // Make the request to the /movie/popular endpoint
        PopularMovies popularMovies = RetrofitClientInstance.getInstance().create(PopularMovies.class);
        Call<PopularMoviesResponse> popularMoviesCall = popularMovies.getPopularMovies(page, BuildConfig.THE_MOVIE_DB_API_KEY);
        popularMoviesCall.enqueue(new Callback<PopularMoviesResponse>() {

            @Override
            public void onResponse(Call<PopularMoviesResponse> call, Response<PopularMoviesResponse> response) {
                PopularMoviesResponse popularMoviesResponse = response.body();
                if (popularMoviesResponse != null) {
                    mCurrentPage = popularMoviesResponse.getPage();
                    mTotalPages = popularMoviesResponse.getTotalPages();
                    mMovieList.addAll(popularMoviesResponse.getResults());
                }
                // Use the PopularMoviesCallback instance to pass the data back to the initial caller
                mPopularMoviesCallback.onMoviesResponse(mMovieList);
            }

            @Override
            public void onFailure(Call<PopularMoviesResponse> call, Throwable t) {
                // Use the PopularMoviesCallback instance to pass the failure reason to the
                // initial caller
                mPopularMoviesCallback.onMoviesFailure(t);
            }
        });
    }
}
