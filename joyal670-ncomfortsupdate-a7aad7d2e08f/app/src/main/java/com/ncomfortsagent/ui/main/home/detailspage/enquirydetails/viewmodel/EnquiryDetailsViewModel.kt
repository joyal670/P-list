package com.ncomfortsagent.ui.main.home.detailspage.enquirydetails.viewmodel

import android.accounts.NetworkErrorException
import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ncomfortsagent.R
import com.ncomfortsagent.data.ApiRepositoryProvider
import com.ncomfortsagent.model.enquiry_change_status.AgentEnquiryChangeStatsResponse
import com.ncomfortsagent.model.enquiry_details.AgentEnquiryDetailsResponse
import com.ncomfortsagent.utils.Constants
import com.ncomfortsagent.utils.Resource
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.net.UnknownHostException

class EnquiryDetailsViewModel(@SuppressLint("StaticFieldLeak") private val activity: Activity) :
    ViewModel() {

    private val enquiryDetailsLiveData = MutableLiveData<Resource<AgentEnquiryDetailsResponse>>()
    private val enquiryChangeStatusLiveData =
        MutableLiveData<Resource<AgentEnquiryChangeStatsResponse>>()

    fun getAgentEnquiryDetailsResponse(): LiveData<Resource<AgentEnquiryDetailsResponse>> {
        return enquiryDetailsLiveData
    }

    fun enquiryDetails(enquiry_id: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            enquiryDetailsLiveData.postValue(Resource.loading(null))
            try {
                repository.enquiryDetails(enquiry_id).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        enquiryDetailsLiveData.postValue(Resource.success(response))
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        enquiryDetailsLiveData.postValue(
                            Resource.error(
                                activity.getString(R.string.bad_request),
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        enquiryDetailsLiveData.postValue(
                            Resource.error(
                                activity.getString(R.string.unauthorized),
                                response
                            )
                        )
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        enquiryDetailsLiveData.postValue(Resource.dataEmpty("null", response))
                    }
                }
            } catch (ex: UnknownHostException) {
                enquiryDetailsLiveData.postValue(
                    Resource.noInternet(
                        activity.getString(R.string.please_check_your_internet_connection),
                        null
                    )
                )
            } catch (ex: NetworkErrorException) {
                enquiryDetailsLiveData.postValue(
                    Resource.noInternet(
                        activity.getString(R.string.please_check_your_internet_connection),
                        null
                    )
                )
            } catch (ex: Exception) {
                enquiryDetailsLiveData.postValue(Resource.noInternet("", null))
            }
        }
    }


    fun getAgentEnquiryChangeStatusResponse(): LiveData<Resource<AgentEnquiryChangeStatsResponse>> {
        return enquiryChangeStatusLiveData
    }

    fun enquiryChangeStatus(
        enquiry_id: String,
        enquiry_status: String,
        amount: String,
        image: File?
    ) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            enquiryChangeStatusLiveData.postValue(Resource.loading(null))
            try {

                val params = mapOf(
                    "enquiry_id" to enquiry_id.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                    "enquiry_status" to enquiry_status.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                    "amount" to amount.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                )

                val imageList: ArrayList<MultipartBody.Part> = ArrayList()
                val payImage: MultipartBody.Part?

                if (image != null) {
                    val requestProfileBody: RequestBody =
                        image.asRequestBody("*/*".toMediaTypeOrNull())
                    payImage = MultipartBody.Part.createFormData(
                        "pay_bill",
                        image.name,
                        requestProfileBody
                    )
                    imageList.add(payImage)
                }

                repository.enquiryChangeStatus(params, imageList).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        enquiryChangeStatusLiveData.postValue(Resource.success(response))
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        enquiryChangeStatusLiveData.postValue(
                            Resource.error(
                                response.response,
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        enquiryChangeStatusLiveData.postValue(
                            Resource.error(
                                activity.getString(R.string.unauthorized),
                                response
                            )
                        )
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        enquiryChangeStatusLiveData.postValue(
                            Resource.error(
                                response.response,
                                response
                            )
                        )
                    }
                }
            } catch (ex: UnknownHostException) {
                enquiryChangeStatusLiveData.postValue(
                    Resource.noInternet(
                        activity.getString(R.string.please_check_your_internet_connection),
                        null
                    )
                )
            } catch (ex: NetworkErrorException) {
                enquiryChangeStatusLiveData.postValue(
                    Resource.noInternet(
                        activity.getString(R.string.please_check_your_internet_connection),
                        null
                    )
                )
            } catch (ex: Exception) {
                Log.e("123", "enquiryChangeStatus: " + ex)
                enquiryChangeStatusLiveData.postValue(Resource.noInternet("", null))
            }
        }
    }
}