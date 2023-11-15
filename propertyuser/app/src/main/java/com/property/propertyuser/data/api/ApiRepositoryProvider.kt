package com.property.propertyuser.data.api

object ApiRepositoryProvider {
    fun providerApiRepository(): MainRepository {
        return MainRepository(ApiService.create("API"))
    }
    fun providerGoogleMapsApiRepository(): MainRepository {
        return MainRepository(ApiService.create("GOOGLE_API"))
    }
}