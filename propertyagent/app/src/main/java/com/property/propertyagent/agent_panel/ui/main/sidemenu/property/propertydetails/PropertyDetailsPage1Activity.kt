package com.property.propertyagent.agent_panel.ui.main.sidemenu.property.propertydetails

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.allShouldShowRationale
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.extension.send
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import com.property.propertyagent.R
import com.property.propertyagent.agent_panel.ui.main.home.home.HomeActivity
import com.property.propertyagent.agent_panel.ui.main.sidemenu.property.propertydetails.adapter.AgentBuilderImagesFromApi
import com.property.propertyagent.agent_panel.ui.main.sidemenu.property.propertydetails.adapter.AgentImageUploadAdapter
import com.property.propertyagent.agent_panel.ui.main.sidemenu.property.propertydetails.adapter.BuildingApartmentAdapter
import com.property.propertyagent.agent_panel.ui.main.sidemenu.property.propertydetails.edit_apartment.EditApartment1Activity
import com.property.propertyagent.base.BaseActivity
import com.property.propertyagent.dialogs.InternetDialogFragmentCopy
import com.property.propertyagent.modal.agent.agent_assigned_property_details.AmenityCategory
import com.property.propertyagent.modal.agent.agent_assigned_property_details.Document
import com.property.propertyagent.modal.agent.agent_assigned_property_details.FloorPlan
import com.property.propertyagent.modal.agent.agent_assigned_property_details.PropertyDetail
import com.property.propertyagent.modal.agent.agent_builder_details.Unit
import com.property.propertyagent.utils.*
import com.property.propertyagent.utils.AppPreferences.clicked_property_id
import com.property.propertyagent.utils.AppPreferences.isConnectionRestored
import com.property.propertyagent.utils.AppPreferences.is_builder
import kotlinx.android.synthetic.main.activity_property_details_page1.*
import kotlinx.android.synthetic.main.toolbar_main.*

@Suppress("PrivatePropertyName")
class PropertyDetailsPage1Activity : BaseActivity() {
    private lateinit var propertyDetailsEditViewModel: PropertyDetailsEditViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var detailsList: ArrayList<PropertyDetail>? = null
    private lateinit var buildingApartmentAdapter: BuildingApartmentAdapter
    private lateinit var passedAmenityCategory: ArrayList<AmenityCategory>

    private lateinit var imageAdapter: AgentImageUploadAdapter
    private lateinit var imageAdapterFromApi: AgentBuilderImagesFromApi

    private val PICK_IMAGES_CODE = 0
    private var deleteImagePosition = -1
    private var position = 0

    var categoryList = ArrayList<String>()
    var statesList = ArrayList<String>()
    var cityList = ArrayList<String>()
    var countryList = ArrayList<String>()
    var zipcodeList = ArrayList<String>()

    var categoryListId = ArrayList<Int>()
    var cityId = ArrayList<Int>()
    var statesId = ArrayList<Int>()
    var countryId = ArrayList<Int>()
    var zipcodeId = ArrayList<Int>()

    private var defaultTypesId = 0
    private var selectedTypesId = -1
    private var selectedStateId = 0
    private var selectedCityId = 0
    private var selectedCountryId = 0
    private var selectedZipCodeId = 0
    private var selectedCountryName: String = ""
    private var selectedStateName: String = ""
    private var selectedCityName: String = ""
    private var selectedZipCode: String = ""

    private var selectedPropertyTo = ""
    private var selectedPropertyCatagorey = 0
    private var spinnerType = ""

    private var imagesList: ArrayList<Uri?>? = null
    private var images: ArrayList<String>? = null

    private var lat: String = "0.0"
    private var lng: String = "0.0"
    private var furnishedTypeId: Int = -1
    private var passedRent: String = ""
    private var passedDescription: String = ""
    private var occupiedStatus: String = ""
    private var passedFeatureValue: Int = -1
    private var passedImages: ArrayList<Document>? = null
    private var builderImages: ArrayList<Document>? = null

