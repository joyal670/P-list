package com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.profile;

import com.ashtechlabs.teleporter.BaseActivity;
import com.ashtechlabs.teleporter.retrofit.ApiService;
import com.ashtechlabs.teleporter.retrofit.RetroClient;
import com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.pojo.TruckingProfile;
import com.ashtechlabs.teleporter.util.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by IROID_ANDROID1 on 03-Mar-17.
 */

public class TruckingGetProfileController extends BaseActivity implements ITruckingGetProfileController {

    TruckingGetProfileControllerCallBack mCallback;

    public TruckingGetProfileController(TruckingGetProfileControllerCallBack callback) {
        mCallback = callback;
    }

    @Override
    public void onGetProfileDetails(String token) {
        mCallback.showLoadingIndicator();
        ApiService api = RetroClient.getApiService();

        Call<TruckingProfile> call = api.getTruckingProfile(token);

        call.enqueue(new Callback<TruckingProfile>() {
            @Override
            public void onResponse(Call<TruckingProfile> call, Response<TruckingProfile> response) {
                mCallback.dismissLoadingIndicator();
                if (response.isSuccessful()) {

                    mCallback.onGetDriverProfileDetails(response.body());

                } else {
                    mCallback.onGetDriverProfileDetailsFailed(Constants.MESSAGE_SERVER_ERROR);
                }
            }

            @Override
            public void onFailure(Call<TruckingProfile> call, Throwable t) {
                mCallback.dismissLoadingIndicator();
                mCallback.onGetDriverProfileDetailsFailed(Constants.MESSAGE_SERVER_ERROR);
            }
        });

    }
}
