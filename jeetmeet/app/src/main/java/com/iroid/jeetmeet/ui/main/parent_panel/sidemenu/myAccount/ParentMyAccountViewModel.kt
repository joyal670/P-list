package com.iroid.jeetmeet.ui.main.parent_panel.sidemenu.myAccount

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iroid.jeetmeet.data.ApiRepositoryProvider
import com.iroid.jeetmeet.modal.parent.my_account.ParentMyAccount
import com.iroid.jeetmeet.modal.parent.my_account_debit_from_advance.ParentMyaccountDebitFromAdvanceResponse
import com.iroid.jeetmeet.modal.parent.my_account_details.ParentMyAccountDetailsResponse
import com.iroid.jeetmeet.modal.parent.my_account_partial_pay.ParentMyAccountPartialPayResponse
import com.iroid.jeetmeet.modal.parent.my_account_payment.ParentMyAccountPaymentResponse
import com.iroid.jeetmeet.modal.parent.students_list.ParentStudentsListResponse
import com.iroid.jeetmeet.utils.Constants
import com.iroid.jeetmeet.utils.Resource
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.net.UnknownHostException

class ParentMyAccountViewModel : ViewModel() {
    /* variables */
    private val parentStudentsListLiveData = MutableLiveData<Resource<ParentStudentsListResponse>>()

    val parentStudentsListData: LiveData<Resource<ParentStudentsListResponse>>
        get() = parentStudentsListLiveData

    private val parentMyAccountLiveData = MutableLiveData<Resource<ParentMyAccount>>()

    val parentMyAccountData: LiveData<Resource<ParentMyAccount>>
        get() = parentMyAccountLiveData

    private val parentMyAccountDetailsLiveData =
        MutableLiveData<Resource<ParentMyAccountDetailsResponse>>()

    val parentMyAccountDetailsData: LiveData<Resource<ParentMyAccountDetailsResponse>>
        get() = parentMyAccountDetailsLiveData

    private val parentMyAccountPayPartiallyLiveData =
        MutableLiveData<Resource<ParentMyAccountPartialPayResponse>>()

    val parentMyAccountPayPartiallyData: LiveData<Resource<ParentMyAccountPartialPayResponse>>
        get() = parentMyAccountPayPartiallyLiveData

    private val parentMyAccountDebitFromAdvanceLiveData =
        MutableLiveData<Resource<ParentMyaccountDebitFromAdvanceResponse>>()

    val parentMyAccountDebitFromAdvanceData: LiveData<Resource<ParentMyaccountDebitFromAdvanceResponse>>
        get() = parentMyAccountDebitFromAdvanceLiveData

    private val parentMyAccountPaymentLiveData =
        MutableLiveData<Resource<ParentMyAccountPaymentResponse>>()

    val parentMyAccountPaymentData: LiveData<Resource<ParentMyAccountPaymentResponse>>
        get() = parentMyAccountPaymentLiveData

