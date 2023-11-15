package com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.notifications;

import com.ashtechlabs.teleporter.ui.ICommonCallback;
import com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.pojo.TruckingNotifications;

/**
 * Created by IROID_ANDROID1 on 07-Mar-17.
 */

public interface TruckingNotificationsControllerCallback extends ICommonCallback{

    void onGetNotifyDetails(TruckingNotifications notify);
    void onGetNotifyDetailsFailed(String notify);

}
