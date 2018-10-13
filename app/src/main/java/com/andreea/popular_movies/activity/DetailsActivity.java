package com.andreea.popular_movies.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.andreea.popular_movies.BuildConfig;
import com.andreea.popular_movies.R;
import com.andreea.popular_movies.cache.DataManager;
import com.andreea.popular_movies.model.Movie;
import com.andreea.popular_movies.model.Video;
import com.andreea.popular_movies.model.VideoResponse;
import com.andreea.popular_movies.network.MoviesApi;
import com.andreea.popular_movies.network.RetrofitClientInstance;
import com.andreea.popular_movies.utils.Constants;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends AppCompatActivity {

    @BindView(R.id.detail_toolbar) Toolbar mToolbar;
    @BindView(R.id.detail_scroll_view) ScrollView mScrollView;
    @BindView(R.id.movie_backdrop) ImageView mMovieBackdrop;
    @BindView(R.id.movie_poster) ImageView mMoviePosterView;
    @BindView(R.id.movie_title) TextView mMovieTitleView;
    @BindView(R.id.movie_rating) TextView mMovieRatingView;
    @BindView(R.id.movie_release_date) TextView mMovieReleaseDateView;
    @BindView(R.id.movie_overview) TextView mMovieOverview;
    @BindView(R.id.trailers_label) TextView mTrailersLabel;
    @BindView(R.id.trailers_container) LinearLayout mTrailersContainer;
    @BindView(R.id.see_reviews_button) View mReviewButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        final int movieId = getIntent().getIntExtra(Constants.Extra.MOVIE_ID, 0);
        final Movie movie = DataManager.getInstance().getMovie(movieId);

        mReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reviewsActivityIntent = new Intent(DetailsActivity.this, ReviewsActivity.class);
                reviewsActivityIntent.putExtra(Constants.Extra.MOVIE_TITLE, movie.getTitle());
                reviewsActivityIntent.putExtra(Constants.Extra.MOVIE_ID, movieId);
                startActivity(reviewsActivityIntent);
            }
        });

        MoviesApi moviesApi = RetrofitClientInstance.getInstance().create(MoviesApi.class);
        Call<VideoResponse> videoResponseCall = moviesApi.getMovieVideos(movieId, BuildConfig.THE_MOVIE_DB_API_KEY);
        videoResponseCall.enqueue(new Callback<VideoResponse>() {
            @Override
            public void onResponse(Call<VideoResponse> call, Response<VideoResponse> response) {
                final VideoResponse videoResponse = response.body();
                if (videoResponse != null && videoResponse.getResults().size() > 0) {
                    LayoutInflater inflater = LayoutInflater.from(DetailsActivity.this);

                    for (int i = 0; i < videoResponse.getResults().size(); i++) {
                        final Video trailer = videoResponse.getResults().get(i);

                        View trailerLayout = inflater.inflate(R.layout.trailer_layout, null);
                        View trailerThumbnail = trailerLayout.findViewById(R.id.trailer_thumbnail);
                        trailerThumbnail.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + trailer.getKey()));
                                Intent webIntent = new Intent(Intent.ACTION_VIEW,
                                        Uri.parse("http://www.youtube.com/watch?v=" + trailer.getKey()));
                                try {
                                    startActivity(appIntent);
                                } catch (ActivityNotFoundException ex) {
                                    startActivity(webIntent);
                                }
                            }
                        });
                        TextView trailerName = trailerLayout.findViewById(R.id.trailer_name);
                        trailerName.setText(trailer.getName());
                        mTrailersContainer.addView(trailerLayout);
                    }
                }
            }

            @Override
            public void onFailure(Call<VideoResponse> call, Throwable t) {
                // TODO
            }
        });

        if (movie != null) {
            setTitle(movie.getTitle());

            boolean hasMovieBackdrop = movie.getBackdropPath() != null;
            if (hasMovieBackdrop) {
                // Download the backdrop image using Picasso
                Picasso.get()
                        .load(movie.computeFinalBackdropUrl())
                        .into(mMovieBackdrop);
            } else {
                // In case the backdropPath is null move the ScrollView below the toolbar and set
                // its background to the primaryColor.
                mToolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mScrollView.getLayoutParams();
                params.addRule(RelativeLayout.BELOW, R.id.detail_toolbar);
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
