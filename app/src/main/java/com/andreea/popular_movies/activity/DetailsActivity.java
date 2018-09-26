package com.andreea.popular_movies.activity;

import android.app.Activity;
import android.os.Bundle;

import com.andreea.popular_movies.R;

import butterknife.ButterKnife;

public class DetailsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
    }
}
