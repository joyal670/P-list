package com.property.propertyagent.owner_panel.ui.main.home.addproperty.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
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
import com.property.propertyagent.databinding.ActivityAddPropertyMainPageBinding
import com.property.propertyagent.dialogs.InternetDialogFragment
import com.property.propertyagent.owner_panel.ui.main.home.addproperty.adapter.image_upload_adapter
import com.property.propertyagent.owner_panel.ui.main.home.addproperty.viewmodel.AddPropertyViewModel
import com.property.propertyagent.owner_panel.ui.main.home.home.activity.HomeOwnerActivity
import com.property.propertyagent.utils.CommonUtils
import com.property.propertyagent.utils.Status
import com.property.propertyagent.utils.Toaster
import kotlinx.android.synthetic.main.toolbar_main_owner.*

class AddPropertyMainPage : BaseActivity1<ActivityAddPropertyMainPageBinding>() {
    private lateinit var addPropertyViewModel: AddPropertyViewModel

    private var selectedMainType = 0
    private var selectedRentBuyType = 0
    private var selectedResidentialCommercial = 0

    private var categoryList = ArrayList<String>()
    private var statesList = ArrayList<String>()
    private var cityList = ArrayList<String>()
    private var countryList = ArrayList<String>()
    private var zipcodeList = ArrayList<String>()

    private var categoryListId = ArrayList<Int>()
    private var cityId = ArrayList<Int>()
    private var statesId = ArrayList<Int>()
    private var countryId = ArrayList<Int>()
    private var zipcodeId = ArrayList<Int>()

    private var selectedTypesId = 0
    private var selectedStateId = 0
    private var selectedCityId = 0
    private var selectedCountryId = 0
    private var selectedZipCodeId = 0
    private var selectedCountryName: String = ""
    private var selectedStateName: String = ""
    private var selectedCityName: String = ""
    private var selectedZipCode: String = ""
    private var spinnerType = ""

    private lateinit var imageAdapter: image_upload_adapter
    private var images: ArrayList<String?>? = null
    private var imagesList: ArrayList<Uri?>? = null

    private var placesClient: PlacesClient? = null

    private var passLng = 0.0
    private var passLat =0.0
    private val PLACE_PICKER_REQUEST = 3

    private var isLoading = false

    override val layoutId: Int
        get() = R.layout.activity_add_property_main_page
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

        /* country api */
        addPropertyViewModel.countryData.observe(this, Observer { it ->
            when (it.status) {
                Status.SUCCESS -> {

                    countryList.clear()
                    countryId.clear()
                    it.data!!.response.forEach {
                        countryList.addAll(listOf(it.name))
                        countryId.addAll(listOf(it.id))
                    }

                    binding.countrySpinner.clearSelectedItem()
                    binding.countrySpinner.setItems(countryList)

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
                    val dialog = InternetDialogFragment(1)
                    dialog.show(supportFragmentManager, "TAG")
                }
                Status.DATA_EMPTY -> {
                    dismissProgressOwner()
                    Toaster.showToast(this, it.message!!, Toaster.State.ERROR, Toast.LENGTH_LONG)
                }
            }
        })

        /* type api */
        addPropertyViewModel.typeListData.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgressOwner()
                    isLoading = false

