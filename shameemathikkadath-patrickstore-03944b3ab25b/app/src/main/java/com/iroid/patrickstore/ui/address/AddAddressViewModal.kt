package com.iroid.patrickstore.ui.address

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iroid.patrickstore.api.ApiRepositoryProvider
import com.iroid.patrickstore.model.add_address.AddAddressResponse
import com.iroid.patrickstore.model.addresslist.AddressListResponse
import com.iroid.patrickstore.model.delete_address.DeleteAddressResponse
import com.iroid.patrickstore.model.edit_address.EditAddressResponse
import com.iroid.patrickstore.model.single_address.SingleAddressResponse
import com.iroid.patrickstore.utils.*
import kotlinx.coroutines.launch

class AddAddressViewModal : ViewModel() {
    private val liveDataAddress = MutableLiveData<Resource<AddAddressResponse>>()
    val addAddressLiveData: LiveData<Resource<AddAddressResponse>> get() = liveDataAddress

    private val liveDataEditAddress = MutableLiveData<Resource<EditAddressResponse>>()
    val editAddressLiveData: LiveData<Resource<EditAddressResponse>> get() = liveDataEditAddress

    private val liveAddressList = MutableLiveData<Resource<AddressListResponse>>()
    val addressListLiveData: LiveData<Resource<AddressListResponse>> get() = liveAddressList

    private val liveSingleAddress = MutableLiveData<Resource<SingleAddressResponse>>()
    val singleAddressLiveData: LiveData<Resource<SingleAddressResponse>> get() = liveSingleAddress

    private val liveDeleteAddress = MutableLiveData<Resource<DeleteAddressResponse>>()
    val deleteAddressLiveData: LiveData<Resource<DeleteAddressResponse>> get() = liveDeleteAddress

    var name: MutableLiveData<String>? = null
    var address1: MutableLiveData<String>? = null
    var city: MutableLiveData<String>? = null
    var state: MutableLiveData<String>? = null
    var country: MutableLiveData<String>? = null
    var pincode: MutableLiveData<String>? = null
    var landMark: MutableLiveData<String>? = null
    var label: MutableLiveData<String>? = null
    var contactNumber: MutableLiveData<String>? = null

    var empty_name: MutableLiveData<String>? = null
    var empty_address1: MutableLiveData<String>? = null
    var empty_city: MutableLiveData<String>? = null
    var empty_state: MutableLiveData<String>? = null
    var empty_country: MutableLiveData<String>? = null
    var empty_pincode: MutableLiveData<String>? = null
    var empty_landMark: MutableLiveData<String>? = null
    var empty_label: MutableLiveData<String>? = null
    var empty_contactNumber: MutableLiveData<String>? = null

    init {
        name = MutableLiveData()
        address1 = MutableLiveData()
        city = MutableLiveData()
        state = MutableLiveData()
        country = MutableLiveData()
        pincode = MutableLiveData()
        landMark = MutableLiveData()
        label = MutableLiveData()
        contactNumber = MutableLiveData()

        empty_name = MutableLiveData()
        empty_address1 = MutableLiveData()
        empty_city = MutableLiveData()
        empty_state = MutableLiveData()
        empty_country = MutableLiveData()
        empty_pincode = MutableLiveData()
        empty_landMark = MutableLiveData()
        empty_contactNumber = MutableLiveData()
        empty_label = MutableLiveData()
    }

    fun onNameTextChanged(text: CharSequence) {
        if (TextUtils.isEmpty(text.toString())) {
            empty_name?.value = FirstNameError
        } else {
            name?.value = text.toString()
            empty_name?.value = null
        }
    }

    fun onContactNumber(text: CharSequence) {
        if (TextUtils.isEmpty(text.toString())) {
            empty_contactNumber?.value = AddressError
        } else {
            contactNumber?.value = text.toString()
            empty_contactNumber?.value = null
        }
    }

