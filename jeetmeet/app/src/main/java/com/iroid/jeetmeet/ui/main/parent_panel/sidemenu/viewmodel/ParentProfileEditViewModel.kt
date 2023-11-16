package com.iroid.jeetmeet.ui.main.parent_panel.sidemenu.viewmodel

import android.accounts.NetworkErrorException
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iroid.jeetmeet.data.ApiRepositoryProvider
import com.iroid.jeetmeet.modal.parent.profile_edit.ParentProfileEditResponse
import com.iroid.jeetmeet.modal.parent.profile_state.ParentProfileStateResponse
import com.iroid.jeetmeet.modal.parent.profile_update.ParentProfileUpdateResponse
import com.iroid.jeetmeet.utils.Constants
import com.iroid.jeetmeet.utils.Resource
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class ParentProfileEditViewModel : ViewModel() {
    /* variables */
    private val parentProfileEditLiveData = MutableLiveData<Resource<ParentProfileEditResponse>>()
    private val parentProfileStateLiveData = MutableLiveData<Resource<ParentProfileStateResponse>>()
    private val parentProfileUpdateLiveData =
        MutableLiveData<Resource<ParentProfileUpdateResponse>>()

    val parentProfileEditData: LiveData<Resource<ParentProfileEditResponse>>
        get() = parentProfileEditLiveData

    val parentProfileStateData: LiveData<Resource<ParentProfileStateResponse>>
        get() = parentProfileStateLiveData

    val parentProfileUpdateData: LiveData<Resource<ParentProfileUpdateResponse>>
        get() = parentProfileUpdateLiveData

    /* Parent profile edit */
    fun parentProfileEdit() {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                parentProfileEditLiveData.postValue(Resource.loading(null))
                repository.parentProfileEdit().let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK_NEW) {
                        parentProfileEditLiveData.postValue(Resource.success(response))
                    } else {
                        parentProfileEditLiveData.postValue(Resource.error(it.message(), response))
                    }

                }
            } catch (ex: UnknownHostException) {
                parentProfileEditLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                parentProfileEditLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                parentProfileEditLiveData.postValue(Resource.error("Something went wrong", null))
                Log.e("TAG", "parentProfileEdit: $ex")
            }
        }
    }

    /* Parent profile state */
    fun parentProfileState(country_id: Int) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                parentProfileStateLiveData.postValue(Resource.loading(null))
                repository.parentProfileState(country_id).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK_NEW) {
                        parentProfileStateLiveData.postValue(Resource.success(response))
                    } else {
                        parentProfileStateLiveData.postValue(Resource.error(it.message(), response))
                    }

                }
            } catch (ex: UnknownHostException) {
                parentProfileStateLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                parentProfileStateLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                parentProfileStateLiveData.postValue(Resource.error("Something went wrong", null))
            }
        }
    }

    /* Parent profile update */
    fun parentProfileUpdate(
        email: String,
        std_code: Int,
        phone: Int,
        address: String,
        place: String,
        zip: Int,
        state: Int,
        country: Int,
        nationality: Int
    ) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                parentProfileUpdateLiveData.postValue(Resource.loading(null))
                repository.parentProfileUpdate(
                    email,
                    std_code,
                    phone,
                    address,
                    place,
                    zip,
                    state,
                    country,
                    nationality
                ).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK_NEW) {
                        parentProfileUpdateLiveData.postValue(Resource.success(response))
                    } else {
                        parentProfileUpdateLiveData.postValue(
                            Resource.error(
                                it.message(),
                                response
                            )
                        )
                    }

                }
            } catch (ex: UnknownHostException) {
                parentProfileUpdateLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                parentProfileUpdateLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                parentProfileUpdateLiveData.postValue(Resource.error("Something went wrong", null))
            }
        }
    }
}