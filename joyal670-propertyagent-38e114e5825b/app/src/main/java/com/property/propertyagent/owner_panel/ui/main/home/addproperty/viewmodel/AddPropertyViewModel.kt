package com.property.propertyagent.owner_panel.ui.main.home.addproperty.viewmodel

import android.accounts.NetworkErrorException
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyagent.data.ApiRepositoryProvider
import com.property.propertyagent.modal.owner.owner_addproperty.OwnerAddPropertyResponse
import com.property.propertyagent.modal.owner.owner_amenities.new.AmenitiesNewResponse
import com.property.propertyagent.modal.owner.owner_city.CityResponce
import com.property.propertyagent.modal.owner.owner_country.CountryResponse
import com.property.propertyagent.modal.owner.owner_propertytype_list.OwnerPropertyTypeResponse
import com.property.propertyagent.modal.owner.owner_states.StatesResponce
import com.property.propertyagent.modal.owner.owner_zipcode.ZipcodeResponse
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

class AddPropertyViewModel(private val context : Context) : ViewModel() {
    private val typeListLiveData = MutableLiveData<Resource<OwnerPropertyTypeResponse>>()
    private val cityLiveData = MutableLiveData<Resource<CityResponce>>()
    private val statesLiveData = MutableLiveData<Resource<StatesResponce>>()
    private val countryLiveData = MutableLiveData<Resource<CountryResponse>>()
    private val zipcodeLiveData = MutableLiveData<Resource<ZipcodeResponse>>()
    private val amenitiesNewLiveData = MutableLiveData<Resource<AmenitiesNewResponse>>()
    private val addBuilderPropertyLiveData = MutableLiveData<Resource<OwnerAddPropertyResponse>>()
    private val addApartmentOwnerPropertyLiveData =
        MutableLiveData<Resource<OwnerAddPropertyResponse>>()

    val typeListData : LiveData<Resource<OwnerPropertyTypeResponse>>
        get() = typeListLiveData

    val cityData : LiveData<Resource<CityResponce>>
        get() = cityLiveData

    val statesData : LiveData<Resource<StatesResponce>>
        get() = statesLiveData

    val countryData : LiveData<Resource<CountryResponse>>
        get() = countryLiveData

    val zipcodeData : LiveData<Resource<ZipcodeResponse>>
        get() = zipcodeLiveData

    val amenitiesNewData : LiveData<Resource<AmenitiesNewResponse>>
        get() = amenitiesNewLiveData

    val addBuilderPropertyData : LiveData<Resource<OwnerAddPropertyResponse>>
        get() = addBuilderPropertyLiveData

    val addApartmentOwnerPropertyData : LiveData<Resource<OwnerAddPropertyResponse>>
        get() = addApartmentOwnerPropertyLiveData


