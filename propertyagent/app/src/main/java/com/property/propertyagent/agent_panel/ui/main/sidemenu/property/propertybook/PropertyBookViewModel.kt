package com.property.propertyagent.agent_panel.ui.main.sidemenu.property.propertybook

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyagent.data.ApiRepositoryProvider
import com.property.propertyagent.modal.CommonResponse
import com.property.propertyagent.modal.agent.agent_pending_amount.AgentFetchPendingAmountResponse
import com.property.propertyagent.modal.agent.agent_proceed_book_details.AgentProceedBookDetails
import com.property.propertyagent.modal.agent.agent_sub_package_list.AgentSubPackageList
import com.property.propertyagent.utils.AppPreferences.selected_package_id
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

class PropertyBookViewModel : ViewModel() {
    private val proceedBookingDetailsResponseLiveData =
        MutableLiveData<Resource<AgentProceedBookDetails>>()
    private val agentUploadDocumentResponseLiveData = MutableLiveData<Resource<CommonResponse>>()

    private val agentPackagesResponseLiveData =
        MutableLiveData<Resource<AgentSubPackageList>>()

    private val agentFetchPendingAmountResponseLiveData =
        MutableLiveData<Resource<AgentFetchPendingAmountResponse>>()

    private val agentPayFullAmountResponseLiveData = MutableLiveData<Resource<CommonResponse>>()

