package com.property.propertyagent.agent_panel.ui.main.sidemenu.property.propertydetails

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.property.propertyagent.R
import com.property.propertyagent.agent_panel.ui.main.sidemenu.property.propertydetails.adapter.AgentAmenitiesAdapter
import com.property.propertyagent.agent_panel.ui.main.sidemenu.property.propertydetails.adapter.AgentPropertyDetailsAdapter
import com.property.propertyagent.base.BaseActivity
import com.property.propertyagent.dialogs.InternetDialogFragmentCopy
import com.property.propertyagent.modal.agent.agent_assigned_property_details.*
import com.property.propertyagent.modal.object_Model.furnishedSupplier
import com.property.propertyagent.modal.owner.owner_amenities.new.AmenityMainResponse
import com.property.propertyagent.modal.owner.owner_amenities.new.Amenity_Detail
import com.property.propertyagent.modal.owner.owner_amenities.new.Amenity_subTitle
import com.property.propertyagent.utils.EnumFromPage
import com.property.propertyagent.utils.Status
import com.property.propertyagent.utils.Toaster
import com.property.propertyagent.utils.isConnected
import kotlinx.android.synthetic.main.activity_property_details_page2.*
import kotlinx.android.synthetic.main.add_commercial_building_property.*
import kotlinx.android.synthetic.main.add_commercial_plot_property.*
import kotlinx.android.synthetic.main.add_hotel_apartment_property.*
import kotlinx.android.synthetic.main.add_office_property.*
import kotlinx.android.synthetic.main.add_residential_plot_property.*
import kotlinx.android.synthetic.main.add_residential_property.*
import kotlinx.android.synthetic.main.add_shop_property.*
import kotlinx.android.synthetic.main.add_vila_property.*
import kotlinx.android.synthetic.main.add_warehouse_property.*
import kotlinx.android.synthetic.main.toolbar_main.*

class PropertyDetailsPage2Activity : BaseActivity() {

    private lateinit var propertyDetailsEditViewModel: PropertyDetailsEditViewModel
    private var furnishedTypeId: Int = -1
    private var passedFeaturesValue: Int = -1
    private var passedRent: String = ""
    private var passedDescription: String = ""
    private var amenitiesNewLList = ArrayList<AmenityMainResponse>()
    private var amenitySub = ArrayList<Amenity_subTitle>()
    private var amenityDetail = ArrayList<Amenity_Detail>()

    private var detailsListPassed: ArrayList<PropertyDetail>? = null

    private val hashMap: HashMap<Int, Int> = HashMap()
    private val hashMapIfPassed: HashMap<Int, Int> = HashMap()

    private var amenitiesList = ArrayList<Int>()

    private var furnishedType = ArrayList<String>()
    private var idList = ArrayList<Int>()
    private var valueList = ArrayList<Int>()

    private var lat = ""
    private var lng = ""

    private var selectedTypesId = 0
    private var spinnerType = ""
    private var selectedPropertyTo = 0
    private var selectedPropertyCategory = 0
    private var tempAddress1 = ""
    private var tempAddress2 = ""
    private var occupiedStatus: String = ""
    private var selectedCountryId = 0
    private var selectedStateId = 0
    private var selectedCityId = 0
    private var selectedZipCodeId = 0
    private var passedImages: ArrayList<Document>? = null
    private var passedFloorPlan: ArrayList<FloorPlan>? = null
    private var passedAmenityCategory: ArrayList<AmenityCategory>? = null

    override val layoutId: Int
        get() = R.layout.activity_property_details_page2
    override val setToolbar: Boolean
        get() = true
    override val hideStatusBar: Boolean
        get() = false

    override fun fragmentLaunch() {

    }

    @SuppressLint("LongLogTag")
    override fun initData() {
        setSupportActionBar(toolbar)
        tvToolbarTitle.text = getString(R.string.property_details)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_green)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        passedImages = ArrayList()
        passedAmenityCategory = ArrayList()
        passedFloorPlan = ArrayList()

