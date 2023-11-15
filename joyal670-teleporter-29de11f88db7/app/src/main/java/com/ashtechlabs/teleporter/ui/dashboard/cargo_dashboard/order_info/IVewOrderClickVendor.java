package com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.order_info;

import com.ashtechlabs.teleporter.ui.OnOrderInfoAdapterCallback;
import com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.pojo.VendorList;

/**
 * Created by Jesna on 6/1/2017.
 */

public interface IVewOrderClickVendor extends OnOrderInfoAdapterCallback{
    void onViewOrder(VendorList vendorList, int pos);
}
