package com.andreea.popular_movies.callback;

import com.andreea.popular_movies.model.Video;

import java.util.List;

/**
 * Interface used to create callbacks for returning the movie videos response
 */
public interface VideosRequestCallback {

    void onVideosResponse(List<Video> videoList);

    void onVideosFailure(Throwable throwable);
}
