package com.property.propertyagent.agent_panel.ui.main.sidemenu.profile

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyagent.data.ApiRepositoryProvider
import com.property.propertyagent.modal.CommonResponse
import com.property.propertyagent.modal.agent.agent_profile.AgentProfileDetailsResponse
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

class ProfileViewModel : ViewModel() {
    private val agentProfileDetailsResponseLiveData =
        MutableLiveData<Resource<AgentProfileDetailsResponse>>()
    private val agentProfileUpdateResponseLiveData =
        MutableLiveData<Resource<CommonResponse>>()
    private val agentRemoveProfileImageResponseLiveData =
        MutableLiveData<Resource<CommonResponse>>()

    fun agentProfileUpdate(
        context: Context,
        name: String,
        email: String,
        phone: String,
        account_number: String,
        bank_name: String,
        ifsc: String,
        selectedFile: String,
    ) {
        Log.i("TAG", "agentProfileUpdate: $selectedFile")
        val params: Map<String, RequestBody>
        if (selectedFile.isNotBlank()) {
            params = mapOf(
                "name" to name.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                "email" to email.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                "phone" to phone.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                "account_number" to account_number.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                "bank_name" to bank_name.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                "ifsc" to ifsc.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                "image" to selectedFile.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            )
        } else {
            params = mapOf(
                "name" to name.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                "email" to email.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                "phone" to phone.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                "account_number" to account_number.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                "bank_name" to bank_name.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                "ifsc" to ifsc.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            )
        }
        var image: MultipartBody.Part? = null
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            if (selectedFile.isNotBlank()) {
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
                if (compressedFile != null) {
                    val requestBody: RequestBody =
                        compressedFile.asRequestBody("*/*".toMediaTypeOrNull())
                    image =
                        MultipartBody.Part.createFormData("image", compressedFile.name, requestBody)
                }
            }
            agentProfileUpdateResponseLiveData.postValue(Resource.loading(null))
            try {
                if (selectedFile.isNotBlank()) {
                    repository.updateAgentProfileDetails(params, image!!).let {
                        val response = it.body()
                        if (response!!.status == Constants.REQUEST_OK) {
                            agentProfileUpdateResponseLiveData.postValue(Resource.success(response))
                        }
                        if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                            agentProfileUpdateResponseLiveData.postValue(
                                Resource.error(
                                    "null",
                                    response
                                )
                            )
                        }
                        if (response.status == Constants.REQUEST_BAD_REQUEST) {
                            agentProfileUpdateResponseLiveData.postValue(
                                Resource.dataEmpty(
                                    "null",
                                    response
                                )
                            )
                        }
                        if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                            agentProfileUpdateResponseLiveData.postValue(
                                Resource.dataEmpty(
                                    "null",
                                    response
                                )
                            )
                        }
                    }
                } else {
                    repository.updateAgentProfileDetails(params).let {
                        val response = it.body()
                        if (response!!.status == Constants.REQUEST_OK) {
                            agentProfileUpdateResponseLiveData.postValue(Resource.success(response))
                        }
                        if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                            agentProfileUpdateResponseLiveData.postValue(
                                Resource.error(
                                    "null",
                                    response
                                )
                            )
                        }
                        if (response.status == Constants.REQUEST_BAD_REQUEST) {
                            agentProfileUpdateResponseLiveData.postValue(
                                Resource.dataEmpty(
                                    "null",
                                    response
                                )
                            )
                        }
                        if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                            agentProfileUpdateResponseLiveData.postValue(
                                Resource.dataEmpty(
                                    "null",
                                    response
                                )
                            )
                        }
                    }
                }
            } catch (ex: Exception) {
                agentProfileUpdateResponseLiveData.postValue(Resource.noInternet("", null))
            }
        }
    }

    fun getAgentProfileUpdateResponse(): LiveData<Resource<CommonResponse>> {
        return agentProfileUpdateResponseLiveData
    }

    fun agentProfile() {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            agentProfileDetailsResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.agentProfileDetails().let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        agentProfileDetailsResponseLiveData.postValue(Resource.success(response))
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        agentProfileDetailsResponseLiveData.postValue(
                            Resource.error(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        agentProfileDetailsResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        agentProfileDetailsResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                }
            } catch (ex: Exception) {
                agentProfileDetailsResponseLiveData.postValue(Resource.noInternet("", null))
            }
        }
    }

    fun getAgentProfileDetailsResponse(): LiveData<Resource<AgentProfileDetailsResponse>> {
        return agentProfileDetailsResponseLiveData
    }

    fun removeAgentProfileImage() {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            agentRemoveProfileImageResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.removeAgentProfileImage().let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        agentRemoveProfileImageResponseLiveData.postValue(Resource.success(response))
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        agentRemoveProfileImageResponseLiveData.postValue(
                            Resource.error(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        agentRemoveProfileImageResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        agentRemoveProfileImageResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                }
            } catch (ex: Exception) {
                agentRemoveProfileImageResponseLiveData.postValue(Resource.noInternet("", null))
            }
        }
    }

    fun getAgentRemoveProfileImageResponse(): LiveData<Resource<CommonResponse>> {
        return agentRemoveProfileImageResponseLiveData
    }
}