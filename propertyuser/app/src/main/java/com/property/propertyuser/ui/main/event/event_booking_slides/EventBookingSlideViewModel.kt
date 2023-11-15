package com.property.propertyuser.ui.main.event.event_booking_slides

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyuser.data.api.ApiRepositoryProvider
import com.property.propertyuser.modal.event_details.EventDetailsResponse
import com.property.propertyuser.modal.event_package.EventPackageResponse
import com.property.propertyuser.utils.Constants
import com.property.propertyuser.utils.Resource
import kotlinx.coroutines.launch

class EventBookingSlideViewModel:ViewModel() {

    private val eventPackageResponseLiveData= MutableLiveData<Resource<EventPackageResponse>>()

    fun fetchEventsPackages(event_id: String){
        val repository= ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            eventPackageResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.eventPackageDetails(event_id.toInt()).let {
                    val response=it.body()
                    if(response!!.status== Constants.REQUEST_OK){
                        eventPackageResponseLiveData.postValue(Resource.success(response))
                    }
                    if(response!!.status== Constants.REQUEST_BAD_REQUEST){
                        eventPackageResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                    if(response!!.status== Constants.REQUEST_UNAUTHORIZED){
                        eventPackageResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                }

            }catch (ex:Exception){
                eventPackageResponseLiveData.postValue(Resource.noInternet("",null))
            }
        }
    }

    fun getEventPackageResponse(): LiveData<Resource<EventPackageResponse>> {
        return eventPackageResponseLiveData
    }
}