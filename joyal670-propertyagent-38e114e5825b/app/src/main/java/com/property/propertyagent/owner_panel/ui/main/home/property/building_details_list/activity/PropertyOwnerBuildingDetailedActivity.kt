package com.property.propertyagent.owner_panel.ui.main.home.property.building_details_list.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.button.MaterialButton
import com.property.propertyagent.R
import com.property.propertyagent.base.BaseActivity
import com.property.propertyagent.dialogs.InternetDialogFragmentCopy
import com.property.propertyagent.modal.owner.owner_building_details.OwnerBuildingDetailsListBuildingApartment
import com.property.propertyagent.modal.owner.owner_building_details.OwnerBuildingDetailsListDocument
import com.property.propertyagent.owner_panel.ui.main.home.property.adapter.OwnerPropertyImageSliderAdapter
import com.property.propertyagent.owner_panel.ui.main.home.property.building_details_list.adapter.OwnerDetailedImagesAdapter
import com.property.propertyagent.owner_panel.ui.main.home.property.building_details_list.add_apartment.AddApartmentPage1Activity
import com.property.propertyagent.owner_panel.ui.main.home.property.building_details_list.viewmodel.OwnerBuildingListingViewModel
import com.property.propertyagent.utils.Status
import com.property.propertyagent.utils.Toaster
import com.property.propertyagent.utils.isConnected
import kotlinx.android.synthetic.main.activity_scrolling.*
import kotlinx.android.synthetic.main.content_scrolling.*
import ru.rhanza.constraintexpandablelayout.State

class PropertyOwnerBuildingDetailedActivity : BaseActivity() {

    private var buildingId = 0
    private var typeId = 0
    private lateinit var ownerBuildingListingViewModel: OwnerBuildingListingViewModel
    private var buildingApartments = ArrayList<OwnerBuildingDetailsListBuildingApartment>()
    private var buildingImages = ArrayList<OwnerBuildingDetailsListDocument>()

    override val layoutId: Int
        get() = R.layout.activity_scrolling
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = false


    override fun fragmentLaunch() {
    }

