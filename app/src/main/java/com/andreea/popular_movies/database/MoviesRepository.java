package com.andreea.popular_movies.database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import com.andreea.popular_movies.data.DataManager;
import com.andreea.popular_movies.model.Movie;

import java.util.List;

public class MoviesRepository {

    private MoviesDao mMoviesDao;
    private LiveData<List<Movie>> mFavoriteMovies;

    MoviesRepository(Application application) {
        MoviesRoomDatabase db = MoviesRoomDatabase.getDatabase(application);
        mMoviesDao = db.favoriteMoviesDao();
        mFavoriteMovies = mMoviesDao.getFavoriteMovies();
    }

    /**
     * Get favorite movies as an observable LiveData object
     */
    public LiveData<List<Movie>> getFavoriteMovies() {
        return mFavoriteMovies;
    }

    /**
     * Get the movie data as a LiveData object
     */
    public LiveData<Movie> getMovie(Integer movieId, boolean isFavorite) {
        MutableLiveData<Movie> regularMovieLiveData = new MutableLiveData<Movie>();
        regularMovieLiveData.setValue(DataManager.getInstance().getMovie(movieId));

        return isFavorite
                ? mMoviesDao.getFavoriteMovie(movieId)
                : regularMovieLiveData;
    }

    /**
     * Insert the movie into the Favorites database table
     */
    public void insert(Movie movie) {
        new InsertAsyncTask(mMoviesDao).execute(movie);
    }

    /**
     * Delete the movie from the Favorites database table
     */
    public void delete(int movieId) {
        new DeleteAsyncTask(mMoviesDao).execute(movieId);
    }

    /**
     * AsyncTask that handles the movie insertion
     */
    private static class InsertAsyncTask extends AsyncTask<Movie, Void, Void> {

        private MoviesDao mAsyncTaskDao;

        InsertAsyncTask(MoviesDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Movie... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    /**
     * AsyncTask that handles the movie deletion
     */
    private static class DeleteAsyncTask extends AsyncTask<Integer, Void, Void> {

        private MoviesDao mAsyncTaskDao;

        DeleteAsyncTask(MoviesDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            mAsyncTaskDao.delete(integers[0]);
            return null;
        }
    }
}