    /* Students list */
    fun parentStudentsList() {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                parentStudentsListLiveData.postValue(Resource.loading(null))
                repository.parentStudentsList().let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK_NEW) {
                        parentStudentsListLiveData.postValue(Resource.success(response))
                    } else {
                        parentStudentsListLiveData.postValue(Resource.error(it.message(), response))
                    }

                }
            } catch (ex: UnknownHostException) {
                parentStudentsListLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                parentStudentsListLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                parentStudentsListLiveData.postValue(Resource.error("Something went wrong", null))
            }
        }
    }

    /* My Account */
    fun parentMyAccount(student_id: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                parentMyAccountLiveData.postValue(Resource.loading(null))
                repository.parentMyAccount(student_id).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK_NEW) {
                        parentMyAccountLiveData.postValue(Resource.success(response))
                    } else {
                        parentMyAccountLiveData.postValue(Resource.error(it.message(), response))
                    }

                }
            } catch (ex: UnknownHostException) {
                parentMyAccountLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                parentMyAccountLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                parentMyAccountLiveData.postValue(Resource.error("Something went wrong", null))
            }
        }
    }

    /* account details */
    fun parentMyAccountDetails(student_code: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                parentMyAccountDetailsLiveData.postValue(Resource.loading(null))
                repository.parentMyAccountDetails(student_code).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK_NEW) {
                        parentMyAccountDetailsLiveData.postValue(Resource.success(response))
                    } else {
                        parentMyAccountDetailsLiveData.postValue(
                            Resource.error(
                                response.data,
                                response
                            )
                        )
                    }

                }
            } catch (ex: UnknownHostException) {
                parentMyAccountDetailsLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                parentMyAccountDetailsLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                parentMyAccountDetailsLiveData.postValue(
                    Resource.error(
                        "Something went wrong",
                        null
                    )
                )
            }
        }
    }

    /* My Account Pay Partially */
    fun parentMyAccountPayPartially(
        student_code: String,
        total: String,
        advance: String,
        advance_balance: String,
        posting_id: ArrayList<Int>
    ) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                parentMyAccountPayPartiallyLiveData.postValue(Resource.loading(null))

                val params = mapOf(
                    "student_code" to student_code.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                    "total" to total.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                    "advance" to advance.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                    "advance_balance" to advance_balance.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                )

                repository.parentMyAccountPayPartially(params, posting_id).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK_NEW) {
                        parentMyAccountPayPartiallyLiveData.postValue(Resource.success(response))
                    } else {
                        parentMyAccountPayPartiallyLiveData.postValue(
                            Resource.error(
                                response.data,
                                response
                            )
                        )
                    }

                }
            } catch (ex: UnknownHostException) {
                parentMyAccountPayPartiallyLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                parentMyAccountPayPartiallyLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                parentMyAccountPayPartiallyLiveData.postValue(
                    Resource.error(
                        "Something went wrong",
                        null
                    )
                )
            }
        }
    }

    /* My Account Debit from Advance */
    fun parentMyAccountDebitFromAdvance(
        student_code: String,
        total: String,
        advance: String,
        advance_balance: String,
        posting_id: ArrayList<Int>
    ) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                parentMyAccountDebitFromAdvanceLiveData.postValue(Resource.loading(null))

                val params = mapOf(
                    "student_code" to student_code.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                    "total" to total.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                    "advance" to advance.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                    "advance_balance" to advance_balance.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                )

                repository.parentMyAccountDebitFromAdvance(params, posting_id).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK_NEW) {
                        parentMyAccountDebitFromAdvanceLiveData.postValue(Resource.success(response))
                    } else {
                        parentMyAccountDebitFromAdvanceLiveData.postValue(
                            Resource.error(
                                response.data,
                                response
                            )
                        )
                    }

                }
            } catch (ex: UnknownHostException) {
                parentMyAccountDebitFromAdvanceLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                parentMyAccountDebitFromAdvanceLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                parentMyAccountDebitFromAdvanceLiveData.postValue(
                    Resource.error(
                        "Something went wrong",
                        null
                    )
                )
            }
        }
    }

    /* My Account Debit from Advance */
    fun parentMyAccountPayment(
        student_code: String,
        total: String,
        advance: String,
        advance_balance: String,
        transaction_id: String,
        posting_id: ArrayList<Int>
    ) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                parentMyAccountPaymentLiveData.postValue(Resource.loading(null))

                val params = mapOf(
                    "student_code" to student_code.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                    "total" to total.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                    "advance" to advance.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                    "advance_balance" to advance_balance.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                    "transaction_id" to transaction_id.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                )

                repository.parentMyAccountPayment(params, posting_id).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK_NEW) {
                        parentMyAccountPaymentLiveData.postValue(Resource.success(response))
                    } else {
                        parentMyAccountPaymentLiveData.postValue(
                            Resource.error(
                                response.data,
                                response
                            )
                        )
                    }

                }
            } catch (ex: UnknownHostException) {
                parentMyAccountPaymentLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                parentMyAccountPaymentLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                parentMyAccountPaymentLiveData.postValue(
                    Resource.error(
                        "Something went wrong",
                        null
                    )
                )
            }
        }
    }
}