package com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard.pending_jobs;


import com.ashtechlabs.teleporter.app_prefs.GlobalPreferManager;
import com.ashtechlabs.teleporter.retrofit.ApiService;
import com.ashtechlabs.teleporter.retrofit.RetroClient;
import com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard.pojo.DriverOrder;
import com.ashtechlabs.teleporter.util.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by IROID_ANDROID1 on 07-Mar-17.
 */

public class DriverPendingJobsController  implements IDriverPendingJobsController {

    DriverPendingJobsControllerCallback mCallback;


    public DriverPendingJobsController(DriverPendingJobsControllerCallback callback) {
        mCallback = callback;
    }

    @Override
    public void getPendingJobs(String token) {

        mCallback.showLoadingIndicator();
        ApiService api = RetroClient.getApiService();

        Call<DriverOrder> call = api.getPendingJobsWithDriverToken(GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_COURIER, ""));

        call.enqueue(new Callback<DriverOrder>() {
            @Override
            public void onResponse(Call<DriverOrder> call, Response<DriverOrder> response) {

                if (response.isSuccessful()) {
                    mCallback.dismissLoadingIndicator();
                    mCallback.onGetPendingJobs(response.body());

                } else {
                    mCallback.dismissLoadingIndicator();
                    mCallback.onGetPendingJobsFailed(Constants.MESSAGE_SERVER_ERROR);
                }
            }

            @Override
            public void onFailure(Call<DriverOrder> call, Throwable t) {
                mCallback.dismissLoadingIndicator();
                mCallback.onGetPendingJobsFailed(Constants.MESSAGE_SERVER_ERROR);
            }
        });
    }
}
