package com.ashtechlabs.teleporter.ui.login;

import com.google.gson.JsonObject;
import com.ashtechlabs.teleporter.ui.ICommonProgressCallback;

/**
 * Created by IROID_ANDROID1 on 28-Feb-17.
 */

public interface ForgotPasswordControllerCallback extends ICommonProgressCallback{
    void onGetForgotPasswordDetails(JsonObject password);

    void onGetForgotPasswordFailed(String message);

}
