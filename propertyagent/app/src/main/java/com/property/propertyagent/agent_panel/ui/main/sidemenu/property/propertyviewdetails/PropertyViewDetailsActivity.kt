package com.property.propertyagent.agent_panel.ui.main.sidemenu.property.propertyviewdetails

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.extension.send
import com.google.android.material.appbar.AppBarLayout
import com.google.gson.Gson
import com.property.propertyagent.R
import com.property.propertyagent.agent_panel.ui.main.sidemenu.property.propertyrentetails.PropertyRentDetailsActivity
import com.property.propertyagent.agent_panel.ui.main.sidemenu.property.propertyviewdetails.adapter.AgentPropertyImageSliderAdapter
import com.property.propertyagent.agent_panel.ui.main.sidemenu.property.propertyviewdetails.adapter.PropertyFloorPlanImageSliderAdapter
import com.property.propertyagent.agent_panel.ui.main.sidemenu.property.propertyviewdetails.adapter.PropertyViewAmenitiesMainAdapter
import com.property.propertyagent.agent_panel.ui.main.sidemenu.property.terms_and_stay.TermsAndStayActivity
import com.property.propertyagent.base.BaseActivity
import com.property.propertyagent.dialogs.InternetDialogFragmentCopy
import com.property.propertyagent.modal.agent.agent_assigned_property_details.AmenityCategory
import com.property.propertyagent.modal.agent.agent_assigned_property_details.Document
import com.property.propertyagent.modal.agent.agent_assigned_property_details.FloorPlan
import com.property.propertyagent.utils.*
import com.property.propertyagent.utils.AppPreferences.clicked_property_id
import com.property.propertyagent.utils.AppPreferences.isConnectionRestored
import com.property.propertyagent.utils.AppPreferences.property_type
import kotlinx.android.synthetic.main.activity_propertyview_details.*
import kotlinx.android.synthetic.main.content_scrolling_view_property_details.*
import ru.rhanza.constraintexpandablelayout.State
import kotlin.math.abs

class PropertyViewDetailsActivity : BaseActivity() {

    private var phone: String = ""
    private var shareMessage: String = ""
    private var shareImage: String = ""

    private lateinit var propertyViewDetailsViewModel: PropertyViewDetailsViewModel
    private var passedPropertyId = ""
    override val layoutId: Int
        get() = R.layout.activity_propertyview_details
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = false

    override fun fragmentLaunch() {

    }

