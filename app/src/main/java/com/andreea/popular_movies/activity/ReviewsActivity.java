package com.andreea.popular_movies.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.andreea.popular_movies.R;
import com.andreea.popular_movies.adapter.ReviewsAdapter;
import com.andreea.popular_movies.callback.ReviewsRequestCallback;
import com.andreea.popular_movies.data.DataManager;
import com.andreea.popular_movies.model.Review;
import com.andreea.popular_movies.utils.Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewsActivity extends AppCompatActivity {

    @BindView(R.id.review_toolbar) Toolbar mToolbar;
    @BindView(R.id.loading_view) ProgressBar mLoadingView;
    @BindView(R.id.reviews_recycler_view) RecyclerView mReviewsRecyclerView;
    @BindView(R.id.no_data_view) View mNoDataView;
    @BindView(R.id.no_data_message) TextView mNoDataMessage;
    @BindView(R.id.retry_button) Button mRetryButton;

    private ReviewsAdapter mReviewsAdapter;
    private String mMovieTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        final int movieId = getIntent().getIntExtra(Constants.Extra.MOVIE_ID, 0);
        mMovieTitle = getIntent().getStringExtra(Constants.Extra.MOVIE_TITLE);

        setTitle(mMovieTitle);

        mReviewsAdapter = new ReviewsAdapter(getApplicationContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mReviewsRecyclerView.setLayoutManager(linearLayoutManager);
        mReviewsRecyclerView.setAdapter(mReviewsAdapter);
        mReviewsRecyclerView.setHasFixedSize(true);
        mReviewsRecyclerView.offsetChildrenVertical(20);

        // Get the reviews for the current movie asynchronously
        DataManager.getInstance().getReviews(movieId, new ReviewsRetrofitCallback());
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
     * Class that implements ReviewsRequestCallback and is used to return the reviews data
     * from the DataManager.
     */
    class ReviewsRetrofitCallback implements ReviewsRequestCallback {

        @Override
        public void onReviewsResponse(List<Review> reviewList) {
            mLoadingView.setVisibility(View.GONE);

            if (reviewList.size() > 0) {
                mReviewsRecyclerView.setVisibility(View.VISIBLE);
                mReviewsAdapter.setReviewsData(reviewList);
                mReviewsAdapter.notifyDataSetChanged();
            } else {
                // Display a message informing the user that there are not reviews available
                mNoDataView.setVisibility(View.VISIBLE);
                String noDataMessage = String.format(getString(R.string.no_reviews_message), mMovieTitle);
                mNoDataMessage.setText(noDataMessage);
                mRetryButton.setVisibility(View.GONE);
            }
        }

        @Override
        public void onReviewsFailure(Throwable throwable) {
            // Display an error message informing the user that the reviews could not be loaded,
            // with an option to retry.
            mLoadingView.setVisibility(View.GONE);
            mNoDataView.setVisibility(View.VISIBLE);
        }
    }
}