                    categoryList.clear()
                    categoryListId.clear()
                    it.data?.response?.forEach {
                        categoryList.addAll(listOf(it.type))
                        categoryListId.addAll(listOf(it.id))
                    }
                    binding.propertyTypeSpinner.clearSelectedItem()
                    binding.propertyTypeSpinner.setItems(categoryList)

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
                    val dialog = InternetDialogFragment(1)
                    dialog.show(supportFragmentManager, "TAG")
                }
                Status.DATA_EMPTY -> {
                    dismissProgressOwner()
                    Toaster.showToast(this, it.message!!, Toaster.State.ERROR, Toast.LENGTH_LONG)
                }
            }
        })

        /* state api */
        addPropertyViewModel.statesData.observe(this, Observer { it ->
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgressOwner()

                    statesList.clear()
                    statesId.clear()
                    it.data!!.response.forEach {
                        statesList.addAll(listOf(it.name))
                        statesId.addAll(listOf(it.id))
                    }
                    val adapter = ArrayAdapter(
                        this,
                        android.R.layout.simple_spinner_item,
                        statesList
                    )

                    binding.stateSpinner.clearSelectedItem()
                    binding.stateSpinner.setItems(statesList)

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
                    val dialog = InternetDialogFragment(1)
                    dialog.show(supportFragmentManager, "TAG")
                }
                Status.DATA_EMPTY -> {
                    dismissProgressOwner()
                    Toaster.showToast(this, it.message!!, Toaster.State.ERROR, Toast.LENGTH_LONG)
                }
            }
        })

        /* city api */
        addPropertyViewModel.cityData.observe(this, Observer { it ->
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgressOwner()

                    cityList.clear()
                    cityId.clear()
                    it.data!!.response.forEach {
                        cityList.addAll(listOf(it.name))
                        cityId.addAll(listOf(it.id))
                    }

                    binding.citySpinner.clearSelectedItem()
                    binding.citySpinner.setItems(cityList)

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
                    val dialog = InternetDialogFragment(1)
                    dialog.show(supportFragmentManager, "TAG")
                }
                Status.ERROR -> {
                    dismissProgressOwner()
                    Toaster.showToast(this, it.message!!, Toaster.State.ERROR, Toast.LENGTH_LONG)
                }
            }
        })

        /* zip code api */
        addPropertyViewModel.zipcodeData.observe(this, Observer { it ->
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgressOwner()

                    zipcodeList.clear()
                    zipcodeId.clear()
                    it.data!!.response.forEach {
                        zipcodeList.addAll(listOf(it.pincode))
                        zipcodeId.addAll(listOf(it.id))
                    }

                    binding.zipSpinner.clearSelectedItem()
                    binding.zipSpinner.setItems(zipcodeList)

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
                    val dialog = InternetDialogFragment(1)
                    dialog.show(supportFragmentManager, "TAG")
                }
                Status.DATA_EMPTY -> {
                    dismissProgressOwner()
                    Toaster.showToast(this, it.message!!, Toaster.State.ERROR, Toast.LENGTH_LONG)
                }
            }
        })

        /* add builder property */
        addPropertyViewModel.addBuilderPropertyData.observe(this, Observer {
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
                    val dialog = InternetDialogFragment(1)
                    dialog.show(supportFragmentManager, "TAG")
                }
                Status.DATA_EMPTY -> {
                    dismissProgressOwner()
                    Toaster.showToast(this, it.message!!, Toaster.State.ERROR, Toast.LENGTH_LONG)
                }
            }
        })
    }

    override fun onClicks() {

        /* country spinner */
        binding.countrySpinner.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newText ->
            selectedCountryId = 0
            selectedCountryId = countryId[newIndex]
            selectedCountryName = newText
            addPropertyViewModel.statesList(selectedCountryId)
        }

        /* state spinner */
        binding.stateSpinner.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newText ->
            selectedStateId = 0
            selectedStateId = statesId[newIndex]
            selectedStateName = newText

            cityList.clear()
            cityId.clear()
            selectedCityId = 0
            addPropertyViewModel.cityList(selectedStateId)
        }

        /* city spinner */
        binding.citySpinner.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newText ->
            selectedCityId = 0
            selectedCityId = cityId[newIndex]
            selectedCityName = newText
            zipcodeList.clear()
            zipcodeId.clear()
            selectedZipCodeId = 0
            addPropertyViewModel.zipcodeList(selectedCityId)
        }

        /* zpi code spinner */
        binding.zipSpinner.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newText ->
            selectedZipCodeId = 0
            selectedZipCodeId = zipcodeId[newIndex]
            selectedZipCode = newText
        }

        /* property type spinner */
        binding.propertyTypeSpinner.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newText ->
            selectedTypesId = 0
            selectedTypesId = categoryListId[newIndex]
            spinnerType = newText
        }

        /* Residential or commercial segmented button */
        binding.sgResidentialCommercial.setOnPositionChangedListener { position ->
            if (position == 0) {
                addPropertyViewModel.getPropertyTypes(binding.sgResidentialCommercial.position)
            } else {
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
                Toaster.showToast(
                    this,
                    getString(R.string.select_property_type),
                    Toaster.State.ERROR,
                    Toast.LENGTH_LONG
                )
            }
            if (propertyCountryId == 0 || propertyStateId == 0 || propertyCityId == 0 || propertyZipCodeId == 0) {
                Toaster.showToast(
                    this,
                    getString(R.string.some_fields_are_missing),
                    Toaster.State.ERROR,
                    Toast.LENGTH_LONG
                )
            }
            if (propertyLocation.isBlank()) {
                Toaster.showToast(this ,
                    getString(R.string.location_is_required) ,
                    Toaster.State.ERROR ,
                    Toast.LENGTH_LONG)
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
                Toaster.showToast(
                    this,
                    getString(R.string.select_property_type),
                    Toaster.State.ERROR,
                    Toast.LENGTH_LONG
                )
            }
            if (propertyCountryId == 0 || propertyStateId == 0 || propertyCityId == 0 || propertyZipCodeId == 0) {
                Toaster.showToast(
                    this,
                    getString(R.string.some_fields_are_missing),
                    Toaster.State.ERROR,
                    Toast.LENGTH_LONG
                )
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
                 Toaster.showToast(this ,
                     getString(R.string.location_is_required) ,
                     Toaster.State.ERROR ,
                     Toast.LENGTH_LONG)
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

    /* option menu setup */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.custom_toolbar_menu_owner, menu)

        val method = menu?.findItem(R.id.customtoolbar_addProperty)
        method?.isVisible = false

        val method1 = menu?.findItem(R.id.customtoolbar_search)
        method1?.isVisible = false

        val method2 = menu?.findItem(R.id.customtoolbar_lineView)
        method2?.isVisible = false

        return super.onCreateOptionsMenu(menu)
    }

    /* option menu clicks */
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.customtoolbar_translate -> {
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    override fun getViewBinging(): ActivityAddPropertyMainPageBinding =
        ActivityAddPropertyMainPageBinding.inflate(layoutInflater)
}