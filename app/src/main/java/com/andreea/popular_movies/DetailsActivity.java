package com.andreea.popular_movies;

import android.app.Activity;
import android.os.Bundle;

import butterknife.ButterKnife;

public class DetailsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
    }
}