    fun onAddressTextChanged(text: CharSequence) {
        if (TextUtils.isEmpty(text.toString())) {
            empty_address1?.value = AddressError
        } else {
            address1?.value = text.toString()
            empty_address1?.value = null
        }
    }

    fun onCityTextChanged(text: CharSequence) {
        if (TextUtils.isEmpty(text.toString())) {
            empty_city?.value = AddressError
        } else {
            city?.value = text.toString()
            empty_city?.value = null
        }
    }

    fun stateTextChanged(text: CharSequence) {
        if (TextUtils.isEmpty(text.toString())) {
            empty_state?.value = STATE_ERROR
        } else {
            state?.value = text.toString()
            empty_state?.value = null
        }
    }

    fun landmarkChanged(text: CharSequence) {
        if (TextUtils.isEmpty(text.toString())) {
            empty_landMark?.value = LANDMARK_ERROR
        } else {
            landMark?.value = text.toString()
            empty_landMark?.value = null
        }
    }

    fun pinCodeTextChanged(text: CharSequence) {
        if (TextUtils.isEmpty(text.toString())) {
            empty_pincode?.value = PinCodeInvalid
        } else {
            pincode?.value = text.toString()
            empty_pincode?.value = null
        }
    }

    fun addressTypeChanged(text: CharSequence) {
        if (TextUtils.isEmpty(text.toString())) {
            empty_label?.value = AddressError
        } else {
            label?.value = text.toString()
            empty_label?.value = null
        }
    }

    fun addAddress() {
        if (isValid()) {
            val repository = ApiRepositoryProvider.providerApiRepository()
            viewModelScope.launch {
                try {
                    liveDataAddress.postValue(Resource.loading(null))
                    repository.addAddress(
                        name?.value.toString(),
                        address1?.value.toString(),
                        city?.value.toString(),
                        state?.value.toString(),
                        //country?.value.toString(),
                        "India",
                        pincode?.value.toString(),
                        landMark?.value.toString(),
                        label?.value.toString(),
                        "9.931233",
                        "76.267303",
                        contactNumber?.value.toString()
                    ).let {
                        val response = it.body()
                        when (response!!.statusCode) {
                            Constants.REQUEST_OK -> {
                                liveDataAddress.postValue(Resource.success(response))
                            }
                            Constants.REQUEST_CREATED -> {
                                liveDataAddress.postValue(Resource.noInterNet("", null))
                            }
                            Constants.REQUEST_BAD_REQUEST -> {
                                liveDataAddress.postValue(Resource.error(response.msg, null))
                            }
                            Constants.REQUEST_UNAUTHORIZED -> {
                                liveDataAddress.postValue(Resource.noInterNet("", null))
                            }
                        }
                    }
                } catch (ex: Exception) {
                    liveDataAddress.postValue(Resource.noInterNet("", null))
                }
            }
        }
    }

