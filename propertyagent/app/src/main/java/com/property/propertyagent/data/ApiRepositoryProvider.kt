package com.property.propertyagent.data

object ApiRepositoryProvider {
    fun providerApiRepository() : MainRepository {
        return MainRepository(ApiService.create())
    }
}