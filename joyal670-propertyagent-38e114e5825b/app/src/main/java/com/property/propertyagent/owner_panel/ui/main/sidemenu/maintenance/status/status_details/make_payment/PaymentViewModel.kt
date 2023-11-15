package com.property.propertyagent.owner_panel.ui.main.sidemenu.maintenance.status.status_details.make_payment

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyagent.data.ApiRepositoryProvider
import com.property.propertyagent.modal.CommonResponse
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

class PaymentViewModel:ViewModel() {
    private val ownerPaymentResponseLiveData= MutableLiveData<Resource<CommonResponse>>()

    fun ownerServicePaymentBillDetails(context: Context ,
                                       request_id: String ,
                                       payment_status: Int? ,
                                 selectedFile: String?
    ) {
        val params = mapOf(
            "request_id" to request_id.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
            "payment_status" to payment_status.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull()))
        var document: MultipartBody.Part
        val repository= ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            ownerPaymentResponseLiveData.postValue(Resource.loading(null))
            val file = File(selectedFile)
            var compressedFile : File? =null
            try {
                compressedFile =
                    Compressor.compress(context , file!! , Dispatchers.IO)
            } catch (e : EOFException) {
                e.printStackTrace()
            } catch (e : IOException) {
                e.printStackTrace()
            }

            val requestBody : RequestBody =
                compressedFile!!.asRequestBody("*/*".toMediaTypeOrNull())
            document = MultipartBody.Part.createFormData("document",compressedFile.name,requestBody)
            try {
                repository.ownerServicePaymentBill(params,document).let {
                    val response=it.body()
                    if(response!!.status== Constants.REQUEST_OK){
                        ownerPaymentResponseLiveData.postValue(Resource.success(response))
                    }
                    if(response.status== Constants.REQUEST_BAD_REQUEST){
                        ownerPaymentResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                    if(response.status== Constants.REQUEST_UNAUTHORIZED){
                        ownerPaymentResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                }

            }catch (ex:Exception){
                ownerPaymentResponseLiveData.postValue(Resource.noInternet("",null))
            }
        }
    }

    fun getOwnerServicePaymentBillResponse(): LiveData<Resource<CommonResponse>> {
        return ownerPaymentResponseLiveData
    }
}