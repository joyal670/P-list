package com.ashtechlabs.teleporter.ui.signup.registerationtrucking;

import com.ashtechlabs.teleporter.ui.ICommonProgressCallback;
import com.google.gson.JsonObject;

/**
 * Created by IROID_ANDROID1 on 20-Jan-17.
 */

public interface RegisterControllerCallback extends ICommonProgressCallback{
    void onGetRegisterDetails(String registrationId);

    void onRegisterFailed(String message);

}
