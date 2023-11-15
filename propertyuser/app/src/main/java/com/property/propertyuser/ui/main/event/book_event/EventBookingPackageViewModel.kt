package com.property.propertyuser.ui.main.event.book_event

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyuser.data.api.ApiRepositoryProvider
import com.property.propertyuser.modal.event_book_package.EventBookPackageResponse
import com.property.propertyuser.modal.event_booking_package_details.EventBookingPackageDetailsResponse
import com.property.propertyuser.modal.event_details.EventDetailsResponse
import com.property.propertyuser.modal.event_package.EventPackageResponse
import com.property.propertyuser.utils.Constants
import com.property.propertyuser.utils.Resource
import kotlinx.coroutines.launch

class EventBookingPackageViewModel:ViewModel() {

    private val eventBookingPackageResponseLiveData= MutableLiveData<Resource<EventBookingPackageDetailsResponse>>()

    fun fetchEventsBookingPackagesDetails(package_id: String){
        val repository= ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            eventBookingPackageResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.eventBookingPackageDetails(package_id.toInt()).let {
                    val response=it.body()
                    if(response!!.status== Constants.REQUEST_OK){
                        eventBookingPackageResponseLiveData.postValue(Resource.success(response))
                    }
                    if(response!!.status== Constants.REQUEST_BAD_REQUEST){
                        eventBookingPackageResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                    if(response!!.status== Constants.REQUEST_UNAUTHORIZED){
                        eventBookingPackageResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                }

            }catch (ex:Exception){
                eventBookingPackageResponseLiveData.postValue(Resource.noInternet("",null))
            }
        }
    }

    fun getEventBookingPackageDetailsResponse(): LiveData<Resource<EventBookingPackageDetailsResponse>> {
        return eventBookingPackageResponseLiveData
    }

    private val eventBookPackageResponseLiveData= MutableLiveData<Resource<EventBookPackageResponse>>()

    fun fetchEventsBookPackagesDetails(package_id: String, name: String, email: String,
                                       phone: String, amount: String, person_count: String){
        val repository= ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            eventBookPackageResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.eventBookPackage(package_id,
                    name,email,phone,amount,person_count).let {
                    val response=it.body()
                    if(response!!.status== Constants.REQUEST_OK){
                        eventBookPackageResponseLiveData.postValue(Resource.success(response))
                    }
                    if(response!!.status== Constants.REQUEST_BAD_REQUEST){
                        eventBookPackageResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                    if(response!!.status== Constants.REQUEST_UNAUTHORIZED){
                        eventBookPackageResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                }

            }catch (ex:Exception){
                eventBookPackageResponseLiveData.postValue(Resource.noInternet("",null))
            }
        }
    }

    fun getEventBookPackageDetailsResponse(): LiveData<Resource<EventBookPackageResponse>> {
        return eventBookPackageResponseLiveData
    }
}