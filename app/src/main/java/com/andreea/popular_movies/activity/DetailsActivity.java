package com.andreea.popular_movies.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.andreea.popular_movies.R;
import com.andreea.popular_movies.cache.DataCache;
import com.andreea.popular_movies.model.Movie;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity {

    @BindView(R.id.detail_toolbar) Toolbar mToolbar;
    @BindView(R.id.movie_backdrop) ImageView mMovieBackdrop;
    @BindView(R.id.movie_poster) ImageView mMoviePosterView;
    @BindView(R.id.movie_title) TextView mMovieTitleView;
    @BindView(R.id.movie_rating) TextView mMovieRatingView;
    @BindView(R.id.movie_release_date) TextView mMovieReleaseDateView;
    @BindView(R.id.movie_overview) TextView mMovieOverview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        int movieId = getIntent().getIntExtra(MoviesActivity.EXTRA_MOVIE_ID, 0);
        Movie movie = DataCache.getInstance().getMovie(movieId);

        if (movie != null) {
            setTitle(movie.getTitle());

            if (movie.getBackdropPath() != null) {
                // Download the backdrop image using Picasso
                Picasso.get()
                        .load(movie.computeFinalBackdropUrl())
                        .into(mMovieBackdrop);
            }

            // Download the poster image using Picasso
            Picasso.get()
                    .load(movie.computeFinalPosterUrl())
                    .into(mMoviePosterView);

            mMovieTitleView.setText(movie.getTitle());
            mMovieRatingView.setText(String.valueOf(movie.getVoteAverage()));
            mMovieReleaseDateView.setText(movie.getReleaseDate());
            mMovieOverview.setText(movie.getOverview());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle back click
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
