package com.property.propertyuser.ui.main.side_menu.feedback

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyuser.data.api.ApiRepositoryProvider
import com.property.propertyuser.modal.CommonResponse
import com.property.propertyuser.modal.faq.FAQResponse
import com.property.propertyuser.utils.Constants
import com.property.propertyuser.utils.Resource
import kotlinx.coroutines.launch

class FeedbackViewModel: ViewModel() {
    private val feedbackResponseLiveData= MutableLiveData<Resource<CommonResponse>>()

    fun sendFeedback(feedback:String){
        val repository= ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            feedbackResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.sendFeedback(feedback).let {
                    val response=it.body()
                    if(response!!.status== Constants.REQUEST_OK){
                        feedbackResponseLiveData.postValue(Resource.success(response))
                    }
                    if(response!!.status== Constants.INTERNAL_SERVER_ERROR){
                        feedbackResponseLiveData.postValue(Resource.error("null",response))
                    }
                    if(response!!.status== Constants.REQUEST_BAD_REQUEST){
                        feedbackResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                    if(response!!.status== Constants.REQUEST_UNAUTHORIZED){
                        feedbackResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                }

            }catch (ex:Exception){
                feedbackResponseLiveData.postValue(Resource.noInternet("",null))
            }
        }
    }

    fun getFeedbackResponse(): LiveData<Resource<CommonResponse>> {
        return feedbackResponseLiveData
    }

}