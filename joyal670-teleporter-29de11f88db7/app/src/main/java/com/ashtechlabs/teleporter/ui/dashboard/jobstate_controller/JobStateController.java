package com.ashtechlabs.teleporter.ui.dashboard.jobstate_controller;

import android.util.Log;

import com.google.gson.JsonObject;
import com.ashtechlabs.teleporter.BaseActivity;
import com.ashtechlabs.teleporter.retrofit.ApiService;
import com.ashtechlabs.teleporter.retrofit.RetroClient;
import com.ashtechlabs.teleporter.util.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by IROID_ANDROID1 on 19-Jan-17.
 */

public class JobStateController extends BaseActivity implements IJobStateController {

    JobStateControllerCallback mCallback;


    public JobStateController(JobStateControllerCallback callback) {
        mCallback = callback;
    }

    @Override
    public void onJobState(String jobid, String state, String token) {

        ApiService api = RetroClient.getApiService();
        mCallback.showLoadingIndicator();

        Call<JsonObject> call = api.setJobState(jobid, state, token);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if (response.isSuccessful()) {

                JsonObject jsonObject = response.body();
                Log.e("RESPONSE >> ", jsonObject.toString());
                String code = jsonObject.get(Constants.TAG_CODE).getAsString();
                if (code.equals(Constants.SUCCESS)) {
                    mCallback.dismissLoadingIndicator();
                    mCallback.onGetJobStateSuccess(jsonObject.get(Constants.TAG_MESSAGE).getAsString());

                }else{
                    mCallback.dismissLoadingIndicator();
                    mCallback.onGetJobStateFailed(jsonObject.get(Constants.TAG_MESSAGE).getAsString());
                }

                } else {
                    mCallback.dismissLoadingIndicator();
                    mCallback.onGetJobStateFailed(Constants.MESSAGE_SERVER_ERROR);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                mCallback.dismissLoadingIndicator();
                mCallback.onGetJobStateFailed(Constants.MESSAGE_SERVER_ERROR);
            }
        });

    }

}
