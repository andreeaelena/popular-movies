package com.andreea.popular_movies;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.andreea.popular_movies.model.PopularMoviesResponse;
import com.andreea.popular_movies.network.GetPopularMovies;
import com.andreea.popular_movies.network.RetrofitClientInstance;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviesActivity extends AppCompatActivity {
    private static final int NUMBER_OF_COLUMNS = 2;

    @BindView(R.id.movies_grid) RecyclerView mMoviesGrid;
    @BindView(R.id.movies_toolbar) Toolbar toolbar;

    private MoviesAdapter mMoviesAdapter;
    private RecyclerView.LayoutManager mMoviesLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        mMoviesGrid.setHasFixedSize(true);
        mMoviesLayoutManager = new GridLayoutManager(this, NUMBER_OF_COLUMNS);
        mMoviesGrid.setLayoutManager(mMoviesLayoutManager);
        mMoviesAdapter = new MoviesAdapter();
        mMoviesGrid.setAdapter(mMoviesAdapter);

        GetPopularMovies popularMovies = RetrofitClientInstance.getRetrofitInstance().create(GetPopularMovies.class);
        Call<PopularMoviesResponse> popularMoviesCall = popularMovies.getPopularMovies(RetrofitClientInstance.API_KEY);
        popularMoviesCall.enqueue(new Callback<PopularMoviesResponse>() {
            @Override
            public void onResponse(Call<PopularMoviesResponse> call, Response<PopularMoviesResponse> response) {
                PopularMoviesResponse popularMoviesResponse = response.body();
                if (popularMoviesResponse != null) {
                    mMoviesAdapter.setMoviesData(popularMoviesResponse.getResults());
                    mMoviesAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<PopularMoviesResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
