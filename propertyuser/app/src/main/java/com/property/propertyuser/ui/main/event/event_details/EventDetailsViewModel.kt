package com.property.propertyuser.ui.main.event.event_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyuser.data.api.ApiRepositoryProvider
import com.property.propertyuser.modal.event_details.EventDetailsResponse
import com.property.propertyuser.modal.events_list.EventListResponse
import com.property.propertyuser.utils.Constants
import com.property.propertyuser.utils.Resource
import kotlinx.coroutines.launch

class EventDetailsViewModel:ViewModel() {
    private val eventDetailsResponseLiveData= MutableLiveData<Resource<EventDetailsResponse>>()

    fun fetchEventsDetails(event_id: String){
        val repository= ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            eventDetailsResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.eventDetails(event_id).let {
                    val response=it.body()
                    if(response!!.status== Constants.REQUEST_OK){
                        eventDetailsResponseLiveData.postValue(Resource.success(response))
                    }
                    if(response!!.status== Constants.REQUEST_BAD_REQUEST){
                        eventDetailsResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                    if(response!!.status== Constants.REQUEST_UNAUTHORIZED){
                        eventDetailsResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                }

            }catch (ex:Exception){
                eventDetailsResponseLiveData.postValue(Resource.noInternet("",null))
            }
        }
    }

    fun getEventDetailsResponse(): LiveData<Resource<EventDetailsResponse>> {
        return eventDetailsResponseLiveData
    }
}