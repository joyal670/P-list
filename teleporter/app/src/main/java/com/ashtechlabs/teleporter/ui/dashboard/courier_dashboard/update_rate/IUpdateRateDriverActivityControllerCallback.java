package com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard.update_rate;

import com.ashtechlabs.teleporter.ui.ICommonProgressCallback;

/**
 * Created by Jesna on 6/14/2017.
 */

public interface IUpdateRateDriverActivityControllerCallback extends ICommonProgressCallback {
    void onUpdateRateSuccess(String message);
    void onUpdateRateFailed(String message);
}
