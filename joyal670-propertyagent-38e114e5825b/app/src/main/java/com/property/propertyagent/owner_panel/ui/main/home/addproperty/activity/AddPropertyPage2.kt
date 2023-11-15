package com.property.propertyagent.owner_panel.ui.main.home.addproperty.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.property.propertyagent.R
import com.property.propertyagent.base.BaseActivity
import com.property.propertyagent.dialogs.InternetDialogFragmentCopy
import com.property.propertyagent.modal.object_Model.furnishedSupplier
import com.property.propertyagent.modal.owner.owner_amenities.new.AmenityMainResponse
import com.property.propertyagent.modal.owner.owner_amenities.new.Amenity_Detail
import com.property.propertyagent.modal.owner.owner_amenities.new.Amenity_subTitle
import com.property.propertyagent.owner_panel.ui.main.home.addproperty.adapter.AnimenitiesAdapter
import com.property.propertyagent.owner_panel.ui.main.home.addproperty.adapter.pDetailsAdapter
import com.property.propertyagent.owner_panel.ui.main.home.addproperty.viewmodel.AddPropertyViewModel
import com.property.propertyagent.utils.Status
import com.property.propertyagent.utils.Toaster
import com.property.propertyagent.utils.isConnected
import kotlinx.android.synthetic.main.activity_add_property_page2_.*
import kotlinx.android.synthetic.main.toolbar_main_owner.*

class AddPropertyPage2 : BaseActivity() {
    private var furnishedTypeId : Int = -1
    private lateinit var amenitieNewViewModel : AddPropertyViewModel

    private var amenitieNewLList = ArrayList<AmenityMainResponse>()
    private var amenitySub = ArrayList<Amenity_subTitle>()
    private var amenityDetail = ArrayList<Amenity_Detail>()

    private val hashMap : HashMap<Int , Int> = HashMap<Int , Int>()

    private var amitiesList = ArrayList<Int>()

    private var furnishedType = ArrayList<String>()
    private var idList = ArrayList<Int>()
    private var valueList = ArrayList<Int>()

    private var propertyMainType = ""
    private var propertyRentBuyType = ""
    private var propertyResidentialCommercialType = ""
    private var propertyName = ""
    private var propertyStreetAddress1 = ""
    private var propertyStreetAddress2 = ""
    private var propertyTypeID = ""
    private var propertyCountryId = ""
    private var propertyStateId = ""
    private var propertyCityId = ""
    private var propertyZipCodeId = ""
    private var propertyLng = ""
    private var propertyLat = ""

    override val layoutId : Int
        get() = R.layout.activity_add_property_page2_
    override val setToolbar : Boolean
        get() = true
    override val hideStatusBar : Boolean
        get() = false

    override fun fragmentLaunch() {

    }

    @SuppressLint("SetTextI18n")
    override fun initData() {

        setSupportActionBar(owner_toolbar)
        tvToolbarTitleOwner.text = "\t" + getString(R.string.add_property)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar?.setIcon(R.drawable.ic_bar_chart_2)


        propertyMainType = intent.extras!!.get("propertyMainType").toString()
        propertyRentBuyType = intent.extras!!.get("propertyRentBuyType").toString()
        propertyResidentialCommercialType =
            intent.extras!!.get("propertyResidentialCommercialType").toString()
        propertyName = intent.extras!!.get("propertyName").toString()
        propertyStreetAddress1 = intent.extras!!.get("propertyStreetAddress1").toString()
        propertyStreetAddress2 = intent.extras!!.get("propertyStreetAddress2").toString()
        propertyTypeID = intent.extras!!.get("propertyTypeID").toString()
        propertyCountryId = intent.extras!!.get("propertyCountryId").toString()
        propertyStateId = intent.extras!!.get("propertyStateId").toString()
        propertyCityId = intent.extras!!.get("propertyCityId").toString()
        propertyZipCodeId = intent.extras!!.get("propertyZipCodeId").toString()
        propertyLng = intent.extras!!.get("propertyLng").toString()
        propertyLat = intent.extras!!.get("propertyLat").toString()

        amenitieNewViewModel.amenitiesNewList(propertyTypeID.toInt())

    }

    override fun setupUI() {

        rvAmenities.layoutManager = LinearLayoutManager(this)
        val gridLayoutManager = GridLayoutManager(this , 2)
        gridLayoutManager.orientation = GridLayoutManager.VERTICAL
        rvProperyDetails.layoutManager = gridLayoutManager
    }

    override fun setupViewModel() {
        amenitieNewViewModel = AddPropertyViewModel(this)
    }

