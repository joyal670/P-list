package com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.pending_jobs;

import com.ashtechlabs.teleporter.ui.ICommonProgressCallback;
import com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.pojo.PendingJob;

/**
 * Created by IROID_ANDROID1 on 07-Mar-17.
 */

public interface WarehousePendingJobsControllerCallback extends ICommonProgressCallback{

    void onGetPendingJobs(PendingJob deliveryServices);

    void onGetPendingJobsFailed(String message);

}
