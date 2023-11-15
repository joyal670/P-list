package com.proteinium.proteiniumdietapp.data.api

import android.util.Log
import com.proteinium.proteiniumdietapp.preference.AppPreferences
import okhttp3.Interceptor
import okhttp3.Response

class SupportInterceptor: Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        Log.e("1232", "intercept: "+AppPreferences.token )
        var request = chain.request()
        request = request.newBuilder()
            .addHeader("Content-Type", "application/json")
            .addHeader("Accept", "application/json")
            .addHeader("Authorization", "Bearer "+ AppPreferences.token)
            .build()
        return chain.proceed(request)
    }

}