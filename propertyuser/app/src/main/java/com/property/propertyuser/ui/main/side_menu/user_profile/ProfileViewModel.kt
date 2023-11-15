package com.property.propertyuser.ui.main.side_menu.user_profile

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyuser.data.api.ApiRepositoryProvider
import com.property.propertyuser.modal.CommonResponse
import com.property.propertyuser.modal.list_requested_service_details.RequestedServiceDetailsResponse
import com.property.propertyuser.modal.profile.ProfileDetailsResponse
import com.property.propertyuser.modal.update_profile.UpdateProfileResponse
import com.property.propertyuser.utils.Constants
import com.property.propertyuser.utils.Resource
import id.zelory.compressor.Compressor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.EOFException
import java.io.File
import java.io.IOException

class ProfileViewModel:ViewModel() {
    private val profileDetailsResponseLiveData= MutableLiveData<Resource<ProfileDetailsResponse>>()
    private val profileUpdateResponseLiveData= MutableLiveData<Resource<UpdateProfileResponse>>()
    private val profileRemoveResponseLiveData= MutableLiveData<Resource<CommonResponse>>()

    fun removeProfilePic(){
        val repository= ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            profileRemoveResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.removeProfilePic().let {
                    val response=it.body()
                    if(response!!.status== Constants.REQUEST_OK){
                        profileRemoveResponseLiveData.postValue(Resource.success(response))
                    }
                    if(response!!.status== Constants.INTERNAL_SERVER_ERROR){
                        profileRemoveResponseLiveData.postValue(Resource.error("null",response))
                    }
                    if(response!!.status== Constants.REQUEST_BAD_REQUEST){
                        profileRemoveResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                    if(response!!.status== Constants.REQUEST_UNAUTHORIZED){
                        profileRemoveResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                }

            }catch (ex:Exception){
                profileRemoveResponseLiveData.postValue(Resource.noInternet("",null))
            }
        }
    }

    fun getRemoveProfilePicResponse(): LiveData<Resource<CommonResponse>> {
        return profileRemoveResponseLiveData
    }

    fun profileUpdate(context: Context, name:String, email:String, phone:String, address:String,
                            selectedFile: String){
        var params:Map<String, RequestBody>
        if(!(selectedFile.isNullOrEmpty())){
            params = mapOf(
                "name" to name.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                "email" to email.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                "phone" to phone.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                "address" to address.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                "profile_pic" to selectedFile.toRequestBody("multipart/form-data".toMediaTypeOrNull()))
        }else{
            params = mapOf(
                "name" to name.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                "email" to email.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                "phone" to phone.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                "address" to address.toRequestBody("multipart/form-data".toMediaTypeOrNull()))
        }
        var image: MultipartBody.Part? =null
        val repository= ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            if(!(selectedFile.isNullOrEmpty())){
                val file = File(selectedFile)
                var compressedFile : File? = null
                try {
                    compressedFile =
                        Compressor.compress(context , file , Dispatchers.IO)
                } catch (e : EOFException) {
                    e.printStackTrace()
                } catch (e : IOException) {
                    e.printStackTrace()
                }
                val requestBody : RequestBody =
                    compressedFile!!.asRequestBody("*/*".toMediaTypeOrNull())
                image= MultipartBody.Part.createFormData("profile_pic" , compressedFile.name , requestBody)
            }
            profileUpdateResponseLiveData.postValue(Resource.loading(null))
            try {
                if(!(selectedFile.isNullOrEmpty())){
                    repository.updateProfileDetails(params,image!!).let {
                        val response=it.body()
                        if(response!!.status== Constants.REQUEST_OK){
                            profileUpdateResponseLiveData.postValue(Resource.success(response))
                        }
                        if(response!!.status== Constants.INTERNAL_SERVER_ERROR){
                            profileUpdateResponseLiveData.postValue(Resource.error("null",response))
                        }
                        if(response!!.status== Constants.REQUEST_BAD_REQUEST){
                            profileUpdateResponseLiveData.postValue(Resource.dataEmpty("null",response))
                        }
                        if(response!!.status== Constants.REQUEST_UNAUTHORIZED){
                            profileUpdateResponseLiveData.postValue(Resource.dataEmpty("null",response))
                        }
                    }
                }else{
                    repository.updateProfileDetails(params).let {
                        val response=it.body()
                        if(response!!.status== Constants.REQUEST_OK){
                            profileUpdateResponseLiveData.postValue(Resource.success(response))
                        }
                        if(response!!.status== Constants.INTERNAL_SERVER_ERROR){
                            profileUpdateResponseLiveData.postValue(Resource.error("null",response))
                        }
                        if(response!!.status== Constants.REQUEST_BAD_REQUEST){
                            profileUpdateResponseLiveData.postValue(Resource.dataEmpty("null",response))
                        }
                        if(response!!.status== Constants.REQUEST_UNAUTHORIZED){
                            profileUpdateResponseLiveData.postValue(Resource.dataEmpty("null",response))
                        }
                    }
                }


            }catch (ex:Exception){
                profileUpdateResponseLiveData.postValue(Resource.noInternet("",null))
            }
        }
    }

    fun getProfileUpdateResponse(): LiveData<Resource<UpdateProfileResponse>> {
        return profileUpdateResponseLiveData
    }


    fun fetchProfileDetails(){
        val repository= ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            profileDetailsResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.profileDetails().let {
                    val response=it.body()
                    if(response!!.status== Constants.REQUEST_OK){
                        profileDetailsResponseLiveData.postValue(Resource.success(response))
                    }
                    if(response!!.status== Constants.INTERNAL_SERVER_ERROR){
                        profileDetailsResponseLiveData.postValue(Resource.error("null",response))
                    }
                    if(response!!.status== Constants.REQUEST_BAD_REQUEST){
                        profileDetailsResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                    if(response!!.status== Constants.REQUEST_UNAUTHORIZED){
                        profileDetailsResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                }

            }catch (ex:Exception){
                profileDetailsResponseLiveData.postValue(Resource.noInternet("",null))
            }
        }
    }

    fun getProfileDetailsResponse(): LiveData<Resource<ProfileDetailsResponse>> {
        return profileDetailsResponseLiveData
    }
}