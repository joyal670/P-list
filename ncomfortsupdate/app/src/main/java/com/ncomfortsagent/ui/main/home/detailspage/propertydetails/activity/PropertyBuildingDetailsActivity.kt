package com.ncomfortsagent.ui.main.home.detailspage.propertydetails.activity

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.ncomfortsagent.R
import com.ncomfortsagent.base.BaseActivity
import com.ncomfortsagent.databinding.ActivityPropertyBuildingDetailsBinding
import com.ncomfortsagent.dialog.InternetDialogFragment
import com.ncomfortsagent.model.building_details.AgentBuildingDetailsDocument
import com.ncomfortsagent.model.property_details.AgentPropertyDetailsAmenityCategory
import com.ncomfortsagent.ui.main.home.detailspage.propertydetails.adapters.BuildingDetailsSubAdapter
import com.ncomfortsagent.ui.main.home.detailspage.propertydetails.adapters.BuildingImageSliderAdapter
import com.ncomfortsagent.ui.main.home.detailspage.propertydetails.viewmodel.BuildingDetailsViewModel
import com.ncomfortsagent.utils.CommonUtils.Companion.showCookieBar
import com.ncomfortsagent.utils.Constants.PROPERTY_ID
import com.ncomfortsagent.utils.Status
import com.ncomfortsagent.utils.isConnected

class PropertyBuildingDetailsActivity : BaseActivity<ActivityPropertyBuildingDetailsBinding>() {

    private var selectedPropertyId: Int? = null
    private lateinit var buildingDetailsViewModel: BuildingDetailsViewModel
    private var propertyImages = ArrayList<AgentBuildingDetailsDocument>()

    //private var amenitiesData = ArrayList<Int>()
    private var amenitiesCategory = ArrayList<AgentPropertyDetailsAmenityCategory>()
    private var propertyShareImage = ""


    override val layoutId: Int
        get() = R.layout.activity_property_details
    override val hideStatusBar: Boolean
        get() = false

    override fun getViewBinging(): ActivityPropertyBuildingDetailsBinding =
        ActivityPropertyBuildingDetailsBinding.inflate(layoutInflater)

    override fun initData() {

        /* setup toolbar */
        setSupportActionBar(binding.tool.toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_left)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        binding.tool.tvToolbarTitle.text = getString(R.string.building_details)

        /* get id's from intent */
        selectedPropertyId = intent.getIntExtra(PROPERTY_ID, 0)

        buildingDetailsViewModel.buildingDetails(selectedPropertyId!!)

    }

    override fun fragmentLaunch() {

    }

    override fun setupUI() {

        /* setup recyclerview */
        val gridLayoutManager = GridLayoutManager(this, 3)
        gridLayoutManager.orientation = GridLayoutManager.VERTICAL
        binding.content.rvDetailedBuildings.layoutManager = gridLayoutManager
        //binding.content.rvDetailedBuildings.addItemDecoration(DividerItemDecoration(this, GridLayoutManager.HORIZONTAL))

    }

    override fun setupViewModel() {
        buildingDetailsViewModel = BuildingDetailsViewModel(this)
    }

    @SuppressLint("SetTextI18n")
    override fun setupObserver() {

        buildingDetailsViewModel.getAgentBuildingDetailsResponse().observe(this, {
            when (it.status) {
                Status.SUCCESS -> {

                    /* property images */
                    propertyImages.clear()
                    propertyImages.addAll(it.data!!.data.documents)

                    setUpPropertyImages()

                    binding.content.tvPropertyName.text = it.data.data.property_name
                    binding.content.tvPropertyCode.text =
                        it.data.data.property_reg_no

                    /* property_to value
                    * 0 -> RENT
                    * 1 -> BUY */
                    if (it.data.data.property_to == 0) {
                        binding.content.tvPropertyType.text =
                            it.data.data.type_details.type + " " + getString(R.string.for1) + " " + getString(
                                R.string.rent
                            )
                    } else {
                        binding.content.tvPropertyType.text =
                            it.data.data.type_details.type + " " + getString(R.string.for1) + " " + getString(
                                R.string.sale
                            )
                    }

                    binding.content.tvLocation.text = it.data.data.city_rel.name
                    binding.content.tvNoOfApartments.text =
                        getString(R.string.noOfApartment) + " " + "(" + it.data.data.appartments_count.toString() + ")"
                    binding.content.tvOccupied.text =
                        it.data.data.occupied_count.toString() + " " + getString(R.string.occupied)
                    binding.content.tvVacant.text =
                        it.data.data.vacated_count.toString() + " " + getString(R.string.vacant)

                    binding.content.rvDetailedBuildings.adapter = BuildingDetailsSubAdapter(this, it.data.data.building_apartments)
                    if(it.data.data.building_apartments.isEmpty()){
                        binding.content.rvDetailedBuildings.visibility = View.GONE
                        binding.content.noData.noData.visibility = View.VISIBLE
                    }else{
                        binding.content.rvDetailedBuildings.visibility = View.VISIBLE
                        binding.content.noData.noData.visibility = View.GONE
                    }


                    binding.content.tvAmenities.text = it.data.data.type_details.type
                    binding.content.tvAmenities.paintFlags = binding.content.tvAmenities.paintFlags or Paint.UNDERLINE_TEXT_FLAG

                    binding.shimmerLayout.stopShimmer()
                    binding.shimmerLayout.visibility = View.GONE
                    binding.content.lv.visibility = View.VISIBLE
                }
                Status.LOADING -> {
                    binding.shimmerLayout.startShimmer()
                }
                Status.NO_INTERNET -> {
                    binding.shimmerLayout.stopShimmer()
                    if (this.isConnected) {
                        showCookieBar(
                            this,
                            getString(R.string.error),
                            getString(R.string.something_wrong),
                            R.color.pomegranate
                        )

                    } else {
                        val dialog = InternetDialogFragment(this)
                        dialog.show(supportFragmentManager, "TAG")
                    }
                }
                Status.ERROR -> {
                    binding.shimmerLayout.stopShimmer()
                    showCookieBar(
                        this,
                        getString(R.string.error),
                        it.message.toString(),
                        R.color.pomegranate
                    )
                }
                Status.DATA_EMPTY -> {
                    binding.shimmerLayout.stopShimmer()
                    showCookieBar(
                        this,
                        getString(R.string.error),
                        getString(R.string.internal_server_error),
                        R.color.pomegranate
                    )
                }
            }
        })
    }

    /* property images viewpager */
    private fun setUpPropertyImages() {
        val vpPropertyImageSliderAdapter =
            BuildingImageSliderAdapter(supportFragmentManager, lifecycle, propertyImages)
        binding.vpPropertyDetailsImageSlider.adapter = vpPropertyImageSliderAdapter
        binding.dotsIndicator.setViewPager2(binding.vpPropertyDetailsImageSlider)
    }

    override fun onClicks() {
    }

}