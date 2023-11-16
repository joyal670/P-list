package com.property.propertyagent.owner_panel.ui.main.home.property.building_details_list.viewmodel

import android.accounts.NetworkErrorException
import android.app.Activity
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyagent.R
import com.property.propertyagent.data.ApiRepositoryProvider
import com.property.propertyagent.modal.owner.owner_add_apartment.OwnerAddApartmentResponse
import com.property.propertyagent.modal.owner.owner_amenities.new.AmenitiesNewResponse
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
import java.net.UnknownHostException

class OwnerAddBuildingViewModel(private var context : Activity) :
    ViewModel() {

    private val ownerPropertyAmenitiesResponseLiveData =
        MutableLiveData<Resource<AmenitiesNewResponse>>()
    private val addApartmentLiveData =
        MutableLiveData<Resource<OwnerAddApartmentResponse>>()

    fun amenitiesList(type : Int) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            ownerPropertyAmenitiesResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.getNewAmenities(type).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        ownerPropertyAmenitiesResponseLiveData.postValue(Resource.success(response))
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        ownerPropertyAmenitiesResponseLiveData.postValue(Resource.error(context.getString(
                            R.string.internal_server_error) ,
                            response))
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        ownerPropertyAmenitiesResponseLiveData.postValue(Resource.error(context.getString(
                            R.string.bad_request) ,
                            response))
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        ownerPropertyAmenitiesResponseLiveData.postValue(Resource.error(context.getString(
                            R.string.unauthorized) ,
                            response))
                    }
                }

            } catch (ex : Exception) {
                ownerPropertyAmenitiesResponseLiveData.postValue(Resource.noInternet("" , null))
                Log.e("TAG" , "paymentListOfProperties: $ex")
            }
        }
    }

    fun getPropertyAmenitiesResponse() : LiveData<Resource<AmenitiesNewResponse>> {
        return ownerPropertyAmenitiesResponseLiveData
    }

    fun getAddApartmentResponse() : LiveData<Resource<OwnerAddApartmentResponse>> {
        return addApartmentLiveData
    }

    /* for add apartment */
    fun addApartmentOwnerAddProperty(
        propertyBuildingId : String ,
        propertyTypeId : String ,
        propertyName : String ,
        propertyStatus : String ,
        propertyAmenities : ArrayList<Int> ,
        propertyDetailsId : ArrayList<Int> ,
        propertyDetailsValue : ArrayList<Int> ,
        propertyFurnishedId : Int ,
        propertyDescription : String ,
        propertyExpectedAmount : String ,
        images : ArrayList<String?> ,
        floorImages : ArrayList<String?>? ,
        videosList : ArrayList<String> ,
    ) {

        val repository = ApiRepositoryProvider.providerApiRepository()
        addApartmentLiveData.postValue(Resource.loading(null))
        viewModelScope.launch {
            /* Request  data */
            val params = mapOf(
                "building_id" to propertyBuildingId.toRequestBody("multipart/form-data".toMediaTypeOrNull()) ,
                "description" to propertyDescription.toRequestBody("multipart/form-data".toMediaTypeOrNull()) ,
                "furnished" to propertyFurnishedId.toString()
                    .toRequestBody("multipart/form-data".toMediaTypeOrNull()) ,
                "expected_amount" to propertyExpectedAmount.toRequestBody("multipart/form-data".toMediaTypeOrNull()) ,
                "type_id" to propertyTypeId.toRequestBody("multipart/form-data".toMediaTypeOrNull()) ,
                "occupied" to propertyStatus.toRequestBody("multipart/form-data".toMediaTypeOrNull()) ,
                "property_name" to propertyName.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            )

            /* floor plan images*/
            try {
                val floorImageList : ArrayList<MultipartBody.Part> = ArrayList()
                if (floorImages!!.isNotEmpty()) {
                    for (i in 0 until floorImages.size) {
                        var floorProperty : MultipartBody.Part? = null
                        if (floorImages[i]!!.isNotEmpty()) {
                            val file = File(floorImages[i])
                            var compressedFileFloor : File? = null
                            try {
                                compressedFileFloor =
                                    Compressor.compress(context , file , Dispatchers.IO)
                            } catch (e : EOFException) {
                                e.printStackTrace()
                            } catch (e : IOException) {
                                e.printStackTrace()
                            }
                            val requestBody : RequestBody =
                                compressedFileFloor!!.asRequestBody("*/*".toMediaTypeOrNull())
                            floorProperty = MultipartBody.Part.createFormData(
                                "floor_plans[$i]" ,
                                compressedFileFloor.name ,
                                requestBody
                            )
                        }
                        floorImageList.add(floorProperty!!)
                    }
                    floorImageList.forEach { image ->
                        Log.e("123456" , "addPropertyList: $image")
                    }
                }

                /* Property images*/
                val imagesList : ArrayList<MultipartBody.Part> = ArrayList()
                if (images.isNotEmpty()) {
                    for (i in 0 until images.size) {
                        var imageProperty : MultipartBody.Part? = null
                        if (images[i]!!.isNotEmpty()) {
                            val file = File(images[i])
                            var compressedFile : File? = null
                            try {
                                compressedFile =
                                    Compressor.compress(context , file , Dispatchers.IO)
                            } catch (e : EOFException) {
                                e.printStackTrace()
                            } catch (e : IOException) {
                                e.printStackTrace()
                            }
                            val requestBody : RequestBody =
                                compressedFile!!.asRequestBody("*/*".toMediaTypeOrNull())
                            imageProperty = MultipartBody.Part.createFormData(
                                "images[$i]" ,
                                compressedFile.name ,
                                requestBody
                            )
                        }
                        imagesList.add(imageProperty!!)
                    }
                }

                /* video list */
                val videoList : ArrayList<MultipartBody.Part> = ArrayList()
                if (videosList.isNotEmpty()) {
                    for (i in 0 until videosList.size) {
                        var videoProperty : MultipartBody.Part? = null
                        if (videosList[i].isNotEmpty()) {
                            val file = File(videosList[i])
                            val requestBody : RequestBody =
                                file.asRequestBody("*/*".toMediaTypeOrNull())
                            videoProperty = MultipartBody.Part.createFormData(
                                "videos[$i]" ,
                                file.name ,
                                requestBody
                            )
                        }
                        videoList.add(videoProperty!!)
                    }
                }

                repository.ownerAddApartment(
                    params ,
                    imagesList ,
                    floorImageList ,
                    videoList ,
                    propertyAmenities ,
                    propertyDetailsId ,
                    propertyDetailsValue
                )
                    .let {
                        if (it.isSuccessful) {
                            addApartmentLiveData.postValue(Resource.success(it.body()))
                        } else {
                            addApartmentLiveData.postValue(Resource.error(it.body()!!.response ,
                                null))
                        }
                    }
            } catch (ex : UnknownHostException) {
                addApartmentLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection" ,
                        null
                    )
                )

            } catch (ex : NetworkErrorException) {
                addApartmentLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection" ,
                        null
                    )
                )

            } catch (ex : Exception) {
                addApartmentLiveData.postValue(Resource.error("Something went wrong" ,
                    null))
            }
        }
    }
}