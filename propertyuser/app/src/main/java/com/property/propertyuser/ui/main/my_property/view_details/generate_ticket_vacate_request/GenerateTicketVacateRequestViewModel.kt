package com.property.propertyuser.ui.main.my_property.view_details.generate_ticket_vacate_request

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyuser.data.api.ApiRepositoryProvider
import com.property.propertyuser.modal.single_vacate_request_details.SingleVacateRequestDetailsResponse
import com.property.propertyuser.modal.vacate_request_list.VacateRequestListResponse
import com.property.propertyuser.utils.Constants
import com.property.propertyuser.utils.Resource
import kotlinx.coroutines.launch

class GenerateTicketVacateRequestViewModel:ViewModel() {

    private val vacateRequestDetailsResponseLiveData=
        MutableLiveData<Resource<SingleVacateRequestDetailsResponse>>()

    fun vacateRequestDetails(vacate_id:String){
        val repository= ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            vacateRequestDetailsResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.singleVacateRequestDetails(vacate_id).let {
                    val response=it.body()
                    if(response!!.status== Constants.REQUEST_OK){
                        vacateRequestDetailsResponseLiveData.postValue(Resource.success(response))
                    }
                    if(response!!.status== Constants.INTERNAL_SERVER_ERROR){
                        vacateRequestDetailsResponseLiveData.postValue(Resource.error("null",response))
                    }
                    if(response!!.status== Constants.REQUEST_BAD_REQUEST){
                        vacateRequestDetailsResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                    if(response!!.status== Constants.REQUEST_UNAUTHORIZED){
                        vacateRequestDetailsResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                }

            }catch (ex:Exception){
                vacateRequestDetailsResponseLiveData.postValue(Resource.noInternet("",null))
            }
        }
    }

    fun getVacateRequestDetailsResponse(): LiveData<Resource<SingleVacateRequestDetailsResponse>> {
        return vacateRequestDetailsResponseLiveData
    }
}