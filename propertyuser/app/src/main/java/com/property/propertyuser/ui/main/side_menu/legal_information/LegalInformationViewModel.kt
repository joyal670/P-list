package com.property.propertyuser.ui.main.side_menu.legal_information

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyuser.data.api.ApiRepositoryProvider
import com.property.propertyuser.modal.CommonResponse
import com.property.propertyuser.modal.faq.FAQResponse
import com.property.propertyuser.modal.legal_informations.LegalInformationResponse
import com.property.propertyuser.utils.Constants
import com.property.propertyuser.utils.Resource
import kotlinx.coroutines.launch

class LegalInformationViewModel:ViewModel() {
    private val legalInformationListDetailsResponseLiveData= MutableLiveData<Resource<LegalInformationResponse>>()

    fun fetchLegalInformationListDetail(page:String){
        val repository= ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            legalInformationListDetailsResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.legalInformationListDetails(page).let {
                    val response=it.body()
                    if(response!!.status== Constants.REQUEST_OK){
                        legalInformationListDetailsResponseLiveData.postValue(Resource.success(response))
                    }
                    if(response!!.status== Constants.INTERNAL_SERVER_ERROR){
                        legalInformationListDetailsResponseLiveData.postValue(Resource.error("null",response))
                    }
                    if(response!!.status== Constants.REQUEST_BAD_REQUEST){
                        legalInformationListDetailsResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                    if(response!!.status== Constants.REQUEST_UNAUTHORIZED){
                        legalInformationListDetailsResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                }

            }catch (ex:Exception){
                legalInformationListDetailsResponseLiveData.postValue(Resource.noInternet("",null))
            }
        }
    }

    fun getLegalInformationListDetailsResponse(): LiveData<Resource<LegalInformationResponse>> {
        return legalInformationListDetailsResponseLiveData
    }

}