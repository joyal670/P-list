package com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.pending_jobs;


import com.ashtechlabs.teleporter.app_prefs.GlobalPreferManager;
import com.ashtechlabs.teleporter.retrofit.ApiService;
import com.ashtechlabs.teleporter.retrofit.RetroClient;
import com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.pojo.TruckingOrder;
import com.ashtechlabs.teleporter.util.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by IROID_ANDROID1 on 07-Mar-17.
 */

public class TruckingPendingJobsController implements ITruckingPendingJobsController {

    TruckingPendingJobsControllerCallback mCallback;


    public TruckingPendingJobsController(TruckingPendingJobsControllerCallback callback) {
        mCallback = callback;
    }

    @Override
    public void getPendingJobs(String token) {

        mCallback.showLoadingIndicator();
        ApiService api = RetroClient.getApiService();

        Call<TruckingOrder> call = api.getPendingJobsWithTruckingToken(GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_TRUCKING, ""));

        call.enqueue(new Callback<TruckingOrder>() {
            @Override
            public void onResponse(Call<TruckingOrder> call, Response<TruckingOrder> response) {

                if (response.isSuccessful()) {
                    mCallback.dismissLoadingIndicator();
                    mCallback.onGetPendingJobs(response.body());

                } else {
                    mCallback.dismissLoadingIndicator();
                    mCallback.onGetPendingJobsFailed(Constants.MESSAGE_SERVER_ERROR);
                }
            }

            @Override
            public void onFailure(Call<TruckingOrder> call, Throwable t) {
                mCallback.dismissLoadingIndicator();
                mCallback.onGetPendingJobsFailed(Constants.MESSAGE_SERVER_ERROR);
            }
        });
    }
}
