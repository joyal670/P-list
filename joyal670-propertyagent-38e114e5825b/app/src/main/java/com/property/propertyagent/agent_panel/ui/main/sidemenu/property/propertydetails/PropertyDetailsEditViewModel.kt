package com.property.propertyagent.agent_panel.ui.main.sidemenu.property.propertydetails

import android.accounts.NetworkErrorException
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.property.propertyagent.data.ApiRepositoryProvider
import com.property.propertyagent.modal.CommonResponse
import com.property.propertyagent.modal.agent.agent_assigned_property_details.AgentAssignedPropertyDetailsResponse
import com.property.propertyagent.modal.agent.agent_builder_details.AgentBuilderDetailsResponse
import com.property.propertyagent.modal.owner.owner_amenities.new.AmenitiesNewResponse
import com.property.propertyagent.modal.owner.owner_city.CityResponce
import com.property.propertyagent.modal.owner.owner_country.CountryResponse
import com.property.propertyagent.modal.owner.owner_propertytype_list.OwnerPropertyTypeResponse
import com.property.propertyagent.modal.owner.owner_states.StatesResponce
import com.property.propertyagent.modal.owner.owner_zipcode.ZipcodeResponse
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

class PropertyDetailsEditViewModel : ViewModel() {
    private val agentAssignedPropertyDetailsEditResponseLiveData =
        MutableLiveData<Resource<AgentAssignedPropertyDetailsResponse>>()

    private val agentApartmentDetailsResponseLiveData =
        MutableLiveData<Resource<AgentAssignedPropertyDetailsResponse>>()

    private val agentBuilderDetailsResponseLiveData =
        MutableLiveData<Resource<AgentBuilderDetailsResponse>>()

    private val agentAddPropertyLiveData = MutableLiveData<Resource<CommonResponse>>()
    val agentAddPropertyData: LiveData<Resource<CommonResponse>>
        get() = agentAddPropertyLiveData

    private val agentUpdateBuildingLiveData = MutableLiveData<Resource<CommonResponse>>()
    val agentUpdateBuildingData: LiveData<Resource<CommonResponse>>
        get() = agentUpdateBuildingLiveData

    private val agentUpdateApartmentLiveData = MutableLiveData<Resource<CommonResponse>>()
    val agentUpdateApartmentData: LiveData<Resource<CommonResponse>>
        get() = agentUpdateApartmentLiveData

