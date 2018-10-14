package com.andreea.popular_movies.database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.andreea.popular_movies.model.Movie;

import java.util.List;

public class FavoriteMoviesRepository {

    private FavoriteMoviesDao mFavoriteMoviesDao;
    private LiveData<List<Movie>> mAllMovies;

    FavoriteMoviesRepository(Application application) {
        FavoriteMoviesRoomDatabase db = FavoriteMoviesRoomDatabase.getDatabase(application);
        mFavoriteMoviesDao = db.favoriteMoviesDao();
        mAllMovies = mFavoriteMoviesDao.getAllMovies();
    }

    /**
     * Get all movies as an observable LiveData object
     */
    public LiveData<List<Movie>> getAllMovies() {
        return mAllMovies;
    }

    /**
     * Insert the movie into the Favorites database table
     */
    public void insert(Movie movie) {
        new InsertAsyncTask(mFavoriteMoviesDao).execute(movie);
    }

    /**
     * Delete the movie from the Favorites database table
     */
    public void delete(int movieId) {
        new DeleteAsyncTask(mFavoriteMoviesDao).execute(movieId);
    }

    /**
     * AsyncTask that handles the movie insertion
     */
    private static class InsertAsyncTask extends AsyncTask<Movie, Void, Void> {

        private FavoriteMoviesDao mAsyncTaskDao;

        InsertAsyncTask(FavoriteMoviesDao dao) {
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

        private FavoriteMoviesDao mAsyncTaskDao;

        DeleteAsyncTask(FavoriteMoviesDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            mAsyncTaskDao.delete(integers[0]);
            return null;
        }
    }
}
