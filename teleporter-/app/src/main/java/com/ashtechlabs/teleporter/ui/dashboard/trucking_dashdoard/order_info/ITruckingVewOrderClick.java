package com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.order_info;

import com.ashtechlabs.teleporter.ui.OnOrderInfoAdapterCallback;
import com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.pojo.MyLogsAndOrderInfoTrucking;
import com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.pojo.TruckingMpService;

/**
 * Created by Jesna on 6/1/2017.
 */

public interface ITruckingVewOrderClick extends OnOrderInfoAdapterCallback{
    void onViewOrder(TruckingMpService myLogsAndOrderInfoDriver, int pos);
}
