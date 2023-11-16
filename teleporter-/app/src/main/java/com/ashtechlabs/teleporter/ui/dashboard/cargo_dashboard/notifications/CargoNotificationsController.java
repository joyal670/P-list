package com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.notifications;

import com.ashtechlabs.teleporter.app_prefs.GlobalPreferManager;
import com.ashtechlabs.teleporter.retrofit.ApiService;
import com.ashtechlabs.teleporter.retrofit.RetroClient;
import com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.pojo.VendorNotifications;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by IROID_ANDROID1 on 07-Mar-17.
 */

public class CargoNotificationsController implements ICargoNotificationsController {

    ICargoNotificationsControllerCallback mCallback;

    public CargoNotificationsController(ICargoNotificationsControllerCallback callback) {
        mCallback = callback;
    }

    @Override
    public void onNotifyController(String token) {
        ApiService api = RetroClient.getApiService();
        mCallback.showLoadingDialog(true);

        Call<VendorNotifications> call = api.getVendorAnnouncement(token);

        call.enqueue(new Callback<VendorNotifications>() {
            @Override
            public void onResponse(Call<VendorNotifications> call, Response<VendorNotifications> response) {
                mCallback.showLoadingDialog(false);
                if (response.isSuccessful()) {

                    mCallback.onGetNotifyDetails(response.body());

                } else {
                    mCallback.onGetNotifyDetailsFailed("Failed");
                }
            }

            @Override
            public void onFailure(Call<VendorNotifications> call, Throwable t) {
                mCallback.showLoadingDialog(false);
            }
        });
    }
}
