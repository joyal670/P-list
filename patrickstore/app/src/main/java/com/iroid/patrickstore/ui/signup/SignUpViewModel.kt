package com.iroid.patrickstore.ui.signup

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iroid.patrickstore.api.ApiRepositoryProvider
import com.iroid.patrickstore.model.signup.SignUpResponse
import com.iroid.patrickstore.utils.*
import kotlinx.coroutines.launch

class SignUpViewModel() : ViewModel() {
    private val liveDataSignUp = MutableLiveData<Resource<SignUpResponse>>()

    val signUpLiveData: LiveData<Resource<SignUpResponse>> get() = liveDataSignUp

    var userFirstName: MutableLiveData<String>? = null
    var userLastName: MutableLiveData<String>? = null
    var userEmail: MutableLiveData<String>? = null
    var userMobile: MutableLiveData<String>? = null
    var userPassword: MutableLiveData<String>? = null
    var userConfirmPassword: MutableLiveData<String>? = null
    var userRefferalCode: MutableLiveData<String>? = null
    var userTermsAndCondition: MutableLiveData<Boolean>? = null

    var emptyFirstName: MutableLiveData<String>? = null
    var emptyLastName: MutableLiveData<String>? = null
    var emptyEmail: MutableLiveData<String>? = null
    var emptyMobile: MutableLiveData<String>? = null
    var emptyPassword: MutableLiveData<String>? = null
    var emptyConfirmPassword: MutableLiveData<String>? = null

    init {
        userFirstName = MutableLiveData()
        userFirstName = MutableLiveData()
        userLastName = MutableLiveData()
        userMobile = MutableLiveData()
        userPassword = MutableLiveData()
        userConfirmPassword = MutableLiveData()
        userEmail = MutableLiveData()
        userTermsAndCondition = MutableLiveData()
        userRefferalCode=MutableLiveData()

        emptyFirstName = MutableLiveData()
        emptyLastName = MutableLiveData()
        emptyMobile = MutableLiveData()
        emptyPassword = MutableLiveData()
        emptyConfirmPassword = MutableLiveData()
        emptyEmail = MutableLiveData()
    }

    fun setSignUp(refferalCode: String) {
        if (isValid()) {
            if (isValid()) {
                val repository = ApiRepositoryProvider.providerApiRepository()
                viewModelScope.launch {
                    try {
                        liveDataSignUp.postValue(Resource.loading(null))
                        repository.registerCustomer(
                            userFirstName?.value.toString(),
                            userLastName?.value.toString(),
                            userEmail?.value.toString(),
                            userMobile?.value.toString(),
                            userPassword?.value.toString(),
                            refferalCode
                        ).let {
                            val response = it.body()
                            when (response!!.statusCode) {
                                Constants.REQUEST_OK -> {
                                    liveDataSignUp.postValue(Resource.success(response))
                                }
                                Constants.REQUEST_CREATED -> {
                                    liveDataSignUp.postValue(Resource.noInterNet("", null))
                                }
                                Constants.REQUEST_BAD_REQUEST -> {
                                    liveDataSignUp.postValue(Resource.error(response.msg, null))
                                }
                                Constants.REQUEST_UNAUTHORIZED -> {
                                    liveDataSignUp.postValue(Resource.noInterNet("", null))
                                }
                            }
                        }
                    } catch (ex: Exception) {
                        liveDataSignUp.postValue(Resource.noInterNet("", null))
                    }
                }
            }
        }
    }

    fun onFirstNameTextChanged(text: CharSequence) {
        if (TextUtils.isEmpty(text.toString())) {
            emptyFirstName?.value = FirstNameError
        } else {
            userFirstName?.value = text.toString()
            emptyFirstName?.value = null
        }
    }
    fun onRefferalCodeTextChanged(text: CharSequence){
        userRefferalCode?.value=text.toString()
    }

    fun onLastTextChanged(text: CharSequence) {
        if (TextUtils.isEmpty(text.toString())) {
            emptyLastName?.value = LastNameError
        } else {
            userLastName?.value = text.toString()
            emptyLastName?.value = null
        }
    }

    fun onEmailChanged(text: CharSequence) {
        if (TextUtils.isEmpty(text.toString())) {
            emptyEmail?.value = EmailError
        } else {
            if (!text.toString().isValidEmail) {
                emptyEmail?.value = EmailInvalid
            } else {
                userEmail?.value = text.toString()
                emptyEmail?.value = null
            }
        }
    }

    fun onMobileTextChanged(text: CharSequence) {
        if (TextUtils.isEmpty(text.toString())) {
            emptyMobile?.value = MobileError
        } else {
            if (!text.toString().isValidMobile) {
                emptyMobile?.value = MobileInvalid
            } else {
                userMobile?.value = text.toString()
                emptyMobile?.value = null
            }
        }
    }

    fun onPasswordTextChanged(text: CharSequence) {
        if (TextUtils.isEmpty(text.toString())) {
            emptyPassword?.value = PasswordError
        } else {
            userPassword?.value = text.toString()
            emptyPassword?.value = null
        }
    }

    fun onConfirmPasswordTextChanged(text: CharSequence) {
        if (TextUtils.isEmpty(text.toString())) {
            emptyConfirmPassword?.value = ConfirmPasswordError
        } else {
            if (userPassword?.value != text.toString()) {
                emptyConfirmPassword?.value = MissMatchPassword
            } else {
                userConfirmPassword?.value = text.toString()
                emptyConfirmPassword?.value = null
            }
        }
    }

    fun onTermsAndConditionChanged(isChecked: Boolean) {
        userTermsAndCondition?.value = isChecked
    }

    private fun isValid(): Boolean {
        when {
            TextUtils.isEmpty(userFirstName?.value) -> {
                emptyFirstName?.value = FirstNameError
                return false
            }
            TextUtils.isEmpty(userLastName?.value) -> {
                emptyLastName?.value = FirstNameError
                return false
            }
            TextUtils.isEmpty(userMobile?.value) -> {
                emptyMobile?.value = MobileError
                return false
            }
            TextUtils.isEmpty(userEmail?.value) -> {
                emptyEmail?.value = EmailError
                return false
            }

            TextUtils.isEmpty(userPassword?.value) -> {
                emptyPassword?.value = PasswordError
                return false
            }
            TextUtils.isEmpty(userConfirmPassword?.value) -> {
                emptyConfirmPassword?.value = ConfirmPasswordError
                return false
            }

            else -> {
                return if (!userEmail?.value?.isValidEmail!!) {
                    emptyEmail?.value = EmailError
                    false
                } else if (!userMobile?.value?.isValidMobile!!) {
                    emptyMobile?.value = MobileError
                    false
                } else if (userPassword?.value != userConfirmPassword?.value) {
                    emptyMobile?.value = MobileError
                    false
                } else if (userTermsAndCondition?.value == false || userTermsAndCondition?.value == null){
                    userTermsAndCondition?.value = false
                    false
                }
                else {
                    true
                }
            }
        }
    }
}