        selectedTypesId = intent.getIntExtra("ADD_PROPERTY_TYPE_ID", 0)
        occupiedStatus = intent.getStringExtra("OCCUPIED_STATUS")!!

        spinnerType = intent.extras?.get("ADD_PROPERTY_TYPE_NAME").toString()
        selectedPropertyTo = intent.extras?.getInt("ADD_PROPERTY_RENT_OR_BUY")!!
        selectedPropertyCategory =
            intent.extras?.getInt("ADD_PROPERTY_RESIDENTIAL_OR_COMMERCIAL")!!
        tempAddress1 = intent.extras?.get("ADD_PROPERTY_STREET_ADDRESS_1").toString()
        tempAddress2 = intent.extras?.get("ADD_PROPERTY_STREET_ADDRESS_2").toString()
        selectedCountryId = intent.extras?.getInt("ADD_PROPERTY_COUNTRY_ID")!!
        selectedStateId = intent.extras?.getInt("ADD_PROPERTY_STATE_ID")!!
        selectedCityId = intent.extras?.getInt("ADD_PROPERTY_CITY_ID")!!
        selectedZipCodeId = intent.extras?.getInt("ADD_PROPERTY_ZIPCODE_ID")!!

        detailsListPassed =
            intent.extras!!.getParcelableArrayList("DETAIL_LIST_PASSED")
        passedImages = intent.extras!!.getParcelableArrayList("passed_images")
        passedFloorPlan = intent.extras!!.getParcelableArrayList("passed_floor_plans")
        passedAmenityCategory =
            intent.extras!!.getParcelableArrayList("passed_amenity_categories")

        passedDescription = intent.getStringExtra("description")!!
        passedRent = intent.getStringExtra("rent")!!

        lat = intent.getStringExtra("LAT")!!
        lng = intent.getStringExtra("LONG")!!

        furnishedTypeId = intent.getIntExtra("furnishedId", -1)
        passedFeaturesValue = intent.getIntExtra("is_featured", -1)

        Log.e("details", Gson().toJson(detailsListPassed))
        Log.e("floorplans", Gson().toJson(passedFloorPlan))
        Log.e("amenities_cat", Gson().toJson(passedAmenityCategory))

        Log.e("passed image 2", Gson().toJson(passedImages))
        etExpectedAmountAgent.setText(passedRent)
        pDetailsEtxAgent.setText(passedDescription)

        Log.e("ADD_PROPERTY_TYPE_ID", selectedTypesId.toString())
        Log.e("ADD_PROPERTY_TYPE_NAME", spinnerType)
        Log.e("ADD_PROPERTY_RENT_OR_BUY", selectedPropertyTo.toString())
        Log.e("ADD_PROPERTY_RESIDENTIAL_OR_COMMERCIAL", selectedPropertyCategory.toString())
        Log.e("ADD_PROPERTY_STREET_ADDRESS_1", tempAddress1)
        Log.e("ADD_PROPERTY_STREET_ADDRESS_2", tempAddress2)
        Log.e("ADD_PROPERTY_COUNTRY_ID", selectedCountryId.toString())
        Log.e("ADD_PROPERTY_STATE_ID", selectedStateId.toString())
        Log.e("ADD_PROPERTY_CITY_ID", selectedCityId.toString())
        Log.e("ADD_PROPERTY_ZIPCODE_ID", selectedCityId.toString())

        if (occupiedStatus == "1") {
            vacantStatus.setPosition(1, true)
        } else {
            vacantStatus.setPosition(0, true)
        }

