package com.andreea.popular_movies.cache;

import com.andreea.popular_movies.BuildConfig;
import com.andreea.popular_movies.callback.PopularMoviesCallback;
import com.andreea.popular_movies.model.Movie;
import com.andreea.popular_movies.model.PopularMoviesResponse;
import com.andreea.popular_movies.network.PopularMovies;
import com.andreea.popular_movies.network.RetrofitClientInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataCache {

    private static DataCache sInstance;

    private PopularMoviesCallback mPopularMoviesCallback;
    private PopularMoviesResponse mPopularMoviesResponse;

    private DataCache() {
        // Empty constructor
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
     * @param callback The callback on which we will pass the PopularMoviesResponse back to the caller
     */
    public void getPopularMovies(PopularMoviesCallback callback) {
        mPopularMoviesCallback = callback;

        // If we already have the popular movies data, use the callback to return it,
        // otherwise make the API call to get the data, and then store it locally and return it.
        if (mPopularMoviesResponse != null) {
            returnCachedPopularMoviesData();
        } else {
            requestPopularMoviesData();
        }
    }

    public Movie getMovie(int id) {
        Movie movie = null;
        if (mPopularMoviesResponse != null) {
            for (Movie currentMovie : mPopularMoviesResponse.getResults()) {
                if (id == currentMovie.getId()) {
                    movie = currentMovie;
                    break;
                }
            }
        }
        return movie;
    }

    private void returnCachedPopularMoviesData() {
        // Use the PopularMoviesCallback instance to pass the data back to the initial caller
        mPopularMoviesCallback.onMoviesResponse(mPopularMoviesResponse);
    }

    private void requestPopularMoviesData() {
        // Make the request to the /movie/popular endpoint
        PopularMovies popularMovies = RetrofitClientInstance.getInstance().create(PopularMovies.class);
        Call<PopularMoviesResponse> popularMoviesCall = popularMovies.getPopularMovies(BuildConfig.THE_MOVIE_DB_API_KEY);
        popularMoviesCall.enqueue(new Callback<PopularMoviesResponse>() {

            @Override
            public void onResponse(Call<PopularMoviesResponse> call, Response<PopularMoviesResponse> response) {
                mPopularMoviesResponse = response.body();
                // Use the PopularMoviesCallback instance to pass the data back to the initial caller
                mPopularMoviesCallback.onMoviesResponse(mPopularMoviesResponse);
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
