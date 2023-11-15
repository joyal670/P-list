package com.ashtechlabs.teleporter.ui.dashboard.jobstate_controller;

import com.ashtechlabs.teleporter.ui.ICommonProgressCallback;

/**
 * Created by IROID_ANDROID1 on 19-Jan-17.
 */

public interface JobStateControllerCallback extends ICommonProgressCallback{

    void onGetJobStateSuccess(String message);

    void onGetJobStateFailed(String message);

}
