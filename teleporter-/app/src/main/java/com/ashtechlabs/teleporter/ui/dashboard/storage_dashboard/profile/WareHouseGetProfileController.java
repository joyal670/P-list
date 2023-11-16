package com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.profile;

import com.ashtechlabs.teleporter.app_prefs.GlobalPreferManager;
import com.ashtechlabs.teleporter.retrofit.ApiService;
import com.ashtechlabs.teleporter.retrofit.RetroClient;
import com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.pojo.WareHouseProfile;
import com.ashtechlabs.teleporter.util.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by IROID_ANDROID1 on 03-Mar-17.
 */

public class WareHouseGetProfileController  implements IWarehouseGetProfileController {

    WareHouseGetProfileControllerCallBack mCallback;

    public WareHouseGetProfileController(WareHouseGetProfileControllerCallBack callback) {
        mCallback = callback;
    }

    @Override
    public void onGetProfileDetails() {

        ApiService api = RetroClient.getApiService();
        mCallback.showLoadingIndicator();

        Call<WareHouseProfile> call = api.getWarehouseProfile(GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_STORAGE, ""));

        call.enqueue(new Callback<WareHouseProfile>() {
            @Override
            public void onResponse(Call<WareHouseProfile> call, Response<WareHouseProfile> response) {
                if (response.isSuccessful()) {

                    mCallback.dismissLoadingIndicator();
                    mCallback.onGetStorageProfileDetails(response.body());

                } else {
                    mCallback.dismissLoadingIndicator();
                    mCallback.onGetStorageProfileDetailsFailed(Constants.MESSAGE_SERVER_ERROR);
                }
            }

            @Override
            public void onFailure(Call<WareHouseProfile> call, Throwable t) {
                mCallback.dismissLoadingIndicator();
                mCallback.onGetStorageProfileDetailsFailed(Constants.MESSAGE_SERVER_ERROR);
            }
        });

    }
}
