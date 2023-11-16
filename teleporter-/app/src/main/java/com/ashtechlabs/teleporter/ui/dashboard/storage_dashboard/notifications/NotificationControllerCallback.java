package com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.notifications;

import com.ashtechlabs.teleporter.ui.ICommonCallback;
import com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.pojo.WarehouseNotification;

/**
 * Created by IROID_ANDROID1 on 07-Mar-17.
 */

public interface NotificationControllerCallback extends ICommonCallback{
    void onGetNotifyDetails(WarehouseNotification notify);

}
