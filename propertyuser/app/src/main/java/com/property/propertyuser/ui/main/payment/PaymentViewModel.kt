package com.property.propertyuser.ui.main.payment

import android.content.Context
import android.util.Log
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

class PaymentViewModel:ViewModel() {
    private val userBookedBillCommonResponseLiveData= MutableLiveData<Resource<CommonResponse>>()
    private val userServiceBillCommonResponseLiveData= MutableLiveData<Resource<CommonResponse>>()
    private val userRentalBillCommonResponseLiveData= MutableLiveData<Resource<CommonResponse>>()

    private val userEventBookingPackageBillCommonResponseLiveData= MutableLiveData<Resource<CommonResponse>>()
    private val userPayNowLiveData = MutableLiveData<Resource<CommonResponse>>()

    fun uploadUserEventBookPackagePaymentBillDetails(context:Context,
                                                     event_booking_id: String,
                                                     payment_status: Int?,
                                                     selectedFile: String?
    ) {
        val params = mapOf(
            "event_booking_id" to event_booking_id.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
            "payment_status" to payment_status.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull()))
        var document: MultipartBody.Part
        val repository= ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            userBookedBillCommonResponseLiveData.postValue(Resource.loading(null))
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
                repository.uploadEventPackagePaymentBill(params,document).let {
                    val response=it.body()
                    if(response!!.status== Constants.REQUEST_OK){
                        userEventBookingPackageBillCommonResponseLiveData.postValue(Resource.success(response))
                    }
                    if(response!!.status== Constants.REQUEST_BAD_REQUEST){
                        userEventBookingPackageBillCommonResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                    if(response!!.status== Constants.REQUEST_UNAUTHORIZED){
                        userEventBookingPackageBillCommonResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                }

            }catch (ex:Exception){
                userEventBookingPackageBillCommonResponseLiveData.postValue(Resource.noInternet("",null))
            }
        }
    }

    fun getUserEventBookPackagePaymentBillResponse(): LiveData<Resource<CommonResponse>> {
        return userEventBookingPackageBillCommonResponseLiveData
    }

    fun uploadUserRentalPaymentBillDetails(context:Context,
                                           userPropertyId: String,
                                           payment_status: Int?,
                                           selectedFile: String?
    ) {
        val params = mapOf(
            "user_property_id" to userPropertyId.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
            "payment_status" to payment_status.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull()))
        var document: MultipartBody.Part
        val repository= ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            userPayNowLiveData.postValue(Resource.loading(null))
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
                repository.uploadUserPropertyRentalPaymentBill(params,document).let {
                    val response=it.body()
                    if(response!!.status== Constants.REQUEST_OK){
                        userPayNowLiveData.postValue(Resource.success(response))
                    }
                    if(response!!.status== Constants.REQUEST_BAD_REQUEST){
                        userPayNowLiveData.postValue(Resource.dataEmpty(response.response,response))
                    }
                    if(response!!.status== Constants.REQUEST_UNAUTHORIZED){
                        userPayNowLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                }

            }catch (ex:Exception){
                Log.e("TAG", "uploadUserRentalPaymentBillDetails: $ex")
                userPayNowLiveData.postValue(Resource.noInternet("",null))
            }
        }
    }

    fun getUserRentalPaymentBillResponse(): LiveData<Resource<CommonResponse>> {
        return userPayNowLiveData
    }

    fun uploadUserServicePaymentBillDetails(context:Context,
                                           requestId: String,
                                           payment_status: Int?,
                                           selectedFile: String?,ownerChecked:String
    ) {
        val params = mapOf(
            "request_id" to requestId.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
            "payment_status" to payment_status.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull()),
            "owner_payment_checked" to ownerChecked.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        )
        var document: MultipartBody.Part
        val repository= ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            userServiceBillCommonResponseLiveData.postValue(Resource.loading(null))
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
                repository.uploadUserPropertyServicePaymentBill(params,document).let {
                    val response=it.body()
                    if(response!!.status== Constants.REQUEST_OK){
                        userServiceBillCommonResponseLiveData.postValue(Resource.success(response))
                    }
                    if(response!!.status== Constants.REQUEST_BAD_REQUEST){
                        userServiceBillCommonResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                    if(response!!.status== Constants.REQUEST_UNAUTHORIZED){
                        userServiceBillCommonResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                }

            }catch (ex:Exception){
                userServiceBillCommonResponseLiveData.postValue(Resource.noInternet("",null))
            }
        }
    }

    fun getUserServicePaymentBillResponse(): LiveData<Resource<CommonResponse>> {
        return userServiceBillCommonResponseLiveData
    }
    fun uploadUserBookedPaymentBillDetails(context:Context,
                                 userPropertyId: String,
                                 payment_status: Int?,
                                 selectedFile: String?
    ) {
        val params = mapOf(
            "user_property_id" to userPropertyId.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
            "payment_status" to payment_status.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull()))
        var document: MultipartBody.Part
        val repository= ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            userBookedBillCommonResponseLiveData.postValue(Resource.loading(null))
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
                repository.uploadUserPropertyBookPaymentBill(params,document).let {
                    val response=it.body()
                    if(response!!.status== Constants.REQUEST_OK){
                        userBookedBillCommonResponseLiveData.postValue(Resource.success(response))
                    }
                    if(response!!.status== Constants.REQUEST_BAD_REQUEST){
                        userBookedBillCommonResponseLiveData.postValue(Resource.dataEmpty(response.response,response))
                    }
                    if(response!!.status== Constants.REQUEST_UNAUTHORIZED){
                        userBookedBillCommonResponseLiveData.postValue(Resource.dataEmpty("null",response))
                    }
                }

            }catch (ex:Exception){
                userBookedBillCommonResponseLiveData.postValue(Resource.noInternet("",null))
            }
        }
    }

    fun getUserBookedPaymentBillResponse(): LiveData<Resource<CommonResponse>> {
        return userBookedBillCommonResponseLiveData
    }
    private val commonResponseLiveData= MutableLiveData<Resource<CommonResponse>>()

    fun uploadPaymentBillDetails(context:Context,
        bookingId: String,
        documentStatus: Int?,
        selectedFile: String?
    ) {
        val params = mapOf(
            "booking_id" to bookingId.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
            "document_status" to documentStatus.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull()))
        var document: MultipartBody.Part
        val repository= ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            commonResponseLiveData.postValue(Resource.loading(null))
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
                repository.uploadBookPaymentBill(params,document).let {
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
                }

            }catch (ex:Exception){
                commonResponseLiveData.postValue(Resource.noInternet("",null))
            }
        }
    }

    fun getPaymentBillResponse(): LiveData<Resource<CommonResponse>> {
        return commonResponseLiveData
    }
}