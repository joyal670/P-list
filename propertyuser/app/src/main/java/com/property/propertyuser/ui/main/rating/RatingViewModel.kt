package com.property.propertyuser.ui.main.rating

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyuser.data.api.ApiRepositoryProvider
import com.property.propertyuser.modal.CommonResponse
import com.property.propertyuser.modal.user_notification.NotificationResponse
import com.property.propertyuser.utils.Constants
import com.property.propertyuser.utils.Resource
import kotlinx.coroutines.launch

class RatingViewModel:ViewModel() {

    private val ratingResponseLiveData= MutableLiveData<Resource<CommonResponse>>()

    fun fetchRatingDetails(property_id: String, rating: String,agent_feedback: String){
        val repository= ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            ratingResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.ratingAndFeedback(property_id, rating,agent_feedback).let {
                    val response=it.body()
                    if(response!!.status== Constants.REQUEST_OK){
                        ratingResponseLiveData.postValue(Resource.success(response))
                    }
                    if(response!!.status== Constants.REQUEST_BAD_REQUEST){
                        ratingResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                    if(response!!.status== Constants.REQUEST_UNAUTHORIZED){
                        ratingResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                    if(response!!.status== Constants.INTERNAL_SERVER_ERROR){
                        ratingResponseLiveData.postValue(Resource.error("null",response))
                    }
                }

            }catch (ex:Exception){
                ratingResponseLiveData.postValue(Resource.noInternet("",null))
            }
        }
    }

    fun getRatingResponse(): LiveData<Resource<CommonResponse>> {
        return ratingResponseLiveData
    }

}