package com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.update_rate;

import com.ashtechlabs.teleporter.ui.ICommonProgressCallback;

/**
 * Created by Jesna on 6/14/2017.
 */

public interface IUpdateRateVendorActivityControllerCallback extends ICommonProgressCallback {
    void onUpdateVendorRateSuccess(String message);
    void onUpdateVendorRateFailed(String message);
}