    override fun initData() {
        setSupportActionBar(toolbar_agent)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_home_back_button)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar_agent.title = getString(R.string.property_details)
        passedPropertyId = intent.getStringExtra("property_id").toString()
        if (passedPropertyId.isNotBlank()) {
            propertyViewDetailsViewModel.agentPropertyViewDetails(passedPropertyId)
        }
        app_barAgent.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            when {
                //  State Expanded
                verticalOffset == 0 -> {
                    supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_home_back_button)
                    toolbar_agent.title = ""
                    green_flag.visibility = View.VISIBLE
                }
                //  State Collapsed
                abs(verticalOffset) >= appBarLayout.totalScrollRange -> {
                    supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_white)
                    toolbar_agent.title = getString(R.string.property_details)
                    green_flag.visibility = View.GONE

                }
                else -> Log.e("onelse", "toolbar")
            }
        })
    }

    private fun setPropertyImageSlider(documents: List<Document>) {
        val vpPropertyImageSliderAdapter =
            AgentPropertyImageSliderAdapter(supportFragmentManager, lifecycle, documents)
        vpPropertyDetailsImageSliderAgent.adapter = vpPropertyImageSliderAdapter
        dotsIndicatorDetailsAgent.setViewPager2(vpPropertyDetailsImageSliderAgent)
    }

    private fun setPropertyFloorPlanImageSlider(floorPlans: List<FloorPlan>) {
        val vpPropertyFloorPlanImageSliderAdapter = PropertyFloorPlanImageSliderAdapter(
            supportFragmentManager, lifecycle, floorPlans
        )
        vpPropertyDetailsFloorImageSlider.adapter = vpPropertyFloorPlanImageSliderAdapter
        dotsIndicatorFloor.setViewPager2(vpPropertyDetailsFloorImageSlider)
    }

    private fun setAmenityList(amenityList: List<AmenityCategory>) {
        rvPropertyAmenities.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val amenityAdapter =
            PropertyViewAmenitiesMainAdapter(amenityList)
        rvPropertyAmenities.adapter = amenityAdapter
    }

    override fun setupUI() {

    }

    override fun setupViewModel() {
        propertyViewDetailsViewModel = PropertyViewDetailsViewModel()
    }

    @SuppressLint("SetTextI18n")
    override fun setupObserver() {
        propertyViewDetailsViewModel.getAgentPropertyViewDetailsResponse().observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()
                    if (it.data?.response != null) {
                        Log.e("response", Gson().toJson(it))
                        if (!it.data.response.equals(null)) {
                            if (!it.data.response.property.equals(null)) {
                                if (!(it.data.response.property.documents.isNullOrEmpty())) {
                                    setPropertyImageSlider(it.data.response.property.documents)
                                }
                                tvOrginalAmount.paintFlags =
                                    tvOrginalAmount.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                                tvPropertyNameInDetails.text =
                                    it.data.response.property.property_name
                                tvPropertyCodeValue.text = it.data.response.property.property_reg_no
                                tvAmountValue.text =
                                    getString(R.string.sar) + " " + it.data.response.property.selling_price
                                tvOrginalAmount.text =
                                    getString(R.string.sar) + " " + it.data.response.property.mrp

                                if (!it.data.response.property.owner_rel.equals(null)) {
                                    phone = it.data.response.property.owner_rel.phone
                                    tvWathmanName.text = it.data.response.property.owner_rel.name
                                    ivWatchMan.loadImagesWithGlideExtByFitCenter(it.data.response.property.owner_rel.profile_image)
                                }

                                tvRating.text = it.data.response.property.rating

                                if (!it.data.response.property.rating.equals(null)) {
                                    val count: Double = it.data.response.property.rating.toDouble()
                                    ratingbar.count = count.toInt()
                                    fragment_user_completed_ratingTv.text =
                                        it.data.response.property.total_rating_count.toString() + " Ratings"
                                }

                                if (it.data.response.property.property_to == 0) {
                                    tvPropertyTo.text = getString(R.string.appartment_for_rent)
                                    tvAmountValue.text =
                                        getString(R.string.sar) + " " + it.data.response.property.rent
                                    tvOrginalAmount.isVisible = false
                                } else {
                                    tvPropertyTo.text = getString(R.string.appartment_for_sale)
                                }
                                property_type = it.data.response.property.property_to
                                clicked_property_id = it.data.response.property.id.toString()

                                if (it.data.response.property.property_to == 1) {
                                    tvRentDetailsBtn.isVisible = false
                                }

                                if (it.data.response.property.is_featured == 0) {
                                    green_flag.isVisible = false
                                }

                                if (!it.data.response.property.latitude.equals(null) &&
                                    !it.data.response.property.longitude.equals(null)
                                ) {
                                    try {
                                        tvPropertyLocation.text = CommonUtils.getAddress(
                                            it.data.response.property.latitude,
                                            it.data.response.property.longitude,
                                            this
                                        )
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                }
                                tvBed.text = it.data.response.property.Beds.toString()
                                tvBath.text = it.data.response.property.Bathroom.toString()
                                tvArea.text = it.data.response.property.area.toString()
                                when (it.data.response.property.furnished) {
                                    "0" -> {
                                        tvFurnished.text = getString(R.string.not_furnished)
                                    }
                                    "1" -> {
                                        tvFurnished.text = getString(R.string.semifurnished)
                                    }
                                    else -> {
                                        tvFurnished.text = getString(R.string.fully)
                                    }
                                }
                                tvFloorNo.text = it.data.response.property.floors.toString()
                                tvDescriptionContent.text = it.data.response.property.description
                                val characterLength = it.data.response.property.description.length
                                if(characterLength <334) {
                                    ivCircularDown.visibility = View.GONE
                                    collapseExpandWithAnimation()
                                }
                                if ((!it.data.response.property.floor_plans.isNullOrEmpty())) {
                                    setPropertyFloorPlanImageSlider(it.data.response.property.floor_plans)
                                } else {
                                    tvFloorPlansHead.isVisible = false
                                    vpPropertyDetailsFloorImageSlider.isVisible = false
                                    dotsIndicatorFloor.isVisible = false
                                }
                                if (!it.data.response.property.amenity_categories.isNullOrEmpty()) {
                                    setAmenityList(it.data.response.property.amenity_categories)
                                }

                                val stringBuilder = StringBuilder()
                                stringBuilder.append("Property Name : " + it.data.response.property.property_name)
                                stringBuilder.append("\nProperty Type : " + tvPropertyTo.text)
                                stringBuilder.append("\n" + tvAmountValue.text)

                                shareMessage = stringBuilder.toString()
                                shareImage = it.data.response.property.documents[0].document_image
                            }
                        }
                    }
                    container.isVisible = true
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
        }

        propertyViewDetailsViewModel.getAgentPropertyPdfResponse().observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()
                    if (it.data!!.data.pdf.isNotBlank()) {
                        val browserIntent =
                            Intent(Intent.ACTION_VIEW, Uri.parse(it.data.data.pdf))
                        startActivity(browserIntent)
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
        }
    }

    override fun onClicks() {

        ivCircularDown.setOnClickListener { collapseExpandWithAnimation() }
        tvDescriptionContent.setOnClickListener { collapseExpandWithAnimation() }

        viewPropertyDetails_CallBtn.setOnClickListener {
            permissionsBuilder(Manifest.permission.CALL_PHONE).build().send { result ->
                if (result.allGranted()) {
                    CommonUtils.dailPhone(this, phone)
                }
            }
        }

        viewPropertyDetails_WhatsappBtn.setOnClickListener {
            CommonUtils.openWhatsAppEnquiry(this, phone, getString(R.string.hai))
        }

        tvRentDetailsBtn.setOnClickListener {
            startActivity(Intent(this, PropertyRentDetailsActivity::class.java))
        }

        btnTerms.setOnClickListener {
            startActivity(Intent(this, TermsAndStayActivity::class.java))
        }

        btnShare.setOnClickListener {
            CommonUtils.shareProperty(shareMessage, this, shareImage)
        }

        btnDownload.setOnClickListener {
            propertyViewDetailsViewModel.agentPropertyPdf(passedPropertyId)
        }

    }

    private fun collapseExpandWithAnimation() {
        Log.e("TAG", "collapseExpandWithAnimation: " + expandableDescription.state )
        if (expandableDescription.state == State.Collapsed) {
            expandableDescription.toggle()
            ivCircularDown.rotation = 180f
        } else if (expandableDescription.state == State.Expanded) {
            expandableDescription.toggle()
            ivCircularDown.rotation = 0f
        }
    }

    override fun onResume() {
        super.onResume()
        if (isConnectionRestored) {
            isConnectionRestored = false
            if (passedPropertyId.isNotBlank()) {
                propertyViewDetailsViewModel.agentPropertyViewDetails(passedPropertyId)
            }
        }
    }
}