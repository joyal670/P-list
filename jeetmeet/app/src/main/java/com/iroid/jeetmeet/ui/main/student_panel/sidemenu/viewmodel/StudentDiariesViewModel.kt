package com.iroid.jeetmeet.ui.main.student_panel.sidemenu.viewmodel

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iroid.jeetmeet.data.ApiRepositoryProvider
import com.iroid.jeetmeet.modal.student.diaries.StudentDiariesResponse
import com.iroid.jeetmeet.modal.student.diaries_delete.StudentDiariesDeleteResponse
import com.iroid.jeetmeet.modal.student.diaries_edit.StudentDiariesEditResponse
import com.iroid.jeetmeet.modal.student.diaries_save.StudentDiariesSaveResponse
import com.iroid.jeetmeet.modal.student.diaries_update.StudentDiariesUpdateResponse
import com.iroid.jeetmeet.utils.Constants
import com.iroid.jeetmeet.utils.Resource
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class StudentDiariesViewModel : ViewModel() {
    /* variables */
    private val studentDiariesLiveData = MutableLiveData<Resource<StudentDiariesResponse>>()
    private val studentDiariesDeleteLiveData =
        MutableLiveData<Resource<StudentDiariesDeleteResponse>>()
    private val studentDiariesEditLiveData = MutableLiveData<Resource<StudentDiariesEditResponse>>()
    private val studentDiariesUpdateLiveData =
        MutableLiveData<Resource<StudentDiariesUpdateResponse>>()
    private val studentDiariesSaveLiveData = MutableLiveData<Resource<StudentDiariesSaveResponse>>()

    val studentDiariesData: LiveData<Resource<StudentDiariesResponse>>
        get() = studentDiariesLiveData

    val studentDiariesDeleteData: LiveData<Resource<StudentDiariesDeleteResponse>>
        get() = studentDiariesDeleteLiveData

    val studentDiariesEditData: LiveData<Resource<StudentDiariesEditResponse>>
        get() = studentDiariesEditLiveData

    val studentDiariesUpdateData: LiveData<Resource<StudentDiariesUpdateResponse>>
        get() = studentDiariesUpdateLiveData

    val studentDiariesSaveData: LiveData<Resource<StudentDiariesSaveResponse>>
        get() = studentDiariesSaveLiveData


    /* Student diaries */
    fun studentDiaries(sort: Int) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                studentDiariesLiveData.postValue(Resource.loading(null))
                repository.studentDiaries(sort).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK_NEW) {
                        studentDiariesLiveData.postValue(Resource.success(response))
                    } else {
                        studentDiariesLiveData.postValue(Resource.error(it.message(), response))
                    }

                }
            } catch (ex: UnknownHostException) {
                studentDiariesLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                studentDiariesLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                studentDiariesLiveData.postValue(Resource.error("Something went wrong", null))
            }
        }
    }

    /* Student diaries delete*/
    fun studentDiariesDelete(diary_id: Int) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                studentDiariesDeleteLiveData.postValue(Resource.loading(null))
                repository.studentDiariesDelete(diary_id).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK_NEW) {
                        studentDiariesDeleteLiveData.postValue(Resource.success(response))
                    } else {
                        studentDiariesDeleteLiveData.postValue(
                            Resource.error(
                                it.message(),
                                response
                            )
                        )
                    }

                }
            } catch (ex: UnknownHostException) {
                studentDiariesDeleteLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                studentDiariesDeleteLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                studentDiariesDeleteLiveData.postValue(Resource.error("Something went wrong", null))
            }
        }
    }

    /* Student diaries edit*/
    fun studentDiariesEdit(diary_id: Int) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                studentDiariesEditLiveData.postValue(Resource.loading(null))
                repository.studentEditDiaries(diary_id).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK_NEW) {
                        studentDiariesEditLiveData.postValue(Resource.success(response))
                    } else {
                        studentDiariesEditLiveData.postValue(Resource.error(it.message(), response))
                    }

                }
            } catch (ex: UnknownHostException) {
                studentDiariesEditLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                studentDiariesEditLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                studentDiariesEditLiveData.postValue(Resource.error("Something went wrong", null))
            }
        }
    }

    /* Student diaries update*/
    fun studentDiariesUpdate(diary_id: Int, note: String, date: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                studentDiariesUpdateLiveData.postValue(Resource.loading(null))
                repository.studentUpdateDiaries(diary_id, note, date).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK_NEW) {
                        studentDiariesUpdateLiveData.postValue(Resource.success(response))
                    } else {
                        studentDiariesUpdateLiveData.postValue(
                            Resource.error(
                                it.message(),
                                response
                            )
                        )
                    }

                }
            } catch (ex: UnknownHostException) {
                studentDiariesUpdateLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                studentDiariesUpdateLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                studentDiariesUpdateLiveData.postValue(Resource.error("Something went wrong", null))
            }
        }
    }

    /* Student diaries save*/
    fun studentDiariesSave(note: String, date: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                studentDiariesSaveLiveData.postValue(Resource.loading(null))
                repository.studentSaveDiaries(note, date).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK_NEW) {
                        studentDiariesSaveLiveData.postValue(Resource.success(response))
                    } else {
                        studentDiariesSaveLiveData.postValue(Resource.error(it.message(), response))
                    }

                }
            } catch (ex: UnknownHostException) {
                studentDiariesSaveLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                studentDiariesSaveLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                studentDiariesSaveLiveData.postValue(Resource.error("Something went wrong", null))
            }
        }
    }
}