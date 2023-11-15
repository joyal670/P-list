package com.proteinium.proteiniumdietapp.data.api

object ApiRepositoryProvider {
    fun providerApiRepository(): MainRepository {
        return MainRepository(ApiService.create())
    }
}