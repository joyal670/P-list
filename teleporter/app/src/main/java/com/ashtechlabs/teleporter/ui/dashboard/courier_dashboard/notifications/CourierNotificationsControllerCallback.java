package com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard.notifications;

import com.ashtechlabs.teleporter.ui.ICommonCallback;
import com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard.pojo.CourierNotifications;

/**
 * Created by IROID_ANDROID1 on 07-Mar-17.
 */

public interface CourierNotificationsControllerCallback extends ICommonCallback{

    void onGetNotifyDetails(CourierNotifications notify);
    void onGetNotifyDetailsFailed(String notify);

}