        propertyDetailsEditViewModel.amenitiesNewList(selectedTypesId)
    }

    override fun setupUI() {

        rvAmenitiesAgent.layoutManager = LinearLayoutManager(this)
        val gridLayoutManager = GridLayoutManager(this, 2)
        gridLayoutManager.orientation = GridLayoutManager.VERTICAL
        rvProperyDetailsAgent.layoutManager = gridLayoutManager
    }

    override fun setupViewModel() {
        propertyDetailsEditViewModel = PropertyDetailsEditViewModel()
    }

    private fun addFurnishedType(): ArrayList<String> {
        furnishedType.clear()
        furnishedSupplier.furnishedItems.forEach {
            furnishedType.add(it.type_name)
        }
        return furnishedType
    }

    private fun saveDataFromAmenities(hashMap: HashMap<Int, Int>) {
        valueList.clear()
        idList.clear()
        hashMap.forEach {
            valueList.add(it.value)
            idList.add(it.key)
        }
    }

    private fun removeItem(pos: Int) {
        Log.e("removeItem", pos.toString())
        amenitiesList.remove(pos)
        Log.e("check list rem", Gson().toJson(amenitiesList))
    }

    private fun addItem(pos: Int) {

        if (!(amenitiesList.contains(pos))) {
            amenitiesList.add(pos)
            Log.e("check list add", Gson().toJson(amenitiesList))
        } else {
            Log.e("additem", pos.toString())
        }
    }

    override fun setupObserver() {
        propertyDetailsEditViewModel.amenitiesNewData.observe(this, {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()

                    val furnishedList: ArrayList<String> = addFurnishedType()
                    if (!furnishedList.equals(null)) {
                        furnishedType_spinnerAgent.clearSelectedItem()
                        furnishedType_spinnerAgent.setItems(furnishedList)
                        furnishedType_spinnerAgent.selectItemByIndex(furnishedTypeId)
                    }

                    amenitiesNewLList.addAll(listOf(it.data!!.response))
                    amenityDetail.addAll(it.data.response.details)
                    amenitySub.addAll(it.data.response.amenitiesData)

                    /* for amenities */
                    if (amenitySub.isNotEmpty()) {
                        for (i in 0 until amenitySub.size) {
                            for (j in 0 until passedAmenityCategory!!.size) {
                                if (amenitySub[i].id == passedAmenityCategory!![j].id) {
                                    Log.e("check 12", amenitySub[i].name)
                                } else {
                                    passedAmenityCategory!!.add(
                                        AmenityCategory(
                                            listOf(AmenityDetail(-1, "", "")),
                                            -1,
                                            ""
                                        )
                                    )
                                }
                            }
                        }
                    }
                    Log.e("check 123", Gson().toJson(passedAmenityCategory))
                    rvAmenitiesAgent.adapter = AgentAmenitiesAdapter(amenitySub,
                        passedAmenityCategory!!,
                        { it1 -> addItem(it1) }) { it2 ->
                        removeItem(it2)
                    }
                    rvAmenitiesAgent.scheduleLayoutAnimation()

                    /* for property  details */
                    if (detailsListPassed != null) {
                        rvProperyDetailsAgent.adapter =
                            AgentPropertyDetailsAdapter(
                                amenityDetail,
                                detailsListPassed!!
                            ) { id, value ->
                                hashMap[id] = value
                                for (key in hashMap.keys) {
                                    if (key == id) {
                                        hashMap[id] = value
                                    }
                                }
                                Log.i("agent add property", "setupObserver: $hashMap")
                                saveDataFromAmenities(hashMap)
                            }
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
                Status.LOADING -> {
                    showProgress()
                }
            }
        })
    }

    override fun onClicks() {
        furnishedType_spinnerAgent.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newText ->
            Log.i("TAG", oldIndex.toString() + oldItem + newText)
            furnishedTypeId = furnishedSupplier.furnishedItems[newIndex].type_id
            spinnerType = newText
        }
        add_property_backBtnAgent.setOnClickListener {
            super.onBackPressed()
        }
        add_property_nextBtnAgent.setOnClickListener {
            if (hashMap.isEmpty()) {
                if (detailsListPassed != null) {
                    if (detailsListPassed!!.isNotEmpty()) {
                        detailsListPassed!!.forEach {
                            hashMapIfPassed[it.detail_id] = it.value
                        }
                        saveDataFromAmenities(hashMapIfPassed)
                        clickNextButtonFun()
                    }
                } else {
                    clickNextButtonFun()
                }
            } else if (hashMap.isEmpty()) {
                Toaster.showToast(
                    this,
                    getString(R.string.pleasefillalldetails),
                    Toaster.State.ERROR,
                    Toast.LENGTH_SHORT
                )
            } else {
                if (detailsListPassed != null) {
                    if (detailsListPassed!!.isNotEmpty() && hashMap.isNotEmpty()) {
                        for (i in 0 until detailsListPassed!!.size) {
                            if (!(hashMap.keys.contains(detailsListPassed!![i].detail_id))) {
                                hashMap[detailsListPassed!![i].detail_id] =
                                    detailsListPassed!![i].value
                            }
                        }
                        saveDataFromAmenities(hashMap)
                        clickNextButtonFun()
                    } else if (amenityDetail.size == hashMap.size) {
                        if (hashMap.isEmpty()) {
                            Toaster.showToast(
                                this,
                                getString(R.string.pleasefillalldetails),
                                Toaster.State.ERROR,
                                Toast.LENGTH_SHORT
                            )
                        } else {
                            clickNextButtonFun()
                        }
                    } else {
                        Toaster.showToast(
                            this,
                            getString(R.string.pleasefillallfileds),
                            Toaster.State.ERROR,
                            Toast.LENGTH_SHORT
                        )
                    }
                } else {
                    clickNextButtonFun()
                }
            }
        }
    }

    private fun clickNextButtonFun() {
        val des = pDetailsEtxAgent.text.toString()
        if (des.isEmpty()) {
            Toaster.showToast(
                this,
                getString(R.string.pleasefillalldetails),
                Toaster.State.ERROR,
                Toast.LENGTH_SHORT
            )
            pDetailsEtxAgent.requestFocus()
        } else {
            val intent = Intent(this, PropertyDetailsPage3Activity::class.java)
            intent.putExtra("ADD_PROPERTY_TYPE_ID", selectedTypesId)
            intent.putExtra("ADD_PROPERTY_TYPE_NAME", spinnerType)
            intent.putExtra("ADD_PROPERTY_RENT_OR_BUY", selectedPropertyTo)
            intent.putExtra(
                "ADD_PROPERTY_RESIDENTIAL_OR_COMMERCIAL",
                selectedPropertyCategory
            )

            intent.putExtra("LAT", lat)
            intent.putExtra("LONG", lng)
            intent.putExtra("EXPECTED_AMOUNT", passedRent)

            intent.putExtra("ADD_PROPERTY_STREET_ADDRESS_1", tempAddress1)
            intent.putExtra("ADD_PROPERTY_STREET_ADDRESS_2", tempAddress2)
            intent.putExtra("ADD_PROPERTY_COUNTRY_ID", selectedCountryId)
            intent.putExtra("ADD_PROPERTY_STATE_ID", selectedStateId)
            intent.putExtra("ADD_PROPERTY_CITY_ID", selectedCityId)
            intent.putExtra("ADD_PROPERTY_ZIPCODE_ID", selectedZipCodeId)
            intent.putExtra("ADD_PROPERTY_AMENITIES", amenitiesList)
            intent.putIntegerArrayListExtra("ADD_PROPERTY_DETAILS_ID", idList)
            intent.putIntegerArrayListExtra(
                "ADD_PROPERTY_DETAILS_VALUE",
                valueList
            )
            intent.putExtra("ADD_PROPERTY_FURNISHED_ID", furnishedTypeId)
            intent.putExtra("ADD_PROPERTY_DES", des)
            intent.putExtra("OCCUPIED_STATUS", vacantStatus.position.toString())
            intent.putParcelableArrayListExtra("passed_images", passedImages)
            intent.putParcelableArrayListExtra("passed_floor_plans", passedFloorPlan)

            startActivity(intent)
        }
    }

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                passedFloorPlan =
                    intent?.getParcelableArrayListExtra("passed_floor_plans")
                passedImages = intent?.getParcelableArrayListExtra("passed_images")
            }
        }

    override fun onBackPressed() {
        val intent = Intent()
        intent.putParcelableArrayListExtra("passed_floor_plans", passedFloorPlan)
        intent.putParcelableArrayListExtra("passed_images", passedImages)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun launchFragment(stringExtra: String?) {
        when (stringExtra) {
            EnumFromPage.RESIDENTIAL_BUILDING.name -> {
                add_residential_property_layout.visibility = View.VISIBLE
                add_property_residential_property_nextBtn.setOnClickListener {
                    val intent = Intent(this, PropertyDetailsPage3Activity::class.java)
                    startActivity(intent)
                }

                add_property_residential_property_backBtn.setOnClickListener {
                    super.onBackPressed()
                }
            }
            EnumFromPage.VILLA.name -> {
                add_vila_property_layout.visibility = View.VISIBLE
                add_property_vila_property_nextBtn.setOnClickListener {
                    val intent = Intent(this, PropertyDetailsPage3Activity::class.java)
                    startActivity(intent)
                }

                add_property_vila_property_backBtn.setOnClickListener {
                    super.onBackPressed()
                }
            }

            EnumFromPage.APARTMENT.name -> {
                add_hotel_apartment_property_layout.visibility = View.VISIBLE
                add_property_hotel_apartment_property_nextBtn.setOnClickListener {
                    val intent = Intent(this, PropertyDetailsPage3Activity::class.java)
                    startActivity(intent)
                }

                add_property_hotel_apartment_property_backBtn.setOnClickListener {
                    super.onBackPressed()
                }
            }
            EnumFromPage.RESIDENTIAL_PLOT.name -> {
                add_residential_plot_property_layout.visibility = View.VISIBLE
                add_property_residential_plot_property_nextBtn.setOnClickListener {
                    val intent = Intent(this, PropertyDetailsPage3Activity::class.java)
                    startActivity(intent)
                }

                add_property_residential_plot_property_backBtn.setOnClickListener {
                    super.onBackPressed()
                }
            }

            EnumFromPage.COMMERCIAL_BUILDING.name -> {
                add_commercial_building_property_layout.visibility = View.VISIBLE
                add_property_commercial_building_property_nextBtn.setOnClickListener {
                    val intent = Intent(this, PropertyDetailsPage3Activity::class.java)
                    startActivity(intent)
                }

                add_property_commercial_building_property_backBtn.setOnClickListener {
                    super.onBackPressed()
                }
            }

            EnumFromPage.COMMERCIAL_PLOT.name -> {
                add_commercial_plot_property_layout.visibility = View.VISIBLE
                add_property_commercial_plot_property_nextBtn.setOnClickListener {
                    val intent = Intent(this, PropertyDetailsPage3Activity::class.java)
                    startActivity(intent)
                }

                add_property_commercial_plot_property_backBtn.setOnClickListener {
                    super.onBackPressed()
                }
            }

            EnumFromPage.OFFICE.name -> {
                add_office_property_layout.visibility = View.VISIBLE
                add_property_office_property_nextBtn.setOnClickListener {
                    val intent = Intent(this, PropertyDetailsPage3Activity::class.java)
                    startActivity(intent)
                }

                add_property_office_property_backBtn.setOnClickListener {
                    super.onBackPressed()
                }
            }

            EnumFromPage.SHOP.name -> {
                add_shop_property_layout.visibility = View.VISIBLE
                add_property_shop_property_nextBtn.setOnClickListener {
                    val intent = Intent(this, PropertyDetailsPage3Activity::class.java)
                    startActivity(intent)
                }

                add_property_shop_property_backBtn.setOnClickListener {
                    super.onBackPressed()
                }
            }

            EnumFromPage.WAREHOUSE.name -> {
                add_warehouse_property_layout.visibility = View.VISIBLE
                add_property_warehouse_property_nextBtn.setOnClickListener {
                    val intent = Intent(this, PropertyDetailsPage3Activity::class.java)
                    startActivity(intent)
                }

                add_property_warehouse_property_backBtn.setOnClickListener {
                    super.onBackPressed()
                }
            }
        }
    }
}