package com.property.propertyagent.agent_panel.ui.main.sidemenu.property.building_view_details

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.property.propertyagent.R
import com.property.propertyagent.agent_panel.ui.main.sidemenu.property.building_view_details.adapter.AgentBuilderApartmentAdapter
import com.property.propertyagent.agent_panel.ui.main.sidemenu.property.propertyviewdetails.adapter.AgentPropertyImageSliderAdapter
import com.property.propertyagent.base.BaseActivity
import com.property.propertyagent.dialogs.InternetDialogFragmentCopy
import com.property.propertyagent.modal.agent.agent_builder_details.Unit
import com.property.propertyagent.utils.AppPreferences.clicked_building_id
import com.property.propertyagent.utils.Status
import com.property.propertyagent.utils.Toaster
import com.property.propertyagent.utils.isConnected
import kotlinx.android.synthetic.main.activity_building_view_details.*
import kotlinx.android.synthetic.main.content_building_details.*
import ru.rhanza.constraintexpandablelayout.State

class BuildingViewDetailsActivity : BaseActivity() {

    private lateinit var buildingDetailsViewModel: BuildingDetailsViewModel

    private var buildingApartments = ArrayList<Unit>()

    private var passedPropertyId = ""

    override val layoutId: Int
        get() = R.layout.activity_building_view_details
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
        toolbar_agent.title = getString(R.string.details)

        passedPropertyId = intent.getStringExtra("property_id").toString()
        clicked_building_id = passedPropertyId
        buildingDetailsViewModel.fetchAgentBuilderDetails(passedPropertyId)

    }

    override fun setupUI() {

        app_bar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            when {
                //  State Expanded
                verticalOffset == 0 -> {
                    supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_home_back_button)
                    toolbar_agent.title = ""
                }
                //  State Collapsed
                kotlin.math.abs(verticalOffset) >= appBarLayout.totalScrollRange -> {
                    supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_white)
                    toolbar_agent.title = getString(R.string.building_details)
                }
            }
        })
    }

    override fun setupViewModel() {
        buildingDetailsViewModel = BuildingDetailsViewModel()
    }

    override fun setupObserver() {

        buildingDetailsViewModel.getAgentBuilderDetailsResponse().observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    dismissProgress()
                    if (!it.data!!.response.equals(null)) {
                        buildingApartments.clear()

                        if (!it.data.response.units.equals(null)) {
                            buildingApartments.addAll(it.data.response.units)
                        }

                        if (!it.data.response.building_details.documents.isNullOrEmpty()) {
                            setUpImageSlider(it.data.response.building_details.documents)
                        }

                        if (buildingApartments.size != 0) {
                            setUpApartmentRecyclerView()
                        } else {
                            expand_collapse_button.visibility = View.GONE
                            noData.visibility = View.VISIBLE
                        }

                        tvPropertyId.text = it.data.response.building_details.property_reg_no
                        tvBuildingName.text = it.data.response.building_details.property_name
                        tvBuildingLocation.text = it.data.response.building_details.city_rel.name
                        tvNoOfApartment.text = it.data.response.building_details.appartments_count
                        tvOccupied.text = it.data.response.building_details.occupied_count
                        tvVacant.text = it.data.response.building_details.vacated_count

                        if (it.data.response.building_details.property_to == 0) {
                            tvPropertyType.text = getString(R.string.appartment_for_rent)
                        } else {
                            tvPropertyType.text = getString(R.string.appartment_for_sale)
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
        }
    }

    private fun setUpApartmentRecyclerView() {
        val gridLayoutManager = GridLayoutManager(this, 2)
        gridLayoutManager.orientation = GridLayoutManager.VERTICAL
        fragment_propert_details_recycerview.layoutManager = gridLayoutManager
        fragment_propert_details_recycerview.adapter =
            AgentBuilderApartmentAdapter(buildingApartments)
    }

    private fun setUpImageSlider(documents: List<com.property.propertyagent.modal.agent.agent_assigned_property_details.Document>) {
        val vpPropertyImageSliderAdapter =
            AgentPropertyImageSliderAdapter(supportFragmentManager, lifecycle, documents)
        vpPropertyDetailsImageSlider.adapter = vpPropertyImageSliderAdapter
        dotsIndicatorDetails.setViewPager2(vpPropertyDetailsImageSlider)
    }

    override fun onClicks() {
        expand_collapse_button.setOnClickListener {
            collapseExpandWithAnimation()
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun collapseExpandWithAnimation() {
        when (fragment_propert_details_ExpandableLayout.state) {
            State.Collapsed -> {
                Log.e("TAG", "collapseExpandWithAnimation: Collapsed")
                fragment_propert_details_ExpandableLayout.toggle()
                expand_collapse_button.text = getString(R.string.show_less)
                expand_collapse_button.icon =
                    resources.getDrawable(R.drawable.ic_arrow_drop_up, null)
            }
            State.Expanded -> {
                Log.e("TAG", "collapseExpandWithAnimation: Expanded")
                fragment_propert_details_ExpandableLayout.toggle()
                expand_collapse_button.text = getString(R.string.show_more)
                expand_collapse_button.icon =
                    resources.getDrawable(R.drawable.ic_arrow_drop_down, null)
            }
            State.Statical -> {
                Log.e("TAG", "collapseExpandWithAnimation: Statical")
                fragment_propert_details_ExpandableLayout.collapse(
                    withAnimation = true,
                    forced = true
                )
                fragment_propert_details_ExpandableLayout.toggle()
                expand_collapse_button.text = getString(R.string.show_more)
                expand_collapse_button.icon =
                    resources.getDrawable(R.drawable.ic_arrow_drop_down, null)
            }
            State.Collapsing -> {

            }
            State.Expanding -> {

            }
        }
    }
}