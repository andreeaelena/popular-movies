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
public class FavoriteMoviesViewModel extends AndroidViewModel {

    private FavoriteMoviesRepository mRepository;
    private LiveData<List<Movie>> mAllMovies;

    public FavoriteMoviesViewModel(@NonNull Application application) {
        super(application);
        mRepository = new FavoriteMoviesRepository(application);
        mAllMovies = mRepository.getAllMovies();
    }

    /**
     * Get all movies as an observable LiveData object
     */
    public LiveData<List<Movie>> getAllMovies() {
        return mAllMovies;
    }

    /**
     * Get a movie from the Favorites database table based on its id
     */
    public Movie getMovie(int id) {
        List<Movie> movieList = mAllMovies.getValue();
        if (movieList != null) {
            for (Movie movie : movieList) {
                if (movie.getId() == id) {
                    return movie;
                }
            }
        }
        return null;
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
