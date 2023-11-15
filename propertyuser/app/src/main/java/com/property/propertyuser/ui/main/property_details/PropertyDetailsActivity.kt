package com.property.propertyuser.ui.main.property_details

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.view.ViewCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.extension.send
import com.google.android.material.appbar.AppBarLayout
import com.google.gson.Gson
import com.property.propertyuser.R
import com.property.propertyuser.base.BaseActivity
import com.property.propertyuser.databinding.ActivityPropertyDetailsBinding
import com.property.propertyuser.dialogs.book_a_tour.BookATourDateAndTimeDialogFragment
import com.property.propertyuser.listeners.ActivityListener
import com.property.propertyuser.modal.property_details.*
import com.property.propertyuser.preference.AppPreferences
import com.property.propertyuser.ui.main.booking.BookingActivity
import com.property.propertyuser.ui.main.map_and_nearby.MapAndNearByActivity
import com.property.propertyuser.ui.main.property_details.adapter.*
import com.property.propertyuser.ui.main.property_details.packages2.PackagesActivity
import com.property.propertyuser.ui.main.property_details.slide_images.PropertyImgAdapter
import com.property.propertyuser.ui.main.sale_and_rent_details.SaleAndRentActivity
import com.property.propertyuser.ui.main.terms_of_stay.TermsofStayActivity
import com.property.propertyuser.utils.*
import com.property.propertyuser.utils.Constants.INTENT_PROPERTY_ID
import kotlinx.android.synthetic.main.activity_property_details.*
import kotlinx.android.synthetic.main.item_property_details_content_scrolling.*
import kotlinx.android.synthetic.main.layout_no_network.*
import kotlinx.android.synthetic.main.toolbar_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.rhanza.constraintexpandablelayout.State
import java.util.*
import kotlin.math.abs


class PropertyDetailsActivity : BaseActivity<ActivityPropertyDetailsBinding>(), ActivityListener {
    override fun getViewBinging(): ActivityPropertyDetailsBinding =
        ActivityPropertyDetailsBinding.inflate(layoutInflater)

    private val bannerImages = ArrayList<Int>()
    private val floorPlanImages = ArrayList<Int>()
    private val similarProductsImage = ArrayList<Int>()
    private lateinit var propertyType: String
    private lateinit var newFragment: DialogFragment
    private lateinit var propertyDetailViewModel: PropertyDetailViewModel
    private var propertyId:Int = 0
    private var agentPhoneNumber=""
    private var propertyName=""
    private var isFeatured=false
    private var favoritePropertyIdList=ArrayList<Int>()
    private var passingLat=""
    private var passingLng=""
    private var downloadPropertyDetails=""
    private var selectedPackageAmount=""
    private var selectedPackageID=""
    private var selectedPackageName=""
    private var drawableTest: Drawable?=null
    private var shareImage=""
    private var passedType=""
    private var amenityDetails = ArrayList<AmenityCategory>()
    private lateinit var adapter : PropertyImgAdapter


    override val layoutId: Int
        get() = R.layout.activity_property_details
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = false

    override fun fragmentLaunch() {
    }
    override fun initData() {
        favoritePropertyIdList=ArrayList()
        setSupportActionBar(toolbarCollapsing)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_toolbar_back)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbarCollapsing.title = propertyName
        //propertyId = intent.getIntExtra(INTENT_PROPERTY_ID,0)
        passedType=intent.getStringExtra("my_property_type").toString()
        Log.e("passedType",passedType)
        val appLinkIntent = intent
        val appLinkData = appLinkIntent.data
        val LINK = appLinkData.toString()
        if (LINK != "null") {
            val parts = LINK.split("/").toTypedArray()
            propertyId = parts[4].toInt()
        } else {
            propertyId = intent.getIntExtra(INTENT_PROPERTY_ID, 0)
            Log.e("property id details",propertyId.toString())
        }
        propertyDetailViewModel.getPropertyDetail(propertyId)
        tvPropertyTypeDetailed.text = ""

