package com.property.propertyagent.agent_panel.ui.main.sidemenu.profile.changePassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyagent.data.ApiRepositoryProvider
import com.property.propertyagent.modal.CommonResponse
import com.property.propertyagent.utils.Constants
import com.property.propertyagent.utils.Resource
import kotlinx.coroutines.launch

class ChangePasswordViewModel : ViewModel() {
    private val changePasswordAgentResponseLiveData =
        MutableLiveData<Resource<CommonResponse>>()

    fun changePasswordAgent(
        current_password: String,
        new_password: String,
        confirm_new_password: String,
    ) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            changePasswordAgentResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.changePasswordAgent(
                    current_password,
                    new_password,
                    confirm_new_password
                ).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        changePasswordAgentResponseLiveData.postValue(Resource.success(response))
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        changePasswordAgentResponseLiveData.postValue(
                            Resource.error(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        changePasswordAgentResponseLiveData.postValue(
                            Resource.dataEmpty(
                                response.response,
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        changePasswordAgentResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                }

            } catch (ex: Exception) {
                changePasswordAgentResponseLiveData.postValue(Resource.noInternet("", null))
            }
        }
    }

    fun getChangePasswordAgentResponse(): LiveData<Resource<CommonResponse>> {
        return changePasswordAgentResponseLiveData
    }
}