package com.proteinium.proteiniumdietapp.ui.start_up.auth.signup

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proteinium.proteiniumdietapp.R
import com.proteinium.proteiniumdietapp.data.api.ApiRepositoryProvider
import com.proteinium.proteiniumdietapp.pojo.CommonResponse
import com.proteinium.proteiniumdietapp.utils.Resource
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class SignupViewModel: ViewModel() {
    private val commonResponseMutableLiveData = MutableLiveData<Resource<CommonResponse>>()

    fun addCustomerDetails(first_name: String,
                                   middle_name:String,
                                   last_name:String,email:String,phone:String,
                               alternative_phone:String,
                               password:String,gender:String,status:Int,
                               device_token:String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            commonResponseMutableLiveData.postValue(Resource.loading(null))
            try {
                repository.addCustomerSignup(first_name,middle_name,last_name,email,phone,alternative_phone,password,gender,status,device_token).let {
                    val response = it.body()
                    if (response?.status!!) {
                        commonResponseMutableLiveData.postValue(Resource.success(response))
                    } else {
                        commonResponseMutableLiveData.postValue(Resource.error(response.message, response))
                    }
                }
            }
            catch (ex: NetworkErrorException) {
                commonResponseMutableLiveData.postValue(Resource.noInternet(R.string.no_internet.toString(),null))

            }
            catch (ex: UnknownHostException) {
                commonResponseMutableLiveData.postValue(Resource.noInternet(R.string.no_internet.toString(),null))

            }
            catch (ex:Exception){
                commonResponseMutableLiveData.postValue(Resource.noInternet(R.string.something_wrong.toString(),null))
            }

        }
    }
    fun addCustomerSignupResponse(): LiveData<Resource<CommonResponse>> {
        return commonResponseMutableLiveData
    }
}