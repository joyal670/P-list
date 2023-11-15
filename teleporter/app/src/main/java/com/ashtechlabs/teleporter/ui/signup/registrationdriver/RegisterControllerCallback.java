package com.ashtechlabs.teleporter.ui.signup.registrationdriver;

import com.google.gson.JsonObject;
import com.ashtechlabs.teleporter.ui.ICommonProgressCallback;

/**
 * Created by IROID_ANDROID1 on 20-Jan-17.
 */

public interface RegisterControllerCallback extends ICommonProgressCallback{
    void onGetRegisterDetails(JsonObject customer);

    void onRegisterFailed(String message);

}