    private var passedFloorPlan: ArrayList<FloorPlan>? = null

    private var isLoadingMain: Boolean = false

    override val layoutId: Int
        get() = R.layout.activity_property_details_page1
    override val setToolbar: Boolean
        get() = true
    override val hideStatusBar: Boolean
        get() = false

    override fun fragmentLaunch() {

    }

    override fun initData() {
        setSupportActionBar(toolbar)
        tvToolbarTitle.text = getString(R.string.property_details)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_green)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getLastKnownLocation()

        Log.i("TAG567", AppPreferences.token.toString())

        passedImages = ArrayList()
        builderImages = ArrayList()
        passedAmenityCategory = ArrayList()
        passedFloorPlan = ArrayList()
        imagesList = ArrayList()
        images = ArrayList()

        if (is_builder) {
            agent_property_nextBtn.isVisible = false
            layoutBuilderUpdates.isVisible = true
        }

        propertyDetailsEditViewModel.countryList()

    }

    private fun getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            methodWithPermissions()
            return
        }
         fusedLocationClient.lastLocation
             .addOnSuccessListener { location ->
                 if (location != null) {
                     Log.e("fusedLocationClient", Gson().toJson(location))
                     lat = location.latitude.toString()
                     lng = location.longitude.toString()
                 }
             }
    }

    private fun methodWithPermissions() {
        permissionsBuilder(Manifest.permission.ACCESS_FINE_LOCATION).build().send { result ->
            if (result.allGranted()) {
                permissionsBuilder(Manifest.permission.ACCESS_COARSE_LOCATION).build()
                    .send { result1 ->
                        if (result1.allGranted()) {
                            getLastKnownLocation()
                        }
                    }
            }
            if (result.allShouldShowRationale()) {
                Toaster.showToast(
                    this,
                    "Please allow permissions",
                    Toaster.State.WARNING,
                    Toast.LENGTH_SHORT
                )
            }
        }
    }

    override fun setupUI() {

    }

    override fun setupViewModel() {
        propertyDetailsEditViewModel = PropertyDetailsEditViewModel()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun setupObserver() {
        /* property types */
        propertyDetailsEditViewModel.countryData.observe(this) { it ->
            when (it.status) {
                Status.SUCCESS -> {
                    if (!isLoadingMain) {
                        dismissProgress()
                    }
                    countryList.clear()
                    countryId.clear()
                    it.data!!.response.forEach {
                        countryList.addAll(listOf(it.name))
                        countryId.addAll(listOf(it.id))
                    }
                    country_spinnerAgent.clearSelectedItem()
                    country_spinnerAgent.setItems(countryList)

                    if (is_builder) {
                        propertyDetailsEditViewModel.fetchAgentBuilderDetails(clicked_property_id)
                    } else {
                        propertyDetailsEditViewModel.fetchAgentAssignedPropertyDetailsEdit(
                            clicked_property_id
                        )
                    }
                }
                Status.LOADING -> {
                    if (!isLoadingMain) {
                        showProgress()
                        isLoadingMain = true
                    }
                }
                Status.ERROR -> {
                    dismissProgress()
                    Toaster.showToast(this, it.message!!, Toaster.State.ERROR, Toast.LENGTH_LONG)
                }
                Status.NO_INTERNET -> {
                    dismissProgress()
                    if (this.isConnected) {
                        Toaster.showToast(
                            this, getString(R.string.something_wrong),
                            Toaster.State.ERROR, Toast.LENGTH_LONG
                        )
                    } else {
                        val dialog = InternetDialogFragmentCopy(this)
                        dialog.show(supportFragmentManager, "TAG")
                    }
                }
                Status.DATA_EMPTY -> {
                    dismissProgress()
                    Toaster.showToast(this, it.message!!, Toaster.State.ERROR, Toast.LENGTH_LONG)
                }
            }
        }
        propertyDetailsEditViewModel.typeListData.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    if (isLoadingMain) {
                        dismissProgress()
                        isLoadingMain = false
                    }
                    categoryList.clear()
                    categoryListId.clear()
                    it.data?.response?.forEach { it2 ->
                        categoryList.addAll(listOf(it2.type))
                        categoryListId.addAll(listOf(it2.id))
                    }
                    agent_propertyType_spinner.clearSelectedItem()
                    agent_propertyType_spinner.setItems(categoryList)

                    for (c in 0 until categoryListId.size) {
                        Log.i("TAG234", "setupObserver: 0" + categoryListId[c].toString())
                        Log.i("TAG2345", "setupObserver: $defaultTypesId")
                        if (categoryListId[c] == defaultTypesId) {
                            agent_propertyType_spinner.selectItemByIndex(c)
                        }
                    }
                    agent_propertyType_spinner.isClickable = false
                }
                Status.LOADING -> {
                    if (!isLoadingMain) {
                        showProgress()
                        isLoadingMain = true
                    }
                }
                Status.ERROR -> {
                    dismissProgress()
                    Toaster.showToast(this, it.message!!, Toaster.State.ERROR, Toast.LENGTH_LONG)
                }
                Status.NO_INTERNET -> {
                    dismissProgress()
                    if (this.isConnected) {
                        Toaster.showToast(
                            this, getString(R.string.something_wrong),
                            Toaster.State.ERROR, Toast.LENGTH_LONG
                        )
                    } else {
                        val dialog = InternetDialogFragmentCopy(this)
                        dialog.show(supportFragmentManager, "TAG")
                    }
                }
                Status.DATA_EMPTY -> {
                    dismissProgress()
                    Toaster.showToast(this, it.message!!, Toaster.State.ERROR, Toast.LENGTH_LONG)
                }
            }
        }
        propertyDetailsEditViewModel.getAgentAssignedPropertyDetailsEditResponse()
            .observe(this) {
                when (it.status) {
                    Status.LOADING ->
                        if (!isLoadingMain) {
                            showProgress()
                        }
                    Status.SUCCESS -> {
                        if (!isLoadingMain) {
                            dismissProgress()
                        }
                        Log.e("response", Gson().toJson(it))
                        if (it.data?.response != null) {
                            if (!it.data.response.property.equals(null)) {
                                if (it.data.response.property.property_to == 0) {
                                    agentRentOrBuy.setPosition(0, true)
                                } else {
                                    agentRentOrBuy.setPosition(1, true)
                                }

                                if (it.data.response.property.category == 0) {
                                    agentCategory.setPosition(0, true)
                                } else {
                                    agentCategory.setPosition(1, true)
                                }

                                occupiedStatus = it.data.response.property.occupied
                                agentAddress1.setText(it.data.response.property.street_address_1)
                                agentAddress2.setText(it.data.response.property.street_address_2)
                                etLocationAgent.setText(
                                    CommonUtils.getAddress(
                                        it.data.response.property.latitude,
                                        it.data.response.property.longitude, this
                                    )
                                )
                                Log.e("full list", Gson().toJson(countryId))
                                for (c in 0 until categoryListId.size) {
                                    if (categoryListId[c] == it.data.response.property.type_id) {
                                        agent_propertyType_spinner.selectItemByIndex(c)
                                        selectedTypesId = it.data.response.property.type_id
                                    }
                                }
                                defaultTypesId = it.data.response.property.type_id

                                countryId.forEach { posCountryId ->
                                    if (countryId[posCountryId - 1] == it.data.response.property.country_rel.id) {
                                        country_spinnerAgent.selectItemByIndex(posCountryId - 1)
                                        selectedCountryId = it.data.response.property.country_rel.id
                                    }
                                }
                                statesList.add(it.data.response.property.state_rel.name)
                                statesId.add(it.data.response.property.state_rel.id)
                                state_spinnerAgent.setItems(statesList)
                                state_spinnerAgent.selectItemByIndex(0)
                                selectedStateId = it.data.response.property.state_rel.id
                                cityList.add(it.data.response.property.city_rel.name)
                                cityId.add(it.data.response.property.city_rel.id)
                                city_spinnerAgent.setItems(cityList)
                                city_spinnerAgent.selectItemByIndex(0)
                                selectedCityId = it.data.response.property.city_rel.id
                                zipcodeList.add(it.data.response.property.zipcode_rel.pincode)
                                zipcodeId.add(it.data.response.property.zipcode_rel.id)
                                zip_spinnerAgent.setItems(zipcodeList)
                                zip_spinnerAgent.selectItemByIndex(0)
                                selectedZipCodeId = it.data.response.property.zipcode_rel.id
                                if (!(it.data.response.property.property_details.isNullOrEmpty())) {
                                    detailsList =
                                        it.data.response.property.property_details as ArrayList<PropertyDetail>
                                }
                                if (!it.data.response.property.furnished.equals(null)) {
                                    furnishedTypeId = it.data.response.property.furnished.toInt()
                                }
                                passedDescription = it.data.response.property.description
                                passedRent = it.data.response.property.expected_amount
                                passedFeatureValue = it.data.response.property.is_featured
                                if (!(it.data.response.property.documents.isNullOrEmpty())) {
                                    passedImages =
                                        it.data.response.property.documents as ArrayList<Document>
                                }
                                if (!(it.data.response.property.amenity_categories.isNullOrEmpty())) {
                                    passedAmenityCategory =
                                        it.data.response.property.amenity_categories as ArrayList<AmenityCategory>
                                }
                                if (!(it.data.response.property.floor_plans.isNullOrEmpty())) {
                                    passedFloorPlan =
                                        it.data.response.property.floor_plans as ArrayList<FloorPlan>
                                }
                            }
                        }
                    }
                    Status.ERROR -> {
                        dismissProgress()
                        Toaster.showToast(
                            this, getString(R.string.internal_server_error),
                            Toaster.State.ERROR, Toast.LENGTH_LONG
                        )
                    }
                    Status.DATA_EMPTY -> {
                        dismissProgress()
                        Toaster.showToast(
                            this,
                            getString(R.string.data_empty),
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    }
                    Status.NO_INTERNET -> {
                        dismissProgress()
                        if (this.isConnected) {
                            Toaster.showToast(
                                this, getString(R.string.something_wrong),
                                Toaster.State.ERROR, Toast.LENGTH_LONG
                            )
                        } else {
                            val dialog = InternetDialogFragmentCopy(this)
                            dialog.show(supportFragmentManager, "TAG")
                        }
                    }
                }
            }

        propertyDetailsEditViewModel.getAgentBuilderDetailsResponse()
            .observe(this) {
                when (it.status) {
                    Status.LOADING ->
                        if (!isLoadingMain) {
                            showProgress()
                        }
                    Status.SUCCESS -> {
                        if (!isLoadingMain) {
                            dismissProgress()
                        }
                        Log.e("response", Gson().toJson(it))
                        if (it.data?.response != null) {
                            if (!it.data.response.building_details.equals(null)) {
                                if (it.data.response.building_details.property_to == 0) {
                                    agentRentOrBuy.setPosition(0, true)
                                } else {
                                    agentRentOrBuy.setPosition(1, true)
                                }
                                if (it.data.response.building_details.category == 0) {
                                    agentCategory.setPosition(0, true)
                                } else {
                                    agentCategory.setPosition(1, true)
                                }
                                agentAddress1.setText(it.data.response.building_details.street_address_1)
                                agentAddress2.setText(it.data.response.building_details.street_address_2)
                                etLocationAgent.setText(
                                    CommonUtils.getAddress(
                                        it.data.response.building_details.latitude,
                                        it.data.response.building_details.longitude, this
                                    )
                                )
                                Log.e("full list", Gson().toJson(countryId))
                                for (c in 0 until categoryListId.size) {
                                    if (categoryListId[c] == it.data.response.building_details.type_id) {
                                        agent_propertyType_spinner.selectItemByIndex(c)
                                        selectedTypesId = it.data.response.building_details.type_id
                                    }
                                }

                                defaultTypesId = it.data.response.building_details.type_id

                                countryId.forEach { posCountryId ->
                                    if (countryId[posCountryId - 1] == it.data.response.building_details.country_rel.id) {
                                        country_spinnerAgent.selectItemByIndex(posCountryId - 1)
                                        selectedCountryId =
                                            it.data.response.building_details.country_rel.id
                                    }
                                }

                                statesList.add(it.data.response.building_details.state_rel.name)
                                statesId.add(it.data.response.building_details.state_rel.id)
                                state_spinnerAgent.setItems(statesList)
                                state_spinnerAgent.selectItemByIndex(0)
                                selectedStateId = it.data.response.building_details.state_rel.id
                                cityList.add(it.data.response.building_details.city_rel.name)
                                cityId.add(it.data.response.building_details.city_rel.id)
                                city_spinnerAgent.setItems(cityList)
                                city_spinnerAgent.selectItemByIndex(0)
                                selectedCityId = it.data.response.building_details.city_rel.id
                                zipcodeList.add(it.data.response.building_details.zipcode_rel.pincode)
                                zipcodeId.add(it.data.response.building_details.zipcode_rel.id)
                                zip_spinnerAgent.setItems(zipcodeList)
                                zip_spinnerAgent.selectItemByIndex(0)
                                selectedZipCodeId = it.data.response.building_details.zipcode_rel.id

                                if (it.data.response.building_details.furnished != "") {
                                    furnishedTypeId =
                                        it.data.response.building_details.furnished.toInt()
                                }
                                passedDescription = it.data.response.building_details.description
                                passedFeatureValue = it.data.response.building_details.is_featured
                                if (!(it.data.response.building_details.documents.isNullOrEmpty())) {
                                    builderImages =
                                        it.data.response.building_details.documents as ArrayList<Document>
                                }

                                setupBuilderImages()
                            }
                            if (!it.data.response.units.equals(null)) {
                                setApartmentRecyclerView(it.data.response.units)
                            } else {
                                rvBuildingApartments.isVisible = false
                            }
                        }
                    }
                    Status.ERROR -> {
                        dismissProgress()
                        Toaster.showToast(
                            this, getString(R.string.internal_server_error),
                            Toaster.State.ERROR, Toast.LENGTH_LONG
                        )
                    }
                    Status.DATA_EMPTY -> {
                        dismissProgress()
                        Toaster.showToast(
                            this,
                            getString(R.string.data_empty),
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    }
                    Status.NO_INTERNET -> {
                        dismissProgress()
                        if (this.isConnected) {
                            Toaster.showToast(
                                this, getString(R.string.something_wrong),
                                Toaster.State.ERROR, Toast.LENGTH_LONG
                            )
                        } else {
                            val dialog = InternetDialogFragmentCopy(this)
                            dialog.show(supportFragmentManager, "TAG")
                        }
                    }
                }
            }

        propertyDetailsEditViewModel.getAgentPropertyRemoveResponse()
            .observe(this) {
                when (it.status) {
                    Status.SUCCESS -> {
                        dismissProgress()
                        builderImages!!.removeAt(deleteImagePosition)
                        Log.e("CLICK POS", "initData: $it - $deleteImagePosition")
                        imageAdapterFromApi.notifyDataSetChanged()
                    }
                    Status.LOADING -> {
                        showProgress()
                    }
                    Status.ERROR -> {
                        dismissProgress()
                        Toaster.showToast(
                            this,
                            it.message!!,
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    }
                    Status.NO_INTERNET -> {
                        dismissProgress()
                        if (this.isConnected) {
                            Toaster.showToast(
                                this, getString(R.string.something_wrong),
                                Toaster.State.ERROR, Toast.LENGTH_LONG
                            )
                        } else {
                            val dialog = InternetDialogFragmentCopy(this)
                            dialog.show(supportFragmentManager, "TAG")
                        }
                    }
                    Status.DATA_EMPTY -> {
                        dismissProgress()
                        Toaster.showToast(
                            this,
                            it.message!!,
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    }
                }
            }

        propertyDetailsEditViewModel.agentUpdateBuildingData.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()
                    Toaster.showToast(
                        this,
                        it.data!!.response,
                        Toaster.State.SUCCESS,
                        Toast.LENGTH_LONG
                    )
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finishAffinity()
                }
                Status.LOADING -> {
                    showProgress()
                }
                Status.ERROR -> {
                    dismissProgress()
                    Toaster.showToast(this, it.message!!, Toaster.State.ERROR, Toast.LENGTH_LONG)
                }
                Status.NO_INTERNET -> {
                    dismissProgress()
                    if (this.isConnected) {
                        Toaster.showToast(
                            this, getString(R.string.something_wrong),
                            Toaster.State.ERROR, Toast.LENGTH_LONG
                        )
                    } else {
                        val dialog = InternetDialogFragmentCopy(this)
                        dialog.show(supportFragmentManager, "TAG")
                    }
                }
                Status.DATA_EMPTY -> {
                    dismissProgress()
                    Toaster.showToast(this, it.message!!, Toaster.State.ERROR, Toast.LENGTH_LONG)
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setupBuilderImages() {
        val gridLayoutManager = GridLayoutManager(this, 3)
        gridLayoutManager.orientation = GridLayoutManager.VERTICAL
        rvBuildingImages.layoutManager = gridLayoutManager
        imageAdapter = AgentImageUploadAdapter(imagesList!!) {
            images!!.removeAt(it)
            imagesList!!.removeAt(it)
            imageAdapter.notifyDataSetChanged()
        }
        rvBuildingImages.adapter = imageAdapter

        if (builderImages?.size!! > 0) {
            rvBuildingImagesFromResponse.visibility = View.VISIBLE
            val gridLayoutManagerForApi = GridLayoutManager(this, 3)
            gridLayoutManagerForApi.orientation = GridLayoutManager.VERTICAL
            rvBuildingImagesFromResponse.layoutManager = gridLayoutManagerForApi
            imageAdapterFromApi = AgentBuilderImagesFromApi(builderImages!!) { it, id ->
                deleteImagePosition = it
                propertyDetailsEditViewModel.agentRemovePropertyDocument(id.toString())
                Log.e("CLICK POS", "initData: $it - $id")
            }
            rvBuildingImagesFromResponse.adapter = imageAdapterFromApi
        }
    }

    private fun setApartmentRecyclerView(units: List<Unit>) {
        rvBuildingApartments.layoutManager = LinearLayoutManager(this)
        buildingApartmentAdapter = BuildingApartmentAdapter(
            units
        ) { editApartment(it) }
        rvBuildingApartments.adapter = buildingApartmentAdapter
    }

    private fun editApartment(it: Int) {
        val intent = Intent(this, EditApartment1Activity::class.java)
        intent.putExtra("UNIT_ID", it)
        startActivity(intent)
    }

    /* for rent or buy */
    private fun getPropertyCategory() {
        when (agentRentOrBuy.position) {
            0 -> selectedPropertyTo = getString(R.string.rent)
            1 -> selectedPropertyTo = getString(R.string.buy)
        }
    }

    /* for residential or commercial */
    private fun getPropertyTo() {
        when (agentCategory.position) {
            0 -> selectedPropertyCatagorey = 0
            1 -> selectedPropertyCatagorey = 1
        }
    }

    override fun onClicks() {

        /* country spinner */
        country_spinnerAgent.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newText ->
            Log.i("TAG", oldIndex.toString() + oldItem + newText)
            selectedCountryId = 0
            selectedCountryId = countryId[newIndex]
            selectedCountryName = newText
            propertyDetailsEditViewModel.statesList()

        }

        agent_propertyType_spinner.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newText ->
            Log.i("TAG", oldIndex.toString() + oldItem + newText)
            selectedTypesId = 0
            selectedTypesId = categoryListId[newIndex]
            spinnerType = newText
        }

        state_spinnerAgent.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newText ->
            Log.i("TAG", oldIndex.toString() + oldItem + newText)
            selectedStateId = 0
            selectedStateId = statesId[newIndex]
            selectedStateName = newText
            cityList.clear()
            cityId.clear()
            selectedCityId = 0
            propertyDetailsEditViewModel.cityList(selectedStateId)

        }
        city_spinnerAgent.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newText ->
            Log.i("TAG", oldIndex.toString() + oldItem + newText)
            selectedCityId = 0
            selectedCityId = cityId[newIndex]
            selectedCityName = newText
            zipcodeList.clear()
            zipcodeId.clear()
            selectedZipCodeId = 0
            propertyDetailsEditViewModel.zipcodeList(selectedCityId)

        }
        zip_spinnerAgent.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newText ->
            Log.i("TAG", oldIndex.toString() + oldItem + newText)
            selectedZipCodeId = 0
            selectedZipCodeId = zipcodeId[newIndex]
            selectedZipCode = newText
        }
        agent_property_nextBtn.setOnClickListener {
            getPropertyTo()
            getPropertyCategory()
            validateAddress()
        }

        agentCategory.setOnPositionChangedListener { position ->
            if (position == 0) {
                propertyDetailsEditViewModel.getPropertyTypes(agentCategory.position)
            } else {
                propertyDetailsEditViewModel.getPropertyTypes(agentCategory.position)
            }
        }

        addBuildingImgBtn.setOnClickListener {
            permissionsBuilder(Manifest.permission.READ_EXTERNAL_STORAGE).build().send { result ->
                if (result.allGranted()) {
                    openImageFileChooser()
                }
            }
        }

        btnVerifyBuilding.setOnClickListener {
            getPropertyTo()
            getPropertyCategory()
            validateAddress()
        }
    }

    private fun openImageFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.action = Intent.ACTION_PICK
        startActivityForResult(Intent.createChooser(intent, "Select Image(s)"), PICK_IMAGES_CODE)
    }

    /* checking for validation */
    @SuppressLint("LongLogTag")
    private fun validateAddress() {
        val tempAddress1 = agentAddress1.text
        val tempAddress2 = agentAddress2.text

        /* if no item selected */
        if (agent_propertyType_spinner.selectedIndex == -1) {
            Toaster.showToast(
                this,
                getString(R.string.select_property_type),
                Toaster.State.ERROR,
                Toast.LENGTH_LONG
            )
        } else {
            /* if no item selected from spinner */
            if (selectedTypesId == 0 || selectedCountryId == 0 || selectedStateId == 0 || selectedCityId == 0 || selectedZipCodeId == 0) {
                Toaster.showToast(
                    this,
                    getString(R.string.something_went_wrong),
                    Toaster.State.ERROR,
                    Toast.LENGTH_LONG
                )
            } else {
                /* if address is empty */
                if (tempAddress1.isEmpty()) {
                    Toaster.showToast(
                        this,
                        getString(R.string.address_is_required),
                        Toaster.State.ERROR,
                        Toast.LENGTH_LONG
                    )
                } else {
                    /* passing value to next page */
                    Log.e("ADD_PROPERTY_TYPE_ID", selectedTypesId.toString())
                    Log.e("ADD_PROPERTY_TYPE_NAME", spinnerType)
                    Log.e("ADD_PROPERTY_RENT_OR_BUY", agentRentOrBuy.position.toString())
                    Log.e(
                        "ADD_PROPERTY_RESIDENTIAL_OR_COMMERCIAL",
                        agentCategory.position.toString()
                    )
                    Log.e("ADD_PROPERTY_STREET_ADDRESS_1", tempAddress1.toString())
                    Log.e("ADD_PROPERTY_STREET_ADDRESS_2", tempAddress2.toString())
                    Log.e("ADD_PROPERTY_COUNTRY_ID", selectedCountryId.toString())
                    Log.e("ADD_PROPERTY_STATE_ID", selectedStateId.toString())
                    Log.e("ADD_PROPERTY_CITY_ID", selectedCityId.toString())
                    Log.e("ADD_PROPERTY_ZIPCODE_ID", selectedCityId.toString())

                    if (is_builder) {
                        propertyDetailsEditViewModel.agentUpdateBuilding(
                            this,
                            clicked_property_id,
                            agentRentOrBuy.position,
                            agentCategory.position,
                            selectedCountryId,
                            selectedStateId,
                            selectedCityId,
                            selectedTypesId,
                            selectedZipCodeId,
                            tempAddress1.toString(),
                            tempAddress2.toString(),
                            images,
                            lat,
                            lng
                        )
                    } else {
                        val intent = Intent(this, PropertyDetailsPage2Activity::class.java)
                        intent.putExtra("ADD_PROPERTY_TYPE_ID", selectedTypesId)
                        intent.putExtra("ADD_PROPERTY_TYPE_NAME", spinnerType)
                        intent.putExtra("ADD_PROPERTY_RENT_OR_BUY", agentRentOrBuy.position)
                        intent.putExtra(
                            "ADD_PROPERTY_RESIDENTIAL_OR_COMMERCIAL",
                            agentCategory.position
                        )
                        intent.putExtra("ADD_PROPERTY_STREET_ADDRESS_1", tempAddress1.toString())
                        intent.putExtra("ADD_PROPERTY_STREET_ADDRESS_2", tempAddress2.toString())
                        intent.putExtra("ADD_PROPERTY_COUNTRY_ID", selectedCountryId)
                        intent.putExtra("ADD_PROPERTY_STATE_ID", selectedStateId)
                        intent.putExtra("ADD_PROPERTY_CITY_ID", selectedCityId)
                        intent.putExtra("ADD_PROPERTY_ZIPCODE_ID", selectedCityId)

                        intent.putParcelableArrayListExtra("DETAIL_LIST_PASSED", detailsList)
                        intent.putExtra("furnishedId", furnishedTypeId)
                        intent.putExtra("description", passedDescription)
                        intent.putExtra("rent", passedRent)

                        intent.putExtra("LAT", lat)
                        intent.putExtra("LONG", lng)

                        intent.putExtra("OCCUPIED_STATUS", occupiedStatus)
                        intent.putExtra("is_featured", passedFeatureValue)
                        intent.putParcelableArrayListExtra("passed_images", passedImages)
                        intent.putParcelableArrayListExtra(
                            "passed_amenity_categories",
                            passedAmenityCategory
                        )
                        intent.putParcelableArrayListExtra("passed_floor_plans", passedFloorPlan)
                        startActivity(intent)
                    }
                }
            }
        }
    }

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                Log.e(
                    "check back press",
                    Gson().toJson(intent?.getParcelableArrayListExtra<FloorPlan>("passed_floor_plans"))
                )
                passedFloorPlan =
                    intent?.getParcelableArrayListExtra("passed_floor_plans")
                passedImages = intent?.getParcelableArrayListExtra("passed_images")

            }
        }

    @SuppressLint("NotifyDataSetChanged")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGES_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data!!.clipData != null) {
                    val count = data.clipData!!.itemCount
                    for (i in 0 until count) {
                        val imageUri = data.clipData!!.getItemAt(i).uri
                        images!!.add(CommonUtils.getImageRealPath(imageUri, this).toString())
                        imagesList!!.add(imageUri)
                        imageAdapter.notifyDataSetChanged()
                    }
                    position = 0
                } else {
                    val imageUri = data.data
                    images!!.add(CommonUtils.getImageRealPath(imageUri, this).toString())
                    imagesList!!.add(imageUri)
                    imageAdapter.notifyDataSetChanged()
                    position = 0
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (isConnectionRestored) {
            isConnectionRestored = false
            propertyDetailsEditViewModel.countryList()
        }
    }
}