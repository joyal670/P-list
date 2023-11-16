package com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.warehouse_order_info;

import com.ashtechlabs.teleporter.ui.OnOrderInfoAdapterCallback;
import com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.pojo.WareHouseList;

/**
 * Created by Jesna on 6/1/2017.
 */

public interface IVewWareHouseOrderClick extends OnOrderInfoAdapterCallback{
    void onViewOrder(WareHouseList wareHouseList, int pos);
}
