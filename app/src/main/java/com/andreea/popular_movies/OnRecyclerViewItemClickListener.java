package com.andreea.popular_movies;

import android.view.View;

/**
 * Interface used to set an item click listener for the RecyclerView
 * NOTE: It took me a while to figure out how to do this in a RecyclerView, and after finding
 * different approaches online, I decided to adopt this one since it seemed to be the cleanest.
 */
public interface OnRecyclerViewItemClickListener {

    void onClick(View view, int movieId);
}
