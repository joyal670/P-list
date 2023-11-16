package com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.profile;

import com.ashtechlabs.teleporter.app_prefs.GlobalPreferManager;
import com.ashtechlabs.teleporter.retrofit.ApiService;
import com.ashtechlabs.teleporter.retrofit.RetroClient;
import com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.pojo.VendorProfile;
import com.ashtechlabs.teleporter.util.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by IROID_ANDROID1 on 03-Mar-17.
 */

public class GetProfileController  implements IGetProfileController {

    GetProfileControllerCallBack mCallback;

    public GetProfileController(GetProfileControllerCallBack callback) {
        mCallback = callback;
    }

    @Override
    public void onGetProfileDetails(String token) {

        ApiService api = RetroClient.getApiService();
        mCallback.showLoadingIndicator();

        Call<VendorProfile> call = api.getVendorProfile(GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_CARGO, ""));

        call.enqueue(new Callback<VendorProfile>() {
            @Override
            public void onResponse(Call<VendorProfile> call, Response<VendorProfile> response) {
                if (response.isSuccessful()) {
                    mCallback.dismissLoadingIndicator();
                    mCallback.onGetVendorProfileDetails(response.body());

                } else {
                    mCallback.dismissLoadingIndicator();
                    mCallback.onGetVendorProfileDetailsFailed(Constants.MESSAGE_SERVER_ERROR);
                }
            }

            @Override
            public void onFailure(Call<VendorProfile> call, Throwable t) {
                mCallback.dismissLoadingIndicator();
                mCallback.onGetVendorProfileDetailsFailed(Constants.MESSAGE_SERVER_ERROR);
            }
        });

    }
}
