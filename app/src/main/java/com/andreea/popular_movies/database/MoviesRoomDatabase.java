package com.andreea.popular_movies.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.andreea.popular_movies.model.Movie;
import com.andreea.popular_movies.utils.Constants;

/**
 * Room database used to store movie data
 */
@Database(entities = {Movie.class}, version = 1)
public abstract class MoviesRoomDatabase extends RoomDatabase {

    private static volatile MoviesRoomDatabase INSTANCE;

    public abstract MoviesDao favoriteMoviesDao();

    static MoviesRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MoviesRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            MoviesRoomDatabase.class,
                            Constants.Database.MOVIES_DATABASE)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
