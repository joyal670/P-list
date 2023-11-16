package com.property.propertyagent.owner_panel.ui.main.sidemenu.change_password

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyagent.data.ApiRepositoryProvider
import com.property.propertyagent.modal.owner.owner_change_password.OwnerChangePasswordReponse
import com.property.propertyagent.utils.Constants
import com.property.propertyagent.utils.Resource
import kotlinx.coroutines.launch

class OwnerChangePasswordViewModel : ViewModel() {
    private val changePasswordResponseLiveData =
        MutableLiveData<Resource<OwnerChangePasswordReponse>>()

    fun changePassword(
        current_password: String,
        new_password: String,
        confirm_new_password: String,
    ) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            changePasswordResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.ownerChangePassword(current_password, new_password, confirm_new_password)
                    .let {
                        val response = it.body()
                        if (response!!.status == Constants.REQUEST_OK) {
                            changePasswordResponseLiveData.postValue(Resource.success(response))
                        }
                        if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                            changePasswordResponseLiveData.postValue(
                                Resource.error(
                                    "null",
                                    response
                                )
                            )
                        }
                        if (response.status == Constants.REQUEST_BAD_REQUEST) {
                            changePasswordResponseLiveData.postValue(
                                Resource.error(
                                    response.response,
                                    response
                                )
                            )
                        }
                        if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                            changePasswordResponseLiveData.postValue(
                                Resource.dataEmpty(
                                    "null",
                                    response
                                )
                            )
                        }
                    }

            } catch (ex: Exception) {
                changePasswordResponseLiveData.postValue(Resource.noInternet("", null))
            }
        }
    }

    fun getChangePasswordAgentResponse(): LiveData<Resource<OwnerChangePasswordReponse>> {
        return changePasswordResponseLiveData
    }
}