package com.property.propertyagent.owner_panel.ui.main.home.property.building_details_list.add_apartment


import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.property.propertyagent.R
import com.property.propertyagent.base.new.BaseActivity1
import com.property.propertyagent.databinding.ActivityAddBuildingPage1Binding
import com.property.propertyagent.dialogs.InternetDialogFragmentCopy
import com.property.propertyagent.modal.object_Model.furnishedSupplier
import com.property.propertyagent.modal.owner.owner_amenities.new.AmenityMainResponse
import com.property.propertyagent.modal.owner.owner_amenities.new.Amenity_Detail
import com.property.propertyagent.modal.owner.owner_amenities.new.Amenity_subTitle
import com.property.propertyagent.owner_panel.ui.main.home.addproperty.adapter.AnimenitiesAdapter
import com.property.propertyagent.owner_panel.ui.main.home.addproperty.adapter.pDetailsAdapter
import com.property.propertyagent.owner_panel.ui.main.home.property.building_details_list.viewmodel.OwnerAddBuildingViewModel
import com.property.propertyagent.utils.Status
import com.property.propertyagent.utils.Toaster
import com.property.propertyagent.utils.isConnected
import kotlinx.android.synthetic.main.activity_add_property_page2_.*
import kotlinx.android.synthetic.main.activity_add_property_page2_.sgStatus
import kotlinx.android.synthetic.main.toolbar_main_owner.*

class AddApartmentPage1Activity : BaseActivity1<ActivityAddBuildingPage1Binding>() {

    private var buildingId = 0
    private var typeId = 0
    private lateinit var ownerAddBuildingViewModel : OwnerAddBuildingViewModel

    private var amenitieNewLList = ArrayList<AmenityMainResponse>()
    private var amenitySub = ArrayList<Amenity_subTitle>()
    private var amenityDetail = ArrayList<Amenity_Detail>()

    private val hashMap : HashMap<Int , Int> = HashMap<Int , Int>()
    private var amitiesList = ArrayList<Int>()
    private var furnishedType = ArrayList<String>()
    private var idList = ArrayList<Int>()
    private var valueList = ArrayList<Int>()
    private var furnishedTypeId : Int = -1

    override val layoutId : Int
        get() = R.layout.activity_add_building_page_1
    override val setToolbar : Boolean
        get() = true
    override val hideStatusBar : Boolean
        get() = false

    override fun fragmentLaunch() {

    }

    @SuppressLint("SetTextI18n")
    override fun initData() {

        buildingId = intent.getIntExtra("selectedId" , 0)
        typeId = intent.getIntExtra("typeId" , 0)

        ownerAddBuildingViewModel.amenitiesList(typeId)

        setSupportActionBar(owner_toolbar)
        tvToolbarTitleOwner.text = "\t" + getString(R.string.add_property)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar?.setIcon(R.drawable.ic_bar_chart_2)

    }

    override fun setupUI() {

        binding.rvAmenities.layoutManager = LinearLayoutManager(this)
        val gridLayoutManager = GridLayoutManager(this , 2)
        gridLayoutManager.orientation = GridLayoutManager.VERTICAL
        binding.rvProperyDetails.layoutManager = gridLayoutManager
    }

    override fun setupViewModel() {
        ownerAddBuildingViewModel = OwnerAddBuildingViewModel(this)
    }

    override fun setupObserver() {

        ownerAddBuildingViewModel.getPropertyAmenitiesResponse().observe(this , Observer {

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


                    /* for amenities */
                    binding.rvAmenities.adapter = AnimenitiesAdapter(amenitySub , { addItem(it) }) {
                        removeItem(it)
                    }
                    binding.rvAmenities.scheduleLayoutAnimation()

                    /* for property  details */
                    binding.rvProperyDetails.adapter =
                        pDetailsAdapter(amenityDetail) { id , value ->
                            hashMap[id] = value
                            for (key in hashMap.keys) {
                                if (key == id) {
                                    hashMap[id] = value.toInt()
                                }
                            }
                            saveDataFromAmenities(hashMap)
                        }
                }
                Status.LOADING -> {
                    showProgressOwner()
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
            }
        })
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

    private fun saveDataFromAmenities(hashMap : HashMap<Int , Int>) {
        valueList.clear()
        idList.clear()
        hashMap.forEach {
            valueList.add(it.value)
            idList.add(it.key)
        }
    }

    override fun onClicks() {

        binding.addPropertyBackBtn.setOnClickListener {
            super.onBackPressed()
        }

        /* furnished type spinner */
        furnishedType_spinner.setOnSpinnerItemSelectedListener<String> { oldIndex , oldItem , newIndex , newText ->
            furnishedTypeId = furnishedSupplier.furnishedItems[newIndex].type_id
        }

        binding.addPropertyNextBtn.setOnClickListener {

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

                            if (binding.etPropertyName.text.trim().toString().isEmpty()) {
                                binding.propertyName.requestFocus()
                                binding.propertyName.error = getString(R.string.property_name_required)
                            } else {
                                if (binding.etExpectedAmount.text.trim().toString().isEmpty()) {
                                    binding.expectedAmt.requestFocus()
                                    binding.expectedAmt.error = getString(R.string.enter_expected_amount)
                                } else {
                                    if (binding.propertyDescriptionOwner.text.trim().toString()
                                            .isEmpty()
                                    ) {
                                        binding.propertyDescriptionOwner.requestFocus()
                                        binding.des.error = getString(R.string.description_required)
                                    } else {

                                        if (furnishedTypeId == -1) {
                                            Toaster.showToast(
                                                this ,
                                                getString(R.string.please_select_furnished_type) ,
                                                Toaster.State.ERROR ,
                                                Toast.LENGTH_SHORT
                                            )
                                        } else {
                                            Log.i("TAG", "clickNextButtonFun: " + sgStatus.position.toString())

                                            val intent = Intent(this , AddApartmentPage2Activity::class.java)

                                            intent.putExtra("propertyBuildingId", buildingId)
                                            intent.putExtra("propertyTypeId", typeId)
                                            intent.putExtra("propertyName", binding.etPropertyName.text.trim().toString())
                                            intent.putExtra("propertyStatus" , binding.sgStatus.position)

                                            intent.putExtra("propertyAmenities" , amitiesList)
                                            intent.putIntegerArrayListExtra("propertyDetailsId" , idList)
                                            intent.putIntegerArrayListExtra("propertyDetailsValue" , valueList)

                                            intent.putExtra("propertyFurnishedId" , furnishedTypeId)
                                            intent.putExtra("propertyDescription" , binding.propertyDescriptionOwner.text.trim().toString())
                                            intent.putExtra("propertyExpectedAmount" , binding.etExpectedAmount.text.trim().toString())

                                            startActivity(intent)
                                        }
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

    override fun getViewBinging() : ActivityAddBuildingPage1Binding =
        ActivityAddBuildingPage1Binding.inflate(layoutInflater)

}