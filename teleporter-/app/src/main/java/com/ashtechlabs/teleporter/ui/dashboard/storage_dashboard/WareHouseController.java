package com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard;

import com.ashtechlabs.teleporter.app_prefs.GlobalPreferManager;
import com.ashtechlabs.teleporter.retrofit.ApiService;
import com.ashtechlabs.teleporter.retrofit.RetroClient;
import com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.pojo.WareHouse;
import com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.pojo.WareHouseProfile;
import com.ashtechlabs.teleporter.util.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by IROID_ANDROID1 on 23-Jan-17.
 */

public class WareHouseController  implements IWareHouseController {

    WareHouseControllerCallback mCallback;


    public WareHouseController(WareHouseControllerCallback callback) {
        mCallback = callback;
    }

    @Override
    public void onWareHouseDetails() {

        ApiService api = RetroClient.getApiService();
        mCallback.showLoadingIndicator();

        Call<WareHouse> call = api.getJobsWithWarehousingToken(GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_STORAGE, ""));

        call.enqueue(new Callback<WareHouse>() {
            @Override
            public void onResponse(Call<WareHouse> call, Response<WareHouse> response) {
                if (response.isSuccessful()) {

                    String code = response.body().getCode();
                    if(code.equals("fail")){
                        mCallback.onGetWareHouseDetailsFailed(response.body().getMessage());
                    }else{
                        mCallback.onGetWareHouseDetails(response.body());
                    }
                }
                mCallback.dismissLoadingIndicator();
            }

            @Override
            public void onFailure(Call<WareHouse> call, Throwable t) {
                mCallback.dismissLoadingIndicator();
                mCallback.onGetWareHouseDetailsFailed(Constants.MESSAGE_SERVER_ERROR);
            }
        });

    }

    @Override
    public void onGetProfileDetails() {

        ApiService api = RetroClient.getApiService();

        Call<WareHouseProfile> call = api.getWarehouseProfile(GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_STORAGE, ""));

        call.enqueue(new Callback<WareHouseProfile>() {
            @Override
            public void onResponse(Call<WareHouseProfile> call, Response<WareHouseProfile> response) {
                if (response.isSuccessful()) {

                    mCallback.onGetStorageProfileDetails(response.body());

                } else {

                    mCallback.onGetStorageProfileDetailsFailed(Constants.MESSAGE_SERVER_ERROR);
                }
            }

            @Override
            public void onFailure(Call<WareHouseProfile> call, Throwable t) {

                mCallback.onGetStorageProfileDetailsFailed(Constants.MESSAGE_SERVER_ERROR);
            }
        });

    }
}
