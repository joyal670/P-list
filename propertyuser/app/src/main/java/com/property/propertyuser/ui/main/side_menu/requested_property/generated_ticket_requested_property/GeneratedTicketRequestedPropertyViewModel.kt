package com.property.propertyuser.ui.main.side_menu.requested_property.generated_ticket_requested_property

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyuser.data.api.ApiRepositoryProvider
import com.property.propertyuser.modal.desired_property_request_details.DesiredPropertyRequestDetailsResponse
import com.property.propertyuser.modal.fetch_requested_property_data.FetchRequestedPropertyDataResponse
import com.property.propertyuser.utils.Constants
import com.property.propertyuser.utils.Resource
import kotlinx.coroutines.launch

class GeneratedTicketRequestedPropertyViewModel:ViewModel() {
    private val requestedPropertyDetailsResponseLiveData=
        MutableLiveData<Resource<DesiredPropertyRequestDetailsResponse>>()

    fun fetchRequestedPropertyDetails(request_id:String){
        val repository= ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            requestedPropertyDetailsResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.desiredPropertyRequestDetails(request_id).let {
                    val response=it.body()
                    if(response!!.status== Constants.REQUEST_OK){
                        requestedPropertyDetailsResponseLiveData.postValue(Resource.success(response))
                    }
                    if(response!!.status== Constants.REQUEST_BAD_REQUEST){
                        requestedPropertyDetailsResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                    if(response!!.status== Constants.REQUEST_UNAUTHORIZED){
                        requestedPropertyDetailsResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                }

            }catch (ex:Exception){
                requestedPropertyDetailsResponseLiveData.postValue(Resource.noInternet("",null))
            }
        }
    }

    fun getRequestedPropertyDetailsResponse(): LiveData<Resource<DesiredPropertyRequestDetailsResponse>> {
        return requestedPropertyDetailsResponseLiveData
    }
}