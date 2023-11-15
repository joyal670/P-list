package com.property.propertyuser.ui.main.my_property.view_details.service_details

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyuser.data.api.ApiRepositoryProvider
import com.property.propertyuser.modal.CommonResponse
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

class ServiceDetailsViewModel: ViewModel() {
    private val commonResponseLiveData= MutableLiveData<Resource<CommonResponse>>()

    fun uploadRequestForServiceDetails(context: Context,service_id: String,user_property_id: String,
                                       request_date: String,time:String,description:String,images : ArrayList<String?>?
    ) {
        val params = mapOf(
            "service_id" to service_id.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
            "user_property_id" to user_property_id.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
            "request_date" to request_date.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
            "time" to time.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
            "description" to description.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull()))

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
                repository.uploadRequestForServiceDetails(params,document).let {
                    val response=it.body()
                    if(response!!.status== Constants.REQUEST_OK){
                        commonResponseLiveData.postValue(Resource.success(response))
                    }
                    if(response!!.status== Constants.REQUEST_BAD_REQUEST){
                        commonResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                    if(response!!.status== Constants.REQUEST_UNAUTHORIZED){
                        commonResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                    if(response!!.status== Constants.INTERNAL_SERVER_ERROR){
                        commonResponseLiveData.postValue(Resource.error("null",response))
                    }
                }

            }catch (ex:Exception){
                commonResponseLiveData.postValue(Resource.noInternet("",null))
            }
        }
    }

    fun getRequestForServiceResponse(): LiveData<Resource<CommonResponse>> {
        return commonResponseLiveData
    }
}