    override fun initData() {
        buildingId = intent.getIntExtra("selectedId", 0)

        ownerBuildingListingViewModel.buildingList(buildingId)

        setSupportActionBar(toolbar_owner)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_home_back_button)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar_owner.title = getString(R.string.details)
    }

    override fun setupUI() {

        fragment_propert_details_recycerview.isNestedScrollingEnabled = false

        app_bar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            when {
                //  State Expanded
                verticalOffset == 0 -> {
                    supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_home_back_button)
                    toolbar_owner.title = ""
                }
                //  State Collapsed
                kotlin.math.abs(verticalOffset) >= appBarLayout.totalScrollRange -> {
                    supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_white)
                    toolbar_owner.title = getString(R.string.building_details)
                }
            }
        })
    }

    override fun setupViewModel() {
        ownerBuildingListingViewModel = OwnerBuildingListingViewModel(this)
    }

    override fun setupObserver() {

        ownerBuildingListingViewModel.getOwnerBuildingListResponse().observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgressOwner()

                    if (it.data!!.data != null) {
                        buildingApartments.clear()

                        if (it.data.data.building_apartments != null) {
                            buildingApartments.addAll(it.data.data.building_apartments)
                        }

                        buildingImages.clear()
                        if (it.data.data.documents != null) {
                            buildingImages.addAll(it.data.data.documents)
                        }


                        if (buildingImages.size != 0) {
                            setUpImageSlider(buildingImages)
                        }

                        if (buildingApartments.size != 0) {
                            setUpBuildingRecyclerView(buildingApartments)
                        } else {
                            expand_collapse_button.visibility = View.GONE
                            noData.visibility = View.VISIBLE
                        }

                        tvBuildingName.text = it.data.data.property_name
                        tvBuildingLocation.text = it.data.data.city_rel.name
                        tvNoOfApartment.text = it.data.data.appartments_count.toString()
                        tvOccupied.text = it.data.data.occupied.toString()
                        tvVacant.text = it.data.data.vacated.toString()
                        tvNetWorth.text = it.data.data.net_worth
                        tvIncome.text = it.data.data.income
                        tvOutStandingDue.text = it.data.data.outstanding_due
                        tvPending.text = it.data.data.pending

                        typeId = it.data.data.type_id

                        /* status-> 0 */ /* contract_owner-> 0 */
                        /* then hide add apartment button */
                        if (it.data.data.status == 0 && it.data.data.contract_owner == "0") {
                            add_apartment_button.visibility = View.VISIBLE
                        } else {
                            add_apartment_button.visibility = View.GONE
                        }

                        /* if owner_confirmation -> 1, then hide add apartment button */
                        if (it.data.data.owner_confirmation == 0) {
                            revenueLayout.visibility = View.GONE
                        } else {
                            revenueLayout.visibility = View.VISIBLE
                            requestBtn.visibility = View.GONE
                            add_apartment_button.visibility = View.GONE
                        }

                        content.visibility = View.VISIBLE
                    }

                }
                Status.LOADING -> {
                    showProgressOwner()
                }
                Status.ERROR -> {
                    dismissProgressOwner()
                    Toaster.showToast(this, it.message!!, Toaster.State.ERROR, Toast.LENGTH_LONG)
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
            }
        })

        /* request for verification */
        ownerBuildingListingViewModel.getOwnerBuildingForVerificationResponse().observe(this, {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgressOwner()
                    Toaster.showToast(
                        this,
                        it.data!!.response,
                        Toaster.State.SUCCESS,
                        Toast.LENGTH_LONG
                    )

                    initData()
                }
                Status.LOADING -> {
                    showProgressOwner()
                }
                Status.DATA_EMPTY -> {
                    dismissProgressOwner()
                    Toaster.showToast(this, it.message!!, Toaster.State.ERROR, Toast.LENGTH_LONG)
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
            }
        })
    }

    /* set up individual building recyclerview */
    private fun setUpBuildingRecyclerView(buildingApartments: ArrayList<OwnerBuildingDetailsListBuildingApartment>) {
        val gridLayoutManager = GridLayoutManager(this, 2)
        gridLayoutManager.orientation = GridLayoutManager.VERTICAL
        fragment_propert_details_recycerview.layoutManager = gridLayoutManager
        fragment_propert_details_recycerview.adapter =
            OwnerDetailedImagesAdapter(this, buildingApartments)
    }

    /* for building image slider */
    private fun setUpImageSlider(buildingImages: ArrayList<OwnerBuildingDetailsListDocument>) {
        val vpPropertyImageSliderAdapter =
            OwnerPropertyImageSliderAdapter(supportFragmentManager, lifecycle, buildingImages)
        vpPropertyDetailsImageSlider.adapter = vpPropertyImageSliderAdapter
        dotsIndicatorDetails.setViewPager2(vpPropertyDetailsImageSlider)
    }

    override fun onClicks() {

        /* expand and collapse btn */
        expand_collapse_button.setOnClickListener {
            collapseExpandWithAnimation()
        }

        /* add apartment btn */
        add_apartment_button.setOnClickListener {
            val intent = Intent(this, AddApartmentPage1Activity::class.java)
            intent.putExtra("selectedId", buildingId)
            intent.putExtra("typeId", typeId)
            startActivity(intent)
        }

        /* request for verification button */
        requestBtn.setOnClickListener {
            setUpDialog()
        }
    }

    /* confirm dialog for verification */
    private fun setUpDialog() {
        val dialog = Dialog(this)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.layout_confirm_verification)

        val yesBtn = dialog.findViewById(R.id.confirmBtnOwner) as MaterialButton
        val noBtn = dialog.findViewById(R.id.cancelBtnOwner) as MaterialButton

        yesBtn.setOnClickListener {
            ownerBuildingListingViewModel.buildingForVerification(buildingId.toString())
            dialog.dismiss()
        }

        noBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()

        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window?.attributes)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window?.attributes = layoutParams
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun collapseExpandWithAnimation() {
        if (fragment_propert_details_ExpandableLayout.state == State.Collapsed) {
            Log.e("TAG", "collapseExpandWithAnimation: Collapsed")
            fragment_propert_details_ExpandableLayout.toggle()
            expand_collapse_button.text = getString(R.string.show_less)
            expand_collapse_button.icon = resources.getDrawable(R.drawable.ic_arrow_drop_up, null)
        } else if (fragment_propert_details_ExpandableLayout.state == State.Expanded) {
            Log.e("TAG", "collapseExpandWithAnimation: Expanded")
            fragment_propert_details_ExpandableLayout.toggle()
            expand_collapse_button.text = getString(R.string.show_more)
            expand_collapse_button.icon =
                resources.getDrawable(R.drawable.ic_arrow_drop_down, null)
        } else if (fragment_propert_details_ExpandableLayout.state == State.Statical) {
            Log.e("TAG", "collapseExpandWithAnimation: Statical")
            fragment_propert_details_ExpandableLayout.collapse(true, true)
            fragment_propert_details_ExpandableLayout.toggle()
            expand_collapse_button.text = getString(R.string.show_more)
            expand_collapse_button.icon = resources.getDrawable(R.drawable.ic_arrow_drop_down, null)
        }
    }

}