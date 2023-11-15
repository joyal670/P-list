package com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.notifications;

import com.ashtechlabs.teleporter.app_prefs.GlobalPreferManager;
import com.ashtechlabs.teleporter.retrofit.ApiService;
import com.ashtechlabs.teleporter.retrofit.RetroClient;
import com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.pojo.WarehouseNotification;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by IROID_ANDROID1 on 07-Mar-17.
 */

public class NotificationController implements INotificationsController {

    NotificationControllerCallback mCallback;

    public NotificationController(NotificationControllerCallback callback) {
        mCallback = callback;
    }

    @Override
    public void onNotifyController(String token) {
        ApiService api = RetroClient.getApiService();
        mCallback.showLoadingDialog(true);

        Call<WarehouseNotification> call = api.getWarehouseAnnoucement(token);

        call.enqueue(new Callback<WarehouseNotification>() {
            @Override
            public void onResponse(Call<WarehouseNotification> call, Response<WarehouseNotification> response) {
                mCallback.showLoadingDialog(false);
                if (response.isSuccessful()) {

                    mCallback.onGetNotifyDetails(response.body());

                } else {
//                    Toast.makeText(NotificationController.this, "Failed", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<WarehouseNotification> call, Throwable t) {
                mCallback.showLoadingDialog(false);
            }
        });
    }
}
