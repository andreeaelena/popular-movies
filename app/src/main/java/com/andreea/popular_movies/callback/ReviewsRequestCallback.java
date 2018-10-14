package com.andreea.popular_movies.callback;

import com.andreea.popular_movies.model.Review;

import java.util.List;

/**
 * Interface used to create callbacks for returning the movie reviews response
 */
public interface ReviewsRequestCallback {

    void onReviewsResponse(List<Review> reviewList);

    void onReviewsFailure(Throwable throwable);
}
