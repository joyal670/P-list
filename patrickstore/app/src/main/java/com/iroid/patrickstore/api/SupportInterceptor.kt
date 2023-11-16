package com.iroid.patrickstore.api

import android.util.Log
import com.iroid.patrickstore.preference.AppPreferences
import com.iroid.patrickstore.preference.AppPreferences.jwt_token
import okhttp3.Interceptor
import okhttp3.Response

class SupportInterceptor: Interceptor  {

    override fun intercept(chain: Interceptor.Chain): Response {
        Log.e("12345", "intercept: ${AppPreferences.jwt_token}" )
        var request = chain.request()
        request = request?.newBuilder()
            ?.addHeader("Content-Type", "application/json")
            ?.addHeader("Accept", "application/json")
            ?.addHeader("Authorization", "Bearer "+ AppPreferences.jwt_token)
            ?.build()!!
        return chain.proceed(request)
    }

}
