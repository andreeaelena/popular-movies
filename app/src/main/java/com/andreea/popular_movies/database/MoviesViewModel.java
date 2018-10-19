package com.andreea.popular_movies.database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.andreea.popular_movies.model.Movie;

import java.util.List;

/**
 * View Model that provides the data to the UI and handles configuration changes
 */
public class MoviesViewModel extends AndroidViewModel {

    private MoviesRepository mRepository;
    private LiveData<List<Movie>> mFavoriteMovies;

    public MoviesViewModel(@NonNull Application application) {
        super(application);
        mRepository = new MoviesRepository(application);
        mFavoriteMovies = mRepository.getFavoriteMovies();
    }

    /**
     * Get favorite movies as an observable LiveData object
     */
    public LiveData<List<Movie>> getFavoriteMovies() {
        return mFavoriteMovies;
    }

    /**
     * Get a movie from the Favorites database table based on its id
     */
    public LiveData<Movie> getMovie(Integer movieId, boolean isFavorite) {
        return mRepository.getMovie(movieId, isFavorite);
    }

    /**
     * Insert the movie into the Favorites database table
     */
    public void insert(Movie movie) {
        mRepository.insert(movie);
    }

    /**
     * Delete the movie from the Favorites database table
     */
    public void delete(int movieId) {
        mRepository.delete(movieId);
    }
}
