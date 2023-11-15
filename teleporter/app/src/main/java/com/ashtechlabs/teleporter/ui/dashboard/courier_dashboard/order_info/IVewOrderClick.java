package com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard.order_info;

import com.ashtechlabs.teleporter.ui.OnOrderInfoAdapterCallback;
import com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard.pojo.MyLogsAndOrderInfoDriver;

/**
 * Created by Jesna on 6/1/2017.
 */

public interface IVewOrderClick extends OnOrderInfoAdapterCallback{
    void onViewOrder(MyLogsAndOrderInfoDriver myLogsAndOrderInfoDriver, int pos);
}
