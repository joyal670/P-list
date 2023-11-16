package com.iroid.patrickstore.ui.forgotpassword.viewmodel

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iroid.patrickstore.api.ApiRepositoryProvider
import com.iroid.patrickstore.model.forgotpassword.ForgotResponse
import com.iroid.patrickstore.utils.Constants
import com.iroid.patrickstore.utils.Resource
import com.iroid.patrickstore.utils.USERNAME_ERROR
import kotlinx.coroutines.launch

class ForgotViewModal() : ViewModel() {
    private val liveDataForgot = MutableLiveData<Resource<ForgotResponse>>()

    val forgotLiveData: LiveData<Resource<ForgotResponse>> get() = liveDataForgot

    var userEmail: MutableLiveData<String>? = null
    var emptyEmail: MutableLiveData<String>? = null

    init {
        userEmail = MutableLiveData()
        emptyEmail = MutableLiveData()
    }

    fun onEmailTextChanged(text: CharSequence) {
        if (!TextUtils.isEmpty(text.toString())) {
            userEmail?.value = text.toString()
            emptyEmail?.value = null
        }else{
            userEmail?.value = null
        }
    }

    fun onForgot() {
        if (isValidate()) {
            val repository = ApiRepositoryProvider.providerApiRepository()
            liveDataForgot.postValue(Resource.loading(null))
            viewModelScope.launch {
                try {
                    repository.forgotPassword(false, userEmail?.value.toString()).let {
                        val response = it.body()
                        when (response!!.statusCode) {
                            Constants.REQUEST_OK -> {
                                liveDataForgot.postValue(Resource.success(response))
                            }
                            Constants.REQUEST_CREATED -> {
                                liveDataForgot.postValue(Resource.noInterNet("", null))
                            }
                            Constants.REQUEST_BAD_REQUEST -> {
                                liveDataForgot.postValue(Resource.error(response.msg, null))
                            }
                            Constants.REQUEST_UNAUTHORIZED -> {
                                liveDataForgot.postValue(Resource.noInterNet("", null))
                            }
                        }
                    }
                } catch (ex: Exception) {
                    liveDataForgot.postValue(Resource.noInterNet(ex.toString(), null))
                }
            }
        }
    }

    private fun isValidate(): Boolean {
        return if (TextUtils.isEmpty(userEmail?.value)) {
            emptyEmail?.value = USERNAME_ERROR
            false
        } else {
            true
        }
    }

}