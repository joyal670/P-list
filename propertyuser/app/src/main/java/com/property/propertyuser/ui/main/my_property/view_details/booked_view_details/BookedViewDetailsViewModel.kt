package com.property.propertyuser.ui.main.my_property.view_details.booked_view_details

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyuser.data.api.ApiRepositoryProvider
import com.property.propertyuser.modal.CommonResponse
import com.property.propertyuser.modal.proceed_book_property_details.ProceedBookPropertyDetailsResponse
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

class BookedViewDetailsViewModel: ViewModel() {

    private val proceedBookPropertyDetailsResponseLiveData= MutableLiveData<Resource<ProceedBookPropertyDetailsResponse>>()
    private val commonResponseLiveData= MutableLiveData<Resource<CommonResponse>>()
    private val cancelCommonResponseLiveData= MutableLiveData<Resource<CommonResponse>>()

    fun cancelBookedPropertyDetails(user_property_id: String){
        val params = mapOf(
            "user_property_id" to user_property_id.toRequestBody("multipart/form-data".toMediaTypeOrNull()))
        val repository= ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            cancelCommonResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.cancelBookedProperty(params).let {
                    val response=it.body()
                    if(response!!.status== Constants.REQUEST_OK){
                        cancelCommonResponseLiveData.postValue(Resource.success(response))
                    }
                    if(response!!.status== Constants.INTERNAL_SERVER_ERROR){
                        cancelCommonResponseLiveData.postValue(Resource.error("null",response))
                    }
                    if(response!!.status== Constants.REQUEST_BAD_REQUEST){
                        cancelCommonResponseLiveData.postValue(Resource.dataEmpty(response.response,response))
                    }
                    if(response!!.status== Constants.REQUEST_UNAUTHORIZED){
                        cancelCommonResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                }

            }catch (ex:Exception){
                cancelCommonResponseLiveData.postValue(Resource.noInternet("",null))
            }
        }
    }

    fun getCancelBookedPropertyResponse(): LiveData<Resource<CommonResponse>> {
        return cancelCommonResponseLiveData
    }

    fun uploadUserPropertyDocumentsDetails(context: Context,user_property_id: String, images : ArrayList<String?>?) {
        val params = mapOf(
            "user_property_id" to user_property_id.toRequestBody("multipart/form-data".toMediaTypeOrNull()))

        val repository= ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            commonResponseLiveData.postValue(Resource.loading(null))

            val document : ArrayList<MultipartBody.Part> = ArrayList()
            if (images!!.isNotEmpty()) {
                for (i in 0 until images.size) {
                    var imageProperty : MultipartBody.Part? = null
                    if (images[i]!!.isNotEmpty()) {
                        val file = File(images[i])
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
                        imageProperty = MultipartBody.Part.createFormData(
                            "document[$i]" ,
                            compressedFile.name ,
                            requestBody
                        )

                    }
                    document.add(imageProperty!!)
                }
            }

            try {
                repository.uploadPropertyDocuments(params,document).let {
                    val response=it.body()
                    if(response!!.status== Constants.REQUEST_OK){
                        commonResponseLiveData.postValue(Resource.success(response))
                    }
                    if(response!!.status== Constants.INTERNAL_SERVER_ERROR){
                        commonResponseLiveData.postValue(Resource.error("null",response))
                    }
                    if(response!!.status== Constants.REQUEST_BAD_REQUEST){
                        commonResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                    if(response!!.status== Constants.REQUEST_UNAUTHORIZED){
                        commonResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                }

            }catch (ex:Exception){
                commonResponseLiveData.postValue(Resource.noInternet("",null))
            }
        }
    }

    fun getUploadUserPropertyDocumentsResponse(): LiveData<Resource<CommonResponse>> {
        return commonResponseLiveData
    }
    fun proceedBookPropertyDetails(property_id: String){
        val repository= ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            proceedBookPropertyDetailsResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.proceedBookedPropertyDetails(property_id).let {
                    val response=it.body()
                    if(response!!.status== Constants.REQUEST_OK){
                        proceedBookPropertyDetailsResponseLiveData.postValue(Resource.success(response))
                    }
                    if(response!!.status== Constants.REQUEST_BAD_REQUEST){
                        proceedBookPropertyDetailsResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                    if(response!!.status== Constants.REQUEST_UNAUTHORIZED){
                        proceedBookPropertyDetailsResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                }

            }catch (ex:Exception){
                proceedBookPropertyDetailsResponseLiveData.postValue(Resource.noInternet("",null))
            }
        }
    }

    fun getProceedBookPropertyDetailsResponse(): LiveData<Resource<ProceedBookPropertyDetailsResponse>> {
        return proceedBookPropertyDetailsResponseLiveData
    }
}