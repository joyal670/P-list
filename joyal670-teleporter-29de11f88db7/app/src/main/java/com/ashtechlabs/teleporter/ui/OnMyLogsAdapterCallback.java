package com.ashtechlabs.teleporter.ui;

import com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard.pojo.MyLogsAndOrderInfoDriver;

/**
 * Created by VIDHU on 3/30/2017.
 */

public interface OnMyLogsAdapterCallback {

    void showProgressAdapter();

    void dismissProgressAdapter();

    void showMessageAlert(String title, String message);

    void onViewInMapClick(MyLogsAndOrderInfoDriver myLogsAndOrderInfoDriver);

}