    fun agentAddPropertyList(
        context: Context, property_id: String,
        property_to: Int,
        category: Int,
        country: Int,
        state: Int,
        city: Int,
        zipcode: Int,
        address1: String,
        address2: String,
        images: ArrayList<String>?,
        floor_plans: ArrayList<String>?,
        videos: ArrayList<String>,
        amenities: ArrayList<Int>,
        detailsKey: ArrayList<Int>,
        detailsValue: ArrayList<Int>,
        furnishedId: Int,
        occupiedStatus: String,
        pDesc: String,
        rent: String,
        latitude: String,
        longitude: String,
    ) {

        Log.e("On click", "Viewmodel:\n $detailsKey and \n $detailsValue")
        Log.e("amenities", amenities.toString())
        Log.e("detailsKey", detailsKey.toString())
        Log.e("detailsValue", detailsValue.toString())

        val repository = ApiRepositoryProvider.providerApiRepository()
        agentAddPropertyLiveData.postValue(Resource.loading(null))
        viewModelScope.launch {
            /* Request  data */
            val params = mapOf(
                "property_id" to property_id
                    .toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                "property_to" to property_to.toString()
                    .toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                "category" to category.toString()
                    .toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                "country" to country.toString()
                    .toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                "state" to state.toString()
                    .toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                "city" to city.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                "zipcode" to zipcode.toString()
                    .toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                "address1" to address1.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                "address2" to address2.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                "furnished" to furnishedId.toString()
                    .toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                "occupied" to occupiedStatus
                    .toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                "description" to pDesc
                    .toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                "expected_amount" to rent
                    .toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                "latitude" to latitude
                    .toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                "longitude" to longitude
                    .toRequestBody("multipart/form-data".toMediaTypeOrNull())
            )

            /* floor plan images*/
            try {
                val floorImageList: ArrayList<MultipartBody.Part> = ArrayList()
                if (floor_plans!!.isNotEmpty()) {
                    for (i in 0 until floor_plans.size) {
                        var floorProperty: MultipartBody.Part? = null
                        if (floor_plans[i].isNotEmpty()) {
                            val file = File(floor_plans[i])
                            var compressedFileFloor: File? = null
                            try {
                                compressedFileFloor =
                                    Compressor.compress(context, file, Dispatchers.IO)
                            } catch (e: EOFException) {
                                e.printStackTrace()
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }
                            val requestBody: RequestBody =
                                compressedFileFloor!!.asRequestBody("*/*".toMediaTypeOrNull())
                            floorProperty = MultipartBody.Part.createFormData(
                                "floor_plans[$i]",
                                compressedFileFloor.name,
                                requestBody
                            )
                        }
                        floorImageList.add(floorProperty!!)
                    }
                    floorImageList.forEach { image ->
                        Log.e("123456", "addPropertyList: $image")
                    }
                }
                /* Property images*/
                val imagesList: ArrayList<MultipartBody.Part> = ArrayList()
                if (images!!.isNotEmpty()) {
                    for (i in 0 until images.size) {
                        var imageProperty: MultipartBody.Part? = null
                        if (images[i].isNotEmpty()) {
                            val file = File(images[i])
                            var compressedFile: File? = null
                            try {
                                compressedFile =
                                    Compressor.compress(context, file, Dispatchers.IO)
                            } catch (e: EOFException) {
                                e.printStackTrace()
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }
                            val requestBody: RequestBody =
                                compressedFile!!.asRequestBody("*/*".toMediaTypeOrNull())
                            imageProperty = MultipartBody.Part.createFormData(
                                "images[$i]",
                                compressedFile.name,
                                requestBody
                            )
                        }
                        imagesList.add(imageProperty!!)
                    }
                }

                /* video list */
                val videoList: ArrayList<MultipartBody.Part> = ArrayList()
                if (videos.isNotEmpty()) {
                    for (i in 0 until videos.size) {
                        var videoProperty: MultipartBody.Part? = null
                        if (videos[i].isNotEmpty()) {
                            val file = File(videos[i])
                            val requestBody: RequestBody =
                                file.asRequestBody("*/*".toMediaTypeOrNull())
                            videoProperty = MultipartBody.Part.createFormData(
                                "videos[$i]",
                                file.name,
                                requestBody
                            )
                        }
                        videoList.add(videoProperty!!)
                    }
                }

                repository.agentAddProperty(
                    params,
                    imagesList,
                    floorImageList,
                    videoList,
                    amenities,
                    detailsKey,
                    detailsValue
                )
                    .let {
                        if (it.isSuccessful) {
                            agentAddPropertyLiveData.postValue(Resource.success(it.body()))
                        } else {
                            agentAddPropertyLiveData.postValue(Resource.error(it.message(), null))
                        }
                    }
            } catch (ex: UnknownHostException) {
                agentAddPropertyLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                agentAddPropertyLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                agentAddPropertyLiveData.postValue(Resource.error("Something went wrong", null))
            }
        }
    }

