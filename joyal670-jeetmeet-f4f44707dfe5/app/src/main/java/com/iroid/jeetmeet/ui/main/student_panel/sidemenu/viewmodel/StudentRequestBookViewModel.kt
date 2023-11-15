package com.iroid.jeetmeet.ui.main.student_panel.sidemenu.viewmodel

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iroid.jeetmeet.data.ApiRepositoryProvider
import com.iroid.jeetmeet.modal.student.request_book.StudentRequestBookResponse
import com.iroid.jeetmeet.modal.student.request_book_apply.StudentRequestBookApplyResponse
import com.iroid.jeetmeet.modal.student.request_book_cancel.StudentRequestBookCancelResponse
import com.iroid.jeetmeet.utils.Constants
import com.iroid.jeetmeet.utils.Resource
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class StudentRequestBookViewModel : ViewModel() {
    /* variables */
    private val studentRequestBookLiveData = MutableLiveData<Resource<StudentRequestBookResponse>>()
    private val studentRequestBookApplyLiveData =
        MutableLiveData<Resource<StudentRequestBookApplyResponse>>()
    private val studentRequestBookCancelLiveData =
        MutableLiveData<Resource<StudentRequestBookCancelResponse>>()

    val studentRequestBookData: LiveData<Resource<StudentRequestBookResponse>>
        get() = studentRequestBookLiveData

    val studentRequestBookApplyData: LiveData<Resource<StudentRequestBookApplyResponse>>
        get() = studentRequestBookApplyLiveData

    val studentRequestBookCancelData: LiveData<Resource<StudentRequestBookCancelResponse>>
        get() = studentRequestBookCancelLiveData

    /* Student request book */
    fun studentRequestBook(sort: Int) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                studentRequestBookLiveData.postValue(Resource.loading(null))
                repository.studentRequestBook(sort).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK_NEW) {
                        studentRequestBookLiveData.postValue(Resource.success(response))
                    } else {
                        studentRequestBookLiveData.postValue(Resource.error(it.message(), response))
                    }

                }
            } catch (ex: UnknownHostException) {
                studentRequestBookLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                studentRequestBookLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                studentRequestBookLiveData.postValue(Resource.error("Something went wrong", null))
            }
        }
    }

    /* Student request book apply*/
    fun studentRequestBookApply(book_id: Int) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                studentRequestBookApplyLiveData.postValue(Resource.loading(null))
                repository.studentRequestBookApply(book_id).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK_NEW) {
                        studentRequestBookApplyLiveData.postValue(Resource.success(response))
                    } else if (response.status == Constants.REQUEST_FAILED) {
                        studentRequestBookApplyLiveData.postValue(
                            Resource.error(
                                it.message(),
                                response
                            )
                        )
                    } else {
                        studentRequestBookApplyLiveData.postValue(
                            Resource.error(
                                it.message(),
                                response
                            )
                        )
                    }

                }
            } catch (ex: UnknownHostException) {
                studentRequestBookApplyLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                studentRequestBookApplyLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                studentRequestBookApplyLiveData.postValue(
                    Resource.error(
                        "Something went wrong",
                        null
                    )
                )
            }
        }
    }

    /* Student request book cancel*/
    fun studentRequestBookCancel(req_id: Int) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                studentRequestBookCancelLiveData.postValue(Resource.loading(null))
                repository.studentRequestBookCancel(req_id).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK_NEW) {
                        studentRequestBookCancelLiveData.postValue(Resource.success(response))
                    } else if (response.status == Constants.REQUEST_FAILED) {
                        studentRequestBookCancelLiveData.postValue(
                            Resource.error(
                                it.message(),
                                response
                            )
                        )
                    } else {
                        studentRequestBookCancelLiveData.postValue(
                            Resource.error(
                                it.message(),
                                response
                            )
                        )
                    }

                }
            } catch (ex: UnknownHostException) {
                studentRequestBookCancelLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                studentRequestBookCancelLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                studentRequestBookCancelLiveData.postValue(
                    Resource.error(
                        "Something went wrong",
                        null
                    )
                )
            }
        }
    }
}