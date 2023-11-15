package com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard.profile;

import com.ashtechlabs.teleporter.BaseActivity;
import com.ashtechlabs.teleporter.retrofit.ApiService;
import com.ashtechlabs.teleporter.retrofit.RetroClient;
import com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard.pojo.DriverProfile;
import com.ashtechlabs.teleporter.util.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by IROID_ANDROID1 on 03-Mar-17.
 */

public class DriverGetProfileController extends BaseActivity implements IDriverGetProfileController {

    DriverGetProfileControllerCallBack mCallback;

    public DriverGetProfileController(DriverGetProfileControllerCallBack callback) {
        mCallback = callback;
    }

    @Override
    public void onGetProfileDetails(String token) {
        mCallback.showLoadingIndicator();
        ApiService api = RetroClient.getApiService();

        Call<DriverProfile> call = api.getDriverProfile(token);

        call.enqueue(new Callback<DriverProfile>() {
            @Override
            public void onResponse(Call<DriverProfile> call, Response<DriverProfile> response) {
                mCallback.dismissLoadingIndicator();
                if (response.isSuccessful()) {

                    mCallback.onGetDriverProfileDetails(response.body());

                } else {
                    mCallback.onGetDriverProfileDetailsFailed(Constants.MESSAGE_SERVER_ERROR);
                }
            }

            @Override
            public void onFailure(Call<DriverProfile> call, Throwable t) {
                mCallback.dismissLoadingIndicator();
                mCallback.onGetDriverProfileDetailsFailed(Constants.MESSAGE_SERVER_ERROR);
            }
        });

    }
}
