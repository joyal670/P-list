package com.ashtechlabs.teleporter.ui.reviews;

import android.util.Log;
import android.widget.Toast;

import com.ashtechlabs.teleporter.BaseActivity;
import com.ashtechlabs.teleporter.retrofit.ApiService;
import com.ashtechlabs.teleporter.retrofit.RetroClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by IROID_ANDROID1 on 20-Jan-17.
 */

public class ReviewService extends BaseActivity implements ReviewsController {

    ReviewControllerCallback mCallback;

    public ReviewService(ReviewControllerCallback callback) {
        mCallback = callback;
    }

    @Override
    public void onReviewController(String token, int mode) {
        ApiService api = RetroClient.getApiService();
        mCallback.showLoadingIndicator();

        Call<ReviewList> call = api.getReview(token, mode);

        call.enqueue(new Callback<ReviewList>() {
            @Override
            public void onResponse(Call<ReviewList> call, Response<ReviewList> response) {
                mCallback.dismissLoadingIndicator();
                if (response.isSuccessful()) {

                    mCallback.onGetReviewDetails(response.body());

                } else {
                    Toast.makeText(ReviewService.this, "Failed", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ReviewList> call, Throwable t) {
                mCallback.dismissLoadingIndicator();
            }
        });
    }
}
