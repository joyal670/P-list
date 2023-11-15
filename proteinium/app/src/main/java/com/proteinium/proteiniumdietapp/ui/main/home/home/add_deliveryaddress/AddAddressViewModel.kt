package com.proteinium.proteiniumdietapp.ui.main.home.home.add_deliveryaddress

import android.accounts.NetworkErrorException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proteinium.proteiniumdietapp.R
import com.proteinium.proteiniumdietapp.data.api.ApiRepositoryProvider
import com.proteinium.proteiniumdietapp.pojo.CommonResponse
import com.proteinium.proteiniumdietapp.pojo.areas.AreaResponse
import com.proteinium.proteiniumdietapp.pojo.blocks.BlockResponse
import com.proteinium.proteiniumdietapp.pojo.edit_address.EditSingleAddressResponse
import com.proteinium.proteiniumdietapp.pojo.governorate.GovernorateResponse
import com.proteinium.proteiniumdietapp.utils.Resource
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class AddAddressViewModel:ViewModel() {
    private val governoratesResponseMutableLiveData = MutableLiveData<Resource<GovernorateResponse>>()
    private val areaResponseMutableLiveData = MutableLiveData<Resource<AreaResponse>>()
    private val blockResponseMutableLiveData = MutableLiveData<Resource<BlockResponse>>()
    private val commonResponseMutableLiveData = MutableLiveData<Resource<CommonResponse>>()
    private val commonResponseMutableLiveDataUpdate = MutableLiveData<Resource<CommonResponse>>()
    private val editSingleAddressResponseMutableLiveData = MutableLiveData<Resource<EditSingleAddressResponse>>()


    fun getGovernorates() {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            governoratesResponseMutableLiveData.postValue(Resource.loading(null))
            try {
                repository.getGovernorates().let {
                    val response = it.body()
                    if (response?.status!!) {
                        governoratesResponseMutableLiveData.postValue(Resource.success(response))
                    } else {
                        governoratesResponseMutableLiveData.postValue(Resource.error(response.message, response))
                    }
                }
            }
            catch (ex: NetworkErrorException) {
                governoratesResponseMutableLiveData.postValue(Resource.noInternet(R.string.no_internet.toString(),null))

            }
            catch (ex: UnknownHostException) {
                governoratesResponseMutableLiveData.postValue(Resource.noInternet(R.string.no_internet.toString(),null))

            }
            catch (ex:Exception){
                governoratesResponseMutableLiveData.postValue(Resource.error(R.string.something_wrong.toString(),null))
            }

        }
    }
    fun getAreas(governorate_id:String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            areaResponseMutableLiveData.postValue(Resource.loading(null))
            try {
                repository.getAreas(governorate_id).let {
                    val response = it.body()
                    if (response?.status!!) {
                        areaResponseMutableLiveData.postValue(Resource.success(response))
                    } else {
                        areaResponseMutableLiveData.postValue(Resource.error(response.message, response))
                    }
                }
            }
            catch (ex: NetworkErrorException) {
                areaResponseMutableLiveData.postValue(Resource.noInternet(R.string.no_internet.toString(),null))

            }
            catch (ex: UnknownHostException) {
                areaResponseMutableLiveData.postValue(Resource.noInternet(R.string.no_internet.toString(),null))

            }
            catch (ex:Exception){
                areaResponseMutableLiveData.postValue(Resource.error(R.string.something_wrong.toString(),null))
            }

        }
    }
    fun getBlocks(area_id:String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            blockResponseMutableLiveData.postValue(Resource.loading(null))
            try {
                repository.getBlocks(area_id).let {
                    val response = it.body()
                    if (response?.status!!) {
                        blockResponseMutableLiveData.postValue(Resource.success(response))
                    } else {
                        blockResponseMutableLiveData.postValue(Resource.error(response.message, response))
                    }
                }
            }
            catch (ex: NetworkErrorException) {
                blockResponseMutableLiveData.postValue(Resource.noInternet(R.string.no_internet.toString(),null))

            }
            catch (ex: UnknownHostException) {
                blockResponseMutableLiveData.postValue(Resource.noInternet(R.string.no_internet.toString(),null))

            }
            catch (ex:Exception){
                blockResponseMutableLiveData.postValue(Resource.error(R.string.something_wrong.toString(),null))
            }

        }
    }
    fun addAddress(user_id:Int,governorate_id:Int, area_id:Int,
                   block:String,avenue:String,
                   street:String,building:String,floor:String,appartment:String,default:Int,
                   additional_direction:String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            commonResponseMutableLiveData.postValue(Resource.loading(null))
            try {
                repository.addAddress(user_id,governorate_id,area_id,block,avenue,street,building,floor,appartment,default,
                    additional_direction).let {
                    val response = it.body()
                    if (response?.status!!) {
                        commonResponseMutableLiveData.postValue(Resource.success(response))
                    } else {
                        commonResponseMutableLiveData.postValue(Resource.error(response.message, response))
                    }
                }
            }
            catch (ex: NetworkErrorException) {
                commonResponseMutableLiveData.postValue(Resource.noInternet(R.string.no_internet.toString(),null))

            }
            catch (ex: UnknownHostException) {
                commonResponseMutableLiveData.postValue(Resource.noInternet(R.string.no_internet.toString(),null))

            }
            catch (ex:Exception){
                commonResponseMutableLiveData.postValue(Resource.error(R.string.something_wrong.toString(),null))
            }

        }
    }
    fun getGovernoratesResponse(): LiveData<Resource<GovernorateResponse>> {
        return governoratesResponseMutableLiveData
    }
    fun getAreaResponse(): LiveData<Resource<AreaResponse>> {
        return areaResponseMutableLiveData
    }
    fun getBlockResponse(): LiveData<Resource<BlockResponse>> {
        return blockResponseMutableLiveData
    }
    fun getAddAddressResponse(): LiveData<Resource<CommonResponse>> {
        return commonResponseMutableLiveData
    }

    fun getEditSingleAddress(address_id:Int) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            editSingleAddressResponseMutableLiveData.postValue(Resource.loading(null))
            try {
                repository.getSingleAddressEdit(address_id).let {
                    val response = it.body()
                    if (response?.status!!) {
                        editSingleAddressResponseMutableLiveData.postValue(Resource.success(response))
                    } else {
                        editSingleAddressResponseMutableLiveData.postValue(Resource.error(response.message, response))
                    }
                }
            }
            catch (ex: NetworkErrorException) {
                editSingleAddressResponseMutableLiveData.postValue(Resource.noInternet(R.string.no_internet.toString(),null))

            }
            catch (ex: UnknownHostException) {
                editSingleAddressResponseMutableLiveData.postValue(Resource.noInternet(R.string.no_internet.toString(),null))

            }
            catch (ex:Exception){
                editSingleAddressResponseMutableLiveData.postValue(Resource.error(R.string.something_wrong.toString(),null))
            }

        }
    }
    fun getEditSingleAddressResponse(): LiveData<Resource<EditSingleAddressResponse>> {
        return editSingleAddressResponseMutableLiveData
    }
    fun updateAddress(id:Int,user_id:Int,governorate_id:Int, area_id:Int,
                   block: String,avenue:String,
                   street:String,building:String,floor:String,appartment:String,default:Int,
                      additional_direction: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            commonResponseMutableLiveDataUpdate.postValue(Resource.loading(null))
            try {
                repository.updateAddress(id,user_id,governorate_id,area_id,block,avenue,street,building,floor,appartment,default,
                    additional_direction).let {
                    val response = it.body()
                    if (response?.status!!) {
                        commonResponseMutableLiveDataUpdate.postValue(Resource.success(response))
                    } else {
                        commonResponseMutableLiveDataUpdate.postValue(Resource.error(response.message, response))
                    }
                }
            }
            catch (ex: NetworkErrorException) {
                commonResponseMutableLiveDataUpdate.postValue(Resource.noInternet(R.string.no_internet.toString(),null))

            }
            catch (ex: UnknownHostException) {
                commonResponseMutableLiveDataUpdate.postValue(Resource.noInternet(R.string.no_internet.toString(),null))

            }
            catch (ex:Exception){
                commonResponseMutableLiveDataUpdate.postValue(Resource.error(R.string.something_wrong.toString(),null))
            }

        }
    }
    fun getUpdateAddressResponse(): LiveData<Resource<CommonResponse>> {
        return commonResponseMutableLiveDataUpdate
    }

}
