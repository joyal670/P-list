package com.property.propertyagent.owner_panel.ui.main.home.addproperty.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.allShouldShowRationale
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.extension.send
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import com.property.propertyagent.R
import com.property.propertyagent.base.new.BaseActivity1
import com.property.propertyagent.databinding.ActivityAddPropertyBinding
import com.property.propertyagent.owner_panel.ui.main.home.addproperty.viewmodel.AddPropertyViewModel
import com.property.propertyagent.utils.Status
import com.property.propertyagent.utils.Toaster
import kotlinx.android.synthetic.main.activity_add_property.*
import kotlinx.android.synthetic.main.toolbar_main_owner.*

class AddPropertyPage1 : BaseActivity1<ActivityAddPropertyBinding>() {
    private lateinit var fusedLocationClient : FusedLocationProviderClient
    private lateinit var addPropertyViewModel : AddPropertyViewModel

    var catagoryList = ArrayList<String>()
    var statesList = ArrayList<String>()
    var cityList = ArrayList<String>()
    var countryList = ArrayList<String>()
    var zipcodeList = ArrayList<String>()

    var catagoryListId = ArrayList<Int>()
    var cityId = ArrayList<Int>()
    var statesId = ArrayList<Int>()
    var countryId = ArrayList<Int>()
    var zipcodeId = ArrayList<Int>()

    private var selectedTypesId = 0
    private var selectedStateId = 0
    private var selectedCityId = 0
    private var selectedCountryId = 0
    private var selectedZipCodeId = 0
    private var selectedCountryName : String = ""
    private var selectedStateName : String = ""
    private var selectedCityName : String = ""
    private var selectedZipCode : String = ""

    private var selectedPropertyTo = ""
    private var selectedPropertyCatagorey = ""
    private var spinnerType = ""
    private var lat : String = ""
    private var lng : String = ""

    override val layoutId : Int
        get() = R.layout.activity_add_property
    override val setToolbar : Boolean
        get() = true
    override val hideStatusBar : Boolean
        get() = false

    override fun getViewBinging() : ActivityAddPropertyBinding =
        ActivityAddPropertyBinding.inflate(layoutInflater)

    override fun fragmentLaunch() {

    }

