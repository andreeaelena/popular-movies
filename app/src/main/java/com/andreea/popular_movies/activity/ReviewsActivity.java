package com.andreea.popular_movies.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.andreea.popular_movies.BuildConfig;
import com.andreea.popular_movies.R;
import com.andreea.popular_movies.adapter.ReviewsAdapter;
import com.andreea.popular_movies.model.Review;
import com.andreea.popular_movies.model.ReviewResponse;
import com.andreea.popular_movies.network.MoviesApi;
import com.andreea.popular_movies.network.RetrofitClientInstance;
import com.andreea.popular_movies.utils.Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewsActivity extends AppCompatActivity {

    @BindView(R.id.review_toolbar) Toolbar mToolbar;
    @BindView(R.id.loading_view) ProgressBar mLoadingView;
    @BindView(R.id.reviews_recycler_view) RecyclerView mReviewsRecyclerView;

    private ReviewsAdapter mReviewsAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        final int movieId = getIntent().getIntExtra(Constants.Extra.MOVIE_ID, 0);
        final String movieTitle = getIntent().getStringExtra(Constants.Extra.MOVIE_TITLE);

        setTitle(movieTitle);

        mReviewsAdapter = new ReviewsAdapter(getApplicationContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mReviewsRecyclerView.setLayoutManager(linearLayoutManager);
        mReviewsRecyclerView.setAdapter(mReviewsAdapter);
        mReviewsRecyclerView.setHasFixedSize(true);
        mReviewsRecyclerView.offsetChildrenVertical(20);

        MoviesApi moviesApi = RetrofitClientInstance.getInstance().create(MoviesApi.class);
        Call<ReviewResponse> reviewResponseCall = moviesApi.getMovieReview(movieId, BuildConfig.THE_MOVIE_DB_API_KEY);
        reviewResponseCall.enqueue(new Callback<ReviewResponse>() {
            @Override
            public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                ReviewResponse reviewResponse = response.body();
                List<Review> reviewList = reviewResponse.getResults();

                mLoadingView.setVisibility(View.GONE);

                if (reviewList.size() > 0) {
                    mReviewsRecyclerView.setVisibility(View.VISIBLE);
                    mReviewsAdapter.setReviewsData(reviewResponse.getResults());
                    mReviewsAdapter.notifyDataSetChanged();
                } else {
                    // TODO: show error view
                }
            }

            @Override
            public void onFailure(Call<ReviewResponse> call, Throwable t) {
                mLoadingView.setVisibility(View.GONE);
                // TODO
            }
        });
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