    /*  for property types */
    fun getPropertyTypes(type : Int) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                typeListLiveData.postValue(Resource.loading(null))
                repository.getPropertyTypes(type).let {
                    if (it.isSuccessful) {
                        typeListLiveData.postValue(Resource.success(it.body()))
                    } else {
                        typeListLiveData.postValue(Resource.error(it.message() , it.body()))
                    }
                }
            } catch (ex : UnknownHostException) {
                typeListLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection" ,
                        null
                    )
                )

            } catch (ex : NetworkErrorException) {
                typeListLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection" ,
                        null
                    )
                )

            } catch (ex : Exception) {
                typeListLiveData.postValue(Resource.error("Something went wrong" , null))
            }
        }
    }

    /* for city list */
    fun cityList(state : Int) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                cityLiveData.postValue(Resource.loading(null))
                repository.getCity(state).let {
                    if (it.isSuccessful) {
                        cityLiveData.postValue(Resource.success(it.body()))
                    } else {
                        cityLiveData.postValue(Resource.error(it.message() , it.body()))
                    }
                }
            } catch (ex : UnknownHostException) {
                cityLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection" ,
                        null
                    )
                )

            } catch (ex : NetworkErrorException) {
                cityLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection" ,
                        null
                    )
                )

            } catch (ex : Exception) {
                cityLiveData.postValue(Resource.error("Something went wrong" , null))
            }
        }
    }

    /* for states list */
    fun statesList(country: Int) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                statesLiveData.postValue(Resource.loading(null))
                repository.getStates(country).let {
                    if (it.isSuccessful) {
                        statesLiveData.postValue(Resource.success(it.body()))
                    } else {
                        statesLiveData.postValue(Resource.error(it.message() , it.body()))
                    }
                }
            } catch (ex : UnknownHostException) {
                statesLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection" ,
                        null
                    )
                )

            } catch (ex : NetworkErrorException) {
                statesLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection" ,
                        null
                    )
                )

            } catch (ex : Exception) {
                statesLiveData.postValue(Resource.error("Something went wrong" , null))
            }
        }
    }

    /* for country list */
    fun countryList() {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                countryLiveData.postValue(Resource.loading(null))
                repository.getCountry().let {
                    if (it.isSuccessful) {
                        countryLiveData.postValue(Resource.success(it.body()))
                    } else {
                        countryLiveData.postValue(Resource.error(it.message() , it.body()))
                    }
                }
            } catch (ex : UnknownHostException) {
                countryLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection" ,
                        null
                    )
                )

            } catch (ex : NetworkErrorException) {
                countryLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection" ,
                        null
                    )
                )

            } catch (ex : Exception) {
                countryLiveData.postValue(Resource.error("Something went wrong" , null))
            }
        }
    }

    /* for zip code list */
    fun zipcodeList(city : Int) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                zipcodeLiveData.postValue(Resource.loading(null))
                repository.getZipcode(city).let {
                    if (it.isSuccessful) {
                        zipcodeLiveData.postValue(Resource.success(it.body()))
                    } else {
                        zipcodeLiveData.postValue(Resource.error(it.message() , it.body()))
                    }
                }
            } catch (ex : UnknownHostException) {
                zipcodeLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection" ,
                        null
                    )
                )

            } catch (ex : NetworkErrorException) {
                zipcodeLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection" ,
                        null
                    )
                )

            } catch (ex : Exception) {
                zipcodeLiveData.postValue(Resource.error("Something went wrong" , null))
            }
        }
    }

    /* for amenities list */
    fun amenitiesNewList(type : Int) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                amenitiesNewLiveData.postValue(Resource.loading(null))
                repository.getNewAmenities(type).let {
                    if (it.isSuccessful) {
                        amenitiesNewLiveData.postValue(Resource.success(it.body()))
                    } else {
                        amenitiesNewLiveData.postValue(Resource.error(it.message() , it.body()))
                    }
                }
            } catch (ex : UnknownHostException) {
                amenitiesNewLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection" ,
                        null
                    )
                )

            } catch (ex : NetworkErrorException) {
                amenitiesNewLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection" ,
                        null
                    )
                )

            } catch (ex : Exception) {
                amenitiesNewLiveData.postValue(Resource.error("Something went wrong" , null))
            }
        }
    }

    /* for Builder add property */
    fun addBuilderPropertyList(
        propertyMainType : Int ,
        propertyRentBuyType : Int ,
        propertyResidentialCommercialType : Int ,
        propertyName : String ,
        propertyStreetAddress1 : String ,
        propertyStreetAddress2 : String ,
        propertyTypeID : Int ,
        propertyCountryId : Int ,
        propertyStateId : Int ,
        propertyCityId : Int ,
        propertyZipCodeId : Int ,
        propertyImage : ArrayList<String?> ,
        passLat : Double ,
        passLng : Double ,
    ) {

        val repository = ApiRepositoryProvider.providerApiRepository()
        addBuilderPropertyLiveData.postValue(Resource.loading(null))
        viewModelScope.launch {
            /* Request  data */
            val params = mapOf(
                "is_builder" to propertyMainType.toString()
                    .toRequestBody("multipart/form-data".toMediaTypeOrNull()) ,
                "property_name" to propertyName.toString()
                    .toRequestBody("multipart/form-data".toMediaTypeOrNull()) ,
                "property_to" to propertyRentBuyType.toString()
                    .toRequestBody("multipart/form-data".toMediaTypeOrNull()) ,
                "category" to propertyResidentialCommercialType.toString()
                    .toRequestBody("multipart/form-data".toMediaTypeOrNull()) ,
                "country" to propertyCountryId.toString()
                    .toRequestBody("multipart/form-data".toMediaTypeOrNull()) ,
                "state" to propertyStateId.toString()
                    .toRequestBody("multipart/form-data".toMediaTypeOrNull()) ,
                "city" to propertyCityId.toString()
                    .toRequestBody("multipart/form-data".toMediaTypeOrNull()) ,
                "zipcode" to propertyZipCodeId.toString()
                    .toRequestBody("multipart/form-data".toMediaTypeOrNull()) ,
                "address1" to propertyStreetAddress1.toRequestBody("multipart/form-data".toMediaTypeOrNull()) ,
                "address2" to propertyStreetAddress2.toRequestBody("multipart/form-data".toMediaTypeOrNull()) ,
                "type_id" to propertyTypeID.toString()
                    .toRequestBody("multipart/form-data".toMediaTypeOrNull()) ,
                "latitude" to passLat.toString()
                    .toRequestBody("multipart/form-data".toMediaTypeOrNull()) ,
                "longitude" to passLng.toString()
                    .toRequestBody("multipart/form-data".toMediaTypeOrNull())
            )

            try {
                /* Property images*/
                val imagesList : ArrayList<MultipartBody.Part> = ArrayList()
                if (propertyImage.isNotEmpty()) {
                    for (i in 0 until propertyImage.size) {
                        var imageProperty : MultipartBody.Part? = null
                        if (propertyImage[i]!!.isNotEmpty()) {
                            val file = File(propertyImage[i])
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
                                "building_images[$i]" ,
                                compressedFile.name ,
                                requestBody
                            )
                        }
                        imagesList.add(imageProperty!!)
                    }
                }

                repository.ownerBuilderAddProperty(params , imagesList)
                    .let {
                        if (it.isSuccessful) {
                            addBuilderPropertyLiveData.postValue(Resource.success(it.body()))
                        } else {
                            addBuilderPropertyLiveData.postValue(Resource.error(it.body()!!.response ,
                                null))
                        }
                    }
            } catch (ex : UnknownHostException) {
                addBuilderPropertyLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection" ,
                        null
                    )
                )

            } catch (ex : NetworkErrorException) {
                addBuilderPropertyLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection" ,
                        null
                    )
                )

            } catch (ex : Exception) {
                addBuilderPropertyLiveData.postValue(Resource.error("Something went wrong" , null))
            }
        }
    }

    /* for apartment owner add property */
    fun addApartmentOwnerAddProperty(
        propertyMainType : String ,
        propertyRentBuyType : String ,
        propertyResidentialCommercialType : String ,
        propertyName : String ,
        propertyStreetAddress1 : String ,
        propertyStreetAddress2 : String ,
        propertyTypeID : String ,
        propertyCountryId : String ,
        propertyStateId : String ,
        propertyCityId : String ,
        propertyZipCodeId : String ,
        propertyStatus : String ,
        images : ArrayList<String?>? ,
        floorImages : ArrayList<String?>? ,
        videosList : ArrayList<String> ,
        amenitiesList : ArrayList<Int> ,
        idList : ArrayList<Int> ,
        valueList : ArrayList<Int> ,
        furnishedId : Int ,
        propertyDescription : String ,
        propertyExpectedAmount : String ,
        propertyLng : String ,
        propertyLat : String ,
    ) {

        val repository = ApiRepositoryProvider.providerApiRepository()
        addApartmentOwnerPropertyLiveData.postValue(Resource.loading(null))
        viewModelScope.launch {
            /* Request  data */
            val params = mapOf(
                "is_builder" to propertyMainType.toRequestBody("multipart/form-data".toMediaTypeOrNull()) ,
                "property_name" to propertyName.toRequestBody("multipart/form-data".toMediaTypeOrNull()) ,
                "property_to" to propertyRentBuyType.toRequestBody("multipart/form-data".toMediaTypeOrNull()) ,
                "category" to propertyResidentialCommercialType.toRequestBody("multipart/form-data".toMediaTypeOrNull()) ,
                "country" to propertyCountryId.toRequestBody("multipart/form-data".toMediaTypeOrNull()) ,
                "state" to propertyStateId.toRequestBody("multipart/form-data".toMediaTypeOrNull()) ,
                "city" to propertyCityId.toRequestBody("multipart/form-data".toMediaTypeOrNull()) ,
                "zipcode" to propertyZipCodeId.toRequestBody("multipart/form-data".toMediaTypeOrNull()) ,
                "address1" to propertyStreetAddress1.toRequestBody("multipart/form-data".toMediaTypeOrNull()) ,
                "address2" to propertyStreetAddress2.toRequestBody("multipart/form-data".toMediaTypeOrNull()) ,
                "occupied" to propertyStatus.toRequestBody("multipart/form-data".toMediaTypeOrNull()) ,
                "description" to propertyDescription.toRequestBody("multipart/form-data".toMediaTypeOrNull()) ,
                "type_id" to propertyTypeID.toRequestBody("multipart/form-data".toMediaTypeOrNull()) ,
                "furnished" to furnishedId.toString()
                    .toRequestBody("multipart/form-data".toMediaTypeOrNull()) ,
                "expected_amount" to propertyExpectedAmount.toRequestBody("multipart/form-data".toMediaTypeOrNull()) ,
                "latitude" to propertyLat.toRequestBody("multipart/form-data".toMediaTypeOrNull()) ,
                "longitude" to propertyLng.toRequestBody("multipart/form-data".toMediaTypeOrNull())
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
                if (images!!.isNotEmpty()) {
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

                repository.ownerApartmentOwnerAddProperty(
                    params ,
                    imagesList ,
                    floorImageList ,
                    videoList ,
                    amenitiesList ,
                    idList ,
                    valueList
                )
                    .let {
                        if (it.isSuccessful) {
                            addApartmentOwnerPropertyLiveData.postValue(Resource.success(it.body()))
                        } else {
                            addApartmentOwnerPropertyLiveData.postValue(Resource.error(it.body()!!.response ,
                                null))
                        }
                    }
            } catch (ex : UnknownHostException) {
                addApartmentOwnerPropertyLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection" ,
                        null
                    )
                )

            } catch (ex : NetworkErrorException) {
                addApartmentOwnerPropertyLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection" ,
                        null
                    )
                )

            } catch (ex : Exception) {
                addApartmentOwnerPropertyLiveData.postValue(Resource.error("Something went wrong" ,
                    null))
            }
        }
    }

}