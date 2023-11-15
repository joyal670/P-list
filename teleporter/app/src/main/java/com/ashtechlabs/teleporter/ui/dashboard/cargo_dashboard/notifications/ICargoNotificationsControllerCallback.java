package com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.notifications;

import com.ashtechlabs.teleporter.ui.ICommonCallback;
import com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.pojo.VendorNotifications;

/**
 * Created by IROID_ANDROID1 on 07-Mar-17.
 */

public interface ICargoNotificationsControllerCallback extends ICommonCallback{
    void onGetNotifyDetails(VendorNotifications notify);
    void onGetNotifyDetailsFailed(String message);

}
