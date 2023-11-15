package com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.add_rate;

import com.ashtechlabs.teleporter.ui.ICommonCallback;

/**
 * Created by IROID_ANDROID1 on 20-Jan-17.
 */

public interface UpdateRateVendorsControllerCallback extends ICommonCallback{
    void onGetUpdateVendor(String customer);

    void showMessage(String message);
}
