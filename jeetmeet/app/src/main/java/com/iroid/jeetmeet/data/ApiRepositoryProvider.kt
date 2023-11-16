package com.iroid.jeetmeet.data

object ApiRepositoryProvider {
    fun providerApiRepository(): MainRepository {
        return MainRepository(ApiService.create())
    }
}