package com.ashtechlabs.teleporter.ui.reviews;

import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ashtechlabs.teleporter.BaseActivity;
import com.ashtechlabs.teleporter.R;
import com.ashtechlabs.teleporter.adapters.ReviewAdapter;
import com.ashtechlabs.teleporter.util.GlobalUtils;

import java.util.ArrayList;

public class ReviewActivity extends BaseActivity implements ReviewControllerCallback {

    LinearLayout layout_title;
    int mode = GlobalUtils.MODE_COURIER;
    String token = "";
    ReviewAdapter adapter;
    RecyclerView listReview;
    TextView tvHolder;
    GridLayoutManager gridLayoutManager;
    ReviewsController reviewsController;
    ArrayList<Review> rev;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        String type = getIntent().getStringExtra("mode");
        token = getIntent().getStringExtra("token");
        mode = Integer.parseInt(type);
        setupToolbar();
        initViews();
        reviewsController = new ReviewService(this);
        reviewsController.onReviewController(token, mode);
    }

    private void initViews() {
        tvHolder = (TextView) findViewById(R.id.tvHolder);
        listReview = (RecyclerView) findViewById(R.id.listReview);
        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        listReview.setLayoutManager(gridLayoutManager);
        listReview.setHasFixedSize(true);
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("My Rating");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }


    @Override
    public void onGetReviewDetails(ReviewList reviews) {
        rev = reviews.getReviewService();
        if (rev != null && rev.size() > 0) {
            tvHolder.setVisibility(View.GONE);
            adapter = new ReviewAdapter(ReviewActivity.this, rev);
            listReview.setAdapter(adapter);
        } else {
            tvHolder.setVisibility(View.VISIBLE);
        }

    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void showLoadingIndicator() {
        showProgress();
    }

    @Override
    public void dismissLoadingIndicator() {
        dismissProgress();
    }
}