    fun agentUpdateApartment(
        context: Context,
        unit_id: String,
        building_id: String,
        images: ArrayList<String>?,
        floor_plans: ArrayList<String>?,
        videos: ArrayList<String>,
        amenities: ArrayList<Int>,
        detailsKey: ArrayList<Int>,
        detailsValue: ArrayList<Int>,
        furnishedId: Int,
        occupiedStatus: String,
        pDesc: String,
        rent: String,
    ) {

        Log.e("On click", "Viewmodel:\n $detailsKey and \n $detailsValue")
        Log.e("amenities", amenities.toString())
        Log.e("detailsKey", detailsKey.toString())
        Log.e("detailsValue", detailsValue.toString())

        val repository = ApiRepositoryProvider.providerApiRepository()
        agentUpdateApartmentLiveData.postValue(Resource.loading(null))
        viewModelScope.launch {
            /* Request  data */
            val params = mapOf(
                "unit_id" to unit_id
                    .toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                "building_id" to building_id
                    .toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                "furnished" to furnishedId.toString()
                    .toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                "occupied" to occupiedStatus
                    .toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                "description" to pDesc
                    .toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                "rent" to rent
                    .toRequestBody("multipart/form-data".toMediaTypeOrNull()),
            )

            /* floor plan images*/
            try {
                val floorImageList: ArrayList<MultipartBody.Part> = ArrayList()
                if (floor_plans!!.isNotEmpty()) {
                    for (i in 0 until floor_plans.size) {
                        var floorProperty: MultipartBody.Part? = null
                        if (floor_plans[i].isNotEmpty()) {
                            val file = File(floor_plans[i])
                            var compressedFileFloor: File? = null
                            try {
                                compressedFileFloor =
                                    Compressor.compress(context, file, Dispatchers.IO)
                            } catch (e: EOFException) {
                                e.printStackTrace()
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }
                            val requestBody: RequestBody =
                                compressedFileFloor!!.asRequestBody("*/*".toMediaTypeOrNull())
                            floorProperty = MultipartBody.Part.createFormData(
                                "floor_plans[$i]",
                                compressedFileFloor.name,
                                requestBody
                            )
                        }
                        floorImageList.add(floorProperty!!)
                    }
                    floorImageList.forEach { image ->
                        Log.e("123456", "addPropertyList: $image")
                    }
                }
                /* Property images*/
                val imagesList: ArrayList<MultipartBody.Part> = ArrayList()
                if (images!!.isNotEmpty()) {
                    for (i in 0 until images.size) {
                        var imageProperty: MultipartBody.Part? = null
                        if (images[i].isNotEmpty()) {
                            val file = File(images[i])
                            var compressedFile: File? = null
                            try {
                                compressedFile =
                                    Compressor.compress(context, file, Dispatchers.IO)
                            } catch (e: EOFException) {
                                e.printStackTrace()
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }
                            val requestBody: RequestBody =
                                compressedFile!!.asRequestBody("*/*".toMediaTypeOrNull())
                            imageProperty = MultipartBody.Part.createFormData(
                                "images[$i]",
                                compressedFile.name,
                                requestBody
                            )
                        }
                        imagesList.add(imageProperty!!)
                    }
                }

                /* video list */
                val videoList: ArrayList<MultipartBody.Part> = ArrayList()
                if (videos.isNotEmpty()) {
                    for (i in 0 until videos.size) {
                        var videoProperty: MultipartBody.Part? = null
                        if (videos[i].isNotEmpty()) {
                            val file = File(videos[i])
                            val requestBody: RequestBody =
                                file.asRequestBody("*/*".toMediaTypeOrNull())
                            videoProperty = MultipartBody.Part.createFormData(
                                "videos[$i]",
                                file.name,
                                requestBody
                            )
                        }
                        videoList.add(videoProperty!!)
                    }
                }

                repository.agentUpdateApartment(
                    params,
                    imagesList,
                    floorImageList,
                    videoList,
                    amenities,
                    detailsKey,
                    detailsValue
                )
                    .let {
                        if (it.isSuccessful) {
                            agentUpdateApartmentLiveData.postValue(Resource.success(it.body()))
                        } else {
                            agentUpdateApartmentLiveData.postValue(
                                Resource.error(
                                    it.message(),
                                    null
                                )
                            )
                        }
                    }
            } catch (ex: UnknownHostException) {
                agentUpdateApartmentLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                agentUpdateApartmentLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                agentUpdateApartmentLiveData.postValue(Resource.error("Something went wrong", null))
            }
        }
    }


