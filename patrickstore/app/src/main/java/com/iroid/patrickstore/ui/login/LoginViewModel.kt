package com.iroid.patrickstore.ui.login

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iroid.patrickstore.api.ApiRepositoryProvider
import com.iroid.patrickstore.model.login.LoginResponse
import com.iroid.patrickstore.utils.Constants
import com.iroid.patrickstore.utils.PasswordError
import com.iroid.patrickstore.utils.Resource
import com.iroid.patrickstore.utils.USERNAME_ERROR
import kotlinx.coroutines.launch

class LoginViewModel() : ViewModel() {
    private val liveDataLogin = MutableLiveData<Resource<LoginResponse>>()

    val loginLiveData: LiveData<Resource<LoginResponse>> get() = liveDataLogin

    var userEmail: MutableLiveData<String>? = null
    var userPassword: MutableLiveData<String>? = null

    var emptyEmail: MutableLiveData<String>? = null
    var emptyPassword: MutableLiveData<String>? = null

    init {
        userPassword = MutableLiveData()
        userEmail = MutableLiveData()
        emptyPassword = MutableLiveData()
        emptyEmail = MutableLiveData()
    }

    fun login() {
        if (isValid()) {
            val repository = ApiRepositoryProvider.providerApiRepository()
            viewModelScope.launch {
                try {
                    liveDataLogin.postValue(Resource.loading(null))
                    repository.login(
                        userEmail?.value.toString(),
                        userPassword?.value.toString()
                    ).let {
                        val response = it.body()
                        when (response!!.statusCode) {
                            Constants.REQUEST_OK -> {
                                liveDataLogin.postValue(Resource.success(response))
                            }
                            Constants.REQUEST_CREATED -> {
                                liveDataLogin.postValue(Resource.noInterNet("", null))
                            }
                            Constants.REQUEST_BAD_REQUEST -> {
                                liveDataLogin.postValue(Resource.error(response.msg, null))
                            }
                            Constants.REQUEST_UNAUTHORIZED -> {
                                liveDataLogin.postValue(Resource.noInterNet("", null))
                            }
                        }
                    }
                } catch (ex: Exception) {
                    Log.e("12345", "login:$ex ")
                    liveDataLogin.postValue(Resource.noInterNet("", null))
                }
            }
        }
    }

    fun onUserNameTextChanged(text: CharSequence) {
        if (!TextUtils.isEmpty(text.toString())) {
            userEmail?.value = text.toString()
            emptyEmail?.value = null
        }
    }

    fun onPasswordTextChanged(text: CharSequence) {
        if (!TextUtils.isEmpty(text.toString())) {
            userPassword?.value = text.toString()
            emptyPassword?.value = null
        }
    }

    private fun isValid(): Boolean {
        return when {
            TextUtils.isEmpty(userEmail?.value) -> {
                emptyEmail?.value = USERNAME_ERROR
                false
            }
            TextUtils.isEmpty(userPassword?.value) -> {
                emptyPassword?.value = PasswordError
                false
            }
            else -> {
                true
            }
        }
    }
}