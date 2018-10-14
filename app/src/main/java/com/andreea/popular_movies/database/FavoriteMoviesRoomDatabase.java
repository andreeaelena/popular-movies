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
public abstract class FavoriteMoviesRoomDatabase extends RoomDatabase {

    private static volatile FavoriteMoviesRoomDatabase INSTANCE;

    public abstract FavoriteMoviesDao favoriteMoviesDao();

    static FavoriteMoviesRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (FavoriteMoviesRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            FavoriteMoviesRoomDatabase.class,
                            Constants.Database.MOVIES_DATABASE)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
