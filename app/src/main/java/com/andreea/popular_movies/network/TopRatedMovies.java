package com.andreea.popular_movies.network;

import com.andreea.popular_movies.model.MoviesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TopRatedMovies {
    @GET("/3/movie/top_rated")
    Call<MoviesResponse> getTopRatedMovies(@Query("page") int page, @Query("api_key") String apiKey);
}
