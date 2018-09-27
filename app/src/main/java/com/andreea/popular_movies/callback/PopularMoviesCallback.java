package com.andreea.popular_movies.callback;

import com.andreea.popular_movies.model.PopularMoviesResponse;

/**
 * Interface used to create callbacks for returning the popular movies response
 */
public interface PopularMoviesCallback {

    void onMoviesResponse(PopularMoviesResponse moviesResponse);

    void onMoviesFailure(Throwable throwable);
}
