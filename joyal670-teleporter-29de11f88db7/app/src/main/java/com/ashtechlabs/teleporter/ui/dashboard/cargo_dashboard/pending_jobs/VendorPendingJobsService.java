package com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.pending_jobs;


import com.ashtechlabs.teleporter.app_prefs.GlobalPreferManager;
import com.ashtechlabs.teleporter.retrofit.ApiService;
import com.ashtechlabs.teleporter.retrofit.RetroClient;
import com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.pojo.PendingJob;
import com.ashtechlabs.teleporter.util.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by IROID_ANDROID1 on 07-Mar-17.
 */

public class VendorPendingJobsService  implements VendorPendingJobsController {

    VendorPendingJobsControllerCallback mCallback;


    public VendorPendingJobsService(VendorPendingJobsControllerCallback callback) {
        mCallback = callback;
    }

    @Override
    public void onDriverDetails(String token) {

        mCallback.showLoadingIndicator();
        ApiService api = RetroClient.getApiService();

        Call<PendingJob> call = api.getPendingJobsWithVendorToken(GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_CARGO, ""));

        call.enqueue(new Callback<PendingJob>() {
            @Override
            public void onResponse(Call<PendingJob> call, Response<PendingJob> response) {
                if (response.isSuccessful()) {
                    mCallback.dismissLoadingIndicator();
                    mCallback.onGetPendingJobs(response.body());

                } else {
                    mCallback.dismissLoadingIndicator();
                    mCallback.onGetPendingJobsFailed(Constants.MESSAGE_SERVER_ERROR);
                }
            }

            @Override
            public void onFailure(Call<PendingJob> call, Throwable t) {
                mCallback.dismissLoadingIndicator();
                mCallback.onGetPendingJobsFailed(Constants.MESSAGE_SERVER_ERROR);
            }
        });
    }
}
