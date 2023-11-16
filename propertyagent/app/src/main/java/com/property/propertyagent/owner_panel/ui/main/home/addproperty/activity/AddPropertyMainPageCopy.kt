package com.property.propertyagent.owner_panel.ui.main.home.addproperty.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.GridLayoutManager
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.extension.send
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.gson.Gson
import com.property.propertyagent.R
import com.property.propertyagent.base.new.BaseActivity1
import com.property.propertyagent.databinding.ActivityAddPropertyMainPageCopyBinding
import com.property.propertyagent.dialogs.InternetDialogFragmentCopy
import com.property.propertyagent.modal.owner.owner_city.City
import com.property.propertyagent.modal.owner.owner_country.Country
import com.property.propertyagent.modal.owner.owner_propertytype_list.OwnerPropertyType
import com.property.propertyagent.modal.owner.owner_states.States
import com.property.propertyagent.modal.owner.owner_zipcode.Zipcode
import com.property.propertyagent.owner_panel.ui.main.home.addproperty.adapter.image_upload_adapter
import com.property.propertyagent.owner_panel.ui.main.home.addproperty.viewmodel.AddPropertyViewModel
import com.property.propertyagent.owner_panel.ui.main.home.home.activity.HomeOwnerActivity
import com.property.propertyagent.utils.CommonUtils
import com.property.propertyagent.utils.Status
import com.property.propertyagent.utils.Toaster
import com.property.propertyagent.utils.isConnected
import kotlinx.android.synthetic.main.toolbar_main_owner.*

class AddPropertyMainPageCopy : BaseActivity1<ActivityAddPropertyMainPageCopyBinding>() {
    private lateinit var addPropertyViewModel: AddPropertyViewModel

    private var selectedMainType = 0
    private var selectedRentBuyType = 0
    private var selectedResidentialCommercial = 0

    private var categoryList = ArrayList<String>()
    private var statesList = ArrayList<String>()
    private var cityList = ArrayList<String>()
    private var countryList = ArrayList<String>()
    private var zipcodeList = ArrayList<String>()

    private var selectedTypesId = 0
    private var selectedStateId = 0
    private var selectedCityId = 0
    private var selectedCountryId = 0
    private var selectedZipCodeId = 0
    private var selectedCountryName: String = ""
    private var selectedStateName: String = ""
    private var selectedCityName: String = ""
    private var selectedZipCode: String = ""
    private var selectedTypeName = ""

    private lateinit var imageAdapter: image_upload_adapter
    private var images: ArrayList<String?>? = null
    private var imagesList: ArrayList<Uri?>? = null

    private var placesClient: PlacesClient? = null

    // Set default as temperary
    private var passLng = 0.0
    private var passLat = 0.0
    private val PLACE_PICKER_REQUEST = 3

    private var isLoading = false

    private var categoryMainList = ArrayList<OwnerPropertyType>()
    private var countryMainList = ArrayList<Country>()
    private var stateMainList = ArrayList<States>()
    private var cityMainList = ArrayList<City>()
    private var zipCodeMainList = ArrayList<Zipcode>()

    override val layoutId: Int
        get() = R.layout.activity_add_property_main_page_copy
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = false

    override fun fragmentLaunch() {

    }

