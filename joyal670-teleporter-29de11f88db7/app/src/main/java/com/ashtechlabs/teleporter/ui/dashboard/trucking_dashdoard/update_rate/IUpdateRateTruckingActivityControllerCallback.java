package com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.update_rate;

import com.ashtechlabs.teleporter.ui.ICommonProgressCallback;

/**
 * Created by Jesna on 6/14/2017.
 */

public interface IUpdateRateTruckingActivityControllerCallback extends ICommonProgressCallback {
    void onUpdateRateSuccess(String message);
    void onUpdateRateFailed(String message);
}
