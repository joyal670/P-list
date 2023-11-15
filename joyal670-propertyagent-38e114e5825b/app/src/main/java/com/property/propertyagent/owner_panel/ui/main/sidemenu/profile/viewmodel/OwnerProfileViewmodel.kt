package com.property.propertyagent.owner_panel.ui.main.sidemenu.profile.viewmodel

import android.accounts.NetworkErrorException
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyagent.data.ApiRepositoryProvider
import com.property.propertyagent.modal.owner.owner_profile.Owner_ProfileResponse
import com.property.propertyagent.utils.Constants
import com.property.propertyagent.utils.Resource
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
import java.net.UnknownHostException

class OwnerProfileViewmodel(private val context : Context) : ViewModel() {
    private val profileLiveData = MutableLiveData<Resource<Owner_ProfileResponse>>()
    private val ownerProfileLiveData = MutableLiveData<Resource<Owner_ProfileResponse>>()

    val ownerProfileData: LiveData<Resource<Owner_ProfileResponse>>
        get() = ownerProfileLiveData
    val profileData : LiveData<Resource<Owner_ProfileResponse>>
        get() = profileLiveData

    fun ownerProfileDetails() {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                ownerProfileLiveData.postValue(Resource.loading(null))
                repository.getOwnerProfileDetails().let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        ownerProfileLiveData.postValue(Resource.success(response))
                    } else {
                        ownerProfileLiveData.postValue(Resource.error(it.message(), response))
                    }
                }
            } catch (ex: UnknownHostException) {
                ownerProfileLiveData.postValue(Resource.noInternet("Please check your internet connection",null))

            }
            catch (ex: NetworkErrorException) {
                ownerProfileLiveData.postValue(Resource.noInternet("Please check your internet connection",null))

            }
            catch (ex:Exception){
                ownerProfileLiveData.postValue(Resource.error("Something went wrong",null))
            }
        }
    }



    fun updateProfile(
        email : String ,
        phone : String ,
        profile_image : File? ,
        id_image : File?
    ) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        profileLiveData.postValue(Resource.loading(null))
        viewModelScope.launch {
            val params = mapOf(
                "email" to email.toRequestBody("multipart/form-data".toMediaTypeOrNull()) ,
                "phone" to phone.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            )
            val imageList : ArrayList<MultipartBody.Part> = ArrayList()
            var IDImage : MultipartBody.Part? = null
            var compressedFileIDImage : File? = null
            var ProfileImage : MultipartBody.Part? = null
            var compressedFileProfileImage : File? = null
            try {

                /* id image */
                if (id_image != null) {
                  /*  try {
                        compressedFileIDImage =
                            id_image.let { Compressor.compress(context , it , Dispatchers.IO) }
                    } catch (e : EOFException) {
                        e.printStackTrace()
                    } catch (e : IOException) {
                        e.printStackTrace()
                    }*/

                    val requestIDBody : RequestBody =
                        id_image.asRequestBody("*/*".toMediaTypeOrNull())
                    IDImage = MultipartBody.Part.createFormData(
                        "id_image" ,
                        id_image.name ,
                        requestIDBody
                    )
                    imageList.add(IDImage)

                } else {

                }
                if (profile_image != null) {
                    /* Profile image */
                    try {
                        compressedFileProfileImage =
                            profile_image?.let {
                                Compressor.compress(
                                    context ,
                                    it ,
                                    Dispatchers.IO
                                )
                            }
                    } catch (e : EOFException) {
                        e.printStackTrace()
                    } catch (e : IOException) {
                        e.printStackTrace()
                    }

                    val requestProfileBody : RequestBody =
                        compressedFileProfileImage!!.asRequestBody("*/*".toMediaTypeOrNull())
                    ProfileImage = MultipartBody.Part.createFormData(
                        "profile_image" ,
                        compressedFileProfileImage.name ,
                        requestProfileBody
                    )
                    imageList.add(ProfileImage)

                } else {


                }
                repository.ownerProfile(params , imageList).let {
                    if (it.isSuccessful) {
                        profileLiveData.postValue(Resource.success(it.body()))
                    } else {
                        profileLiveData.postValue(Resource.error(it.message() , null))
                    }
                }
            } catch (ex : UnknownHostException) {
                profileLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection" ,
                        null
                    )
                )

            } catch (ex : NetworkErrorException) {
                profileLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection" ,
                        null
                    )
                )

            } catch (ex : Exception) {
                profileLiveData.postValue(Resource.error("Something went wrong" , null))
                Log.e("TAG" , "updateProfile: " + ex)
            }
        }
    }
}