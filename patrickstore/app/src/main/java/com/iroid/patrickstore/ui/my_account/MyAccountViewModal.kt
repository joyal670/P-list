package com.iroid.patrickstore.ui.my_account

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iroid.patrickstore.api.ApiRepositoryProvider
import com.iroid.patrickstore.model.change_password.ChangePasswordResponse
import com.iroid.patrickstore.utils.Constants
import com.iroid.patrickstore.utils.NEW_PASSWORD_REQUIRED
import com.iroid.patrickstore.utils.OLD_PASSWORD_REQUIRED
import com.iroid.patrickstore.utils.Resource
import kotlinx.coroutines.launch

class MyAccountViewModal : ViewModel() {

    private val liveDataChangePassword = MutableLiveData<Resource<ChangePasswordResponse>>()
    val changePasswordLiveData: LiveData<Resource<ChangePasswordResponse>> get() = liveDataChangePassword

    private var oldPassword: MutableLiveData<String>? = null
    private var newPassword: MutableLiveData<String>? = null
    var emptyOldPassword: MutableLiveData<String>? = null
    var emptyNewPassword: MutableLiveData<String>? = null

    init {
        oldPassword = MutableLiveData()
        newPassword = MutableLiveData()
        emptyOldPassword = MutableLiveData()
        emptyNewPassword = MutableLiveData()
    }

    fun onOldTextChanged(text: CharSequence) {
        oldPassword?.value = text.toString()
        if (TextUtils.isEmpty(text.toString())) {
            emptyOldPassword?.value = OLD_PASSWORD_REQUIRED
        } else {
            emptyOldPassword?.value = null
        }
    }

    fun onNewTextChanged(text: CharSequence) {
        newPassword?.value = text.toString()
        if (TextUtils.isEmpty(text.toString())) {
            emptyNewPassword?.value = NEW_PASSWORD_REQUIRED
        } else {
            emptyNewPassword?.value = null
        }
    }

    fun changePassword() {
        if (isValid()) {
            val repository = ApiRepositoryProvider.providerApiRepository()
            viewModelScope.launch {
                try {
                    liveDataChangePassword.postValue(Resource.loading(null))
                    repository.changePassword(
                        oldPassword!!.value.toString(),
                        newPassword!!.value.toString()
                    ).let {
                        val response = it.body()
                        when (response!!.statusCode) {
                            Constants.REQUEST_OK -> {
                                liveDataChangePassword.postValue(Resource.success(response))
                            }
                            Constants.REQUEST_CREATED -> {
                                liveDataChangePassword.postValue(Resource.noInterNet("", null))
                            }
                            Constants.REQUEST_BAD_REQUEST -> {
                                liveDataChangePassword.postValue(Resource.error(response.msg, null))
                            }
                            Constants.REQUEST_UNAUTHORIZED -> {
                                liveDataChangePassword.postValue(Resource.noInterNet("", null))
                            }
                        }
                    }
                } catch (ex: Exception) {
                    liveDataChangePassword.postValue(Resource.noInterNet("", null))
                }
            }
        }
    }

    private fun isValid(): Boolean {
        return when {
            TextUtils.isEmpty(oldPassword?.value) -> {
                emptyOldPassword?.value = OLD_PASSWORD_REQUIRED
                false
            }
            TextUtils.isEmpty(newPassword?.value) -> {
                emptyNewPassword?.value = NEW_PASSWORD_REQUIRED
                false
            }
            else -> {
                true
            }
        }
    }
}
