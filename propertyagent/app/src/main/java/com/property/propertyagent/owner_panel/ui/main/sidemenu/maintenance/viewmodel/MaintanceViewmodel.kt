package com.property.propertyagent.owner_panel.ui.main.sidemenu.maintenance.viewmodel

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyagent.data.ApiRepositoryProvider
import com.property.propertyagent.modal.owner.owner_maintance.OwnerMaintenanceServiceResponse
import com.property.propertyagent.utils.Constants
import com.property.propertyagent.utils.Resource
import kotlinx.coroutines.launch
import java.net.UnknownHostException


class MaintanceViewmodel : ViewModel() {

    private val maintanaceLiveData = MutableLiveData<Resource<OwnerMaintenanceServiceResponse>>()

    val maintanaceData : LiveData<Resource<OwnerMaintenanceServiceResponse>>
        get() = maintanaceLiveData

    fun maintance(page : String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                maintanaceLiveData.postValue(Resource.loading(null))
                repository.getMaintains(page).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        maintanaceLiveData.postValue(Resource.success(response))
                    } else {
                        maintanaceLiveData.postValue(Resource.error(it.message() , response))
                    }
                }
            } catch (ex : UnknownHostException) {
                maintanaceLiveData.postValue(Resource.noInternet("Please check your internet connection" ,
                    null))

            } catch (ex : NetworkErrorException) {
                maintanaceLiveData.postValue(Resource.noInternet("Please check your internet connection" ,
                    null))

            } catch (ex : Exception) {
                maintanaceLiveData.postValue(Resource.error("Something went wrong" , null))
            }
        }
    }
}