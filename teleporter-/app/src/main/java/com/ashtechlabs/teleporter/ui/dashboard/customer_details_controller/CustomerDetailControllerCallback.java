package com.ashtechlabs.teleporter.ui.dashboard.customer_details_controller;

import com.ashtechlabs.teleporter.ui.ICommonProgressCallback;

/**
 * Created by IROID_ANDROID1 on 20-Jan-17.
 */

public interface CustomerDetailControllerCallback extends ICommonProgressCallback {

    void onGetCustomerDetails(String name, String phone);

    void onGetCustomerDetailsFailed(String message);

}