    fun agentPackageList(property_id: Int) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            agentPackagesResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.agentPackageList(property_id).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        agentPackagesResponseLiveData.postValue(Resource.success(response))
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        agentPackagesResponseLiveData.postValue(Resource.error("null", response))
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        agentPackagesResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        agentPackagesResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                }
            } catch (ex: Exception) {
                agentPackagesResponseLiveData.postValue(Resource.noInternet("", null))
            }
        }
    }

    fun getAgentPackageListResponse(): LiveData<Resource<AgentSubPackageList>> {
        return agentPackagesResponseLiveData
    }

    fun agentUploadUserPropertyDocumentsDetails(
        context: Context,
        user_property_id: String,
        images: ArrayList<String>,
    ) {
        val params = mapOf(
            "user_property_id" to user_property_id.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        )

        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            agentUploadDocumentResponseLiveData.postValue(Resource.loading(null))

            val document: ArrayList<MultipartBody.Part> = ArrayList()
            if (images.isNotEmpty()) {
                for (i in 0 until images.size) {
                    var imageProperty: MultipartBody.Part? = null
                    if (images[i].isNotEmpty()) {
                        val file = File(images[i])
                        var compressedFile: File? = null
                        try {
                            compressedFile =
                                Compressor.compress(context, file, Dispatchers.IO)
                        } catch (e: EOFException) {
                            e.printStackTrace()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                        val requestBody: RequestBody =
                            compressedFile!!.asRequestBody("*/*".toMediaTypeOrNull())
                        imageProperty = MultipartBody.Part.createFormData(
                            "document[$i]",
                            compressedFile.name,
                            requestBody
                        )
                    }
                    document.add(imageProperty!!)
                }
            }
            try {
                repository.agentUploadPropertyDocuments(params, document).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        agentUploadDocumentResponseLiveData.postValue(Resource.success(response))
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        agentUploadDocumentResponseLiveData.postValue(
                            Resource.error(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        agentUploadDocumentResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        agentUploadDocumentResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                }
            } catch (ex: Exception) {
                agentUploadDocumentResponseLiveData.postValue(Resource.noInternet("", null))
            }
        }
    }

    fun getAgentUploadPropertyDocumentsResponse(): LiveData<Resource<CommonResponse>> {
        return agentUploadDocumentResponseLiveData
    }

    fun agentProceedBookDetails(tour_id: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            proceedBookingDetailsResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.agentProceedBookDetails(tour_id).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        proceedBookingDetailsResponseLiveData.postValue(Resource.success(response))
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        proceedBookingDetailsResponseLiveData.postValue(
                            Resource.error(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        proceedBookingDetailsResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        proceedBookingDetailsResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                }

            } catch (ex: Exception) {
                proceedBookingDetailsResponseLiveData.postValue(Resource.noInternet("", null))
            }
        }
    }

    fun getAgentProceedBookDetailsResponse(): LiveData<Resource<AgentProceedBookDetails>> {
        return proceedBookingDetailsResponseLiveData
    }

    private val commonResponseLiveData = MutableLiveData<Resource<CommonResponse>>()

    fun uploadBookPropertyDetails(
        context: Context,
        property_id: String, user_id: String, check_in: String, check_out: String,
        payment_status: Int?, payment_count: String, tour_id: String,
        selectedFile: String,
    ) {
        val params = mapOf(
            "property_id" to property_id.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
            "user_id" to user_id.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
            "check_in" to check_in.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
            "check_out" to check_out.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
            "payment_status" to payment_status.toString()
                .toRequestBody("multipart/form-data".toMediaTypeOrNull()),
            "payment_count" to payment_count.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
            "tour_id" to tour_id
                .toRequestBody("multipart/form-data".toMediaTypeOrNull()),
            "package_id" to selected_package_id
                .toRequestBody("multipart/form-data".toMediaTypeOrNull())
        )
        var document: MultipartBody.Part
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            commonResponseLiveData.postValue(Resource.loading(null))
            val file = File(selectedFile)
            var compressedFile: File? = null
            try {
                compressedFile =
                    Compressor.compress(context, file, Dispatchers.IO)
            } catch (e: EOFException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            val requestBody: RequestBody =
                compressedFile!!.asRequestBody("*/*".toMediaTypeOrNull())
            document =
                MultipartBody.Part.createFormData("document", compressedFile.name, requestBody)
            try {
                repository.uploadBookPropertyDetails(params, document).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        commonResponseLiveData.postValue(Resource.success(response))
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        commonResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "Server Error",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        commonResponseLiveData.postValue(Resource.dataEmpty(response.response, response))
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        commonResponseLiveData.postValue(Resource.dataEmpty(response.response, response))
                    }
                }
            } catch (ex: Exception) {
                commonResponseLiveData.postValue(Resource.noInternet("", null))
            }
        }
    }

    fun getBookPropertyResponse(): LiveData<Resource<CommonResponse>> {
        return commonResponseLiveData
    }

    private val agentRequestContractResponseLiveData = MutableLiveData<Resource<CommonResponse>>()

    fun agentRequestContract(user_property_id: String, tour_id: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            agentRequestContractResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.agentRequestContract(user_property_id, tour_id).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        agentRequestContractResponseLiveData.postValue(Resource.success(response))
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        agentRequestContractResponseLiveData.postValue(
                            Resource.error(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        agentRequestContractResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        agentRequestContractResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                }
            } catch (ex: Exception) {
                agentRequestContractResponseLiveData.postValue(Resource.noInternet("", null))
            }
        }
    }

    fun getAgentRequestContractDetailsResponse(): LiveData<Resource<CommonResponse>> {
        return agentRequestContractResponseLiveData
    }

    fun agentFetchPendingAmount(user_property_id: String, tour_id: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            agentFetchPendingAmountResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.agentFetchPendingAmount(user_property_id, tour_id).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        agentFetchPendingAmountResponseLiveData.postValue(Resource.success(response))
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        agentFetchPendingAmountResponseLiveData.postValue(
                            Resource.error(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        agentFetchPendingAmountResponseLiveData.postValue(
                            Resource.dataEmpty(response.response,
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        agentFetchPendingAmountResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                }
            } catch (ex: Exception) {
                agentFetchPendingAmountResponseLiveData.postValue(Resource.noInternet("", null))
            }
        }
    }

    fun getAgentPendingAmountResponse(): LiveData<Resource<AgentFetchPendingAmountResponse>> {
        return agentFetchPendingAmountResponseLiveData
    }

    fun agentPayFullAmount(
        context: Context,
        pending_amount: String,
        document_status: Int?,
        tour_id: String,
        selectedFile: String,
    ) {
        val params = mapOf(
            "pending_amount" to pending_amount.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
            "document_status" to document_status.toString()
                .toRequestBody("multipart/form-data".toMediaTypeOrNull()),
            "tour_id" to tour_id
                .toRequestBody("multipart/form-data".toMediaTypeOrNull())
        )
        var document: MultipartBody.Part
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            agentPayFullAmountResponseLiveData.postValue(Resource.loading(null))
            val file = File(selectedFile)
            var compressedFile: File? = null
            try {
                compressedFile =
                    Compressor.compress(context, file, Dispatchers.IO)
            } catch (e: EOFException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            val requestBody: RequestBody =
                compressedFile!!.asRequestBody("*/*".toMediaTypeOrNull())
            document =
                MultipartBody.Part.createFormData("document", compressedFile.name, requestBody)
            try {
                repository.agentPayFullAmount(params, document).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        agentPayFullAmountResponseLiveData.postValue(Resource.success(response))
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        agentPayFullAmountResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "Server Error",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        agentPayFullAmountResponseLiveData.postValue(
                            Resource.dataEmpty(
                                response.response,
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        agentPayFullAmountResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                }
            } catch (ex: Exception) {
                agentPayFullAmountResponseLiveData.postValue(Resource.noInternet("", null))
            }
        }
    }

    fun getAgentPayFullAmountResponse(): LiveData<Resource<CommonResponse>> {
        return agentPayFullAmountResponseLiveData
    }
}