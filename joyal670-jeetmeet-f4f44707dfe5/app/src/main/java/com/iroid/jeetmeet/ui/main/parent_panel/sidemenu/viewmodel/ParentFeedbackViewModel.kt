package com.iroid.jeetmeet.ui.main.parent_panel.sidemenu.viewmodel

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iroid.jeetmeet.data.ApiRepositoryProvider
import com.iroid.jeetmeet.modal.parent.feedback_delete.ParentFeedbackDeleteResponse
import com.iroid.jeetmeet.modal.parent.feedback_edit.ParentFeedbackEditResponse
import com.iroid.jeetmeet.modal.parent.feedback_save.ParentFeedbackSaveResponse
import com.iroid.jeetmeet.modal.parent.feedback_update.ParentFeedbackUpdateResponse
import com.iroid.jeetmeet.modal.parent.feedbacks.ParentFeedbacksResponse
import com.iroid.jeetmeet.utils.Constants
import com.iroid.jeetmeet.utils.Resource
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class ParentFeedbackViewModel : ViewModel() {
    /* variables */
    private val parentFeedbackLiveData = MutableLiveData<Resource<ParentFeedbacksResponse>>()
    private val parentFeedbackEditLiveData = MutableLiveData<Resource<ParentFeedbackEditResponse>>()
    private val parentFeedbackUpdateLiveData =
        MutableLiveData<Resource<ParentFeedbackUpdateResponse>>()
    private val parentFeedbackDeleteLiveData =
        MutableLiveData<Resource<ParentFeedbackDeleteResponse>>()
    private val parentFeedbackSaveLiveData = MutableLiveData<Resource<ParentFeedbackSaveResponse>>()

    val parentFeedbackData: LiveData<Resource<ParentFeedbacksResponse>>
        get() = parentFeedbackLiveData

    val parentFeedbackEditData: LiveData<Resource<ParentFeedbackEditResponse>>
        get() = parentFeedbackEditLiveData

    val parentFeedbackUpdateData: LiveData<Resource<ParentFeedbackUpdateResponse>>
        get() = parentFeedbackUpdateLiveData

    val parentFeedbackDeleteData: LiveData<Resource<ParentFeedbackDeleteResponse>>
        get() = parentFeedbackDeleteLiveData

    val parentFeedbackSaveData: LiveData<Resource<ParentFeedbackSaveResponse>>
        get() = parentFeedbackSaveLiveData

    /* Parent feedback */
    fun parentFeedbacks(sort: Int) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                parentFeedbackLiveData.postValue(Resource.loading(null))
                repository.parentFeedback(sort).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK_NEW) {
                        parentFeedbackLiveData.postValue(Resource.success(response))
                    } else {
                        parentFeedbackLiveData.postValue(Resource.error(it.message(), response))
                    }

                }
            } catch (ex: UnknownHostException) {
                parentFeedbackLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                parentFeedbackLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                parentFeedbackLiveData.postValue(Resource.error("Something went wrong", null))
            }
        }
    }

    /* Parent feedback edit */
    fun parentFeedbackEdit(feedback_id: Int) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                parentFeedbackEditLiveData.postValue(Resource.loading(null))
                repository.parentFeedbackEdit(feedback_id).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK_NEW) {
                        parentFeedbackEditLiveData.postValue(Resource.success(response))
                    } else {
                        parentFeedbackEditLiveData.postValue(Resource.error(it.message(), response))
                    }

                }
            } catch (ex: UnknownHostException) {
                parentFeedbackEditLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                parentFeedbackEditLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                parentFeedbackEditLiveData.postValue(Resource.error("Something went wrong", null))
            }
        }
    }

    /* Parent feedback update */
    fun parentFeedbackUpdate(feedback_id: Int, date: String, note: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                parentFeedbackUpdateLiveData.postValue(Resource.loading(null))
                repository.parentFeedbackUpdate(feedback_id, date, note).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK_NEW) {
                        parentFeedbackUpdateLiveData.postValue(Resource.success(response))
                    } else {
                        parentFeedbackUpdateLiveData.postValue(
                            Resource.error(
                                it.message(),
                                response
                            )
                        )
                    }

                }
            } catch (ex: UnknownHostException) {
                parentFeedbackUpdateLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                parentFeedbackUpdateLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                parentFeedbackUpdateLiveData.postValue(Resource.error("Something went wrong", null))
            }
        }
    }

    /* Parent feedback delete */
    fun parentFeedbackDelete(feedback_id: Int) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                parentFeedbackDeleteLiveData.postValue(Resource.loading(null))
                repository.parentFeedbackDelete(feedback_id).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK_NEW) {
                        parentFeedbackDeleteLiveData.postValue(Resource.success(response))
                    } else {
                        parentFeedbackDeleteLiveData.postValue(
                            Resource.error(
                                it.message(),
                                response
                            )
                        )
                    }

                }
            } catch (ex: UnknownHostException) {
                parentFeedbackDeleteLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                parentFeedbackDeleteLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                parentFeedbackDeleteLiveData.postValue(Resource.error("Something went wrong", null))
            }
        }
    }

    /* Parent feedback save */
    fun parentFeedbackSave(date: String, note: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                parentFeedbackSaveLiveData.postValue(Resource.loading(null))
                repository.parentFeedbackSave(date, note).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK_NEW) {
                        parentFeedbackSaveLiveData.postValue(Resource.success(response))
                    } else {
                        parentFeedbackSaveLiveData.postValue(Resource.error(it.message(), response))
                    }

                }
            } catch (ex: UnknownHostException) {
                parentFeedbackSaveLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                parentFeedbackSaveLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                parentFeedbackSaveLiveData.postValue(Resource.error("Something went wrong", null))
            }
        }
    }
}