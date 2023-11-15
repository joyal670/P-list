package com.iroid.emergency.api

import com.iroid.emergency.api.ApiService
import com.iroid.emergency.api.MainRepository


object ApiRepositoryProvider {
    fun providerApiRepository(): MainRepository {
        return MainRepository(ApiService.create())
    }
}
