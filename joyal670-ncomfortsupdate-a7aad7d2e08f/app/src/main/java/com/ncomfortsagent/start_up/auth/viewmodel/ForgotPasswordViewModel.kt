package com.ncomfortsagent.start_up.auth.viewmodel

import android.accounts.NetworkErrorException
import android.annotation.SuppressLint
import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ncomfortsagent.R
import com.ncomfortsagent.data.ApiRepositoryProvider
import com.ncomfortsagent.model.forgot_password.AgentForgotPasswordResponse
import com.ncomfortsagent.utils.Constants
import com.ncomfortsagent.utils.Resource
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class ForgotPasswordViewModel(@SuppressLint("StaticFieldLeak") private var activity: Activity) :
    ViewModel() {

    private val forgotPasswordLiveData = MutableLiveData<Resource<AgentForgotPasswordResponse>>()

    fun getAgentForgotPasswordResponse(): LiveData<Resource<AgentForgotPasswordResponse>> {
        return forgotPasswordLiveData
    }

    fun forgotPassword(phone: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            forgotPasswordLiveData.postValue(Resource.loading(null))
            try {
                repository.forgotPassword(phone).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        forgotPasswordLiveData.postValue(Resource.success(response))
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        forgotPasswordLiveData.postValue(
                            Resource.error(
                                response.response,
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        forgotPasswordLiveData.postValue(
                            Resource.error(
                                response.response,
                                response
                            )
                        )
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        forgotPasswordLiveData.postValue(Resource.dataEmpty("null", response))
                    }
                }
            } catch (ex: UnknownHostException) {
                forgotPasswordLiveData.postValue(
                    Resource.noInternet(
                        activity.getString(R.string.please_check_your_internet_connection),
                        null
                    )
                )
            } catch (ex: NetworkErrorException) {
                forgotPasswordLiveData.postValue(
                    Resource.noInternet(
                        activity.getString(R.string.please_check_your_internet_connection),
                        null
                    )
                )
            } catch (ex: Exception) {
                forgotPasswordLiveData.postValue(
                    Resource.error(
                        activity.getString(R.string.some_thing_went_wrong),
                        null
                    )
                )
            }
        }
    }

}