    private fun getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(
                this ,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this ,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            methodWithPermissions()
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    Log.e("fusedLocationClient" , Gson().toJson(location))
                    lat = location.latitude.toString()
                    lng = location.longitude.toString()
                    //premissionGranded=true
                    // use your location object
                    // get latitude , longitude and other info from this
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
                Toaster.showToast(this ,
                    "Please allow permissions" ,
                    Toaster.State.WARNING ,
                    Toast.LENGTH_SHORT)
            }
        }
    }

    override fun initData() {
        /* toolbar setup */
        setSupportActionBar(owner_toolbar)
        supportActionBar?.title = getString(R.string.add_property)
        supportActionBar!!.setDisplayShowTitleEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_white)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getLastKnownLocation()
        //setupObserver()
        addPropertyViewModel.countryList()
    }

    override fun setupUI() {
    }

    override fun setupViewModel() {
        addPropertyViewModel = AddPropertyViewModel(this)
    }

    override fun setupObserver() {
        /* property types */
        addPropertyViewModel.countryData.observe(this , Observer { it ->
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()

                    countryList.clear()
                    countryId.clear()
                    it.data!!.response.forEach {
                        countryList.addAll(listOf(it.name))
                        countryId.addAll(listOf(it.id))
                    }
                    val adapter = ArrayAdapter(
                        this ,
                        android.R.layout.simple_spinner_item ,
                        countryList
                    )
//                    adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
//                    binding.countrySpinner.adapter = adapter

                    binding.countrySpinner.clearSelectedItem()
                    binding.countrySpinner.setItems(countryList)

                    addPropertyViewModel.getPropertyTypes(sgRC.position)

                }
                Status.LOADING -> {
                    showProgress()
                }
                Status.ERROR -> {
                    dismissProgress()
                    Toaster.showToast(this , it.message!! , Toaster.State.ERROR , Toast.LENGTH_LONG)
                }
                Status.NO_INTERNET -> {
                    dismissProgress()
                    Toaster.showToast(this , it.message!! , Toaster.State.ERROR , Toast.LENGTH_LONG)
                }
                Status.DATA_EMPTY -> {
                    dismissProgress()
                    Toaster.showToast(this , it.message!! , Toaster.State.ERROR , Toast.LENGTH_LONG)
                }
            }
        })
        addPropertyViewModel.typeListData.observe(this , Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()
                    catagoryList.clear()
                    catagoryListId.clear()
                    it.data?.response?.forEach {
                        catagoryList.addAll(listOf(it.type))
                        catagoryListId.addAll(listOf(it.id))
                    }
                    binding.propertyTypeSpinner.clearSelectedItem()
                    binding.propertyTypeSpinner.setItems(catagoryList)


                }
                Status.LOADING -> {
                    showProgress()
                }
                Status.ERROR -> {
                    dismissProgress()
                    Toaster.showToast(this , it.message!! , Toaster.State.ERROR , Toast.LENGTH_LONG)
                }
                Status.NO_INTERNET -> {
                    dismissProgress()
                    Toaster.showToast(this , it.message!! , Toaster.State.ERROR , Toast.LENGTH_LONG)
                }
                Status.DATA_EMPTY -> {
                    dismissProgress()
                    Toaster.showToast(this , it.message!! , Toaster.State.ERROR , Toast.LENGTH_LONG)
                }
            }
        })
        addPropertyViewModel.statesData.observe(this , Observer { it ->
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()

                    statesList.clear()
                    statesId.clear()
                    it.data!!.response.forEach {
                        statesList.addAll(listOf(it.name))
                        statesId.addAll(listOf(it.id))
                    }
                    val adapter = ArrayAdapter(
                        this ,
                        android.R.layout.simple_spinner_item ,
                        statesList
                    )
//                    adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
//                    binding.stateSpinner.adapter = adapter

                    binding.stateSpinner.clearSelectedItem()
                    binding.stateSpinner.setItems(statesList)

                }
                Status.LOADING -> {
                    showProgress()
                }
                Status.ERROR -> {
                    dismissProgress()
                    Toaster.showToast(this , it.message!! , Toaster.State.ERROR , Toast.LENGTH_LONG)
                }
                Status.NO_INTERNET -> {
                    dismissProgress()
                    Toaster.showToast(this , it.message!! , Toaster.State.ERROR , Toast.LENGTH_LONG)
                }
                Status.DATA_EMPTY -> {
                    dismissProgress()
                    Toaster.showToast(this , it.message!! , Toaster.State.ERROR , Toast.LENGTH_LONG)
                }

            }
        })
        addPropertyViewModel.cityData.observe(this , Observer { it ->
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()

                    cityList.clear()
                    cityId.clear()
                    it.data!!.response.forEach {
                        cityList.addAll(listOf(it.name))
                        cityId.addAll(listOf(it.id))
                    }
                    val adapter = ArrayAdapter(
                        this ,
                        android.R.layout.simple_spinner_item ,
                        cityList
                    )

                    binding.citySpinner.clearSelectedItem()
                    binding.citySpinner.setItems(cityList)

//                    adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
//                    binding.citySpinner.adapter = adapter

                }
                Status.LOADING -> {
                    showProgress()
                }
                Status.DATA_EMPTY -> {
                    dismissProgress()
                    Toaster.showToast(this , it.message!! , Toaster.State.ERROR , Toast.LENGTH_LONG)
                }
                Status.NO_INTERNET -> {
                    dismissProgress()
                    Toaster.showToast(this , it.message!! , Toaster.State.ERROR , Toast.LENGTH_LONG)
                }
                Status.ERROR -> {
                    dismissProgress()
                    Toaster.showToast(this , it.message!! , Toaster.State.ERROR , Toast.LENGTH_LONG)
                }
            }
        })
        addPropertyViewModel.zipcodeData.observe(this , Observer { it ->
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()

                    zipcodeList.clear()
                    zipcodeId.clear()
                    it.data!!.response.forEach {
                        zipcodeList.addAll(listOf(it.pincode))
                        zipcodeId.addAll(listOf(it.id))
                    }
                    val adapter = ArrayAdapter(
                        this ,
                        android.R.layout.simple_spinner_item ,
                        zipcodeList
                    )

                    binding.zipSpinner.clearSelectedItem()
                    binding.zipSpinner.setItems(zipcodeList)
//                    adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
//                    binding.zipSpinner.adapter = adapter

                }
                Status.LOADING -> {
                    showProgress()
                }
                Status.ERROR -> {
                    dismissProgress()
                    Toaster.showToast(this , it.message!! , Toaster.State.ERROR , Toast.LENGTH_LONG)
                }
                Status.NO_INTERNET -> {
                    dismissProgress()
                    Toaster.showToast(this , it.message!! , Toaster.State.ERROR , Toast.LENGTH_LONG)
                }
                Status.DATA_EMPTY -> {
                    dismissProgress()
                    Toaster.showToast(this , it.message!! , Toaster.State.ERROR , Toast.LENGTH_LONG)
                }
            }
        })

    }

    override fun onClicks() {

        /* next button */
        binding.addPropertyNextBtn.setOnClickListener {
            getPropertyTo()
            getPropertyCategory()
            validateAddress()
        }

        /* country spinner */
        binding.countrySpinner.setOnSpinnerItemSelectedListener<String> { oldIndex , oldItem , newIndex , newText ->
            selectedCountryId = 0
            selectedCountryId = countryId[newIndex]
            selectedCountryName = newText
            //addPropertyViewModel.statesList()

        }
        binding.etLocation.setOnClickListener {

        }
        /*
        binding.countrySpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent : AdapterView<*>? ,
                    view : View? ,
                    position : Int ,
                    id : Long
                ) {
                    selectedCountryId = countryId[position]
                }

                override fun onNothingSelected(parent : AdapterView<*>?) {}
            }
        */

        /* state spinner */

        binding.stateSpinner.setOnSpinnerItemSelectedListener<String> { oldIndex , oldItem , newIndex , newText ->
            selectedStateId = 0
            selectedStateId = statesId[newIndex]
            selectedStateName = newText

            cityList.clear()
            cityId.clear()
            selectedCityId = 0
            // binding.citySpinner.adapter = null
            addPropertyViewModel.cityList(selectedStateId)


        }

        /* binding.stateSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
             override fun onItemSelected(
                 parent : AdapterView<*>? ,
                 view : View? ,
                 position : Int ,
                 id : Long
             ) {
                 selectedStateId = statesId[position]
                 cityList.clear()
                 cityId.clear()
                 selectedCityId = 0
                 binding.citySpinner.adapter = null
                 setCityApiObserver(selectedStateId)

             }

             override fun onNothingSelected(parent : AdapterView<*>?) {}
         }*/

        /* city spinner */

        binding.citySpinner.setOnSpinnerItemSelectedListener<String> { oldIndex , oldItem , newIndex , newText ->
            selectedCityId = 0
            selectedCityId = cityId[newIndex]
            selectedCityName = newText
            zipcodeList.clear()
            zipcodeId.clear()
            selectedZipCodeId = 0
            //binding.zipSpinner.adapter = null
            addPropertyViewModel.zipcodeList(selectedCityId)

        }


        /*     binding.citySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                 override fun onItemSelected(
                     parent : AdapterView<*>? ,
                     view : View? ,
                     position : Int ,
                     id : Long
                 ) {
                     selectedCityId = cityId[position]
                     zipcodeList.clear()
                     zipcodeId.clear()
                     selectedZipCodeId = 0
                     binding.zipSpinner.adapter = null
                     setupZipcodeObserver(selectedCityId)

                 }

                 override fun onNothingSelected(parent : AdapterView<*>?) {}
             }*/

        /* zip code spinner*/

        binding.zipSpinner.setOnSpinnerItemSelectedListener<String> { oldIndex , oldItem , newIndex , newText ->
            selectedZipCodeId = 0
            selectedZipCodeId = zipcodeId[newIndex]
            selectedZipCode = newText
        }

        /*        binding.zipSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent : AdapterView<*>? ,
                view : View? ,
                position : Int ,
                id : Long
            ) {
                selectedZipCodeId = zipcodeId[position]
            }

            override fun onNothingSelected(parent : AdapterView<*>?) {}
        }*/

        /* property type spinner */
        binding.propertyTypeSpinner.setOnSpinnerItemSelectedListener<String> { oldIndex , oldItem , newIndex , newText ->
            selectedTypesId = 0
            selectedTypesId = catagoryListId[newIndex]
            spinnerType = newText
        }

        /* Residential or commercial segmented button */
        binding.sgRC.setOnPositionChangedListener { position ->
            if (position == 0) {
                addPropertyViewModel.getPropertyTypes(sgRC.position)
            } else {
                addPropertyViewModel.getPropertyTypes(sgRC.position)
            }
        }
    }

    /* checking for validation */
    private fun validateAddress() {
        val tempAddress1 = binding.etxStreetAddress1.text
        val tempAddress2 = binding.etxStreetAddress2.text

        /* if no item selected */
        if (binding.propertyTypeSpinner.selectedIndex == -1) {
            Toaster.showToast(
                this ,
                getString(R.string.select_property_type) ,
                Toaster.State.ERROR ,
                Toast.LENGTH_LONG
            )
        } else {
            /* if no item selected from spinner */
            if (selectedTypesId == 0 || selectedCountryId == 0 || selectedStateId == 0 || selectedCityId == 0 || selectedZipCodeId == 0) {
                Toaster.showToast(
                    this ,
                    getString(R.string.something_went_wrong) ,
                    Toaster.State.ERROR ,
                    Toast.LENGTH_LONG
                )
            } else {
                /* if address is empty */
                when {
                    etPropertyName.text.trim().toString().isEmpty() -> {
                        Toaster.showToast(
                            this ,
                            getString(R.string.property_name_required) ,
                            Toaster.State.ERROR ,
                            Toast.LENGTH_LONG
                        )
                    }
                    tempAddress1.isEmpty() -> {
                        Toaster.showToast(
                            this ,
                            getString(R.string.address_is_required) ,
                            Toaster.State.ERROR ,
                            Toast.LENGTH_LONG
                        )
                    }
                    else -> {
                        /* passing value to next page */
                        val intent = Intent(this , AddPropertyPage2::class.java)
                        intent.putExtra("ADD_PROPERTY_TYPE_ID" , selectedTypesId)
                        intent.putExtra("ADD_PROPERTY_TYPE_NAME" , spinnerType)
                        intent.putExtra("ADD_PROPERTY_RENT_OR_BUY" , sgRentBuy.position)
                        intent.putExtra("ADD_PROPERTY_RESIDENTIAL_OR_COMMERCIAL" , sgRC.position)
                        intent.putExtra("ADD_PROPERTY_NAME" , etPropertyName.text.trim().toString())
                        intent.putExtra("ADD_PROPERTY_STREET_ADDRESS_1" , tempAddress1.toString())
                        intent.putExtra("ADD_PROPERTY_STREET_ADDRESS_2" , tempAddress2.toString())
                        intent.putExtra("ADD_PROPERTY_COUNTRY_ID" , selectedCountryId)
                        intent.putExtra("ADD_PROPERTY_STATE_ID" , selectedStateId)
                        intent.putExtra("ADD_PROPERTY_CITY_ID" , selectedCityId)
                        intent.putExtra("ADD_PROPERTY_ZIPCODE_ID" , selectedZipCodeId)
                        startActivity(intent)
                    }
                }
            }
        }
    }

    /* for rent or buy */
    private fun getPropertyCategory() {
        when (binding.sgRentBuy.position) {
            0 -> selectedPropertyTo = getString(R.string.rent)
            1 -> selectedPropertyTo = getString(R.string.buy)
        }
    }

    /* for residential or commercial */
    private fun getPropertyTo() {
        when (binding.sgRC.position) {
            0 -> selectedPropertyCatagorey = getString(R.string.residential)
            1 -> selectedPropertyCatagorey = getString(R.string.commercial)
        }
    }


    /* option menu setup */
    override fun onCreateOptionsMenu(menu : Menu?) : Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.custom_toolbar_menu_owner , menu)

        val method = menu?.findItem(R.id.customtoolbar_addProperty)
        method?.isVisible = false

        val method1 = menu?.findItem(R.id.customtoolbar_search)
        method1?.isVisible = false

        val method2 = menu?.findItem(R.id.customtoolbar_lineView)
        method2?.isVisible = false

        return super.onCreateOptionsMenu(menu)
    }

    /* option menu clicks */
    override fun onOptionsItemSelected(item : MenuItem) = when (item.itemId) {
        R.id.customtoolbar_translate -> {
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }


}