    @SuppressLint("SetTextI18n")
    override fun initData() {
        /* toolbar setup */
        setSupportActionBar(owner_toolbar)
        tvToolbarTitleOwner.text = "\t" + getString(R.string.add_property)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar?.setIcon(R.drawable.ic_bar_chart_2)

        if (!Places.isInitialized()) {
            Places.initialize(this, getString(R.string.google_maps_key))
        }
        placesClient = Places.createClient(this)

        addPropertyViewModel.countryList()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun setupUI() {

        /* setup recyclerview */
        images = ArrayList()
        imagesList = ArrayList()

        val gridLayoutManager = GridLayoutManager(this, 3)
        gridLayoutManager.orientation = GridLayoutManager.VERTICAL
        binding.rvBuildingImages.layoutManager = gridLayoutManager
        imageAdapter = image_upload_adapter(this, imagesList!!) {
            images!!.removeAt(it)
            imagesList!!.removeAt(it)
            imageAdapter.notifyDataSetChanged()
        }
        binding.rvBuildingImages.adapter = imageAdapter

    }

    override fun setupViewModel() {
        addPropertyViewModel = AddPropertyViewModel(this)
    }

    override fun setupObserver() {

        /* type api */
        addPropertyViewModel.typeListData.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgressOwner()
                    isLoading = false

                    categoryList.clear()
                    categoryMainList.clear()
                    it.data?.response?.forEach {
                        categoryList.addAll(listOf(it.type))
                        categoryMainList.addAll(listOf(it))
                    }

                    val adapter = ArrayAdapter(this, R.layout.spinner_item, categoryList)
                    binding.propertyTypeSpinner.setAdapter(adapter)

                }
                Status.LOADING -> {
                    if (!isLoading) {
                        showProgressOwner()
                        isLoading = true
                    }
                }
                Status.ERROR -> {
                    dismissProgressOwner()
                    Toaster.showToast(this, it.message!!, Toaster.State.ERROR, Toast.LENGTH_LONG)
                }
                Status.NO_INTERNET -> {
                    dismissProgressOwner()
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
                    dismissProgressOwner()
                    Toaster.showToast(this, it.message!!, Toaster.State.ERROR, Toast.LENGTH_LONG)
                }
            }
        }

        /* country api */
        addPropertyViewModel.countryData.observe(this) { it ->
            when (it.status) {
                Status.SUCCESS -> {

                    countryList.clear()
                    countryMainList.clear()
                    it.data!!.response.forEach {
                        countryList.addAll(listOf(it.name))
                        countryMainList.addAll(listOf(it))
                    }

                    val adapter = ArrayAdapter(this, R.layout.spinner_item, countryList)
                    binding.countrySpinner.setAdapter(adapter)

                    addPropertyViewModel.getPropertyTypes(binding.sgResidentialCommercial.position)

                }
                Status.LOADING -> {
                    if (!isLoading) {
                        showProgressOwner()
                        isLoading = true
                    }
                }
                Status.ERROR -> {
                    dismissProgressOwner()
                    Toaster.showToast(this, it.message!!, Toaster.State.ERROR, Toast.LENGTH_LONG)
                }
                Status.NO_INTERNET -> {
                    dismissProgressOwner()
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
                    dismissProgressOwner()
                    Toaster.showToast(this, it.message!!, Toaster.State.ERROR, Toast.LENGTH_LONG)
                }
            }
        }

        /* state api */
        addPropertyViewModel.statesData.observe(this) { it ->
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgressOwner()

                    statesList.clear()
                    stateMainList.clear()
                    it.data!!.response.forEach {
                        statesList.addAll(listOf(it.name))
                        stateMainList.addAll(listOf(it))
                    }

                    val adapter = ArrayAdapter(this, R.layout.spinner_item, statesList)
                    binding.stateSpinner.setAdapter(adapter)

                }
                Status.LOADING -> {
                    showProgressOwner()
                }
                Status.ERROR -> {
                    dismissProgressOwner()
                    Toaster.showToast(this, it.message!!, Toaster.State.ERROR, Toast.LENGTH_LONG)
                }
                Status.NO_INTERNET -> {
                    dismissProgressOwner()
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
                    dismissProgressOwner()
                    Toaster.showToast(this, it.message!!, Toaster.State.ERROR, Toast.LENGTH_LONG)
                }
            }
        }

        /* city api */
        addPropertyViewModel.cityData.observe(this) { it ->
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgressOwner()

                    cityList.clear()
                    cityMainList.clear()
                    it.data!!.response.forEach {
                        cityList.addAll(listOf(it.name))
                        cityMainList.addAll(listOf(it))
                    }

                    val adapter = ArrayAdapter(this, R.layout.spinner_item, cityList)
                    binding.citySpinner.setAdapter(adapter)

                }
                Status.LOADING -> {
                    showProgressOwner()
                }
                Status.DATA_EMPTY -> {
                    dismissProgressOwner()
                    Toaster.showToast(this, it.message!!, Toaster.State.ERROR, Toast.LENGTH_LONG)
                }
                Status.NO_INTERNET -> {
                    dismissProgressOwner()
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
                Status.ERROR -> {
                    dismissProgressOwner()
                    Toaster.showToast(this, it.message!!, Toaster.State.ERROR, Toast.LENGTH_LONG)
                }
            }
        }

        /* zip code api */
        addPropertyViewModel.zipcodeData.observe(this) { it ->
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgressOwner()

                    zipcodeList.clear()
                    zipCodeMainList.clear()
                    it.data!!.response.forEach {
                        zipcodeList.addAll(listOf(it.pincode))
                        zipCodeMainList.addAll(listOf(it))
                    }

                    val adapter = ArrayAdapter(this, R.layout.spinner_item, zipcodeList)
                    binding.zipSpinner.setAdapter(adapter)

                }
                Status.LOADING -> {
                    showProgressOwner()
                }
                Status.ERROR -> {
                    dismissProgressOwner()
                    Toaster.showToast(this, it.message!!, Toaster.State.ERROR, Toast.LENGTH_LONG)
                }
                Status.NO_INTERNET -> {
                    dismissProgressOwner()
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
                    dismissProgressOwner()
                    Toaster.showToast(this, it.message!!, Toaster.State.ERROR, Toast.LENGTH_LONG)
                }
            }
        }

        /* add builder property */
        addPropertyViewModel.addBuilderPropertyData.observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgressOwner()
                    Toaster.showToast(
                        this,
                        it.data!!.response,
                        Toaster.State.SUCCESS,
                        Toast.LENGTH_LONG
                    )
                    startActivity(Intent(this, HomeOwnerActivity::class.java))
                    finish()
                }
                Status.LOADING -> {
                    showProgressOwner()
                }
                Status.ERROR -> {
                    dismissProgressOwner()
                    Toaster.showToast(this, it.message!!, Toaster.State.ERROR, Toast.LENGTH_LONG)
                }
                Status.NO_INTERNET -> {
                    dismissProgressOwner()
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
                    dismissProgressOwner()
                    Toaster.showToast(this, it.message!!, Toaster.State.ERROR, Toast.LENGTH_LONG)
                }
            }
        }
    }

    override fun onClicks() {

        /* country spinner */
        binding.countrySpinner.setOnItemClickListener { parent, view, position, id ->
            selectedCountryId = 0
            val tempCountry = binding.countrySpinner.text.toString()
            countryMainList.forEach {
                if (it.name.lowercase() == tempCountry.lowercase()) {
                    selectedCountryId = it.id
                    selectedCountryName = it.name
                    addPropertyViewModel.statesList(selectedCountryId)
                    binding.countyMain.isErrorEnabled = false
                }
            }
        }

        binding.countrySpinner.doOnTextChanged { text, start, before, count ->
            binding.countyMain.isErrorEnabled = false
            binding.stateSpinner.setText("")
        }

        /* state spinner */
        binding.stateSpinner.setOnItemClickListener { parent, view, position, id ->
            selectedStateId = 0
            val tempState = binding.stateSpinner.text.toString()
            stateMainList.forEach {
                if (it.name.lowercase() == tempState.lowercase()) {
                    selectedStateId = it.id
                    selectedStateName = it.name
                    addPropertyViewModel.cityList(selectedStateId)
                    binding.stateMain.isErrorEnabled = false
                }
            }
        }

        binding.stateSpinner.doOnTextChanged { text, start, before, count ->
            if (stateMainList.size == 0) {
                binding.countyMain.error = getString(R.string.select_country)
            }
            binding.stateMain.isErrorEnabled = false
            binding.citySpinner.setText("")
        }

        /* city spinner */
        binding.citySpinner.setOnItemClickListener { parent, view, position, id ->
            selectedCityId = 0
            val tempCity = binding.citySpinner.text.toString()
            cityMainList.forEach {
                if (it.name.lowercase() == tempCity.lowercase()) {
                    selectedCityId = it.id
                    selectedCityName = it.name
                    addPropertyViewModel.zipcodeList(selectedCityId)
                    binding.cityMain.isErrorEnabled = false
                }
            }
        }

        binding.citySpinner.doOnTextChanged { text, start, before, count ->
            if (cityMainList.size == 0) {
                binding.stateMain.error = getString(R.string.select_state)
            }
            binding.cityMain.isErrorEnabled = false
            binding.zipSpinner.setText("")
        }

        /* zpi code spinner */
        binding.zipSpinner.setOnItemClickListener { parent, view, position, id ->
            selectedZipCodeId = 0
            val tempZipCode = binding.zipSpinner.text.toString()
            zipCodeMainList.forEach {
                if (it.pincode == tempZipCode) {
                    selectedZipCodeId = it.id
                    selectedZipCode = it.pincode
                    binding.zipMain.isErrorEnabled = false
                }
            }
        }

        binding.zipSpinner.doOnTextChanged { text, start, before, count ->
            if (zipCodeMainList.size == 0) {
                binding.cityMain.error = getString(R.string.select_city)
                binding.zipMain.error = getString(R.string.select_zipcode)
            }
            //binding.zipMain.isErrorEnabled = false
        }

        /* property type spinner */
        binding.propertyTypeSpinner.setOnItemClickListener { parent, view, position, id ->
            selectedTypesId = 0
            val tempType = binding.propertyTypeSpinner.text.toString()
            categoryMainList.forEach {
                if (it.type.lowercase() == tempType.lowercase()) {
                    selectedTypesId = it.id
                    selectedTypeName = it.type
                }
            }
        }

        binding.etPropertyName.doOnTextChanged { text, start, before, count ->
            binding.pName.isErrorEnabled = false
        }

        binding.etxStreetAddress1.doOnTextChanged { text, start, before, count ->
            binding.address1.isErrorEnabled = false
        }

        binding.etxStreetAddress2.doOnTextChanged { text, start, before, count ->
            binding.address2.isErrorEnabled = false
        }

        /* Residential or commercial segmented button */
        binding.sgResidentialCommercial.setOnPositionChangedListener { position ->
            if (position == 0) {
                binding.propertyTypeSpinner.setText("")
                addPropertyViewModel.getPropertyTypes(binding.sgResidentialCommercial.position)
            } else {
                binding.propertyTypeSpinner.setText("")
                addPropertyViewModel.getPropertyTypes(binding.sgResidentialCommercial.position)
            }
        }

        binding.sgMainType.setOnPositionChangedListener { position ->
            if (position == 0) {
                binding.addPropertyNextBtn.text = getString(R.string.complete)
                binding.tv1.visibility = View.VISIBLE
                binding.lv1.visibility = View.VISIBLE
            } else {
                binding.addPropertyNextBtn.text = getString(R.string.next)
                binding.tv1.visibility = View.GONE
                binding.lv1.visibility = View.GONE
            }
        }

        /* add images btn */
        binding.addBuildingImgBtn.setOnClickListener {
            permissionsBuilder(Manifest.permission.READ_EXTERNAL_STORAGE).build().send { result ->
                if (result.allGranted()) {
                    openImageFileChooser()
                }
            }
        }

        /* complete btn */
        binding.addPropertyNextBtn.setOnClickListener {
            if (binding.addPropertyNextBtn.text == getString(R.string.complete)) {
                validateBuilderValues()
            } else {
                validateOwnerValues()
            }
        }

        /* location btn */
        binding.etxLocation.setOnClickListener {
            permissionsBuilder(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ).build().send { result ->
                if (result.allGranted()) {
                    getLocationDetails()
                }
            }
        }
    }

    private fun getLocationDetails() {
        val fields: List<Place.Field> = listOf(
            Place.Field.ID, Place.Field.NAME,
            Place.Field.ADDRESS,
            Place.Field.LAT_LNG
        )
        val intent: Intent = Autocomplete.IntentBuilder(
            AutocompleteActivityMode.FULLSCREEN, fields
        )
            .build(this)
        startActivityForResult(intent, PLACE_PICKER_REQUEST)
    }

    /* validate apartment owner */
    private fun validateOwnerValues() {

        getMainType()
        getRentBuyType()
        getResidentialCommercialType()

        val propertyMainType = selectedMainType
        val propertyRentBuyType = selectedRentBuyType
        val propertyResidentialCommercialType = selectedResidentialCommercial
        val propertyName = binding.etPropertyName.text.toString()
        val propertyStreetAddress1 = binding.etxStreetAddress1.text.toString()
        val propertyStreetAddress2 = binding.etxStreetAddress2.text.toString()
        val propertyTypeID = selectedTypesId
        val propertyCountryId = selectedCountryId
        val propertyStateId = selectedStateId
        val propertyCityId = selectedCityId
        val propertyZipCodeId = selectedZipCodeId
        val propertyLocation = binding.etxLocation.text.toString()


        if (propertyName.isBlank() || propertyStreetAddress1.isBlank() || propertyStreetAddress2.isBlank() || propertyTypeID == 0 || propertyCountryId == 0 || propertyStateId == 0 || propertyCityId == 0 || propertyZipCodeId == 0 || propertyLocation.isBlank()) {
            if (propertyName.isBlank()) {
                binding.pName.error = getString(R.string.property_name_is_required)
            }
            if (propertyStreetAddress1.isBlank()) {
                binding.address1.error = getString(R.string.street_address_is_required)
            }
            if (propertyStreetAddress2.isBlank()) {
                binding.address2.error = getString(R.string.street_address_is_required)
            }
            if (propertyTypeID == 0) {
                binding.typeSpinner.error = getString(R.string.select_property_category)
            }
            if (propertyCountryId == 0) {
                binding.countyMain.error = getString(R.string.select_country)
            }
            if (propertyStateId == 0) {
                binding.stateMain.error = getString(R.string.select_state)
            }
            if (propertyCityId == 0) {
                binding.cityMain.error = getString(R.string.select_city)
            }
            if (propertyZipCodeId == 0) {
                binding.zipMain.error = getString(R.string.select_zipcode)
            }
            if (propertyLocation.isBlank()) {
                Toaster.showToast(
                    this,
                    getString(R.string.location_is_required),
                    Toaster.State.ERROR,
                    Toast.LENGTH_SHORT
                )
            }

        } else {
            /* passing value to next page */
            val intent = Intent(this, AddPropertyPage2::class.java)
            intent.putExtra("propertyMainType", propertyMainType)
            intent.putExtra("propertyRentBuyType", propertyRentBuyType)
            intent.putExtra("propertyResidentialCommercialType", propertyResidentialCommercialType)
            intent.putExtra("propertyName", propertyName)
            intent.putExtra("propertyStreetAddress1", propertyStreetAddress1)
            intent.putExtra("propertyStreetAddress2", propertyStreetAddress2)
            intent.putExtra("propertyTypeID", propertyTypeID)
            intent.putExtra("propertyCountryId", propertyCountryId)
            intent.putExtra("propertyStateId", propertyStateId)
            intent.putExtra("propertyCityId", propertyCityId)
            intent.putExtra("propertyZipCodeId", propertyZipCodeId)
            intent.putExtra("propertyLng", passLng)
            intent.putExtra("propertyLat", passLat)
            startActivity(intent)
        }
    }

    /* validate builder */
    private fun validateBuilderValues() {

        getMainType()
        getRentBuyType()
        getResidentialCommercialType()

        val propertyMainType = selectedMainType
        val propertyRentBuyType = selectedRentBuyType
        val propertyResidentialCommercialType = selectedResidentialCommercial
        val propertyName = binding.etPropertyName.text.toString()
        val propertyStreetAddress1 = binding.etxStreetAddress1.text.toString()
        val propertyStreetAddress2 = binding.etxStreetAddress2.text.toString()
        val propertyTypeID = selectedTypesId
        val propertyCountryId = selectedCountryId
        val propertyStateId = selectedStateId
        val propertyCityId = selectedCityId
        val propertyZipCodeId = selectedZipCodeId
        val propertyImage = images
        //  val propertyLocation = binding.etxLocation.text.toString()
        val propertyLocation = "Kochi"


        if (propertyName.isBlank() || propertyStreetAddress1.isBlank() || propertyStreetAddress2.isBlank() || propertyTypeID == 0 || propertyCountryId == 0 || propertyStateId == 0 || propertyCityId == 0 || propertyZipCodeId == 0 || propertyImage!!.size == 0 || propertyLocation.isBlank()) {
            if (propertyName.isBlank()) {
                binding.pName.error = getString(R.string.property_name_is_required)
            }
            if (propertyStreetAddress1.isBlank()) {
                binding.address1.error = getString(R.string.street_address_is_required)
            }
            if (propertyStreetAddress2.isBlank()) {
                binding.address2.error = getString(R.string.street_address_is_required)
            }
            if (propertyTypeID == 0) {
                binding.typeSpinner.error = getString(R.string.select_property_category)
            }
            if (propertyCountryId == 0) {
                binding.countyMain.error = getString(R.string.select_country)
            }
            if (propertyStateId == 0) {
                binding.stateMain.error = getString(R.string.select_state)
            }
            if (propertyCityId == 0) {
                binding.cityMain.error = getString(R.string.select_city)
            }
            if (propertyZipCodeId == 0) {
                binding.zipMain.error = getString(R.string.select_zipcode)
            }
            if (propertyImage!!.size == 0) {
                Toaster.showToast(
                    this,
                    getString(R.string.building_image_is_required),
                    Toaster.State.ERROR,
                    Toast.LENGTH_LONG
                )
            }
            if (propertyLocation.isBlank()) {
                Toaster.showToast(
                    this,
                    getString(R.string.location_is_required),
                    Toaster.State.ERROR,
                    Toast.LENGTH_LONG
                )
            }

        } else {
            addPropertyViewModel.addBuilderPropertyList(
                propertyMainType, propertyRentBuyType,
                propertyResidentialCommercialType, propertyName,
                propertyStreetAddress1, propertyStreetAddress2,
                propertyTypeID, propertyCountryId,
                propertyStateId, propertyCityId,
                propertyZipCodeId, propertyImage,
                passLat, passLng
            )
        }
    }

    private fun openImageFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.action = Intent.ACTION_PICK
        startActivityForResult(Intent.createChooser(intent, "Select Image(s)"), 101)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101) {
            if (resultCode == Activity.RESULT_OK) {
                if (data!!.clipData != null) {
                    val count = data.clipData!!.itemCount
                    for (i in 0 until count) {
                        val imageUri = data.clipData!!.getItemAt(i).uri
                        images!!.add(CommonUtils.getImageRealPath(imageUri, this))
                        imagesList!!.add(imageUri)
                        imageAdapter.notifyDataSetChanged()
                    }
                } else {
                    val imageUri = data.data
                    images!!.add(CommonUtils.getImageRealPath(imageUri, this))
                    imagesList!!.add(imageUri)
                    imageAdapter.notifyDataSetChanged()
                }
            }
        }
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                val place = Autocomplete.getPlaceFromIntent(data!!)
                Log.e("placedata" , Gson().toJson(place))
                Log.e("place details" , "Place: " + place.name + ", " + place.id)
                passLat = place.latLng!!.latitude
                passLng = place.latLng!!.longitude
                binding.etxLocation.text = place.name

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                val status = Autocomplete.getStatusFromIntent(data!!)
                Log.e("onStatus error" , status.statusMessage.toString())
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    /* function to get property type */
    private fun getMainType() {
        when (binding.sgMainType.position) {
            0 -> selectedMainType = 1
            1 -> selectedMainType = 0
        }
    }

    /* function to get rent or buy type */
    private fun getRentBuyType() {
        when (binding.sgRentBuy.position) {
            0 -> selectedRentBuyType = 0
            1 -> selectedRentBuyType = 1
        }
    }

    /* function to get residential or commercial type */
    private fun getResidentialCommercialType() {
        when (binding.sgResidentialCommercial.position) {
            0 -> selectedResidentialCommercial = 0
            1 -> selectedResidentialCommercial = 1
        }
    }

    override fun getViewBinging(): ActivityAddPropertyMainPageCopyBinding =
        ActivityAddPropertyMainPageCopyBinding.inflate(layoutInflater)
}