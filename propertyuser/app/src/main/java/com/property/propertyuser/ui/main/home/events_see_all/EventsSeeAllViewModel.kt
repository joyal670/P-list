package com.property.propertyuser.ui.main.home.events_see_all

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyuser.data.api.ApiRepositoryProvider
import com.property.propertyuser.modal.events_list.EventListResponse
import com.property.propertyuser.utils.Constants
import com.property.propertyuser.utils.Resource
import kotlinx.coroutines.launch

class EventsSeeAllViewModel:ViewModel() {
    private val eventsListResponseLiveData= MutableLiveData<Resource<EventListResponse>>()

    fun fetchEventsList(lat: String,lan: String,page:String){
        val repository= ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            eventsListResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.listEvents(lat,lan,page).let {
                    val response=it.body()
                    if(response!!.status== Constants.REQUEST_OK){
                        eventsListResponseLiveData.postValue(Resource.success(response))
                    }
                    if(response!!.status== Constants.REQUEST_BAD_REQUEST){
                        eventsListResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                    if(response!!.status== Constants.REQUEST_UNAUTHORIZED){
                        eventsListResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                }

            }catch (ex:Exception){
                eventsListResponseLiveData.postValue(Resource.noInternet("",null))
            }
        }
    }

    fun getEventsListResponse(): LiveData<Resource<EventListResponse>> {
        return eventsListResponseLiveData
    }
}