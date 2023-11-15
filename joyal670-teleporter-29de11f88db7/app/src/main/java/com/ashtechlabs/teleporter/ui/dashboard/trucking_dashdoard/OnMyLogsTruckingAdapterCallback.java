package com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard;

import com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard.pojo.MyLogsAndOrderInfoDriver;
import com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.pojo.MyLogsAndOrderInfoTrucking;
import com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.pojo.TruckingMpService;

/**
 * Created by VIDHU on 3/30/2017.
 */

public interface OnMyLogsTruckingAdapterCallback {

    void showProgressAdapter();

    void dismissProgressAdapter();

    void showMessageAlert(String title, String message);

    void onViewInMapClick(TruckingMpService myLogsAndOrderInfoDriver);

}
