package com.proteinium.proteiniumdietapp.ui.main.home.myaccount.myprofile

import android.accounts.NetworkErrorException
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proteinium.proteiniumdietapp.R
import com.proteinium.proteiniumdietapp.data.api.ApiRepositoryProvider
import com.proteinium.proteiniumdietapp.pojo.CommonResponse
import com.proteinium.proteiniumdietapp.pojo.myprofile.MyProfileResponse
import com.proteinium.proteiniumdietapp.pojo.user.UserResponse
import com.proteinium.proteiniumdietapp.preference.AppPreferences
import com.proteinium.proteiniumdietapp.utils.Resource
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.net.UnknownHostException

class MyProfileViewModel :ViewModel(){

    private val userInfoResponseLiveData = MutableLiveData<Resource<MyProfileResponse>>()
    private val updateUserInfoResponseLiveData = MutableLiveData<Resource<UserResponse>>()
    private val changePasswordLiveData =MutableLiveData<Resource<CommonResponse>>()


    fun changePassword(email: String,password: String,confirm_password:String){
        val repository=ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            userInfoResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.updatePassword(email, password, confirm_password).let {
                    val response=it.body()
                    if(response!!.status){
                        changePasswordLiveData.postValue(Resource.success(response))
                    }else{
                        changePasswordLiveData.postValue(Resource.dataEmpty("",response))
                    }
                }
            }catch (ex:Exception){
                userInfoResponseLiveData.postValue(Resource.noInternet(R.string.no_internet.toString(),null))
            }
        }
    }

    fun fetchUserInfo(user_id:Int) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            userInfoResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.getUserInfo(user_id).let {
                    val response = it.body()
                    if (response?.status!!) {
                        userInfoResponseLiveData.postValue(Resource.success(response))
                    } else {
                        userInfoResponseLiveData.postValue(Resource.error(response.message, response))
                    }
                }
            }
            catch (ex: NetworkErrorException) {
                userInfoResponseLiveData.postValue(Resource.noInternet(R.string.no_internet.toString(),null))

            }
            catch (ex: UnknownHostException) {
                userInfoResponseLiveData.postValue(Resource.noInternet(R.string.no_internet.toString(),null))

            }
            catch (ex:Exception){
                userInfoResponseLiveData.postValue(Resource.error(R.string.something_wrong.toString(),null))
            }

        }
    }

    fun getUserInfoResponse(): LiveData<Resource<MyProfileResponse>> {
        return userInfoResponseLiveData
    }

    fun fetchUpdateUserInfo(
        user_id:Int, first_name:String,
        middle_name:String,
        last_name:String,
        gender:String, phone:String, alternative_phone:String,
        image: File?
    ) {
        val params = mapOf(
            "id" to user_id.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull()),
            "first_name" to first_name.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull()),
            "middle_name" to middle_name.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull()),
            "last_name" to last_name.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull()),
            "gender" to gender.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull()),
            "phone" to phone.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull()),
            "lang_id" to AppPreferences.chooseLanguage!!.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
            "alternative_phone" to alternative_phone.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull()))


        var multiPart: MultipartBody.Part? =null
        if(image?.name!=null){
            val requestBody: RequestBody? = image?.asRequestBody("*/*".toMediaTypeOrNull())
             multiPart= MultipartBody.Part.createFormData("image", image?.name, requestBody!!)
        }

        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            updateUserInfoResponseLiveData.postValue(Resource.loading(null))
            try {
                if(multiPart!=null){
                    repository.updateUserInfo(params,multiPart!!)
                }else{
                    repository.updateUserInfoWithNoImage(params)
                }
                .let {
                    val response = it.body()
                    if (response?.status!!) {
                        updateUserInfoResponseLiveData.postValue(Resource.success(response))
                    } else {
                        updateUserInfoResponseLiveData.postValue(Resource.error(response.message, response))
                    }
                }
            }
            catch (ex: NetworkErrorException) {

                updateUserInfoResponseLiveData.postValue(Resource.noInternet(R.string.no_internet.toString(),null))

            }
            catch (ex: UnknownHostException) {
                updateUserInfoResponseLiveData.postValue(Resource.noInternet(R.string.no_internet.toString(),null))

            }
            catch (ex:Exception){
                Log.e("1233", "fetchUpdateUserInfo: $ex" )
                updateUserInfoResponseLiveData.postValue(Resource.error(R.string.something_wrong.toString(),null))
            }

        }
    }

    fun getUpdateUserInfoResponse(): LiveData<Resource<UserResponse>> {
        return updateUserInfoResponseLiveData
    }
    fun getChangePassword():LiveData<Resource<CommonResponse>>{
        return changePasswordLiveData
    }
}