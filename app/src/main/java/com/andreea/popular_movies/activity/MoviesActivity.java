package com.andreea.popular_movies.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.andreea.popular_movies.BuildConfig;
import com.andreea.popular_movies.OnRecyclerViewItemClickListener;
import com.andreea.popular_movies.R;
import com.andreea.popular_movies.adapter.MoviesAdapter;
import com.andreea.popular_movies.model.Movie;
import com.andreea.popular_movies.model.PopularMoviesResponse;
import com.andreea.popular_movies.network.PopularMovies;
import com.andreea.popular_movies.network.RetrofitClientInstance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviesActivity extends AppCompatActivity {
    public static final String EXTRA_MOVIE_ID = "movie_id";
    private static final int NUMBER_OF_COLUMNS = 2;

    @BindView(R.id.movies_grid) RecyclerView mMoviesGrid;
    @BindView(R.id.movies_toolbar) Toolbar mToolbar;

    private MoviesAdapter mMoviesAdapter;
    private RecyclerView.LayoutManager mMoviesLayoutManager;
    private PopularMoviesResponse mPopularMoviesResponse;
    private int sortMenuSelectedItemIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        mMoviesGrid.setHasFixedSize(true);
        mMoviesLayoutManager = new GridLayoutManager(this, NUMBER_OF_COLUMNS);
        mMoviesGrid.setLayoutManager(mMoviesLayoutManager);
        mMoviesAdapter = new MoviesAdapter(new OnMovieGridItemClickListener());
        mMoviesGrid.setAdapter(mMoviesAdapter);

        PopularMovies popularMovies = RetrofitClientInstance.getInstance().create(PopularMovies.class);
        Call<PopularMoviesResponse> popularMoviesCall = popularMovies.getPopularMovies(BuildConfig.THE_MOVIE_DB_API_KEY);
        popularMoviesCall.enqueue(new Callback<PopularMoviesResponse>() {
            @Override
            public void onResponse(Call<PopularMoviesResponse> call, Response<PopularMoviesResponse> response) {
                mPopularMoviesResponse = response.body();
                if (mPopularMoviesResponse != null) {
                    mMoviesAdapter.setMoviesData(mPopularMoviesResponse.getResults());
                    mMoviesAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<PopularMoviesResponse> call, Throwable t) {
                // TODO: display error message to the user
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort:
                showPopup(mToolbar);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showPopup(View view) {
        PopupMenu popup = new PopupMenu(this, view, Gravity.RIGHT);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                item.setChecked(true);

                switch (item.getItemId()) {
                    case R.id.most_popular:
                        sortMenuSelectedItemIndex = 0;
                        showMostPopular();
                        return true;
                    case R.id.top_rated:
                        sortMenuSelectedItemIndex = 1;
                        showTopRated();
                        return true;
                    default:
                        return false;
                }
            }
        });
        popup.inflate(R.menu.sort_menu);
        popup.getMenu().getItem(sortMenuSelectedItemIndex).setChecked(true);
        popup.show();
    }

    /**
     * The list coming from the API is sorted by most popular by default
     */
    private void showMostPopular() {
        List<Movie> sortedMovieList = mPopularMoviesResponse.getResults();
        mMoviesAdapter.setMoviesData(sortedMovieList);
        mMoviesAdapter.notifyDataSetChanged();
    }

    /**
     * We need to sort the list returned by the API by the most rated (vote_average)
     */
    private void showTopRated() {
        List<Movie> sortedMovieList = sortMovies();
        mMoviesAdapter.setMoviesData(sortedMovieList);
        mMoviesAdapter.notifyDataSetChanged();
    }

    private List<Movie> sortMovies() {
        List<Movie> movieList = new ArrayList<>(mPopularMoviesResponse.getResults());
        Collections.sort(movieList, new Comparator<Movie>() {
            @Override
            public int compare(Movie movie1, Movie movie2) {
                if (movie1.getVoteAverage() < movie2.getVoteAverage()) {
                    return 1;
                } else if (movie1.getVoteAverage() > movie2.getVoteAverage()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
        return movieList;
    }

    class OnMovieGridItemClickListener implements OnRecyclerViewItemClickListener {
        @Override
        public void onClick(View view, int movieId) {
            Intent detailsActivityIntent = new Intent(MoviesActivity.this, DetailsActivity.class);
            detailsActivityIntent.putExtra(EXTRA_MOVIE_ID, movieId);
            startActivity(detailsActivityIntent);
        }
    }
}

