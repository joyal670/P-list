package com.property.propertyuser.data.api

import com.property.propertyuser.preference.AppPreferences
import com.property.propertyuser.preference.ConstantPreference.USER_TOKEN
import okhttp3.Interceptor
import okhttp3.Response

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class SupportInterceptor: Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        request = request?.newBuilder()
            ?.addHeader("Content-Type", "application/json")
            ?.addHeader("Accept", "application/json")
            ?.addHeader("Authorization", "Bearer "+ AppPreferences.token)
            ?.build()
        return chain.proceed(request)
    }

}