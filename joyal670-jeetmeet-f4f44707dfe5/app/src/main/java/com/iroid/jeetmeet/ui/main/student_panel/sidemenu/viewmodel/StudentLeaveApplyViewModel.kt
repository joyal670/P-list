package com.iroid.jeetmeet.ui.main.student_panel.sidemenu.viewmodel

import android.accounts.NetworkErrorException
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iroid.jeetmeet.data.ApiRepositoryProvider
import com.iroid.jeetmeet.modal.student.edit_leave.StudentEditLeaveResponse
import com.iroid.jeetmeet.modal.student.leave_apply.StudentLeaveApplyResponse
import com.iroid.jeetmeet.modal.student.leave_apply_catagory.StudentLeaveApplyCatagoryResonse
import com.iroid.jeetmeet.modal.student.leave_apply_delete.StudentLeaveApplyDeleteResponse
import com.iroid.jeetmeet.modal.student.leave_update.StudentLeaveUpdateResponse
import com.iroid.jeetmeet.modal.student.submit_leave_application.StudentSubmitLeaveApplicationResponse
import com.iroid.jeetmeet.utils.Constants
import com.iroid.jeetmeet.utils.Resource
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

class StudentLeaveApplyViewModel(private val context: Context) : ViewModel() {
    /* variables */
    private val studentLeaveApplyLiveData = MutableLiveData<Resource<StudentLeaveApplyResponse>>()
    private val studentLeaveApplyCategoryLiveData =
        MutableLiveData<Resource<StudentLeaveApplyCatagoryResonse>>()
    private val studentSubmitLeaveApplicationLiveData =
        MutableLiveData<Resource<StudentSubmitLeaveApplicationResponse>>()
    private val studentEditLeaveLiveData = MutableLiveData<Resource<StudentEditLeaveResponse>>()
    private val studentUpdateLeaveLiveData = MutableLiveData<Resource<StudentLeaveUpdateResponse>>()
    private val studentDeleteLeaveLiveData =
        MutableLiveData<Resource<StudentLeaveApplyDeleteResponse>>()

    val studentLeaveApplyData: LiveData<Resource<StudentLeaveApplyResponse>>
        get() = studentLeaveApplyLiveData

    val studentLeaveApplyCategoryData: LiveData<Resource<StudentLeaveApplyCatagoryResonse>>
        get() = studentLeaveApplyCategoryLiveData

    val studentSubmitLeaveApplicationData: LiveData<Resource<StudentSubmitLeaveApplicationResponse>>
        get() = studentSubmitLeaveApplicationLiveData

    val studentEditLeaveData: LiveData<Resource<StudentEditLeaveResponse>>
        get() = studentEditLeaveLiveData

    val studentUpdateLeaveData: LiveData<Resource<StudentLeaveUpdateResponse>>
        get() = studentUpdateLeaveLiveData

    val studentDeleteLeaveData: LiveData<Resource<StudentLeaveApplyDeleteResponse>>
        get() = studentDeleteLeaveLiveData


