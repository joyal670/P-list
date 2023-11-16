package com.iroid.emergency.api

import android.util.Log
import com.iroid.emergency.preference.AppPreferences
import okhttp3.Interceptor
import okhttp3.Response

class SupportInterceptor: Interceptor  {

    override fun intercept(chain: Interceptor.Chain): Response {
        Log.e("#123456", AppPreferences.userToken.toString())
        var request = chain.request()
        request = request?.newBuilder()
            ?.addHeader("Content-Type", "application/json")
            ?.addHeader("Accept", "application/json")
            ?.addHeader("Authorization", "Bearer "+ AppPreferences.userToken)
            ?.build()!!
        return chain.proceed(request)
    }

}
