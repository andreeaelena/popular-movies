package com.andreea.popular_movies.activity;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.Button;
import android.widget.TextView;

import com.andreea.popular_movies.callback.MoviesRequestCallback;
import com.andreea.popular_movies.data.DataManager;
import com.andreea.popular_movies.callback.OnRecyclerViewItemClickListener;
import com.andreea.popular_movies.R;
import com.andreea.popular_movies.adapter.MoviesAdapter;
import com.andreea.popular_movies.database.MoviesViewModel;
import com.andreea.popular_movies.model.Movie;
import com.andreea.popular_movies.utils.Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviesActivity extends AppCompatActivity {
    private static final String SORT_MENU_SELECTED_ITEM_INDEX = "sort_menu_selected_item_index";

    @BindView(R.id.loading_view) View mLoadingView;
    @BindView(R.id.movies_grid) RecyclerView mMoviesGrid;
    @BindView(R.id.no_data_view) View mNoDataView;
    @BindView(R.id.no_data_message) TextView mNoDataMessage;
    @BindView(R.id.retry_button) Button mRetryButton;
    @BindView(R.id.movies_toolbar) Toolbar mToolbar;

    private MoviesAdapter mMoviesAdapter;
    private GridLayoutManager mMoviesLayoutManager;
    private MoviesViewModel mMoviesViewModel;

    private DataManager.SortBy mSortBy;
    private int mSortMenuSelectedItemIndex;
    private boolean mIsLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        int numberOfColumns = getResources().getInteger(R.integer.grid_columns_count);

        mMoviesAdapter = new MoviesAdapter(new OnMovieGridItemClickListener());
        mMoviesLayoutManager = new GridLayoutManager(this, numberOfColumns);
        mMoviesLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                // The loading view is two columns wide, so we need to set the span to 2
                switch (mMoviesAdapter.getItemViewType(position)) {
                    case MoviesAdapter.ITEM_TYPE_MOVIE:
                        return 1;
                    case MoviesAdapter.ITEM_TYPE_LOADING:
                        return 2;
                }
                return 0;
            }
        });

        mMoviesGrid.setHasFixedSize(true);
        mMoviesGrid.setLayoutManager(mMoviesLayoutManager);
        mMoviesGrid.setAdapter(mMoviesAdapter);
        mMoviesGrid.addOnScrollListener(new OnMovieGridScrollListener());

        // Initialize the MoviesViewModel that provides the Favorite Movies data
        mMoviesViewModel = new MoviesViewModel(getApplication());

        // Re-trigger the movies request when the error view Retry button is clicked
        mRetryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNoDataView.setVisibility(View.GONE);
                mLoadingView.setVisibility(View.VISIBLE);
                loadData(true, false);
            }
        });

        // Set tha sort item selected index based on the saved instance state
        mSortMenuSelectedItemIndex = savedInstanceState != null
                ? savedInstanceState.getInt(SORT_MENU_SELECTED_ITEM_INDEX)
                : 0;

        switch (mSortMenuSelectedItemIndex) {
            case 0:
                mSortBy = DataManager.SortBy.MOST_POPULAR;
                loadData(false, false);
                break;
            case 1:
                mSortBy = DataManager.SortBy.TOP_RATED;
                loadData(false, false);
                break;
            case 2:
                loadFavoriteMovies();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(SORT_MENU_SELECTED_ITEM_INDEX, mSortMenuSelectedItemIndex);
        super.onSaveInstanceState(outState);
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
                showSortMenu();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Load the Movies data
     * @param forced Force a network request
     */
    private void loadData(boolean forced, boolean changeSortOrder) {
        mIsLoading = true;
        // If the sort order was just changed, set the page to 1
        int page = changeSortOrder ? 1 : DataManager.getInstance().getCurrentPage() + 1;
        DataManager.getInstance().getMovies(mSortBy, page, forced, new OnMoviesRequestCallback());
    }

    private void loadFavoriteMovies() {
        mMoviesViewModel.getFavoriteMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movieList) {
                mLoadingView.setVisibility(View.GONE);

                if (movieList != null) {
                    if (movieList.size() > 0) {
                        hideNoDataView();
                    } else {
                        showNoDataView();
                    }

                    mMoviesAdapter.setMoviesData(movieList, false);
                    mMoviesAdapter.notifyDataSetChanged();
                } else {
                    showNoDataView();
                    mMoviesAdapter.clearMoviesData();
                    mMoviesAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void showNoDataView() {
        if (mSortMenuSelectedItemIndex == 2) {
            mNoDataMessage.setText(R.string.no_favorite_movies);
            mRetryButton.setVisibility(View.GONE);
        } else {
            mNoDataMessage.setText(R.string.movies_error_message);
            mRetryButton.setVisibility(View.VISIBLE);
        }
        mMoviesGrid.setVisibility(View.GONE);
        mNoDataView.setVisibility(View.VISIBLE);
    }

    private void hideNoDataView() {
        mMoviesGrid.setVisibility(View.VISIBLE);
        mNoDataView.setVisibility(View.GONE);
    }

    /**
     * Display the sort type popup menu.
     */
    private void showSortMenu() {
        PopupMenu popup = new PopupMenu(this, mToolbar, Gravity.END);
        popup.inflate(R.menu.sort_menu);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                item.setChecked(true);

                switch (item.getItemId()) {
                    case R.id.most_popular:
                        mSortMenuSelectedItemIndex = 0;
                        mSortBy = DataManager.SortBy.MOST_POPULAR;
                        loadData(true, true);
                        return true;
                    case R.id.top_rated:
                        mSortMenuSelectedItemIndex = 1;
                        mSortBy = DataManager.SortBy.TOP_RATED;
                        loadData(true, true);
                        return true;
                    case R.id.favorite_movies:
                        mSortMenuSelectedItemIndex = 2;
                        loadFavoriteMovies();
                        return true;
                    default:
                        return false;
                }
            }
        });
        // Set the checked state of the items
        popup.getMenu().getItem(mSortMenuSelectedItemIndex).setChecked(true);
        popup.show();
    }

    private boolean areFavoritesSelected() {
        return mSortMenuSelectedItemIndex == 2;
    }

    /**
     * Class that implements MoviesRequestCallback and is used to return the movies data
     * from the DataManager.
     */
    class OnMoviesRequestCallback implements MoviesRequestCallback {
        @Override
        public void onMoviesResponse(List<Movie> movieList) {
            mIsLoading = false;
            mLoadingView.setVisibility(View.GONE);

            if (movieList.size() > 0) {
                hideNoDataView();
                // Set the received data on the Adapter and notify it
                boolean shouldShowLoadingView = !DataManager.getInstance().isLastPage();
                mMoviesAdapter.setMoviesData(movieList, shouldShowLoadingView);
                mMoviesAdapter.notifyDataSetChanged();
            } else {
                showNoDataView();
            }
        }

        @Override
        public void onMoviesFailure(Throwable throwable) {
            mIsLoading = false;
            mLoadingView.setVisibility(View.GONE);
            showNoDataView();
        }
    }

    /**
     * Class that implements OnRecyclerViewItemClickListener and is used for setting the grid item
     * click listener.
     */
    class OnMovieGridItemClickListener implements OnRecyclerViewItemClickListener {
        @Override
        public void onClick(View view, int movieId) {
            Intent detailsActivityIntent = new Intent(MoviesActivity.this, DetailsActivity.class);
            detailsActivityIntent.putExtra(Constants.Extra.MOVIE_ID, movieId);
            detailsActivityIntent.putExtra(Constants.Extra.IS_FAVORITE_MOVIE, areFavoritesSelected());
            startActivity(detailsActivityIntent);
        }
    }

    /**
     * Class that extends RecyclerView.OnScrollListener and checks if it should load the next
     * page of movies.
     */
    class OnMovieGridScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            int visibleItemCount = mMoviesLayoutManager.getChildCount();
            int totalItemCount = mMoviesLayoutManager.getItemCount();
            int firstVisibleItemPosition = mMoviesLayoutManager.findFirstVisibleItemPosition();

            boolean shouldLoadNextPage = !mIsLoading
                    && mSortMenuSelectedItemIndex != 2 // Favorites are not paginated
                    && !DataManager.getInstance().isLastPage()
                    && visibleItemCount + firstVisibleItemPosition >= totalItemCount
                    && firstVisibleItemPosition >= 0;

            if (shouldLoadNextPage) {
                loadData(true, false);
            }
        }
    }
}