    /* Student leave apply */
    fun studentLeaveApply(sort: Int) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                studentLeaveApplyLiveData.postValue(Resource.loading(null))
                repository.studentLeaveApply(sort).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK_NEW) {
                        studentLeaveApplyLiveData.postValue(Resource.success(response))
                    } else {
                        studentLeaveApplyLiveData.postValue(Resource.error(it.message(), response))
                    }

                }
            } catch (ex: UnknownHostException) {
                studentLeaveApplyLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                studentLeaveApplyLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                studentLeaveApplyLiveData.postValue(Resource.error("Something went wrong", null))
            }
        }
    }

    /* Student leave apply category*/
    fun studentLeaveApplyCategory() {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                studentLeaveApplyCategoryLiveData.postValue(Resource.loading(null))
                repository.studentLeaveApplyCategory().let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK_NEW) {
                        studentLeaveApplyCategoryLiveData.postValue(Resource.success(response))
                    } else {
                        studentLeaveApplyCategoryLiveData.postValue(
                            Resource.error(
                                it.message(),
                                response
                            )
                        )
                    }

                }
            } catch (ex: UnknownHostException) {
                studentLeaveApplyCategoryLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                studentLeaveApplyCategoryLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                studentLeaveApplyCategoryLiveData.postValue(
                    Resource.error(
                        "Something went wrong",
                        null
                    )
                )
            }
        }
    }

    /* Student Submit leave */
    fun studentSubmitLeave(
        tempCategory: Int,
        tempStartDate: String,
        tempStartTime: String,
        tempEndDate: String,
        tempEndTime: String,
        tempReason: String,
        attachmentFile: File
    ) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        studentSubmitLeaveApplicationLiveData.postValue(Resource.loading(null))
        viewModelScope.launch {

            val params = mapOf(
                "start_date" to tempStartDate.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                "end_date" to tempEndDate.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                "start_time" to tempStartTime.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                "end_time" to tempEndTime.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                "leave_category" to tempCategory.toString()
                    .toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                "reason" to tempReason.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            )

            val attachmentList: ArrayList<MultipartBody.Part> = ArrayList()
            var AttachmentFile: MultipartBody.Part? = null
            var compressedFileImage: File? = null

            try {
                if (attachmentFile != null) {
                    try {
                        compressedFileImage = attachmentFile.let {
                            Compressor.compress(context, it, Dispatchers.IO)
                        }
                    } catch (e: EOFException) {
                        e.printStackTrace()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                    val requestBody: RequestBody =
                        compressedFileImage!!.asRequestBody("*/*".toMediaTypeOrNull())
                    AttachmentFile = MultipartBody.Part.createFormData(
                        "attachment",
                        compressedFileImage.name,
                        requestBody
                    )
                    attachmentList.add(AttachmentFile)
                }

                repository.studentSubmitLeaveApplication(params, attachmentList).let {
                    if (it.isSuccessful) {
                        studentSubmitLeaveApplicationLiveData.postValue(Resource.success(it.body()))
                    } else {
                        studentSubmitLeaveApplicationLiveData.postValue(
                            Resource.error(
                                it.message(),
                                null
                            )
                        )
                    }
                }

            } catch (ex: UnknownHostException) {
                studentSubmitLeaveApplicationLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                studentSubmitLeaveApplicationLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                studentSubmitLeaveApplicationLiveData.postValue(
                    Resource.error(
                        "Something went wrong",
                        null
                    )
                )
            }
        }
    }

    /* Student edit leave */
    /* Student leave apply category*/
    fun studentEditLeave(leave_id: Int) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                studentEditLeaveLiveData.postValue(Resource.loading(null))
                repository.studentEditLeave(leave_id).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK_NEW) {
                        studentEditLeaveLiveData.postValue(Resource.success(response))
                    } else {
                        studentEditLeaveLiveData.postValue(Resource.error(it.message(), response))
                    }

                }
            } catch (ex: UnknownHostException) {
                studentEditLeaveLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                studentEditLeaveLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                studentEditLeaveLiveData.postValue(Resource.error("Something went wrong", null))
            }
        }
    }

    /* Student Submit edit leave  */
    fun studentUpdateLeave(
        leaveId: Int,
        selectedId: Int,
        tempStartDate: String,
        tempStartTime: String,
        tempEndDate: String,
        tempEndTime: String,
        tempReason: String,
        attachmentFile: File?
    ) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        studentUpdateLeaveLiveData.postValue(Resource.loading(null))
        viewModelScope.launch {
            val params = mapOf(
                "leave_id" to leaveId.toString()
                    .toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                "start_date" to tempStartDate.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                "end_date" to tempEndDate.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                "start_time" to tempStartTime.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                "end_time" to tempEndTime.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                "reason" to tempReason.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                "leave_category" to selectedId.toString()
                    .toRequestBody("multipart/form-data".toMediaTypeOrNull())
            )

            val attachmentList: ArrayList<MultipartBody.Part> = ArrayList()
            var AttachmentFile: MultipartBody.Part? = null
            var compressedFileImage: File? = null

            try {
                if (attachmentFile != null) {
                    try {
                        compressedFileImage = attachmentFile.let {
                            Compressor.compress(context, it, Dispatchers.IO)
                        }
                    } catch (e: EOFException) {
                        e.printStackTrace()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                    val requestBody: RequestBody =
                        compressedFileImage!!.asRequestBody("*/*".toMediaTypeOrNull())
                    AttachmentFile = MultipartBody.Part.createFormData(
                        "attachment",
                        compressedFileImage.name,
                        requestBody
                    )
                    attachmentList.add(AttachmentFile)
                }

                repository.studentLeaveUpdate(params, attachmentList).let {
                    if (it.isSuccessful) {
                        studentUpdateLeaveLiveData.postValue(Resource.success(it.body()))
                    } else {
                        studentUpdateLeaveLiveData.postValue(Resource.error(it.message(), null))
                    }
                }

            } catch (ex: UnknownHostException) {
                studentUpdateLeaveLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                studentUpdateLeaveLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                studentUpdateLeaveLiveData.postValue(Resource.error("Something went wrong", null))
            }
        }
    }

    /* Student delete leave */
    fun studentDeleteLeave(leave_id: Int) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                studentDeleteLeaveLiveData.postValue(Resource.loading(null))
                repository.studentDeleteLeave(leave_id).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK_NEW) {
                        studentDeleteLeaveLiveData.postValue(Resource.success(response))
                    } else if (response.status == Constants.REQUEST_FAILED) {
                        studentDeleteLeaveLiveData.postValue(Resource.error(it.message(), response))
                    } else {
                        studentDeleteLeaveLiveData.postValue(Resource.error(it.message(), response))
                    }

                }
            } catch (ex: UnknownHostException) {
                studentDeleteLeaveLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                studentDeleteLeaveLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                studentDeleteLeaveLiveData.postValue(Resource.error("Something went wrong", null))
            }
        }
    }

}