    override fun setupObserver() {

        amenitieNewViewModel.amenitiesNewData.observe(this , Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgressOwner()

                    val furnishedList : ArrayList<String> = addFurnishedType()
                    if (furnishedList != null) {
                        furnishedType_spinner.clearSelectedItem()
                        furnishedType_spinner.setItems(furnishedList)
                    }

                    amenitieNewLList.addAll(listOf(it.data!!.response))
                    amenityDetail.addAll(it.data.response.details)
                    amenitySub.addAll(it.data.response.amenitiesData)

                    /* for amienites */
                    rvAmenities.adapter = AnimenitiesAdapter(amenitySub , { addItem(it) }) {
                        removeItem(it)
                    }
                    rvAmenities.scheduleLayoutAnimation()

                    /* for property  details */
                    rvProperyDetails.adapter = pDetailsAdapter(amenityDetail) { id , value ->
                        hashMap[id] = value
                        for (key in hashMap.keys) {
                            if (key == id) {
                                hashMap[id] = value.toInt()
                            }
                        }
                        saveDataFromAmenities(hashMap)
                    }

                }
                Status.ERROR -> {
                    dismissProgressOwner()
                    Toaster.showToast(this , it.message!! , Toaster.State.ERROR , Toast.LENGTH_LONG)

                }
                Status.NO_INTERNET -> {
                    dismissProgressOwner()
                    if (this.isConnected) {
                        Toaster.showToast(
                            this , getString(R.string.something_wrong) ,
                            Toaster.State.ERROR , Toast.LENGTH_LONG
                        )
                    } else {
                        val dialog = InternetDialogFragmentCopy(this)
                        dialog.show(supportFragmentManager , "TAG")
                    }
                }
                Status.DATA_EMPTY -> {
                    dismissProgressOwner()
                    Toaster.showToast(this , it.message!! , Toaster.State.ERROR , Toast.LENGTH_LONG)
                }
                Status.LOADING -> {
                    showProgressOwner()
                }
            }
        })
    }

    private fun saveDataFromAmenities(hashMap : HashMap<Int , Int>) {
        valueList.clear()
        idList.clear()
        hashMap.forEach {
            valueList.add(it.value)
            idList.add(it.key)
        }
    }

    private fun addFurnishedType() : ArrayList<String> {
        furnishedType.clear()
        furnishedSupplier.furnishedItems.forEach {
            furnishedType.add(it.type_name)
        }
        return furnishedType
    }

    private fun removeItem(pos : Int) {
        amitiesList.remove(pos)
    }

    private fun addItem(pos : Int) {
        amitiesList.add(pos)
    }

    override fun onClicks() {
        add_property_backBtn.setOnClickListener {
            super.onBackPressed()
        }

        /* furnished type spinner */
        furnishedType_spinner.setOnSpinnerItemSelectedListener<String> { oldIndex , oldItem , newIndex , newText ->
            furnishedTypeId = furnishedSupplier.furnishedItems[newIndex].type_id
        }

        add_property_nextBtn.setOnClickListener {

            if (amitiesList.size == 0) {
                Toaster.showToast(
                    this ,
                    getString(R.string.please_add_atleast_one_amineites) ,
                    Toaster.State.ERROR ,
                    Toast.LENGTH_SHORT
                )
            } else {
                if (hashMap.isEmpty()) {
                    Toaster.showToast(
                        this ,
                        getString(R.string.pleasefillalldetails) ,
                        Toaster.State.ERROR ,
                        Toast.LENGTH_SHORT
                    )
                } else {
                    if (amenityDetail.size == hashMap.size) {
                        if (hashMap.isEmpty()) {
                            Toaster.showToast(
                                this ,
                                getString(R.string.pleasefillalldetails) ,
                                Toaster.State.ERROR ,
                                Toast.LENGTH_SHORT
                            )
                        } else {

                            if (etExpectedAmount.text.trim().toString().isEmpty()) {
                                Toaster.showToast(
                                    this ,
                                    getString(R.string.enter_expected_amount) ,
                                    Toaster.State.ERROR ,
                                    Toast.LENGTH_SHORT
                                )
                            } else {
                                if (propertyDescriptionOwner.text.trim().toString().isEmpty()) {
                                    Toaster.showToast(
                                        this ,
                                        getString(R.string.pleasefillalldetails) ,
                                        Toaster.State.ERROR ,
                                        Toast.LENGTH_SHORT
                                    )
                                    propertyDescriptionOwner.requestFocus()
                                } else {

                                    if (furnishedTypeId == -1) {
                                        Toaster.showToast(
                                            this ,
                                            getString(R.string.please_select_furnished_type) ,
                                            Toaster.State.ERROR ,
                                            Toast.LENGTH_SHORT
                                        )
                                    } else {
                                        val intent = Intent(this , AddPropertyPage3::class.java)
                                        intent.putExtra("propertyMainType" , propertyMainType)
                                        intent.putExtra("propertyRentBuyType" , propertyRentBuyType)
                                        intent.putExtra("propertyResidentialCommercialType" ,
                                            propertyResidentialCommercialType)
                                        intent.putExtra("propertyName" , propertyName)
                                        intent.putExtra("propertyStreetAddress1" ,
                                            propertyStreetAddress1)
                                        intent.putExtra("propertyStreetAddress2" ,
                                            propertyStreetAddress2)
                                        intent.putExtra("propertyTypeID" , propertyTypeID)
                                        intent.putExtra("propertyCountryId" , propertyCountryId)
                                        intent.putExtra("propertyStateId" , propertyStateId)
                                        intent.putExtra("propertyCityId" , propertyCityId)
                                        intent.putExtra("propertyZipCodeId" , propertyZipCodeId)

                                        intent.putExtra("propertyStatus" , sgStatus.position)
                                        intent.putExtra("propertyAmenities" , amitiesList)
                                        intent.putIntegerArrayListExtra("propertyDetailsId" ,
                                            idList)
                                        intent.putIntegerArrayListExtra("propertyDetailsValue" ,
                                            valueList)
                                        intent.putExtra("propertyFurnishedId" , furnishedTypeId)
                                        intent.putExtra("propertyDescription" ,
                                            propertyDescriptionOwner.text.trim().toString())
                                        intent.putExtra("propertyLng" , propertyLng)
                                        intent.putExtra("propertyLat" , propertyLat)
                                        intent.putExtra("propertyExpectedAmount" ,
                                            etExpectedAmount.text.trim().toString())

                                        startActivity(intent)
                                    }
                                }
                            }
                        }
                    } else {
                        Toaster.showToast(
                            this ,
                            getString(R.string.pleasefillallfileds) ,
                            Toaster.State.ERROR ,
                            Toast.LENGTH_SHORT
                        )
                    }
                }
            }
        }
    }

}