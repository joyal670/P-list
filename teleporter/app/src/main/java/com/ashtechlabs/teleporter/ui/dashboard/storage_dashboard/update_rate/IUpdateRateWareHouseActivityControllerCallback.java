package com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.update_rate;

import com.ashtechlabs.teleporter.ui.ICommonProgressCallback;

/**
 * Created by Jesna on 6/14/2017.
 */

public interface IUpdateRateWareHouseActivityControllerCallback extends ICommonProgressCallback {
    void onUpdateWareHouseRateSuccess(String message);

    void onUpdateWareHouseRateFailed(String message);
}
