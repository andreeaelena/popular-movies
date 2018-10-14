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

import com.andreea.popular_movies.R;
import com.andreea.popular_movies.callback.VideosRequestCallback;
import com.andreea.popular_movies.data.DataManager;
import com.andreea.popular_movies.model.Movie;
import com.andreea.popular_movies.model.Video;
import com.andreea.popular_movies.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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
    @BindView(R.id.trailers_section) View mTrailersSection;
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

        setupData(movie);

        // Get the videos for the current movie asynchronously
        DataManager.getInstance().getVideos(movie.getId(), new OnVideosRequestCallback());
    }

    private void setupData(final Movie movie) {
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

            mReviewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent reviewsActivityIntent = new Intent(DetailsActivity.this, ReviewsActivity.class);
                    reviewsActivityIntent.putExtra(Constants.Extra.MOVIE_TITLE, movie.getTitle());
                    reviewsActivityIntent.putExtra(Constants.Extra.MOVIE_ID, movie.getId());
                    startActivity(reviewsActivityIntent);
                }
            });
        }
    }

    private void openVideoInYouTube(Video trailer) {
        String youtubeAppUri = String.format(
                Constants.Other.YOUTUBE_VIDEO_APP_URI_FORMAT,
                trailer.getKey());
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(youtubeAppUri));

        String youtubeWebUrl = String.format(
                Constants.Other.YOUTUBE_VIDEO_WEB_URL_FORMAT,
                trailer.getKey());
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(youtubeWebUrl));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
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

    /**
     * Class that implements VideosRequestCallback and is used to return the videos data
     * from the DataManager.
     */
    class OnVideosRequestCallback implements VideosRequestCallback {

        @Override
        public void onVideosResponse(List<Video> videoList) {
            if (videoList.size() > 0) {
                LayoutInflater inflater = LayoutInflater.from(DetailsActivity.this);

                // Iterate through all the videos and programmatically add a trailer item view
                // in the trailer section
                for (int i = 0; i < videoList.size(); i++) {
                    mTrailersSection.setVisibility(View.VISIBLE);

                    final Video trailer = videoList.get(i);
                    View trailerLayout = inflater.inflate(R.layout.trailer_layout, null);
                    trailerLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openVideoInYouTube(trailer);
                        }
                    });
                    TextView trailerName = trailerLayout.findViewById(R.id.trailer_name);
                    trailerName.setText(trailer.getName());
                    mTrailersContainer.addView(trailerLayout);
                }
            } else {
                // Do not show the Trailers section if there are not trailers available
                mTrailersSection.setVisibility(View.GONE);
            }
        }

        @Override
        public void onVideosFailure(Throwable throwable) {
            // Do not show the Trailers section if there are not trailers available
            mTrailersSection.setVisibility(View.GONE);
        }
    }
}
