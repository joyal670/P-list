package com.property.propertyagent.owner_panel.ui.main.sidemenu.maintenance.service.service_detailed_page.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyagent.data.ApiRepositoryProvider
import com.property.propertyagent.modal.CommonResponse
import com.property.propertyagent.modal.owner.owner_properties_for_service.OwnerPropertiesForServiceResponse
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

class ServiceDetailedViewModel:ViewModel() {

    private val ownerPropertyDetailsResponseLiveData=
        MutableLiveData<Resource<OwnerPropertiesForServiceResponse>>()

    fun ownerPropertyDetails(service_id : Int){
        val repository= ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            ownerPropertyDetailsResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.ownerPropertiesForService(service_id).let {
                    val response=it.body()
                    if(response!!.status== Constants.REQUEST_OK){
                        ownerPropertyDetailsResponseLiveData.postValue(Resource.success(response))
                    }
                    if(response.status== Constants.INTERNAL_SERVER_ERROR){
                        ownerPropertyDetailsResponseLiveData.postValue(Resource.error("null",response))
                    }
                    if(response.status== Constants.REQUEST_BAD_REQUEST){
                        ownerPropertyDetailsResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                    if(response.status== Constants.REQUEST_UNAUTHORIZED){
                        ownerPropertyDetailsResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                }

            }catch (ex:Exception){
                ownerPropertyDetailsResponseLiveData.postValue(Resource.noInternet("",null))
            }
        }
    }

    fun getOwnerPropertyDetailsResponse(): LiveData<Resource<OwnerPropertiesForServiceResponse>> {
        return ownerPropertyDetailsResponseLiveData
    }

    private val ownerUploadServiceDocumentResponseLiveData= MutableLiveData<Resource<CommonResponse>>()

    fun ownerUploadUserServiceDocumentsDetails(context: Context ,
                                               owner_property_id: String ,
                                               service_id: String ,
                                               request_date: String ,
                                               time: String ,
                                               description: String ,
                                               images : ArrayList<String?>?) {
        val params = mapOf(
            "owner_property_id" to owner_property_id.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
            "service_id" to service_id.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
            "request_date" to request_date.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
            "time" to time.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
            "description" to description.toRequestBody("multipart/form-data".toMediaTypeOrNull()))

        val repository= ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            ownerUploadServiceDocumentResponseLiveData.postValue(Resource.loading(null))

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
                repository.ownerRequestService(params,document).let {
                    val response=it.body()
                    if(response!!.status== Constants.REQUEST_OK){
                        ownerUploadServiceDocumentResponseLiveData.postValue(Resource.success(response))
                    }
                    if(response!!.status== Constants.INTERNAL_SERVER_ERROR){
                        ownerUploadServiceDocumentResponseLiveData.postValue(Resource.error("null",response))
                    }
                    if(response!!.status== Constants.REQUEST_BAD_REQUEST){
                        ownerUploadServiceDocumentResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                    if(response!!.status== Constants.REQUEST_UNAUTHORIZED){
                        ownerUploadServiceDocumentResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                }

            }catch (ex:Exception){
                ownerUploadServiceDocumentResponseLiveData.postValue(Resource.noInternet("",null))
            }
        }
    }

    fun getOwnerUploadServiceDocumentsResponse(): LiveData<Resource<CommonResponse>> {
        return ownerUploadServiceDocumentResponseLiveData
    }

}