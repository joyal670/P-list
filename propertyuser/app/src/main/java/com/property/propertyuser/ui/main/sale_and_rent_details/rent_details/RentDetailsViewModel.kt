package com.property.propertyuser.ui.main.sale_and_rent_details.rent_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyuser.data.api.ApiRepositoryProvider
import com.property.propertyuser.modal.property_rental_details.PropertyRentalDetailsResponse
import com.property.propertyuser.utils.Constants
import com.property.propertyuser.utils.Resource
import kotlinx.coroutines.launch

class RentDetailsViewModel:ViewModel() {
    private val propertyRentalDetailsResponseLiveData= MutableLiveData<Resource<PropertyRentalDetailsResponse>>()

    fun fetchPropertyRentalDetails(property_id: String){
        val repository= ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            propertyRentalDetailsResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.propertyRentalDetails(property_id).let {
                    val response=it.body()
                    if(response!!.status== Constants.REQUEST_OK){
                        propertyRentalDetailsResponseLiveData.postValue(Resource.success(response))
                    }
                    if(response!!.status== Constants.INTERNAL_SERVER_ERROR){
                        propertyRentalDetailsResponseLiveData.postValue(Resource.error("null",response))
                    }
                    if(response!!.status== Constants.REQUEST_BAD_REQUEST){
                        propertyRentalDetailsResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                    if(response!!.status== Constants.REQUEST_UNAUTHORIZED){
                        propertyRentalDetailsResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                }

            }catch (ex:Exception){
                propertyRentalDetailsResponseLiveData.postValue(Resource.noInternet("",null))
            }
        }
    }

    fun getPropertyRentalDetailsResponse(): LiveData<Resource<PropertyRentalDetailsResponse>> {
        return propertyRentalDetailsResponseLiveData
    }
}