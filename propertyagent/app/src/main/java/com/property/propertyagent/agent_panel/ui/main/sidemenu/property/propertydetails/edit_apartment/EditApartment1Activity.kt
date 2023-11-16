package com.property.propertyagent.agent_panel.ui.main.sidemenu.property.propertydetails.edit_apartment

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.property.propertyagent.R
import com.property.propertyagent.agent_panel.ui.main.sidemenu.property.propertydetails.PropertyDetailsEditViewModel
import com.property.propertyagent.agent_panel.ui.main.sidemenu.property.propertydetails.adapter.AgentAmenitiesAdapter
import com.property.propertyagent.agent_panel.ui.main.sidemenu.property.propertydetails.adapter.AgentPropertyDetailsAdapter
import com.property.propertyagent.base.BaseActivity
import com.property.propertyagent.dialogs.InternetDialogFragmentCopy
import com.property.propertyagent.modal.agent.agent_assigned_property_details.*
import com.property.propertyagent.modal.object_Model.furnishedSupplier
import com.property.propertyagent.modal.owner.owner_amenities.new.AmenityMainResponse
import com.property.propertyagent.modal.owner.owner_amenities.new.Amenity_Detail
import com.property.propertyagent.modal.owner.owner_amenities.new.Amenity_subTitle
import com.property.propertyagent.utils.AppPreferences.clicked_property_id
import com.property.propertyagent.utils.Status
import com.property.propertyagent.utils.Toaster
import com.property.propertyagent.utils.isConnected
import kotlinx.android.synthetic.main.activity_edit_apartment1.*
import kotlinx.android.synthetic.main.activity_property_details_page2.add_property_backBtnAgent
import kotlinx.android.synthetic.main.activity_property_details_page2.etExpectedAmountAgent
import kotlinx.android.synthetic.main.activity_property_details_page2.furnishedType_spinnerAgent
import kotlinx.android.synthetic.main.activity_property_details_page2.pDetailsEtxAgent
import kotlinx.android.synthetic.main.activity_property_details_page2.rvAmenitiesAgent
import kotlinx.android.synthetic.main.activity_property_details_page2.rvProperyDetailsAgent
import kotlinx.android.synthetic.main.toolbar_main.*

class EditApartment1Activity : BaseActivity() {

    private lateinit var propertyDetailsEditViewModel: PropertyDetailsEditViewModel

    private var detailsList: ArrayList<PropertyDetail>? = null
    private var furnishedTypeId: Int = -1
    private var passedRent: String = ""
    private var passedDescription: String = ""
    private var selectedTypesId = -1
    private var passedImages: ArrayList<Document>? = null
    private lateinit var passedAmenityCategory: ArrayList<AmenityCategory>
    private var passedFloorPlan: ArrayList<FloorPlan>? = null

    private var furnishedType = ArrayList<String>()

    private var amenitiesNewList = ArrayList<AmenityMainResponse>()
    private var amenitySub = ArrayList<Amenity_subTitle>()
    private var amenityDetail = ArrayList<Amenity_Detail>()
    private var amenitiesList = ArrayList<Int>()

    private var idList = ArrayList<Int>()
    private var valueList = ArrayList<Int>()

    private val hashMap: HashMap<Int, Int> = HashMap()
    private val hashMapIfPassed: HashMap<Int, Int> = HashMap()

    private var spinnerType = ""
    private var unitId = 0

    override val layoutId: Int
        get() = R.layout.activity_edit_apartment1
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

        passedImages = ArrayList()
        passedAmenityCategory = ArrayList()
        passedFloorPlan = ArrayList()

        unitId = intent.getIntExtra("UNIT_ID", 0)
        Log.i("TAG", unitId.toString())

        propertyDetailsEditViewModel.fetchAgentApartmentDetails(
            unitId.toString(),
            clicked_property_id
        )
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

