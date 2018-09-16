package com.andreea.popular_movies;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import butterknife.BindView;

public class MoviesActivity extends Activity {
    private static final int NUMBER_OF_COLUMNS = 2;

    @BindView(R.id.movies_grid) RecyclerView mMoviesGrid;

    private RecyclerView.Adapter mMoviesAdapter;
    private RecyclerView.LayoutManager mMoviesLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        mMoviesGrid.setHasFixedSize(true);
        mMoviesLayoutManager = new GridLayoutManager(this, NUMBER_OF_COLUMNS);
        mMoviesGrid.setLayoutManager(mMoviesLayoutManager);

    }
}
