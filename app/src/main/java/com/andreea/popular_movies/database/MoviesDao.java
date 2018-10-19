package com.andreea.popular_movies.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.andreea.popular_movies.model.Movie;

import java.util.List;

/**
 * The Movies data access object
 */
@Dao
public interface MoviesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Movie movie);

    @Query("DELETE FROM favorite_movies_table WHERE id=:movieId")
    void delete(Integer movieId);

    @Query("DELETE FROM favorite_movies_table")
    void deleteAll();

    @Query("SELECT * from favorite_movies_table ORDER BY title ASC")
    LiveData<List<Movie>> getFavoriteMovies();

    @Query("SELECT * from favorite_movies_table WHERE id=:movieId ORDER BY title ASC")
    LiveData<Movie> getFavoriteMovie(Integer movieId);
}
