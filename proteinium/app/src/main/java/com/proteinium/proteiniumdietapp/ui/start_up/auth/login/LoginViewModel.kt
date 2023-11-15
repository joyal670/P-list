package com.proteinium.proteiniumdietapp.ui.start_up.auth.login

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proteinium.proteiniumdietapp.R
import com.proteinium.proteiniumdietapp.data.api.ApiRepositoryProvider
import com.proteinium.proteiniumdietapp.pojo.login.LoginResponse
import com.proteinium.proteiniumdietapp.utils.Resource
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class LoginViewModel: ViewModel() {
    private val loginResponseMutableLiveData = MutableLiveData<Resource<LoginResponse>>()

    fun setLoginDetails(email:String,
                           password:String,
                        device_token:String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            loginResponseMutableLiveData.postValue(Resource.loading(null))
            try {
                repository.getLoginDetails(email,password,device_token).let {
                    val response = it.body()
                    if (response?.status!!) {
                        loginResponseMutableLiveData.postValue(Resource.success(response))
                    } else {
                        loginResponseMutableLiveData.postValue(Resource.error(response.message, response))
                    }
                }
            }
            catch (ex: NetworkErrorException) {
                loginResponseMutableLiveData.postValue(Resource.noInternet(R.string.no_internet.toString(),null))

            }
            catch (ex: UnknownHostException) {
                loginResponseMutableLiveData.postValue(Resource.noInternet(R.string.no_internet.toString(),null))

            }
            catch (ex:Exception){
                loginResponseMutableLiveData.postValue(Resource.error(R.string.something_wrong.toString(),null))
            }

        }
    }
    fun getLoginResponse(): LiveData<Resource<LoginResponse>> {
        return loginResponseMutableLiveData
    }
}