    fun fetchAgentAssignedPropertyDetailsEdit(property_id: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            agentAssignedPropertyDetailsEditResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.agentAssignedPropertyDetails(property_id).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        agentAssignedPropertyDetailsEditResponseLiveData.postValue(
                            Resource.success(
                                response
                            )
                        )
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        agentAssignedPropertyDetailsEditResponseLiveData.postValue(
                            Resource.error(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        agentAssignedPropertyDetailsEditResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        agentAssignedPropertyDetailsEditResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                }

            } catch (ex: Exception) {
                agentAssignedPropertyDetailsEditResponseLiveData.postValue(
                    Resource.noInternet(
                        "",
                        null
                    )
                )
            }
        }
    }

    fun getAgentAssignedPropertyDetailsEditResponse(): LiveData<Resource<AgentAssignedPropertyDetailsResponse>> {
        return agentAssignedPropertyDetailsEditResponseLiveData
    }

    private val agentRemovePropertyDocumentResponseLiveData =
        MutableLiveData<Resource<CommonResponse>>()

    fun agentRemovePropertyDocument(property_id: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            agentRemovePropertyDocumentResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.agentRemovePropertyDocument(property_id).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        agentRemovePropertyDocumentResponseLiveData.postValue(
                            Resource.success(
                                response
                            )
                        )
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        agentRemovePropertyDocumentResponseLiveData.postValue(
                            Resource.error(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        agentRemovePropertyDocumentResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        agentRemovePropertyDocumentResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                }

            } catch (ex: Exception) {
                agentRemovePropertyDocumentResponseLiveData.postValue(
                    Resource.noInternet(
                        "",
                        null
                    )
                )
            }
        }
    }

    fun getAgentPropertyRemoveResponse(): LiveData<Resource<CommonResponse>> {
        return agentRemovePropertyDocumentResponseLiveData
    }

    private val agentRemovePropertyFloorDocumentResponseLiveData =
        MutableLiveData<Resource<CommonResponse>>()

    fun agentRemovePropertyFloorDocument(property_id: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            agentRemovePropertyFloorDocumentResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.agentRemovePropertyDocument(property_id).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        agentRemovePropertyFloorDocumentResponseLiveData.postValue(
                            Resource.success(
                                response
                            )
                        )
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        agentRemovePropertyFloorDocumentResponseLiveData.postValue(
                            Resource.error(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        agentRemovePropertyFloorDocumentResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        agentRemovePropertyFloorDocumentResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                }
            } catch (ex: Exception) {
                agentRemovePropertyFloorDocumentResponseLiveData.postValue(
                    Resource.noInternet(
                        "",
                        null
                    )
                )
            }
        }
    }

    fun getAgentPropertyFloorRemoveResponse(): LiveData<Resource<CommonResponse>> {
        return agentRemovePropertyFloorDocumentResponseLiveData
    }

    private val typeListLiveDataAgent = MutableLiveData<Resource<OwnerPropertyTypeResponse>>()
    private val cityLiveDataAgent = MutableLiveData<Resource<CityResponce>>()
    private val statesLiveDataAgent = MutableLiveData<Resource<StatesResponce>>()
    private val countryLiveDataAgent = MutableLiveData<Resource<CountryResponse>>()
    private val zipcodeLiveDataAgent = MutableLiveData<Resource<ZipcodeResponse>>()
    private val amenitiesNewLiveDataAgent = MutableLiveData<Resource<AmenitiesNewResponse>>()

    val typeListData: LiveData<Resource<OwnerPropertyTypeResponse>>
        get() = typeListLiveDataAgent

    val countryData: LiveData<Resource<CountryResponse>>
        get() = countryLiveDataAgent

    val amenitiesNewData: LiveData<Resource<AmenitiesNewResponse>>
        get() = amenitiesNewLiveDataAgent

    /*  for property types */
    fun getPropertyTypes(type: Int) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {

                typeListLiveDataAgent.postValue(Resource.loading(null))
                repository.getPropertyTypes(type).let {
                    if (it.isSuccessful) {
                        typeListLiveDataAgent.postValue(Resource.success(it.body()))
                    } else {
                        typeListLiveDataAgent.postValue(Resource.error(it.message(), it.body()))
                    }
                }
            } catch (ex: UnknownHostException) {
                typeListLiveDataAgent.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )
            } catch (ex: NetworkErrorException) {
                typeListLiveDataAgent.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )
            } catch (ex: Exception) {
                typeListLiveDataAgent.postValue(Resource.error("Something went wrong", null))
            }
        }
    }

    /* for city list */
    fun cityList(state: Int) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                cityLiveDataAgent.postValue(Resource.loading(null))
                repository.getCity(state).let {
                    if (it.isSuccessful) {
                        cityLiveDataAgent.postValue(Resource.success(it.body()))
                    } else {
                        cityLiveDataAgent.postValue(Resource.error(it.message(), it.body()))
                    }
                }
            } catch (ex: UnknownHostException) {
                cityLiveDataAgent.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )
            } catch (ex: NetworkErrorException) {
                cityLiveDataAgent.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )
            } catch (ex: Exception) {
                cityLiveDataAgent.postValue(Resource.error("Something went wrong", null))
            }
        }
    }

    /* for states list */
    fun statesList() {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                statesLiveDataAgent.postValue(Resource.loading(null))
                repository.getStates().let {
                    if (it.isSuccessful) {
                        statesLiveDataAgent.postValue(Resource.success(it.body()))
                    } else {
                        statesLiveDataAgent.postValue(Resource.error(it.message(), it.body()))
                    }
                }
            } catch (ex: UnknownHostException) {
                statesLiveDataAgent.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )
            } catch (ex: NetworkErrorException) {
                statesLiveDataAgent.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )
            } catch (ex: Exception) {
                statesLiveDataAgent.postValue(Resource.error("Something went wrong", null))
            }
        }
    }

    /* for country list */
    fun countryList() {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                countryLiveDataAgent.postValue(Resource.loading(null))
                repository.getCountry().let {
                    if (it.isSuccessful) {
                        countryLiveDataAgent.postValue(Resource.success(it.body()))
                    } else {
                        countryLiveDataAgent.postValue(Resource.error(it.message(), it.body()))
                    }
                }
            } catch (ex: UnknownHostException) {
                countryLiveDataAgent.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )
            } catch (ex: NetworkErrorException) {
                countryLiveDataAgent.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )
            } catch (ex: Exception) {
                countryLiveDataAgent.postValue(Resource.error("Something went wrong", null))
            }
        }
    }

    /* for zip code list */
    fun zipcodeList(city: Int) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                zipcodeLiveDataAgent.postValue(Resource.loading(null))
                repository.getZipcode(city).let {
                    if (it.isSuccessful) {
                        zipcodeLiveDataAgent.postValue(Resource.success(it.body()))
                    } else {
                        zipcodeLiveDataAgent.postValue(Resource.error(it.message(), it.body()))
                    }
                }
            } catch (ex: UnknownHostException) {
                zipcodeLiveDataAgent.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )
            } catch (ex: NetworkErrorException) {
                zipcodeLiveDataAgent.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )
            } catch (ex: Exception) {
                zipcodeLiveDataAgent.postValue(Resource.error("Something went wrong", null))
            }
        }
    }

    /* for amenities list */
    fun amenitiesNewList(type: Int) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            try {
                amenitiesNewLiveDataAgent.postValue(Resource.loading(null))
                repository.getNewAmenities(type).let {
                    if (it.isSuccessful) {
                        amenitiesNewLiveDataAgent.postValue(Resource.success(it.body()))
                    } else {
                        amenitiesNewLiveDataAgent.postValue(Resource.error(it.message(), it.body()))
                    }
                }
            } catch (ex: UnknownHostException) {
                amenitiesNewLiveDataAgent.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )
            } catch (ex: NetworkErrorException) {
                amenitiesNewLiveDataAgent.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )
            } catch (ex: Exception) {
                amenitiesNewLiveDataAgent.postValue(Resource.error("Something went wrong", null))
            }
        }
    }

    fun fetchAgentBuilderDetails(property_id: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            agentBuilderDetailsResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.agentBuilderDetails(property_id).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        agentBuilderDetailsResponseLiveData.postValue(
                            Resource.success(
                                response
                            )
                        )
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        agentBuilderDetailsResponseLiveData.postValue(
                            Resource.error(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        agentBuilderDetailsResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        agentBuilderDetailsResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                }
            } catch (ex: Exception) {
                agentBuilderDetailsResponseLiveData.postValue(
                    Resource.noInternet(
                        "",
                        null
                    )
                )
            }
        }
    }

    fun getAgentBuilderDetailsResponse(): LiveData<Resource<AgentBuilderDetailsResponse>> {
        return agentBuilderDetailsResponseLiveData
    }

    fun agentUpdateBuilding(
        context: Context, property_id: String,
        property_to: Int,
        category: Int,
        country: Int,
        state: Int,
        city: Int,
        type_id: Int,
        zipcode: Int,
        address1: String,
        address2: String,
        images: ArrayList<String>?,
        latitude: String,
        longitude: String,
    ) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        agentUpdateBuildingLiveData.postValue(Resource.loading(null))
        viewModelScope.launch {
            /* Request  data */
            val params = mapOf(
                "building_id" to property_id
                    .toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                "property_to" to property_to.toString()
                    .toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                "category" to category.toString()
                    .toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                "country" to country.toString()
                    .toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                "state" to state.toString()
                    .toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                "city" to city.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                "zipcode" to zipcode.toString()
                    .toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                "type_id" to type_id.toString()
                    .toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                "address1" to address1.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                "address2" to address2.toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                "latitude" to latitude
                    .toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                "longitude" to longitude
                    .toRequestBody("multipart/form-data".toMediaTypeOrNull())
            )

            try {
                /* Property images*/
                val imagesList: ArrayList<MultipartBody.Part> = ArrayList()
                if (images!!.isNotEmpty()) {
                    for (i in 0 until images.size) {
                        var imageProperty: MultipartBody.Part? = null
                        if (images[i].isNotEmpty()) {
                            val file = File(images[i])
                            var compressedFile: File? = null
                            try {
                                compressedFile =
                                    Compressor.compress(context, file, Dispatchers.IO)
                            } catch (e: EOFException) {
                                e.printStackTrace()
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }
                            val requestBody: RequestBody =
                                compressedFile!!.asRequestBody("*/*".toMediaTypeOrNull())
                            imageProperty = MultipartBody.Part.createFormData(
                                "images[$i]",
                                compressedFile.name,
                                requestBody
                            )
                        }
                        imagesList.add(imageProperty!!)
                    }
                }
                repository.agentUpdateBuilding(
                    params,
                    imagesList
                )
                    .let {
                        if (it.isSuccessful) {
                            agentUpdateBuildingLiveData.postValue(Resource.success(it.body()))
                        } else {
                            agentUpdateBuildingLiveData.postValue(
                                Resource.error(
                                    it.message(),
                                    null
                                )
                            )
                        }
                    }
            } catch (ex: UnknownHostException) {
                agentUpdateBuildingLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: NetworkErrorException) {
                agentUpdateBuildingLiveData.postValue(
                    Resource.noInternet(
                        "Please check your internet connection",
                        null
                    )
                )

            } catch (ex: Exception) {
                agentUpdateBuildingLiveData.postValue(Resource.error("Something went wrong", null))
            }
        }
    }

    fun fetchAgentApartmentDetails(property_id: String, building_id: String) {
        val repository = ApiRepositoryProvider.providerApiRepository()
        viewModelScope.launch {
            agentApartmentDetailsResponseLiveData.postValue(Resource.loading(null))
            try {
                repository.agentApartmentDetails(property_id, building_id).let {
                    val response = it.body()
                    if (response!!.status == Constants.REQUEST_OK) {
                        agentApartmentDetailsResponseLiveData.postValue(
                            Resource.success(
                                response
                            )
                        )
                    }
                    if (response.status == Constants.INTERNAL_SERVER_ERROR) {
                        agentApartmentDetailsResponseLiveData.postValue(
                            Resource.error(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_BAD_REQUEST) {
                        agentApartmentDetailsResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                    if (response.status == Constants.REQUEST_UNAUTHORIZED) {
                        agentApartmentDetailsResponseLiveData.postValue(
                            Resource.dataEmpty(
                                "null",
                                response
                            )
                        )
                    }
                }
            } catch (ex: Exception) {
                agentApartmentDetailsResponseLiveData.postValue(
                    Resource.noInternet(
                        "",
                        null
                    )
                )
            }
        }
    }

    fun getAgentApartmentDetailsResponse(): LiveData<Resource<AgentAssignedPropertyDetailsResponse>> {
        return agentApartmentDetailsResponseLiveData
    }
}