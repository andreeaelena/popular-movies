package com.andreea.popular_movies.data;

import com.andreea.popular_movies.BuildConfig;
import com.andreea.popular_movies.callback.MoviesRequestCallback;
import com.andreea.popular_movies.callback.ReviewsRequestCallback;
import com.andreea.popular_movies.callback.VideosRequestCallback;
import com.andreea.popular_movies.model.Movie;
import com.andreea.popular_movies.model.MoviesResponse;
import com.andreea.popular_movies.model.Review;
import com.andreea.popular_movies.model.ReviewResponse;
import com.andreea.popular_movies.model.Video;
import com.andreea.popular_movies.model.VideoResponse;
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
     * Uses a callback to return the popular movies data, as a List of Movie objects.
     * The caller does not need to know if the object comes from the API or from a local cache.
     * @param page The page to request
     * @param callback The callback on which we will pass the List of Movie objects back to the caller
     */
    public void getMovies(SortBy sortBy, int page, boolean forced, MoviesRequestCallback callback) {
        // If we already have the movies data, use the callback to return it,
        // otherwise make the API call to get the data form the appropriate endpoint,
        // and then store it locally and return it.
        if (!forced && mMovieList.size() > 0) {
            returnCachedMoviesData(callback);
        } else {
            switch (sortBy) {
                case MOST_POPULAR:
                    requestMostPopularMoviesData(page, callback);
                    break;
                case TOP_RATED:
                    requestTopRatedMoviesData(page, callback);
                    break;
            }

        }
    }

    /**
     * Uses a callback to return the movie videos data, as a List of Video objects.
     * @param movieId The id of the movie for which to request the videos
     * @param callback The callback on which we will pass the List of Video objects back to the caller
     */
    public void getVideos(int movieId, VideosRequestCallback callback) {
        requestMovieVideosData(movieId, callback);
    }

    /**
     * Uses a callback to return the movie reviews data, as a List of Review objects.
     * @param movieId The id of the movie for which to request the reviews
     * @param callback The callback on which we will pass the List of Review objects back to the caller
     */
    public void getReviews(int movieId, ReviewsRequestCallback callback) {
        requestMovieReviewsData(movieId, callback);
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

    private void returnCachedMoviesData(MoviesRequestCallback callback) {
        // Use the MoviesRequestCallback instance to pass the data back to the initial caller
        callback.onMoviesResponse(mMovieList);
    }

    private void requestMostPopularMoviesData(int page, MoviesRequestCallback callback) {
        // Make the request to the /movie/popular endpoint
        MoviesApi moviesApi = RetrofitClientInstance.getInstance().create(MoviesApi.class);
        Call<MoviesResponse> popularMoviesCall = moviesApi.getPopularMovies(page, BuildConfig.THE_MOVIE_DB_API_KEY);
        popularMoviesCall.enqueue(new MoviesRetrofitCallback(callback));
    }

    private void requestTopRatedMoviesData(int page, MoviesRequestCallback callback) {
        // Make the request to the /movie/top_rated endpoint
        MoviesApi topRatedMovies = RetrofitClientInstance.getInstance().create(MoviesApi.class);
        Call<MoviesResponse> topRatedMoviesCall = topRatedMovies.getTopRatedMovies(page, BuildConfig.THE_MOVIE_DB_API_KEY);
        topRatedMoviesCall.enqueue(new MoviesRetrofitCallback(callback));
    }

    private void requestMovieVideosData(int movieId, VideosRequestCallback callback) {
        MoviesApi moviesApi = RetrofitClientInstance.getInstance().create(MoviesApi.class);
        Call<VideoResponse> videoResponseCall = moviesApi.getMovieVideos(movieId, BuildConfig.THE_MOVIE_DB_API_KEY);
        videoResponseCall.enqueue(new VideosRetrofitCallback(callback));
    }

    private void requestMovieReviewsData(int movieId, ReviewsRequestCallback callback) {
        MoviesApi moviesApi = RetrofitClientInstance.getInstance().create(MoviesApi.class);
        Call<ReviewResponse> reviewResponseCall = moviesApi.getMovieReview(movieId, BuildConfig.THE_MOVIE_DB_API_KEY);
        reviewResponseCall.enqueue(new ReviewsRetrofitCallback(callback));
    }

    /**
     * Class that implements the Retrofit Callback class and is used as a callback for
     * the /movie/{movie_id}/reviews endpoint.
     */
    class ReviewsRetrofitCallback implements Callback<ReviewResponse> {

        private ReviewsRequestCallback mCallback;

        public ReviewsRetrofitCallback(ReviewsRequestCallback callback) {
            this.mCallback = callback;
        }

        @Override
        public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
            List<Review> reviewList = new ArrayList<>();

            final ReviewResponse reviewResponse = response.body();
            if (reviewResponse != null) {
                reviewList = reviewResponse.getResults();
            }

            mCallback.onReviewsResponse(reviewList);
        }

        @Override
        public void onFailure(Call<ReviewResponse> call, Throwable t) {
            // Use the ReviewsRequestCallback instance to pass the failure reason to the
            // initial caller
            mCallback.onReviewsFailure(t);
        }
    }

    /**
     * Class that implements the Retrofit Callback class and is used as a callback for
     * the /movie/{movie_id}/videos endpoint.
     */
    class VideosRetrofitCallback implements Callback<VideoResponse> {

        private VideosRequestCallback mCallback;

        public VideosRetrofitCallback(VideosRequestCallback callback) {
            this.mCallback = callback;
        }

        @Override
        public void onResponse(Call<VideoResponse> call, Response<VideoResponse> response) {
            List<Video> videoList = new ArrayList<>();

            final VideoResponse videoResponse = response.body();
            if (videoResponse != null) {
                videoList = videoResponse.getResults();
            }

            mCallback.onVideosResponse(videoList);
        }

        @Override
        public void onFailure(Call<VideoResponse> call, Throwable t) {
            // Use the VideosRequestCallback instance to pass the failure reason to the
            // initial caller
            mCallback.onVideosFailure(t);
        }
    }

    /**
     * Class that implements the Retrofit Callback class and is used as a callback for both
     * the /movie/popular and /movie/top_rated endpoints.
     */
    class MoviesRetrofitCallback implements Callback<MoviesResponse> {

        private MoviesRequestCallback mCallback;

        public MoviesRetrofitCallback(MoviesRequestCallback callback) {
            this.mCallback = callback;
        }

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
            // Use the MoviesRequestCallback instance to pass the data back to the initial caller
            mCallback.onMoviesResponse(mMovieList);
        }

        @Override
        public void onFailure(Call<MoviesResponse> call, Throwable t) {
            // Use the MoviesRequestCallback instance to pass the failure reason to the
            // initial caller
            mCallback.onMoviesFailure(t);
        }
    }
}
