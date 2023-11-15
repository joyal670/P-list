package com.property.propertyuser.ui.main.side_menu.faq

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

class FAQViewModel:ViewModel() {
    private val faqListDetailsResponseLiveData= MutableLiveData<Resource<FAQResponse>>()

    fun fetchFaqListDetail(page:String){
        val repository= ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            faqListDetailsResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.faqListDetails(page).let {
                    val response=it.body()
                    if(response!!.status== Constants.REQUEST_OK){
                        faqListDetailsResponseLiveData.postValue(Resource.success(response))
                    }
                    if(response!!.status== Constants.INTERNAL_SERVER_ERROR){
                        faqListDetailsResponseLiveData.postValue(Resource.error("null",response))
                    }
                    if(response!!.status== Constants.REQUEST_BAD_REQUEST){
                        faqListDetailsResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                    if(response!!.status== Constants.REQUEST_UNAUTHORIZED){
                        faqListDetailsResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                }

            }catch (ex:Exception){
                faqListDetailsResponseLiveData.postValue(Resource.noInternet("",null))
            }
        }
    }

    fun getFaqListDetailsResponse(): LiveData<Resource<FAQResponse>> {
        return faqListDetailsResponseLiveData
    }

}