    override fun setupObserver() {
        propertyDetailsEditViewModel.getAgentApartmentDetailsResponse()
            .observe(this, {
                when (it.status) {
                    Status.LOADING -> {
                        showProgress()
                    }
                    Status.SUCCESS -> {
                        dismissProgress()

                        Log.e("response", Gson().toJson(it))
                        if (it.data?.response != null) {
                            if (!it.data.response.property.equals(null)) {

                                selectedTypesId = it.data.response.property.type_id

                                if (it.data.response.property.occupied == "1") {
                                    sgStatus.setPosition(1, true)
                                } else {
                                    sgStatus.setPosition(0, true)
                                }

                                if (!(it.data.response.property.property_details.isNullOrEmpty())) {
                                    detailsList =
                                        it.data.response.property.property_details as ArrayList<PropertyDetail>
                                }
                                if (!it.data.response.property.furnished.equals(null)) {
                                    furnishedTypeId = it.data.response.property.furnished.toInt()
                                }
                                passedDescription = it.data.response.property.description
                                passedRent = it.data.response.property.expected_amount
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
                                propertyDetailsEditViewModel.amenitiesNewList(selectedTypesId)

                                etExpectedAmountAgent.setText(it.data.response.property.expected_amount)
                                pDetailsEtxAgent.setText(it.data.response.property.description)
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
            })

        propertyDetailsEditViewModel.amenitiesNewData.observe(this, { it ->
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()

                    val furnishedList: ArrayList<String> = addFurnishedType()
                    if (!furnishedList.equals(null)) {
                        furnishedType_spinnerAgent.clearSelectedItem()
                        furnishedType_spinnerAgent.setItems(furnishedList)
                        furnishedType_spinnerAgent.selectItemByIndex(furnishedTypeId)
                    }

                    amenitiesNewList.addAll(listOf(it.data!!.response))
                    amenityDetail.addAll(it.data.response.details)
                    amenitySub.addAll(it.data.response.amenitiesData)

                    /* for amenities */
                    if (amenitySub.isNotEmpty()) {
                        for (i in 0 until amenitySub.size) {
                            for (j in 0 until passedAmenityCategory.size) {
                                if (amenitySub[i].id == passedAmenityCategory[j].id) {
                                    Log.e("check 12", amenitySub[i].name)
                                } else {
                                    passedAmenityCategory.add(
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
                        passedAmenityCategory,
                        { addItem(it) }) {
                        removeItem(it)
                    }
                    rvAmenitiesAgent.scheduleLayoutAnimation()

                    /* for property  details */
                    if (detailsList != null) {
                        rvProperyDetailsAgent.adapter =
                            AgentPropertyDetailsAdapter(
                                amenityDetail,
                                detailsList!!
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
                Status.LOADING -> {
                    showProgress()
                }
            }
        })
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

    override fun onClicks() {

        furnishedType_spinnerAgent.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newText ->
            Log.i("TAG", oldIndex.toString() + oldItem + newText)
            furnishedTypeId = furnishedSupplier.furnishedItems[newIndex].type_id
            spinnerType = newText
        }
        add_property_backBtnAgent.setOnClickListener {
            super.onBackPressed()
        }

        btnNext.setOnClickListener {
            if (hashMap.isEmpty()) {
                if (detailsList != null) {
                    if (detailsList!!.isNotEmpty()) {
                        detailsList!!.forEach {
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
                if (detailsList != null) {
                    if (detailsList!!.isNotEmpty() && hashMap.isNotEmpty()) {
                        for (i in 0 until detailsList!!.size) {
                            if (!(hashMap.keys.contains(detailsList!![i].detail_id))) {
                                hashMap[detailsList!![i].detail_id] =
                                    detailsList!![i].value
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
            val intent = Intent(this, EditApartment2Activity::class.java)
            intent.putExtra("UNIT_ID", unitId)
            intent.putExtra("ADD_PROPERTY_TYPE_NAME", spinnerType)
            intent.putExtra("EXPECTED_AMOUNT", passedRent)
            intent.putExtra("ADD_PROPERTY_AMENITIES", amenitiesList)
            intent.putIntegerArrayListExtra("ADD_PROPERTY_DETAILS_ID", idList)
            intent.putIntegerArrayListExtra(
                "ADD_PROPERTY_DETAILS_VALUE",
                valueList
            )
            intent.putExtra("ADD_PROPERTY_FURNISHED_ID", furnishedTypeId)
            intent.putExtra("OCCUPIED_STATUS", sgStatus.position.toString())
            intent.putExtra("ADD_PROPERTY_DES", des)
            intent.putParcelableArrayListExtra("passed_images", passedImages)
            intent.putParcelableArrayListExtra("passed_floor_plans", passedFloorPlan)
            startActivity(intent)
        }
    }
}