package com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard.order_info_detail;

import com.ashtechlabs.teleporter.ui.ICommonCallback;

/**
 * Created by Jesna on 6/21/2017.
 */

public interface IOrderDetailDriverActivityControllerCallback extends ICommonCallback {

    void getDeliveryAndPickupAddressDriver(String Address);

    void getDeliveryAndPickupAddressDriverFailed(String Failed);
}
