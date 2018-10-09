package com.andreea.popular_movies.network;

import com.andreea.popular_movies.model.MoviesResponse;
import com.andreea.popular_movies.model.ReviewResponse;
import com.andreea.popular_movies.model.VideoResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MoviesApi {
    @GET("/3/movie/popular")
    Call<MoviesResponse> getPopularMovies(@Query("page") int page, @Query("api_key") String apiKey);

    @GET("/3/movie/top_rated")
    Call<MoviesResponse> getTopRatedMovies(@Query("page") int page, @Query("api_key") String apiKey);

    @GET("/3/movie/{movie_id}/videos")
    Call<VideoResponse> getMovieVideos(@Path("movie_id") int movieId, @Query("api_key") String apiKey);

    @GET("/3/movie/{movie_id}/reviews")
    Call<ReviewResponse> getMovieReview(@Path("movie_id") int movieId, @Query("api_key") String apiKey);
}
