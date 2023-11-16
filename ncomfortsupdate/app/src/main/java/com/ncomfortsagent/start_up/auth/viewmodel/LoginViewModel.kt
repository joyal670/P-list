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
import com.ncomfortsagent.model.login.AgentLoginResponse
import com.ncomfortsagent.utils.Constants
import com.ncomfortsagent.utils.Resource
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class LoginViewModel(@SuppressLint("StaticFieldLeak") private var activity: Activity) : ViewModel() {

    private val loginLiveData = MutableLiveData<Resource<AgentLoginResponse>>()

    fun getAgentLoginResponse(): LiveData<Resource<AgentLoginResponse>> {
        return loginLiveData
    }

    fun login(phone: String, password: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            loginLiveData.postValue(Resource.loading(null))
            try {
                repository.login(phone, password).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        loginLiveData.postValue(Resource.success(response))
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        loginLiveData.postValue(Resource.error(response.response, response))
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        loginLiveData.postValue(Resource.error(response.response, response))
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        loginLiveData.postValue(Resource.dataEmpty("null", response))
                    }
                }
            } catch (ex: UnknownHostException) {
                loginLiveData.postValue(
                    Resource.noInternet(
                        activity.getString(R.string.please_check_your_internet_connection),
                        null
                    )
                )
            } catch (ex: NetworkErrorException) {
                loginLiveData.postValue(
                    Resource.noInternet(
                        activity.getString(R.string.please_check_your_internet_connection),
                        null
                    )
                )
            } catch (ex: Exception) {
                loginLiveData.postValue(Resource.error(
                    activity.getString(R.string.some_thing_went_wrong),
                    null
                ))
            }
        }
    }

}