        for (i in 0..5) {
            bannerImages.add(R.drawable.building_2)
            floorPlanImages.add(R.drawable.floor_plan)
            similarProductsImage.add(R.drawable.event)
        }
        app_bar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            when {
                //  State Expanded
                verticalOffset == 0 -> {
                    supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_circle_back_arrow)
                    toolbarCollapsing.title = ""
                    if(isFeatured){
                        ivGreenTagDetails.visibility = View.VISIBLE
                    }
                }
                //  State Collapsed
                abs(verticalOffset) >= appBarLayout.totalScrollRange -> {
                    supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_toolbar_back)
                    toolbarCollapsing.title = propertyName
                    ivGreenTagDetails.visibility = View.GONE

                }
                else -> Log.e("onelse", "toolbar")//  Do anything for Ideal State

            }
        })
    }

    override fun onStart() {
        super.onStart()

        //setPropertyDetailsFeaturesRecyclerView()
        //setPropertyDetailsBuildingsRecyclerView()
        //setPropertyDetailsHealthFitnessRecyclerView()


        if (AppPreferences.chooseLanguage == "arabic") {
            ivArrowRight.rotation = 180.0f
        }else{
            ivArrowRight.rotation = 0f
        }

    }

    private fun showDateAndTimeDialog(it: String) {
        Log.e("check id",it.toString())
        var isLargeLayout = resources.getBoolean(R.bool.large_layout)
        val fragmentManager =supportFragmentManager
        BookATourDateAndTimeDialogFragment.newInstance(it.toString())
        newFragment = BookATourDateAndTimeDialogFragment(this)
        if (!isLargeLayout) {
            newFragment.show(fragmentManager, "dialog")
        } else {
            val transaction = fragmentManager.beginTransaction()
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            transaction
                .add(android.R.id.content, newFragment)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun setPropertyImageSlider(documents: List<Document>) {
        val vpPropertyImageSliderAdapter =
            PropertyImageSliderAdapter(supportFragmentManager, lifecycle, documents)
        vpPropertyDetailsImageSlider.adapter = vpPropertyImageSliderAdapter
        dotsIndicatorDetails.setViewPager2(vpPropertyDetailsImageSlider)

        rvDot.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        adapter = PropertyImgAdapter(this@PropertyDetailsActivity, documents, 0 )
        rvDot.adapter = adapter

        vpPropertyDetailsImageSlider.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                adapter = PropertyImgAdapter(this@PropertyDetailsActivity, documents, position )
                rvDot.adapter = adapter
            }
        })


        /*dotsIndicatorDetails.apply {
            setSliderColor(
                ContextCompat.getColor(this@PropertyDetailsActivity,R.color.lightGray),
                ContextCompat.getColor(this@PropertyDetailsActivity,R.color.green_solid))
            setSliderWidth(resources.getDimension(R.dimen.margin_10dp))
            setSliderHeight(resources.getDimension(R.dimen.margin_10dp))
            setSlideMode(IndicatorSlideMode.WORM)
            setIndicatorStyle(IndicatorStyle.CIRCLE)
            setupWithViewPager(vpPropertyDetailsImageSlider)

        }*/
        vpPropertyDetailsImageSlider.setPageTransformer(ZoomOutPageTransformer())


      /*  var currentPage = 0
        val handler = Handler()
        val update = Runnable {
            vpPropertyDetailsImageSlider.setCurrentItem(currentPage % documents.size, true)
            currentPage++
        }
        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(update)
            }
        }, 1000, 3000)*/


        vpPropertyDetailsImageSlider.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                Log.e("Selected_Page", position.toString())
                /* if(position.equals(1)){
                     ivDotVideo1.visibility=View.GONE
                     ivDotVideo2.visibility=View.VISIBLE
                 }
                 else{
                     ivDotVideo1.visibility=View.VISIBLE
                     ivDotVideo2.visibility=View.GONE
                 }*/

            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
            }
        })
    }

    private fun setPropertyFloorPlanImageSlider(floorPlans: List<FloorPlan>) {
        val vpPropertyFloorPlanImageSliderAdapter =
            PropertyFloorPlanImageSlider2Adapter(this,floorPlans)
        vpPropertyDetailsFloorImageSlider.adapter = vpPropertyFloorPlanImageSliderAdapter
        dotsIndicatorFloor.setViewPager2(vpPropertyDetailsFloorImageSlider)
    }

    private fun setSimilarProductsListSlider(similarProperties: List<SimilarProperty>) {
        //val vpSimilarProductsPropertyAdapter=SimilarProductsImageSliderAdapter(supportFragmentManager,lifecycle,floorPlanImages)
        //vpSimilarItemSlider.adapter=vpSimilarProductsPropertyAdapter

        vpSimilarItemSlider.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        vpSimilarItemSlider.adapter = SimilarProductsImageSlider2Adapter(this, similarProperties,
            {functionInfoSimilar(it)}, { functionMapSimilar(it) },
            { functionBookSimilar(it) },
            { id, pos -> functionAddToFavSimilar(id,pos) },
            {id1,pos1->functionRemoveFavSimilar(id1,pos1)})
        vpSimilarItemSlider.offscreenPageLimit = 3

        val pageMargin =
            resources.getDimensionPixelOffset(R.dimen.pageMargin1).toFloat()
        val pageOffset =
            resources.getDimensionPixelOffset(R.dimen.offset1).toFloat()
        vpSimilarItemSlider.setPageTransformer { page, position ->
            val myOffset = position * -(3 * pageOffset + pageMargin)
            if (vpSimilarItemSlider.orientation == ViewPager2.ORIENTATION_HORIZONTAL) {
                if (ViewCompat.getLayoutDirection(vpSimilarItemSlider) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                    page.translationX = -myOffset
                } else {
                    page.translationX = myOffset
                }
            } else {
                page.translationY = myOffset
            }
        }
    }

    private fun functionRemoveFavSimilar(remFav: Int, pos1: Int) {
        if(favoritePropertyIdList.contains(remFav)) {
            favoritePropertyIdList.remove(remFav)
        }
        propertyDetailViewModel.updateFavoriteProperty(remFav)
    }

    private fun functionAddToFavSimilar(addFav: Int, pos: Int) {
        if(favoritePropertyIdList.contains(addFav)){
            favoritePropertyIdList.remove(addFav)
        }else{
            favoritePropertyIdList.add(addFav)
        }
        propertyDetailViewModel.updateFavoriteProperty(addFav)
    }

    private fun functionBookSimilar(it: Int) {
        showDateAndTimeDialog(it)
    }
    private fun showDateAndTimeDialog(it: Int) {
        var isLargeLayout = resources.getBoolean(R.bool.large_layout)
        val fragmentManager =supportFragmentManager
        BookATourDateAndTimeDialogFragment.newInstance(it.toString())
        newFragment = BookATourDateAndTimeDialogFragment(this)
        if (!isLargeLayout) {
            newFragment.show(fragmentManager, "dialog")
        } else {
            val transaction = fragmentManager.beginTransaction()
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            transaction
                .add(android.R.id.content, newFragment)
                .addToBackStack(null)
                .commit()
        }
    }
    private fun functionMapSimilar(it: Int) {
        val intent=Intent(this, MapAndNearByActivity::class.java)
        intent.putExtra("property_id",it.toString())
        intent.putExtra("from_type","home_property_list")
        startActivity(intent)
    }

    private fun functionInfoSimilar(propertyId: Int) {
        val intent=Intent(this,PropertyDetailsActivity::class.java)
        intent.putExtra(Constants.INTENT_PROPERTY_ID,propertyId)
        startActivity(intent)
    }

    private fun collapseExpandWithAnimation() {
        if (expandableDescription.state == State.Collapsed) {
            expandableDescription.toggle()
            ivCircularDown.rotation = 180f
        } else if (expandableDescription.state == State.Expanded) {
            expandableDescription.toggle()
            ivCircularDown.rotation = 0f
        }
    }


    private fun setPropertyDetailsFeaturesRecyclerView(amenityDetails: List<AmenityDetail>) {
        rvPropertyDetailsFeaturedList.layoutManager =
            GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false)
        rvPropertyDetailsFeaturedList.adapter = PropertyDetailsFeaturesAdapter(this,amenityDetails)
    }

    private fun setPropertyDetailsBuildingsRecyclerView(amenityDetails: List<AmenityDetail>) {
        rvPropertyDetailsBuildingList.layoutManager =
            GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false)
        rvPropertyDetailsBuildingList.adapter = PropertyDetailsBuildingAdapter(this,amenityDetails)
    }

    private fun setPropertyDetailsHealthFitnessRecyclerView(amenityDetails: List<AmenityDetail>) {
        rvPropertyDetailsHealthList.layoutManager =
            GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false)
        rvPropertyDetailsHealthList.adapter = PropertyDetailsHealthAndFitnessAdapter(this,amenityDetails)
    }

    override fun setupUI() {
    }

    override fun setupViewModel() {
        propertyDetailViewModel = PropertyDetailViewModel()

    }

    override fun setupObserver() {
        propertyDetailViewModel.getPropertyDetailLiveData().observe(this, Observer {
            when (it.status) {
                Status.LOADING -> showLoader()
                Status.SUCCESS -> {
                    dismissLoader()
                    Log.e("onresponse", Gson().toJson(it))
                    if(it.data!=null){
                        if(it.data.property_data!=null){
                            if(it.data.property_data.property_details!=null){
                                setSupportActionBar(toolbarCollapsing)
                                supportActionBar!!.setDisplayShowTitleEnabled(false)
                                supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_toolbar_back)
                                supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                                coordinatorLayoutPropertyDetails.visibility=View.VISIBLE
                                linearNodataNoInternet.visibility=View.GONE
                                includeNoInternet.visibility=View.GONE
                                linearNoDataFound.visibility=View.GONE
                                app_bar.visibility=View.VISIBLE
                                includeContentScrolling.visibility=View.VISIBLE
                                if(passedType=="scheduled"){
                                    vpSimilarItemSlider.setMargins(0,20,0,20)
                                    linearTest.visibility=View.GONE
                                }
                                else{
                                    linearTest.visibility=View.VISIBLE
                                }
                                setData(it.data!!.property_data)
                            }
                            else{
                                coordinatorLayoutPropertyDetails.visibility=View.VISIBLE
                                linearNodataNoInternet.visibility=View.VISIBLE
                                includeNoInternet.visibility=View.GONE
                                linearNoDataFound.visibility=View.VISIBLE
                                app_bar.visibility=View.GONE
                                includeContentScrolling.visibility=View.GONE
                                linearTest.visibility=View.GONE
                            }
                        }
                        else{
                            setSupportActionBar(toolbar)
                            supportActionBar!!.setDisplayShowTitleEnabled(false)
                            supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_toolbar_back)
                            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                            coordinatorLayoutPropertyDetails.visibility=View.VISIBLE
                            linearNodataNoInternet.visibility=View.VISIBLE
                            includeNoInternet.visibility=View.GONE
                            linearNoDataFound.visibility=View.VISIBLE
                            app_bar.visibility=View.GONE
                            includeContentScrolling.visibility=View.GONE
                            linearTest.visibility=View.GONE
                        }
                    }
                }
                Status.DATA_EMPTY -> {
                    dismissLoader()
                    setSupportActionBar(toolbar)
                    supportActionBar!!.setDisplayShowTitleEnabled(false)
                    supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_toolbar_back)
                    supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                    coordinatorLayoutPropertyDetails.visibility=View.VISIBLE
                    linearNodataNoInternet.visibility=View.VISIBLE
                    includeNoInternet.visibility=View.GONE
                    linearNoDataFound.visibility=View.VISIBLE
                    app_bar.visibility=View.GONE
                    includeContentScrolling.visibility=View.GONE
                    linearTest.visibility=View.GONE
                }
                Status.NO_INTERNET -> {
                    dismissLoader()
                    coordinatorLayoutPropertyDetails.visibility=View.VISIBLE
                    includeContentScrolling.visibility=View.GONE
                    if (this.isConnected) {
                        Toaster.showToast(
                            this, getString(R.string.something_wrong),
                            Toaster.State.ERROR, Toast.LENGTH_LONG
                        )
                    } else {
                        setSupportActionBar(toolbar)
                        supportActionBar!!.setDisplayShowTitleEnabled(false)
                        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_toolbar_back)
                        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
                        linearNodataNoInternet.visibility=View.VISIBLE
                        includeNoInternet.visibility=View.VISIBLE
                        linearNoDataFound.visibility=View.GONE
                        app_bar.visibility=View.GONE
                        includeContentScrolling.visibility=View.GONE
                        linearTest.visibility=View.GONE
                    }
                }

            }
        })
        propertyDetailViewModel.updateFavoritePropertyResponse().observe(this, Observer {
            when(it.status){
                Status.LOADING->showLoader()
                Status.SUCCESS->{
                    dismissLoader()
                    Log.e("updateFav", Gson().toJson(it))
                    AppPreferences.reload_property_list=true
                    coordinatorLayoutPropertyDetails.snack(it.data!!.response)
                }
                Status.DATA_EMPTY->{
                    dismissLoader()
                    Toaster.showToast(this,it.data!!.response, Toaster.State.ERROR,Toast.LENGTH_LONG)
                }
                Status.NO_INTERNET->{
                    dismissLoader()
                    if(this.isConnected){
                        Toaster.showToast(this,getString(R.string.something_wrong),
                            Toaster.State.ERROR,Toast.LENGTH_LONG)
                    }else{
                        Toaster.showToast(this,getString(R.string.no_internet),
                            Toaster.State.ERROR,Toast.LENGTH_LONG)
                    }
                }

            }
        })
        propertyDetailViewModel.getPropertyDetailsPdfLiveData().observe(this, Observer {
            when(it.status){
                Status.LOADING->showLoader()
                Status.SUCCESS->{
                    dismissLoader()
                    Log.e("updateFav", Gson().toJson(it))
                    if(it.data!=null){
                        if(it.data.data!=null){
                            if (it.data.data.pdf.isNotEmpty()){
                                downloadPropertyDetails=it.data.data.pdf
                                val builder = CustomTabsIntent.Builder()
                                val colorInt : Int = Color.parseColor("#009639")
                                builder.setToolbarColor(colorInt)
                                val customTabsIntent = builder.build()
                                customTabsIntent.launchUrl(this , Uri.parse(downloadPropertyDetails))
                            }
                        }
                    }
                }
                Status.ERROR->{
                    dismissLoader()
                    Toaster.showToast(this,getString(R.string.internal_server_error), Toaster.State.ERROR,Toast.LENGTH_LONG)
                }
                Status.DATA_EMPTY->{
                    dismissLoader()
                    Toaster.showToast(this,getString(R.string.data_empty), Toaster.State.ERROR,Toast.LENGTH_LONG)
                }
                Status.NO_INTERNET->{
                    dismissLoader()
                    if(this.isConnected){
                        Toaster.showToast(this,getString(R.string.something_wrong),
                            Toaster.State.ERROR,Toast.LENGTH_LONG)
                    }else{
                        Toaster.showToast(this,getString(R.string.no_internet),
                            Toaster.State.ERROR,Toast.LENGTH_LONG)
                    }
                }

            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
    private fun setData(propertyData: PropertyData) {
        var allNonNullValues = listOfNotNull(propertyData.property_details.property_reg_no).any { it != null }
        if (allNonNullValues) {
            Log.e("alskda",allNonNullValues.toString())
            if(!(propertyData.property_details.documents.isNullOrEmpty())){
                setPropertyImageSlider(propertyData.property_details.documents)
                shareImage=propertyData.property_details.documents[0].document
            }
            GlobalScope.launch(Dispatchers.IO) {
                drawableTest=CommonMethods.
                LoadImageFromWebOperations(shareImage)
            }
            propertyName = propertyData.property_details.property_name
            if(propertyData.property_details.is_featured==1){
                isFeatured=true
            }
            tvPropertyName.text=propertyData.property_details.property_name
            val propertyCode = StringBuilder()
            propertyCode.append(getString(R.string.tvPropertyCode))
            propertyCode.append(" ")
            propertyCode.append(propertyData.property_details.property_reg_no)
            tvPropertyCode.text = propertyCode.toString()

            val sellerPrice = StringBuilder()
            sellerPrice.append("SAR")
            sellerPrice.append(" ")
            if(propertyData.property_details.property_to==0){
                sellerPrice.append(propertyData.property_details.rent)
            }
            else{
                sellerPrice.append(propertyData.property_details.selling_price)
            }
            tvPropertyAmount.text = sellerPrice.toString()

            val mrpPrice = StringBuilder()
            mrpPrice.append("SAR")
            mrpPrice.append(" ")
            if(propertyData.property_details.property_to==0){
                tvPropertyDiscountAmount.visibility=View.GONE
            }
            else{
                mrpPrice.append(propertyData.property_details.mrp)
            }
            tvPropertyDiscountAmount.text = mrpPrice.toString()
            tvPropertyDiscountAmount.paintFlags =
                tvPropertyDiscountAmount.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

            propertyData.property_details.rating?.let {
                Log.e("onData","found")
                ratingbar.rating = propertyData.property_details.rating.toFloat()
                tvRating.text = propertyData.property_details.rating

                val totalRating = StringBuilder()
                totalRating.append(propertyData.property_details.total_rating_count)
                totalRating.append(" ")
                totalRating.append("Rating")
                tvRatingValue.paintFlags or Paint.UNDERLINE_TEXT_FLAG
                tvRatingValue.text = totalRating.toString()
            }

            passingLat=propertyData.property_details.latitude
            passingLng=propertyData.property_details.longitude

            tvPropertyLocation.text = propertyData.property_details.location
            propertyData.property_details.Beds?.let{
                ivIconFeturedBed.visibility=View.VISIBLE
                tvPropertyDetailsFeaturedBed.visibility=View.VISIBLE
                tvPropertyDetailsFeaturedBed.text = it.toString()
            }
            propertyData.property_details.Bathroom?.let {
                ivIconFeturedBath.visibility=View.VISIBLE
                tvPropertyDetailsFeaturedBath.visibility=View.VISIBLE
                tvPropertyDetailsFeaturedBath.text = it.toString()
            }
            propertyData.property_details.area?.let {
                ivIconFeturedArea.visibility=View.VISIBLE
                tvPropertyDetailsFeaturedArea.visibility=View.VISIBLE
                tvPropertyDetailsFeaturedArea.text = "$it sq.M"
            }

            propertyData.property_details.furnished?.let {
                ivIconFeturedSofa.visibility=View.VISIBLE
                tvPropertyDetailsFeaturedSofa.visibility=View.VISIBLE
                Log.e("furnished==",propertyData.property_details.furnished.toString().toDouble().toInt().toString())
                when (propertyData.property_details.furnished.toString().toDouble().toInt()) {
                    1 -> {
                        tvPropertyDetailsFeaturedSofa.text=getString(R.string.semi)
                    }
                    2 -> {
                        tvPropertyDetailsFeaturedSofa.text=getString(R.string.fully)
                    }
                    else -> {
                        tvPropertyDetailsFeaturedSofa.text=getString(R.string.not_furnished)
                    }
                }
            }
            propertyData.property_details.floors?.let {
                ivIconFeturedBlock.visibility=View.VISIBLE
                tvPropertyDetailsFeaturedBlock.visibility=View.VISIBLE
                tvPropertyDetailsFeaturedBlock.text=it.toString()
            }
            propertyData.property_details.assigned_agent_details?.let{
                propertyData.property_details.assigned_agent_details.image?.let {
                    sivProfile.loadImagesWithGlideExt(propertyData.property_details.assigned_agent_details.image)
                }
                propertyData.property_details.assigned_agent_details.name?.let {
                    tvAgentName.text=propertyData.property_details.assigned_agent_details.name
                }?: kotlin.run {
                    linearSubThree.visibility=View.GONE
                }
                propertyData.property_details.assigned_agent_details.phone?.let {
                    agentPhoneNumber=propertyData.property_details.assigned_agent_details.phone
                }
            }
            propertyData.property_details.description?.let{
                if(propertyData.property_details.description.isEmpty()){
                    ivUserDetailIcon.visibility=View.GONE
                    tvLocationDescription.visibility=View.GONE
                    ivCircularDown.visibility=View.GONE
                    expandableDescription.visibility=View.GONE
                    tvDescriptionContentShort.visibility=View.GONE
                }
                val charLength=propertyData.property_details.description.length
                if(charLength>334){
                    ivCircularDown.visibility=View.VISIBLE
                    tvDescriptionContent.visibility=View.VISIBLE
                    tvDescriptionContentShort.visibility=View.GONE
                    tvDescriptionContent.text=propertyData.property_details.description
                }else{
                    ivCircularDown.visibility=View.GONE
                    expandableDescription.visibility=View.GONE
                    tvDescriptionContent.visibility=View.GONE
                    tvDescriptionContentShort.visibility=View.VISIBLE
                    tvDescriptionContentShort.text=propertyData.property_details.description
                }
            }
            propertyData.property_details.property_to?.let{
                if(propertyData.property_details.property_to==0){
                    tvPropertyTypeDetailed.text=getString(R.string.property_for_rent)
                    btnPropertyPricing.visibility = View.GONE
                    btnRentOrSalePrice.visibility = View.VISIBLE
                    btnTermsOfStay.visibility = View.VISIBLE
                    btnOtherPackage.visibility = View.VISIBLE
                } else {
                    tvPropertyTypeDetailed.text=getString(R.string.property_for_sale)
                    //btnPropertyPricing.visibility = View.VISIBLE
                    btnRentOrSalePrice.visibility = View.GONE
                    btnTermsOfStay.visibility = View.GONE
                    btnOtherPackage.visibility = View.GONE
                }
            }
            propertyData.property_details.floor_plans?.let {
                if(it.isNotEmpty()){
                    Log.e("floor",it.size.toString())
                    tvFloorPlans.visibility=View.VISIBLE
                    ivFloorPlan.visibility=View.VISIBLE
                    lineartest.visibility=View.VISIBLE
                    vpPropertyDetailsFloorImageSlider.visibility=View.VISIBLE
                    setPropertyFloorPlanImageSlider(it)
                }
            }

            tvPackageName.text = propertyData.property_details.frequency

            if(!(propertyData.property_details.amenity_categories.isNullOrEmpty())){
                propertyData.property_details.amenity_categories.forEach {
                    amenityDetails.add(it)
                }

                setUpAmenities(amenityDetails)

              /*  for(i in propertyData.property_details.amenity_categories.indices){
                    if(propertyData.property_details.amenity_categories[i].name.equals("Features",true)){
                        if(!(propertyData.property_details.amenity_categories[i].amenity_details.isNullOrEmpty())){
                            tvFeaturesBottom.visibility=View.VISIBLE
                            setPropertyDetailsFeaturesRecyclerView(propertyData.property_details.amenity_categories[i].amenity_details)
                        }
                    }
                    if(propertyData.property_details.amenity_categories[i].name.equals("Building",true)){
                        if(!(propertyData.property_details.amenity_categories[i].amenity_details.isNullOrEmpty())){
                            tvBuildingBottom.visibility=View.VISIBLE
                            setPropertyDetailsBuildingsRecyclerView(propertyData.property_details.amenity_categories[i].amenity_details)
                        }
                    }
                    if(propertyData.property_details.amenity_categories[i].name.equals("Health & Fitness",true)){
                        if(!(propertyData.property_details.amenity_categories[i].amenity_details.isNullOrEmpty())){
                            tvHealthBottom.visibility=View.VISIBLE
                            setPropertyDetailsHealthFitnessRecyclerView(propertyData.property_details.amenity_categories[i].amenity_details)
                        }
                    }
                }*/
            }
        }
        else{
            Toaster.showToast(this@PropertyDetailsActivity, getString(R.string.data_empty),
                Toaster.State.ERROR, Toast.LENGTH_SHORT)
            onBackPressed()
        }

        if(!(propertyData.similar_properties.isNullOrEmpty())){
            ivBarChart.visibility=View.VISIBLE
            tvSimilarProperty.visibility=View.VISIBLE
            setSimilarProductsListSlider(propertyData.similar_properties)
        }
    }

    private fun setUpAmenities(amenityDetails: ArrayList<AmenityCategory>) {
        rvPropertyAmenities.layoutManager = LinearLayoutManager(this)
        rvPropertyAmenities.adapter = PropertyAmenitiesAdapter(this, amenityDetails)
    }

    private fun sharePropertyDetails(content:String,
                                     ivPropertyImage: Drawable,
                                     context: Context) {
        Log.e("checkimage",ivPropertyImage.toString())
        Log.e("checkShareImage",CommonMethods.getLocalBitmapUriShare(ivPropertyImage, context).toString())
        val uriImage=CommonMethods.getLocalBitmapUriShare(ivPropertyImage, context)
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, content)
            putExtra(Intent.EXTRA_STREAM, uriImage)
            type = "image/*"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }
    override fun onClicks() {
        btnTryAgain.setOnClickListener {
            if(this.isConnected){
                coordinatorLayoutPropertyDetails.visibility=View.VISIBLE
                linearNodataNoInternet.visibility=View.GONE
                propertyDetailViewModel.getPropertyDetail(propertyId)
            }
        }
        btnShare.setOnClickListener {
            val stringBuilder = java.lang.StringBuilder()
            stringBuilder!!.append("Property Name :"+tvPropertyName.text)
            stringBuilder!!.append("\nProperty Type"+tvPropertyTypeDetailed.text)
            stringBuilder!!.append("\nhttps://siaaha.com/property/$propertyId")
            Log.e("te",stringBuilder.toString())

            if(drawableTest==null){
                Toast.makeText(this,getString(R.string.loading_content),Toast.LENGTH_SHORT).show()
                Timer().schedule(object : TimerTask() {
                    override fun run() {
                        if(drawableTest!=null){
                            sharePropertyDetails(stringBuilder.toString(),drawableTest!!,this@PropertyDetailsActivity)
                        }
                        else{
                            Toast.makeText(this@PropertyDetailsActivity,getString(R.string.something_wrong),Toast.LENGTH_SHORT).show()
                        }
                    }
                } , 4000)
            }
            else{
                sharePropertyDetails(stringBuilder.toString(),drawableTest!!,this@PropertyDetailsActivity)
            }
        }
        ivCircularDown.setOnClickListener { collapseExpandWithAnimation() }
        tvDescriptionContent.setOnClickListener { collapseExpandWithAnimation() }
        constraintLocationNear.setOnClickListener {
            startActivity(Intent(this, MapAndNearByActivity::class.java))
        }
        btnOtherPackage.setOnClickListener {
            val intent=Intent(this, PackagesActivity::class.java)
            intent.putExtra("property_id",propertyId.toString())
            startActivityForResult(intent, 11)

        }
        btnRentOrSalePrice.setOnClickListener {
            val intent=Intent(this, SaleAndRentActivity::class.java)
            intent.putExtra("property_id",propertyId.toString())
            intent.putExtra("property_to","rent")
            startActivity(intent)
        }
        btnBookATour.setOnClickListener {
            if(tvAgentName.text.trim().toString().isNullOrEmpty()){
                AwesomeDialog.build(this)
                    .title("Agent is not available")
                    .body("This person isn't available right now")
                    .icon(R.drawable.warning, true)
                    .onPositive("Ok") {
                    }

                //Toaster.showToast(this,"Agent is not available", Toaster.State.WARNING,Toast.LENGTH_LONG)
            }else{
                showDateAndTimeDialog(propertyId.toString())
            }

        }
        btnBookAProperty.setOnClickListener {
            if(tvAgentName.text.trim().toString().isNullOrEmpty()){
                AwesomeDialog.build(this)
                    .title("Agent is not available")
                    .body("This person isn't available right now")
                    .icon(R.drawable.warning, true)
                    .onPositive("Ok") {
                    }
            }else{
                val intent = Intent(this, BookingActivity::class.java)
                intent.putExtra(Constants.TYPE_BOOKING, TYPE_BOOKING.PROPERTY.name)
                intent.putExtra("property_id",propertyId.toString())
                intent.putExtra("package_amount",selectedPackageAmount)
                startActivity(intent)
            }
        }
        btnTermsOfStay.setOnClickListener {
            val intent = Intent(this, TermsofStayActivity::class.java)
            intent.putExtra("property_id",propertyId.toString())
            startActivity(intent)
        }
        btnPropertyPricing.setOnClickListener {
            val intent=Intent(this, SaleAndRentActivity::class.java)
            intent.putExtra("property_id",propertyId.toString())
            intent.putExtra("property_to","sale")
            startActivity(intent)
        }
        tvPhone.setOnClickListener {
            permissionsBuilder(Manifest.permission.CALL_PHONE).build().send { result ->
                if (result.allGranted()) {
                    val intent = Intent(Intent.ACTION_DIAL)
                    intent.data = Uri.parse("tel:$agentPhoneNumber")
                    startActivity(intent)
                }
            }
        }
        tvChat.setOnClickListener {
            /*val phoneNumberWithCountryCode = agentPhoneNumber
            val message = "Hallo"
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(
                        String.format(
                            "https://api.whatsapp.com/send?phone=%s&text=%s",
                            phoneNumberWithCountryCode,
                            message
                        )
                    )
                )
            )*/
            /*Log.e("agentnumber",agentPhoneNumber)
            val phoneNumber = "91$agentPhoneNumber"
            val url = "https://api.whatsapp.com/send?phone=$phoneNumber"
            try {
                packageManager.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES)
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                startActivity(i)
            } catch (e: PackageManager.NameNotFoundException) {
                Toast.makeText(this, "Whatsapp is not installed in your phone.", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }*/
            CommonMethods.openWhatsAppEnquiry(this,agentPhoneNumber,"Hi,")
        }
        constraintLocationNear.setOnClickListener {
            if(passingLat.isNotEmpty()&&passingLng.isNotEmpty()){
                val intent=Intent(this,MapAndNearByActivity::class.java)
                intent.putExtra("property_id",propertyId.toString())
                intent.putExtra("from_type","property_details")
                intent.putExtra("passed_lat",passingLat)
                intent.putExtra("passed_lng",passingLng)
                startActivity(intent)
            }
        }
        btnDownload.setOnClickListener {
            propertyDetailViewModel.downloadPropertyDetailsPdf(propertyId.toString())
        }
    }

    private fun shareFile() {
        try {
            /*val uri: Uri = FileProvider.getUriForFile(
                context,
                BuildConfig.APPLICATION_ID.toString() + ".provider",
                File(file)
            )*/
            val share = Intent(Intent.ACTION_SEND)
            share.type = "*/*"
            //share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            share.putExtra(Intent.EXTRA_TEXT, "passs")
            startActivity(Intent.createChooser(share, "Share via"))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun navigateToFragment(fragment: Fragment) {

    }

    override fun setTitle(title: String) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 11) {
            if (resultCode == Activity.RESULT_OK) {
                selectedPackageAmount = data!!.getStringExtra("discountAmount").toString()
                selectedPackageID = data.getStringExtra("selectedId").toString()
                selectedPackageName = data.getStringExtra("selectedPackageName").toString()
                Log.e("result2",selectedPackageAmount + selectedPackageID+ selectedPackageName)

                tvPackageName.text = selectedPackageName
            }
        }
    }
}
