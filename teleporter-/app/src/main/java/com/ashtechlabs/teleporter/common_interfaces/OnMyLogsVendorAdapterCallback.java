package com.ashtechlabs.teleporter.common_interfaces;

import com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.pojo.VendorList;

/**
 * Created by VIDHU on 3/30/2017.
 */

public interface OnMyLogsVendorAdapterCallback {

    void showProgressAdapter();

    void dismissProgressAdapter();

    void showMessageAlert(String title, String message);

    void onViewInMapClick(VendorList myLogsAndOrderInfoDriver);

}
