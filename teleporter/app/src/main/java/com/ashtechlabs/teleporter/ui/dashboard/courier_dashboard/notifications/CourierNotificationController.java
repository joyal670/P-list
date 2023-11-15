package com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard.notifications;

import com.ashtechlabs.teleporter.app_prefs.GlobalPreferManager;
import com.ashtechlabs.teleporter.retrofit.ApiService;
import com.ashtechlabs.teleporter.retrofit.RetroClient;
import com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard.pojo.CourierNotifications;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by IROID_ANDROID1 on 07-Mar-17.
 */

public class CourierNotificationController implements ICourierNotificationsController {

    CourierNotificationsControllerCallback mCallback;

    public CourierNotificationController(CourierNotificationsControllerCallback callback) {
        mCallback = callback;
    }

    @Override
    public void onNotifyController(String token) {
        ApiService api = RetroClient.getApiService();
        mCallback.showLoadingDialog(true);

        Call<CourierNotifications> call = api.getDriverAnnouncement(token);

        call.enqueue(new Callback<CourierNotifications>() {
            @Override
            public void onResponse(Call<CourierNotifications> call, Response<CourierNotifications> response) {
                mCallback.showLoadingDialog(false);
                if (response.isSuccessful()) {

                    mCallback.onGetNotifyDetails(response.body());

                } else {
                    mCallback.onGetNotifyDetailsFailed("Failed");
                }
            }

            @Override
            public void onFailure(Call<CourierNotifications> call, Throwable t) {
                mCallback.showLoadingDialog(false);
            }
        });
    }
}
