package com.andreea.popular_movies.callback;

import com.andreea.popular_movies.model.Movie;

import java.util.List;

/**
 * Interface used to create callbacks for returning the popular movies response
 */
public interface PopularMoviesCallback {

    void onMoviesResponse(List<Movie> movieList);

    void onMoviesFailure(Throwable throwable);
}
