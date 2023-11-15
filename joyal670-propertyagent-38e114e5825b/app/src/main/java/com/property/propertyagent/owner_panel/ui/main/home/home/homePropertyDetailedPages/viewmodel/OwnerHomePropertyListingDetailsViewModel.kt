package com.property.propertyagent.owner_panel.ui.main.home.home.homePropertyDetailedPages.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyagent.data.ApiRepositoryProvider
import com.property.propertyagent.modal.owner.owner_property_main_listing.OwnerPropertyMainListingResponse
import com.property.propertyagent.utils.Constants
import com.property.propertyagent.utils.Resource
import kotlinx.coroutines.launch

class OwnerHomePropertyListingDetailsViewModel : ViewModel() {

    private val ownerHomePropertyListingDetailsResponseLiveData =
        MutableLiveData<Resource<OwnerPropertyMainListingResponse>>()

    fun homePropertyListingDetails(page : String , list_id : String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            ownerHomePropertyListingDetailsResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.ownerHomePropertyListingDetails(page , list_id).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        ownerHomePropertyListingDetailsResponseLiveData.postValue(Resource.success(
                            response))
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        ownerHomePropertyListingDetailsResponseLiveData.postValue(Resource.error("null" ,
                            response))
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        ownerHomePropertyListingDetailsResponseLiveData.postValue(Resource.dataEmpty(
                            "null" ,
                            response))
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        ownerHomePropertyListingDetailsResponseLiveData.postValue(Resource.dataEmpty(
                            "null" ,
                            response))
                    }
                }

            } catch (ex : Exception) {
                ownerHomePropertyListingDetailsResponseLiveData.postValue(Resource.noInternet("" ,
                    null))
                Log.e("TAG" , "homePropertyListingDetails: $ex")
            }
        }
    }

    fun getOwnerHomePropertyListingDetails() : LiveData<Resource<OwnerPropertyMainListingResponse>> {
        return ownerHomePropertyListingDetailsResponseLiveData
    }
}