    fun getAddressList() {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                liveAddressList.postValue(Resource.loading(null))
                repository.getAddressList().let {
                    val response = it.body()
                    when (response!!.statusCode) {
                        Constants.REQUEST_OK -> {
                            liveAddressList.postValue(Resource.success(response))
                        }
                        Constants.REQUEST_CREATED -> {
                            liveAddressList.postValue(Resource.noInterNet("", null))
                        }
                        Constants.REQUEST_BAD_REQUEST -> {
                            liveAddressList.postValue(Resource.error(response.msg, null))
                        }
                        Constants.REQUEST_UNAUTHORIZED -> {
                            liveAddressList.postValue(Resource.noInterNet("", null))
                        }
                    }
                }
            } catch (ex: Exception) {
                liveAddressList.postValue(Resource.noInterNet("", null))
            }
        }
    }

    fun getSingleAddress(id: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                liveSingleAddress.postValue(Resource.loading(null))
                repository.getSingleAddress(id).let {
                    val response = it.body()
                    when (response!!.statusCode) {
                        Constants.REQUEST_OK -> {
                            liveSingleAddress.postValue(Resource.success(response))
                        }
                        Constants.REQUEST_CREATED -> {
                            liveSingleAddress.postValue(Resource.noInterNet("", null))
                        }
                        Constants.REQUEST_BAD_REQUEST -> {
                            liveSingleAddress.postValue(Resource.error(response.msg, null))
                        }
                        Constants.REQUEST_UNAUTHORIZED -> {
                            liveSingleAddress.postValue(Resource.noInterNet("", null))
                        }
                    }
                }
            } catch (ex: Exception) {
                liveSingleAddress.postValue(Resource.noInterNet("", null))
            }
        }
    }

    fun deleteAddress(id: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                liveDeleteAddress.postValue(Resource.loading(null))
                repository.deleteAddress(id).let {
                    val response = it.body()
                    when (response!!.statusCode) {
                        Constants.REQUEST_OK -> {
                            liveDeleteAddress.postValue(Resource.success(response))
                        }
                        Constants.REQUEST_CREATED -> {
                            liveDeleteAddress.postValue(Resource.noInterNet("", null))
                        }
                        Constants.REQUEST_BAD_REQUEST -> {
                            liveDeleteAddress.postValue(Resource.error(response.msg, null))
                        }
                        Constants.REQUEST_UNAUTHORIZED -> {
                            liveDeleteAddress.postValue(Resource.noInterNet("", null))
                        }
                    }
                }
            } catch (ex: Exception) {
                liveDeleteAddress.postValue(Resource.noInterNet("", null))
            }
        }
    }

    fun editAddress(addressId: String) {
        if (isValid()) {
            val repository = ApiRepositoryProvider.providerApiRepository()
            viewModelScope.launch {
                try {
                    liveDataEditAddress.postValue(Resource.loading(null))
                    repository.editAddress(
                        addressId,
                        name?.value.toString(),
                        address1?.value.toString(),
                        city?.value.toString(),
                        state?.value.toString(),
                        "India",
                        pincode?.value.toString(),
                        landMark?.value.toString(),
                        label?.value.toString(),
                        "9.931233",
                        "76.267303",
                        contactNumber?.value.toString()
                    ).let {
                        val response = it.body()
                        when (response!!.statusCode) {
                            Constants.REQUEST_OK -> {
                                liveDataEditAddress.postValue(Resource.success(response))
                            }
                            Constants.REQUEST_CREATED -> {
                                liveDataEditAddress.postValue(Resource.noInterNet("", null))
                            }
                            Constants.REQUEST_BAD_REQUEST -> {
                                liveDataEditAddress.postValue(Resource.error(response.msg, null))
                            }
                            Constants.REQUEST_UNAUTHORIZED -> {
                                liveDataEditAddress.postValue(Resource.noInterNet("", null))
                            }
                        }
                    }
                } catch (ex: Exception) {
                    liveDataEditAddress.postValue(Resource.noInterNet("", null))
                }
            }
        }
    }

    private fun isValid(): Boolean {
        return when {
            TextUtils.isEmpty(name?.value) -> {
                empty_name?.value = NAME_ERROR
                false
            }
            TextUtils.isEmpty(address1?.value) -> {
                empty_address1?.value = AddressError
                false
            }
            TextUtils.isEmpty(city?.value) -> {
                empty_city?.value = CityError
                false
            }
            TextUtils.isEmpty(state?.value) -> {
                empty_state?.value = StateError
                false
            }

            TextUtils.isEmpty(pincode?.value) -> {
                empty_pincode?.value = PIN_CODE_ERROR
                false
            }
            TextUtils.isEmpty(landMark?.value) -> {
                empty_landMark?.value = LandMarkError
                false
            }
            TextUtils.isEmpty(label?.value) -> {
                empty_label?.value = AddressTypeError
                false
            }
            TextUtils.isEmpty(contactNumber?.value) -> {
                empty_contactNumber?.value = CONTACT_ERROR
                false
            }
            else -> {
                true
            }
        }
    }
}