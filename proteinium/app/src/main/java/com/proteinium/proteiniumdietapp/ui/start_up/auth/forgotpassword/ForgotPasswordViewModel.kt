package com.proteinium.proteiniumdietapp.ui.start_up.auth.forgotpassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.proteinium.proteiniumdietapp.base.BaseViewModel
import com.proteinium.proteiniumdietapp.data.api.ApiRepositoryProvider
import com.proteinium.proteiniumdietapp.pojo.CommonResponse
import com.proteinium.proteiniumdietapp.utils.Resource
import kotlinx.coroutines.launch
import java.lang.Exception

class ForgotPasswordViewModel :BaseViewModel(){
    private val forgotLiveData=MutableLiveData<Resource<CommonResponse>>()

    fun getForgotLiveData(email:String){
        val repository=ApiRepositoryProvider.providerApiRepository()
        forgotLiveData.postValue(Resource.loading(null))
        viewModelScope.launch {
            try {
                repository.resetPassword(email).let {
                    val response=it.body()
                    if(response!!.status){
                        forgotLiveData.postValue(Resource.success(response))
                    }else{
                        forgotLiveData.postValue(Resource.dataEmpty( "",response))
                    }
                }
            }catch (ex:Exception){
                forgotLiveData.postValue(Resource.error("",null))
            }

        }
    }

    fun getForgotLiveData():LiveData<Resource<CommonResponse>>{
        return forgotLiveData
    }
}