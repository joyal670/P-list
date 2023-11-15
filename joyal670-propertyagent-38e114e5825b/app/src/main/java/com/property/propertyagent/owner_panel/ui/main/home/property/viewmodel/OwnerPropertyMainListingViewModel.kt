package com.property.propertyagent.owner_panel.ui.main.home.property.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyagent.data.ApiRepositoryProvider
import com.property.propertyagent.modal.owner.owner_property_main_listing.OwnerPropertyMainListingResponse
import com.property.propertyagent.modal.owner.owner_property_types.OwnerPropertyTypesResponse
import com.property.propertyagent.utils.Constants
import com.property.propertyagent.utils.Resource
import kotlinx.coroutines.launch

class OwnerPropertyMainListingViewModel : ViewModel() {

    private val ownerPropertyMainListingResponseLiveData =
        MutableLiveData<Resource<OwnerPropertyMainListingResponse>>()

    private val ownerPropertyTypesListingResponseLiveData =
        MutableLiveData<Resource<OwnerPropertyTypesResponse>>()

    private val ownerPropertyFilterListingResponseLiveData =
        MutableLiveData<Resource<OwnerPropertyMainListingResponse>>()

    /* property listing */
    fun propertyList(page: String, category: String, list_id: String, verification_id: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            ownerPropertyMainListingResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.ownerPropertyMainListing(page, category, list_id, verification_id).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        ownerPropertyMainListingResponseLiveData.postValue(Resource.success(response))
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        ownerPropertyMainListingResponseLiveData.postValue(
                            Resource.error(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        ownerPropertyMainListingResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        ownerPropertyMainListingResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                }

            } catch (ex: Exception) {
                ownerPropertyMainListingResponseLiveData.postValue(Resource.noInternet("", null))
                Log.e("TAG", "paymentListOfProperties: $ex")
            }
        }
    }

    fun getOwnerPaymentResponse(): LiveData<Resource<OwnerPropertyMainListingResponse>> {
        return ownerPropertyMainListingResponseLiveData
    }

    /* property type listing */
    fun propertyTypes() {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            ownerPropertyTypesListingResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.ownerPropertyTypes().let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        ownerPropertyTypesListingResponseLiveData.postValue(
                            Resource.success(
                                response
                            )
                        )
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        ownerPropertyTypesListingResponseLiveData.postValue(
                            Resource.error(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        ownerPropertyTypesListingResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        ownerPropertyTypesListingResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                }

            } catch (ex: Exception) {
                ownerPropertyTypesListingResponseLiveData.postValue(Resource.noInternet("", null))
                Log.e("TAG", "paymentListOfProperties: $ex")
            }
        }
    }

    fun getOwnerPropertyTypesResponse(): LiveData<Resource<OwnerPropertyTypesResponse>> {
        return ownerPropertyTypesListingResponseLiveData
    }

    /* property filter listing */
    fun propertyFilterListing(page: String, type_id: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            ownerPropertyFilterListingResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.ownerPropertyFilterList(page, type_id).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        ownerPropertyFilterListingResponseLiveData.postValue(
                            Resource.success(
                                response
                            )
                        )
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        ownerPropertyFilterListingResponseLiveData.postValue(
                            Resource.error(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        ownerPropertyFilterListingResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        ownerPropertyFilterListingResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                }

            } catch (ex: Exception) {
                ownerPropertyFilterListingResponseLiveData.postValue(Resource.noInternet("", null))
                Log.e("TAG", "paymentListOfProperties: $ex")
            }
        }
    }

    fun getOwnerPropertyFilterListingResponse(): LiveData<Resource<OwnerPropertyMainListingResponse>> {
        return ownerPropertyFilterListingResponseLiveData
    }
}