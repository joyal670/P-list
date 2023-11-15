package com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.order_info_detail;

import com.ashtechlabs.teleporter.ui.ICommonCallback;

/**
 * Created by Jesna on 6/21/2017.
 */

public interface IOrderDetailTruckingActivityControllerCallback extends ICommonCallback {

    void getDeliveryAndPickupAddressDriver(String Address);

    void getDeliveryAndPickupAddressDriverFailed(String Failed);
}
