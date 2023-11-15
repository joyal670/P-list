package com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.notifications;

import com.ashtechlabs.teleporter.app_prefs.GlobalPreferManager;
import com.ashtechlabs.teleporter.retrofit.ApiService;
import com.ashtechlabs.teleporter.retrofit.RetroClient;
import com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.pojo.TruckingNotifications;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by IROID_ANDROID1 on 07-Mar-17.
 */

public class TruckingNotificationController implements ITruckingNotificationsController {

    TruckingNotificationsControllerCallback mCallback;

    public TruckingNotificationController(TruckingNotificationsControllerCallback callback) {
        mCallback = callback;
    }

    @Override
    public void onNotifyController(String token) {
        ApiService api = RetroClient.getApiService();
        mCallback.showLoadingDialog(true);

        Call<TruckingNotifications> call = api.getTruckingAnnouncement(token);

        call.enqueue(new Callback<TruckingNotifications>() {
            @Override
            public void onResponse(Call<TruckingNotifications> call, Response<TruckingNotifications> response) {
                mCallback.showLoadingDialog(false);
                if (response.isSuccessful()) {

                    mCallback.onGetNotifyDetails(response.body());

                } else {
                    mCallback.onGetNotifyDetailsFailed("Failed");
                }
            }

            @Override
            public void onFailure(Call<TruckingNotifications> call, Throwable t) {
                mCallback.showLoadingDialog(false);
            }
        });
    }
}
