package com.ncomfortsagent.ui.main.sideMenu.bankDetails.viewmodel

import android.accounts.NetworkErrorException
import android.annotation.SuppressLint
import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ncomfortsagent.R
import com.ncomfortsagent.data.ApiRepositoryProvider
import com.ncomfortsagent.model.add_bank_details.AgentAddBankDetailsResponse
import com.ncomfortsagent.model.edit_bank_details.AgentEditBankDetailsResponse
import com.ncomfortsagent.model.remove_bank_details.AgentRemoveBankDetailsResponse
import com.ncomfortsagent.model.view_bank_details.AgentViewBankDetailsResponse
import com.ncomfortsagent.utils.Constants
import com.ncomfortsagent.utils.Resource
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class BankViewModel(@SuppressLint("StaticFieldLeak") private var activity: Activity) :
    ViewModel() {

    private val addBankDetailsLiveData = MutableLiveData<Resource<AgentAddBankDetailsResponse>>()
    private val viewBankDetailsLiveData = MutableLiveData<Resource<AgentViewBankDetailsResponse>>()
    private val editBankDetailsLiveData = MutableLiveData<Resource<AgentEditBankDetailsResponse>>()
    private val removeBankDetailsLiveData =
        MutableLiveData<Resource<AgentRemoveBankDetailsResponse>>()

    fun getAgentAddBankDetailsResponse(): LiveData<Resource<AgentAddBankDetailsResponse>> {
        return addBankDetailsLiveData
    }

    fun addBankDetails(
        account_name: String,
        account_number: String,
        bank_name: String,
        branch_name: String,
        ifsc: String
    ) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            addBankDetailsLiveData.postValue(Resource.loading(null))
            try {
                repository.addBankAccount(
                    account_name,
                    account_number,
                    bank_name,
                    branch_name,
                    ifsc
                ).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        addBankDetailsLiveData.postValue(Resource.success(response))
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        addBankDetailsLiveData.postValue(
                            Resource.error(
                                activity.getString(R.string.bad_request),
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        addBankDetailsLiveData.postValue(
                            Resource.error(
                                activity.getString(R.string.unauthorized),
                                response
                            )
                        )
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        addBankDetailsLiveData.postValue(Resource.dataEmpty("null", response))
                    }
                }
            } catch (ex: UnknownHostException) {
                addBankDetailsLiveData.postValue(
                    Resource.noInternet(
                        activity.getString(R.string.please_check_your_internet_connection),
                        null
                    )
                )
            } catch (ex: NetworkErrorException) {
                addBankDetailsLiveData.postValue(
                    Resource.noInternet(
                        activity.getString(R.string.please_check_your_internet_connection),
                        null
                    )
                )
            } catch (ex: Exception) {
                addBankDetailsLiveData.postValue(
                    Resource.error(
                        activity.getString(R.string.some_thing_went_wrong),
                        null
                    )
                )
            }
        }
    }


    fun getAgentViewBankDetailsResponse(): LiveData<Resource<AgentViewBankDetailsResponse>> {
        return viewBankDetailsLiveData
    }

    fun viewBankDetails(page: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            viewBankDetailsLiveData.postValue(Resource.loading(null))
            try {
                repository.viewBankDetails(page).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        viewBankDetailsLiveData.postValue(Resource.success(response))
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        viewBankDetailsLiveData.postValue(
                            Resource.error(
                                activity.getString(R.string.bad_request),
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        viewBankDetailsLiveData.postValue(
                            Resource.error(
                                activity.getString(R.string.unauthorized),
                                response
                            )
                        )
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        viewBankDetailsLiveData.postValue(Resource.dataEmpty("null", response))
                    }
                }
            } catch (ex: UnknownHostException) {
                viewBankDetailsLiveData.postValue(
                    Resource.noInternet(
                        activity.getString(R.string.please_check_your_internet_connection),
                        null
                    )
                )
            } catch (ex: NetworkErrorException) {
                viewBankDetailsLiveData.postValue(
                    Resource.noInternet(
                        activity.getString(R.string.please_check_your_internet_connection),
                        null
                    )
                )
            } catch (ex: Exception) {
                viewBankDetailsLiveData.postValue(
                    Resource.error(
                        activity.getString(R.string.some_thing_went_wrong),
                        null
                    )
                )
            }
        }
    }


    fun getAgentEditBankDetailsResponse(): LiveData<Resource<AgentEditBankDetailsResponse>> {
        return editBankDetailsLiveData
    }

    fun editBankDetails(
        account_name: String,
        account_number: String,
        bank_name: String,
        branch_name: String,
        ifsc: String,
        table_id: String
    ) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            editBankDetailsLiveData.postValue(Resource.loading(null))
            try {
                repository.editBankDetails(
                    account_name,
                    account_number,
                    bank_name,
                    branch_name,
                    ifsc,
                    table_id
                ).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        editBankDetailsLiveData.postValue(Resource.success(response))
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        editBankDetailsLiveData.postValue(
                            Resource.error(
                                activity.getString(R.string.bad_request),
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        editBankDetailsLiveData.postValue(
                            Resource.error(
                                activity.getString(R.string.unauthorized),
                                response
                            )
                        )
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        editBankDetailsLiveData.postValue(Resource.dataEmpty("null", response))
                    }
                }
            } catch (ex: UnknownHostException) {
                editBankDetailsLiveData.postValue(
                    Resource.noInternet(
                        activity.getString(R.string.please_check_your_internet_connection),
                        null
                    )
                )
            } catch (ex: NetworkErrorException) {
                editBankDetailsLiveData.postValue(
                    Resource.noInternet(
                        activity.getString(R.string.please_check_your_internet_connection),
                        null
                    )
                )
            } catch (ex: Exception) {
                editBankDetailsLiveData.postValue(
                    Resource.error(
                        activity.getString(R.string.some_thing_went_wrong),
                        null
                    )
                )
            }
        }
    }

    fun getAgentRemoveBankDetailsResponse(): LiveData<Resource<AgentRemoveBankDetailsResponse>> {
        return removeBankDetailsLiveData
    }

    fun removeBankDetails(table_id: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            removeBankDetailsLiveData.postValue(Resource.loading(null))
            try {
                repository.removeBankDetails(table_id).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        removeBankDetailsLiveData.postValue(Resource.success(response))
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        removeBankDetailsLiveData.postValue(
                            Resource.error(
                                activity.getString(R.string.bad_request),
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        removeBankDetailsLiveData.postValue(
                            Resource.error(
                                activity.getString(R.string.unauthorized),
                                response
                            )
                        )
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        removeBankDetailsLiveData.postValue(Resource.dataEmpty("null", response))
                    }
                }
            } catch (ex: UnknownHostException) {
                removeBankDetailsLiveData.postValue(
                    Resource.noInternet(
                        activity.getString(R.string.please_check_your_internet_connection),
                        null
                    )
                )
            } catch (ex: NetworkErrorException) {
                removeBankDetailsLiveData.postValue(
                    Resource.noInternet(
                        activity.getString(R.string.please_check_your_internet_connection),
                        null
                    )
                )
            } catch (ex: Exception) {
                removeBankDetailsLiveData.postValue(
                    Resource.error(
                        activity.getString(R.string.some_thing_went_wrong),
                        null
                    )
                )
            }
        }
    }

}