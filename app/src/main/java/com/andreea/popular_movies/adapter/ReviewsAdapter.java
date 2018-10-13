package com.andreea.popular_movies.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andreea.popular_movies.R;
import com.andreea.popular_movies.model.Review;

import java.util.ArrayList;
import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder> {

    private Context mContext;
    private List<Review> mReviewList = new ArrayList<>();

    public ReviewsAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View reviewItemView = inflater.inflate(R.layout.review_item, parent, false);
        ReviewViewHolder holder = new ReviewViewHolder(reviewItemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = mReviewList.get(position);
        String reviewedByFormat = mContext.getString(R.string.reviewed_by);
        String reviewedByFormattedString = String.format(reviewedByFormat, review.getAuthor());
        holder.mReviewerName.setText(reviewedByFormattedString);
        holder.mReview.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        return mReviewList.size();
    }

    /**
     * Sets a review list that should be displayed inside the RecyclerView.
     */
    public void setReviewsData(List<Review> reviewList) {
        mReviewList.clear();
        mReviewList.addAll(reviewList);
    }

    static class ReviewViewHolder extends RecyclerView.ViewHolder {
        private TextView mReviewerName;
        private TextView mReview;

        ReviewViewHolder(View itemView) {
            super(itemView);
            mReviewerName = itemView.findViewById(R.id.reviewer_name);
            mReview = itemView.findViewById(R.id.review);
        }
    }
}
