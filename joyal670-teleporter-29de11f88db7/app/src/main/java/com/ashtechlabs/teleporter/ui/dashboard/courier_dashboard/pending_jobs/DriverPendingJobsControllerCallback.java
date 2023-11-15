package com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard.pending_jobs;

import com.ashtechlabs.teleporter.ui.ICommonProgressCallback;
import com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard.pojo.DriverOrder;

/**
 * Created by IROID_ANDROID1 on 07-Mar-17.
 */

public interface DriverPendingJobsControllerCallback extends ICommonProgressCallback {

    void onGetPendingJobs(DriverOrder deliveryServices);

    void onGetPendingJobsFailed(String message);

}
