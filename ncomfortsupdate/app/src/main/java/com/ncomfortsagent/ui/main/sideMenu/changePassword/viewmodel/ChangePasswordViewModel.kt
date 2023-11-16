package com.ncomfortsagent.ui.main.sideMenu.changePassword.viewmodel

import android.accounts.NetworkErrorException
import android.annotation.SuppressLint
import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ncomfortsagent.R
import com.ncomfortsagent.data.ApiRepositoryProvider
import com.ncomfortsagent.model.change_password.AgentChangePasswordResponse
import com.ncomfortsagent.utils.Constants
import com.ncomfortsagent.utils.Resource
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class ChangePasswordViewModel(@SuppressLint("StaticFieldLeak") private var activity: Activity) :
    ViewModel() {

    private val changePasswordLiveData = MutableLiveData<Resource<AgentChangePasswordResponse>>()

    fun getAgentChangePasswordResponse(): LiveData<Resource<AgentChangePasswordResponse>> {
        return changePasswordLiveData
    }

    fun changePassword(
        current_password: String,
        new_password: String,
        confirm_new_password: String
    ) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            changePasswordLiveData.postValue(Resource.loading(null))
            try {
                repository.changePassword(current_password, new_password, confirm_new_password)
                    .let {
                        val response = it.body()
                        if (response!!.status == Constants.REQUEST_OK) {
                            changePasswordLiveData.postValue(Resource.success(response))
                        }
                        if (response.status == Constants.REQUEST_BAD_REQUEST) {
                            changePasswordLiveData.postValue(
                                Resource.error(response.response, response)
                            )
                        }
                        if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                            changePasswordLiveData.postValue(
                                Resource.error(
                                    "Unauthorized",
                                    response
                                )
                            )
                        }
                        if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                            changePasswordLiveData.postValue(Resource.dataEmpty("null", response))
                        }
                    }
            } catch (ex: UnknownHostException) {
                changePasswordLiveData.postValue(
                    Resource.noInternet(
                        activity.getString(R.string.please_check_your_internet_connection),
                        null
                    )
                )
            } catch (ex: NetworkErrorException) {
                changePasswordLiveData.postValue(
                    Resource.noInternet(
                        activity.getString(R.string.please_check_your_internet_connection),
                        null
                    )
                )
            } catch (ex: Exception) {
                changePasswordLiveData.postValue(
                    Resource.error(
                        activity.getString(R.string.some_thing_went_wrong),
                        null
                    )
                )
            }
        }
    }

}