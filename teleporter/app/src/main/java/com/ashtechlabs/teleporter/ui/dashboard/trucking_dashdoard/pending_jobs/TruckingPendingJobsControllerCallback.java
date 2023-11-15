package com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.pending_jobs;

import com.ashtechlabs.teleporter.ui.ICommonProgressCallback;
import com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.pojo.TruckingOrder;

/**
 * Created by IROID_ANDROID1 on 07-Mar-17.
 */

public interface TruckingPendingJobsControllerCallback extends ICommonProgressCallback {

    void onGetPendingJobs(TruckingOrder deliveryServices);

    void onGetPendingJobsFailed(String message);

}
