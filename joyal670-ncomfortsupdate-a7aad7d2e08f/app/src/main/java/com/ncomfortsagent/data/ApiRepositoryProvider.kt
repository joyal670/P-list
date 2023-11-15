package com.ncomfortsagent.data

object ApiRepositoryProvider {
    fun providerApiRepository(): MainRepository {
        return MainRepository(ApiService.create())
    }
}