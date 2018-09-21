package com.andreea.popular_movies.network;

import com.andreea.popular_movies.model.PopularMoviesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetPopularMovies {
    @GET("/3/movie/popular")
    Call<PopularMoviesResponse> getPopularMovies(@Query("api_key") String apiKey);
}
