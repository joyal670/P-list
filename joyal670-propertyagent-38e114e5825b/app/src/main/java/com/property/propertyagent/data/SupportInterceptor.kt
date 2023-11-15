package com.property.propertyagent.data

import com.property.propertyagent.utils.AppPreferences.token
import okhttp3.Interceptor
import okhttp3.Response

class SupportInterceptor : Interceptor {

    override fun intercept(chain : Interceptor.Chain) : Response {
        var request = chain.request()
        request = request.newBuilder()
            .addHeader("Content-Type" , "application/json")
            .addHeader("Accept" , "application/json")
            .addHeader("Authorization" , "Bearer $token")
            .build()
        return chain.proceed(request)
    }

}