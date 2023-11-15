package com.property.propertyuser.ui.main.home


import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.ListPopupWindow
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bolaware.walk_through_guider.AnchorView
import com.bolaware.walk_through_guider.WalkThroughDialog
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.google.gson.Gson
import com.property.propertyuser.R
import com.property.propertyuser.base.BaseActivity
import com.property.propertyuser.databinding.ActivityHomeBinding
import com.property.propertyuser.dialogs.CustomProgressDialog
import com.property.propertyuser.dialogs.book_a_tour.BookATourDateAndTimeDialogFragment
import com.property.propertyuser.modal.filter_home.*
import com.property.propertyuser.modal.property_list.Event
import com.property.propertyuser.modal.property_list.Property
import com.property.propertyuser.modal.testing.HomePropertyEventModel
import com.property.propertyuser.preference.AppPreferences
import com.property.propertyuser.preference.AppPreferences.home_lat
import com.property.propertyuser.preference.AppPreferences.home_long
import com.property.propertyuser.preference.AppPreferences.isLocationLoaded
import com.property.propertyuser.preference.AppPreferences.token
import com.property.propertyuser.ui.main.favorites.FavoritePropertyActivity
import com.property.propertyuser.ui.main.filter.FilterActivity
import com.property.propertyuser.ui.main.home.adapter.*
import com.property.propertyuser.ui.main.home.scanner.QrScannerActivity
import com.property.propertyuser.ui.main.home.search_property.SearchPropertyActivity
import com.property.propertyuser.ui.main.map_and_nearby.MapAndNearByActivity
import com.property.propertyuser.ui.main.my_property.MyPropertyActivity
import com.property.propertyuser.ui.main.notification.NotificationActivity
import com.property.propertyuser.ui.main.property_details.PropertyDetailsActivity
import com.property.propertyuser.ui.main.side_menu.SideMenuActivity
import com.property.propertyuser.ui.main.side_menu.about_us.AboutUsActivity
import com.property.propertyuser.ui.main.side_menu.faq.FAQActivity
import com.property.propertyuser.ui.main.side_menu.feedback.FeedbackActivity
import com.property.propertyuser.ui.main.side_menu.legal_information.LegalInformationActivity
import com.property.propertyuser.ui.main.side_menu.privacy_policy.PrivacyPolicyActivity
import com.property.propertyuser.ui.main.side_menu.user_profile.ProfileActivity
import com.property.propertyuser.ui.startup.auth.AuthActivity
import com.property.propertyuser.utils.*
import com.property.propertyuser.utils.CommonMethods.changeLanguageAwareContext
import com.property.propertyuser.utils.Constants.TYPE
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.layout_home.*
import kotlinx.android.synthetic.main.layout_home_toolbar_price.*
import kotlinx.android.synthetic.main.layout_home_toolbar_rent_type_bed.*
import kotlinx.android.synthetic.main.layout_no_network.*
import kotlinx.android.synthetic.main.layout_side_menu.*
import kotlinx.android.synthetic.main.toolbar_home.*
import java.util.*
import kotlin.concurrent.schedule


class HomeActivity : BaseActivity<ActivityHomeBinding>() {


    private val progressDialog = CustomProgressDialog()
    private var dialog: Dialog? = null
    private var arrowUP: Boolean = false
    private lateinit var homeViewModel: HomeViewModel
    private var doubleBackToExitPressedOnce = false
    private var homePropertyEventModelList = ArrayList<HomePropertyEventModel>()
    private val listEvent: List<Event> = listOf()
    private val listProperty: List<Property> = listOf()
    private lateinit var newFragment: DialogFragment
    private var pageNo = 0
    private var totalPageCount = 0
    private var isLoading: Boolean = false
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var testMainRecyclerViewAdapter: TestMainRecyclerViewAdapter
    private lateinit var homeSeletedFilteredMainCategoryItemAdapter0: HomeSeletedFilteredMainCategoryItemAdapter_0
    private lateinit var homeSeletedFilteredMainCategoryItemAdapter1: HomeSeletedFilteredMainCategoryItemAdapter_1
    private lateinit var homeSeletedFilteredMainCategoryItemAdapter3: HomeSeletedFilteredMainCategoryItemAdapter_3
    private lateinit var homeSelectedFilteredMainSubCategoryItemAdapter: HomeSeletedFilteredMainSubCategoryItemAdapter_0
    private lateinit var homeSelectedFilteredMainSubCategoryItemAdapter1_1: HomeSeletedFilteredMainSubCategoryItemAdapter_1_1
    private lateinit var homeSelectedFilteredMainSubCategoryItemAdapter1_2: HomeSeletedFilteredMainSubCategoryItemAdapter_1_2
    private var propertiesOld = ArrayList<Property>()
    private var selectedFilterMainItems0 = ArrayList<RentModel>()
    private var selectedFilterMainItems1 = ArrayList<TypeModel>()
    private var selectedFilterMainItems3 = ArrayList<BedModel>()
    private var selectedFilterMainSubItemsBuy = ArrayList<BuyModel>()
    private var selectedFilterMainSubItemsResid = ArrayList<ResidentialModel>()
    private var selectedFilterMainSubItemsComm = ArrayList<CommercialModel>()
    private var selectedFilterMainSubCategories = ArrayList<Category>()
    private var checkMaxPrice: Boolean = false
    private var checkMinPrice: Boolean = false
    private lateinit var homeSelectedFilteredItemAdapter: HomeSeletedFilteredItemAdapter
    private var filteredList = ArrayList<String>()
    private var passedHomeFilterType = ""
    private var passingPropertyToForFilter = ""
    private var passingCategoryForFilter = ""
    private var passingCategoryTypeIdList = ArrayList<Int>()
    private var passingBedIdList = ArrayList<Int>()
    private var passingBedSevenPlus = ""
    private var passingMaxPrice = ""
    private var passingMinPrice = ""
    private var passingFilterPageNo = 0
    private var filterApplyClicked = false
    private var filterMinPriceCheck = false
    private var filterMaxPriceCheck = false

    val LOCATION_PERMISSION_REQUEST_CODE = 999
    private var passLng = 0.0
    private var passLat = 0.0
    private var isLocationFound = true
    private var progressDialog1: ProgressDialog? = null

    override val layoutId: Int
        get() = R.layout.activity_home
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = false

    @SuppressLint("LogNotTimber")
    override fun initData() {

        progressDialog1 = ProgressDialog(this)
        progressDialog1!!.isIndeterminate = true
        progressDialog1!!.setCancelable(false)
        progressDialog1!!.setMessage("Fetching your current location...")

        Log.e("TAG", "initData: "+ token)

    }

    private fun getLocationDetails() {
        when {
            PermissionUtils.isAccessFineLocationGranted(this) -> {
                when {
                    PermissionUtils.isLocationEnabled(this) -> {
                        if (isLocationFound) {
                            Log.e("TAG", "getLocationDetails: found")
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                progressDialog1!!.show()
                            }
                        }
                        setUpLocationListener()
                    }
                    else -> {
                        enableGps()
                    }
                }
            }
            else -> {
                PermissionUtils.requestAccessFineLocationPermission(
                    this,
                    LOCATION_PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    private fun setUpLocationListener() {
        val fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this)
        val locationRequest = LocationRequest().setInterval(2000).setFastestInterval(2000)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    for (location in locationResult.locations) {
                        if (location.latitude.toString().isNotEmpty() &&
                            location.longitude.toString().isNotEmpty()
                        ) {
                            if (isLocationFound) {
                                isLocationFound = false
                                progressDialog1!!.dismiss()
                                Log.e("called", "final")
                                passLat = location.latitude
                                passLng = location.longitude
                                Log.e("lat", location.latitude.toString())
                                Log.e("lon", location.longitude.toString())
                                home_lat = passLat.toString()
                                home_long = passLng.toString()
                                pageNo = 1
                                homeSwipeToRefresh.isRefreshing = false
                                setFilteredItemsRecyclerView()
                                filteredList.addAll(listOf("Rent", "Type", "Price", "Bed"))
                                if (!(AppPreferences.user_profile_image.isNullOrBlank())) {
                                    sivUserImage.loadImagesProfileWithGlideExt(
                                        AppPreferences.user_profile_image!!
                                    )
                                }
                                if (!(AppPreferences.user_name.isNullOrBlank())) {
                                    tvUserName.text = AppPreferences.user_name!!
                                }
                                if (!(AppPreferences.user_email.isNullOrBlank())) {
                                    tvUserEmail.text = AppPreferences.user_email!!
                                }
                                homePropertyEventModelList = ArrayList()
                                propertiesOld = ArrayList()

                                setupFilterHome()

                                homeViewModel.getPropertyList(
                                    passLat.toString(),
                                    passLng.toString(),
                                    pageNo.toString()
                                )
                                break
                            }
                        }

                    }
                }
            },
            Looper.myLooper()
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    when {
                        PermissionUtils.isLocationEnabled(this) -> {
                            setUpLocationListener()
                        }
                        /*else -> {
                            PermissionUtils.showGPSNotEnabledDialog(requireContext())
                        }*/
                    }
                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.location_permission_not_granted),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun enableGps() {

        val mLocationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(2000)
            .setFastestInterval(1000)

        val settingsBuilder = LocationSettingsRequest.Builder()
            .addLocationRequest(mLocationRequest)
        settingsBuilder.setAlwaysShow(true)

        val client: SettingsClient = LocationServices.getSettingsClient(this)
        val task: Task<LocationSettingsResponse> =
            client.checkLocationSettings(settingsBuilder.build())
        task.addOnSuccessListener { locationSettingsResponse ->
            Log.e("task on success", locationSettingsResponse.toString())
            setUpLocationListener()
        }

        task.addOnFailureListener { exception ->

            Log.e("TAG", "enableGps: fail")
            /* val dialog = LocationDialogFragment()
             dialog.show(supportFragmentManager, "TAG")*/
            if (exception is ResolvableApiException) {
                try {
                    Log.e("on location failure", exception.toString())
                    val intentSenderRequest =
                        IntentSenderRequest.Builder(exception.resolution).build()
                    resolutionForResult.launch(intentSenderRequest)
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }
    }

    private val resolutionForResult =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { activityResult ->
            if (activityResult.resultCode == Activity.RESULT_OK) {
                Log.e("on resolution result", "success")
                setUpLocationListener()
            } else {
                Toast.makeText(
                    this,
                    "we can't determine your location",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


    private fun startTutorial() {
        WalkThroughDialog(
            this,
            listOf(
                AnchorView(
                    constraintAppBarHome,
                    "Search property in here...."
                ),
                AnchorView(tvFilterItemRent, "Click for filters"),
                AnchorView(ivNotification, "View Notifications"),
                AnchorView(ivFavorite, "View Wishlist"),
                AnchorView(ivMapHome, "View Nearby location"),
                AnchorView(ivFilter, "Apply Filter")
            )
        ).setAroundColor(R.color.transparentColor) //optional, advisable to make transparent
            .setContentTintColor(android.R.color.white) //optional
            .setHighLighterColor(android.R.color.white) //optional
            .setStepsPageIndicatorTextColor(android.R.color.darker_gray) //optional
            .show() //compulsory
    }

    private fun setupFilterHome() {
        selectedFilterMainItems0 = ArrayList()
        selectedFilterMainItems1 = ArrayList()
        selectedFilterMainItems3 = ArrayList()

        selectedFilterMainSubItemsBuy = ArrayList()
        selectedFilterMainSubItemsResid = ArrayList()
        selectedFilterMainSubItemsComm = ArrayList()

        selectedFilterMainSubCategories = ArrayList()

        selectedFilterMainItems0.add(RentModel("Rent", false))
        selectedFilterMainItems0.add(RentModel("Buy", false))

        selectedFilterMainItems1.add(TypeModel("Residential", false))
        selectedFilterMainItems1.add(TypeModel("Commercial", false))

        selectedFilterMainItems3.add(BedModel(-1, "Any", true))
        selectedFilterMainItems3.add(BedModel(1, "1 Bed", false))
        selectedFilterMainItems3.add(BedModel(2, "2 Beds", false))
        selectedFilterMainItems3.add(BedModel(3, "3 Beds", false))
        selectedFilterMainItems3.add(BedModel(4, "4 Beds", false))
        selectedFilterMainItems3.add(BedModel(5, "5 Beds", false))
        selectedFilterMainItems3.add(BedModel(6, "6 Beds", false))
        selectedFilterMainItems3.add(BedModel(7, "7+", false))

        selectedFilterMainSubItemsBuy.add(BuyModel("Ready", false))
        selectedFilterMainSubItemsBuy.add(BuyModel("Off Plan", false))

        selectedFilterMainSubItemsResid.add(ResidentialModel("Any", true))
        selectedFilterMainSubItemsResid.add(ResidentialModel("Apartment", false))
        selectedFilterMainSubItemsResid.add(ResidentialModel("House", false))
        selectedFilterMainSubItemsResid.add(ResidentialModel("Plot", false))

        selectedFilterMainSubItemsComm.add(CommercialModel("Any", true))
        selectedFilterMainSubItemsComm.add(CommercialModel("Office", false))
        selectedFilterMainSubItemsComm.add(CommercialModel("Retail", false))
        selectedFilterMainSubItemsComm.add(CommercialModel("Plot", false))

        rvFilterItemMainHome0.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        homeSeletedFilteredMainCategoryItemAdapter0 =
            HomeSeletedFilteredMainCategoryItemAdapter_0(
                this,
                selectedFilterMainItems0
            ) { selectedFilterMainItem(it) }
        rvFilterItemMainHome0.adapter = homeSeletedFilteredMainCategoryItemAdapter0

        rvFilterItemMainHome1.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        homeSeletedFilteredMainCategoryItemAdapter1 =
            HomeSeletedFilteredMainCategoryItemAdapter_1(
                this,
                selectedFilterMainItems1
            ) { selectedFilterMainItem(it) }
        rvFilterItemMainHome1.adapter = homeSeletedFilteredMainCategoryItemAdapter1

        rvFilterItemMainHome3.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        homeSeletedFilteredMainCategoryItemAdapter3 =
            HomeSeletedFilteredMainCategoryItemAdapter_3(
                this,
                selectedFilterMainItems3
            ) { selectedFilterMainItem(it) }
        rvFilterItemMainHome3.adapter = homeSeletedFilteredMainCategoryItemAdapter3

        rvFilterItemSubHome0.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        homeSelectedFilteredMainSubCategoryItemAdapter =
            HomeSeletedFilteredMainSubCategoryItemAdapter_0(
                this,
                selectedFilterMainSubItemsBuy
            ) { selectedFilterSubItem(it) }
        rvFilterItemSubHome0.adapter = homeSelectedFilteredMainSubCategoryItemAdapter

        rvFilterItemSubHome1_1.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        homeSelectedFilteredMainSubCategoryItemAdapter1_1 =
            HomeSeletedFilteredMainSubCategoryItemAdapter_1_1(
                this,
                selectedFilterMainSubCategories
            ) { selectedFilterSubItem(it) }
        rvFilterItemSubHome1_1.adapter = homeSelectedFilteredMainSubCategoryItemAdapter1_1

        rvFilterItemSubHome1_2.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        homeSelectedFilteredMainSubCategoryItemAdapter1_2 =
            HomeSeletedFilteredMainSubCategoryItemAdapter_1_2(
                this,
                selectedFilterMainSubCategories
            ) { selectedFilterSubItem(it) }
        rvFilterItemSubHome1_2.adapter = homeSelectedFilteredMainSubCategoryItemAdapter1_2
    }

    override fun onResume() {
        super.onResume()
        Log.e("get language on res", AppPreferences.chooseLanguage.toString())
        if (AppPreferences.chooseLanguage == "arabic") {
            constraintEndlayout.background =
                ContextCompat.getDrawable(this, R.drawable.border_rounded_side_lightgreen_left)
        } else {
            constraintEndlayout.background =
                ContextCompat.getDrawable(this, R.drawable.border_rounded_side_lightgreen)
        }
        if (AppPreferences.reload_property_list) {
            Log.e("inside", "resume")
            AppPreferences.reload_property_list = false
            /* homePropertyEventModelList= ArrayList()
             totalPageCount=0
             pageNo=1
             Log.e("size",homePropertyEventModelList.size.toString())
             homeViewModel.getPropertyList("10.00084910","76.30134207",pageNo.toString())*/
        }
        if (AppPreferences.reload_profile_details) {
            AppPreferences.reload_profile_details = false
            if (!(AppPreferences.user_profile_image.isNullOrBlank())) {
                sivUserImage.loadImagesProfileWithGlideExt(
                    AppPreferences.user_profile_image!!
                )
            } else {
                sivUserImage.loadImagesProfileWithGlideExt("")
            }
            if (!(AppPreferences.user_name.isNullOrBlank())) {
                tvUserName.text = AppPreferences.user_name!!
            }
            if (!(AppPreferences.user_email.isNullOrBlank())) {
                tvUserEmail.text = AppPreferences.user_email!!
            }
        }

      /*  if(isLocationLoaded){
            pageNo = 1
          *//*  homePropertyEventModelList.clear()
            propertiesOld.clear()*//*
            homeViewModel.getPropertyList(
                home_lat.toString(),
                home_long.toString(),
                pageNo.toString()
            )
        }else {
            getLocationDetails()
        }*/
        getLocationDetails()

    }

    private fun setFilteredItemsRecyclerView() {
        rvFilterSelectedItemList.layoutManager =
            GridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false)
        /*LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)*/
        homeSelectedFilteredItemAdapter =
            HomeSeletedFilteredItemAdapter(filteredList, this) { selectedFilter(it) }
        rvFilterSelectedItemList.adapter = homeSelectedFilteredItemAdapter
    }

    private fun selectedFilter(it: Int) {
        setFirstFilterRecyclerView(it)
    }

    private fun setFirstFilterRecyclerView(pos: Int) {
        expand.expand()
        //btnCancelHome.visibility = View.VISIBLE
        //btnApplyHome.visibility = View.VISIBLE
        when (pos) {
            0 -> {
                includeFilterMainType.visibility = View.VISIBLE
                includeFilterMainPrice.visibility = View.GONE

                rvFilterItemMainHome0.visibility = View.VISIBLE
                rvFilterItemMainHome1.visibility = View.GONE
                rvFilterItemMainHome3.visibility = View.GONE

                if (selectedFilterMainItems0[1].checked) {
                    // rvFilterItemSubHome0.visibility=View.VISIBLE
                    rvFilterItemSubHome0.visibility = View.GONE
                }
                rvFilterItemSubHome1_1.visibility = View.GONE
                rvFilterItemSubHome1_2.visibility = View.GONE
            }
            1 -> {
                includeFilterMainType.visibility = View.VISIBLE
                includeFilterMainPrice.visibility = View.GONE

                rvFilterItemMainHome0.visibility = View.GONE
                rvFilterItemMainHome1.visibility = View.VISIBLE
                rvFilterItemMainHome3.visibility = View.GONE

                rvFilterItemSubHome0.visibility = View.GONE
                if (selectedFilterMainItems1[0].checked) {
                    rvFilterItemSubHome1_1.visibility = View.VISIBLE
                    rvFilterItemSubHome1_2.visibility = View.GONE
                }
                if (selectedFilterMainItems1[1].checked) {
                    rvFilterItemSubHome1_1.visibility = View.GONE
                    rvFilterItemSubHome1_2.visibility = View.VISIBLE
                }
            }
            3 -> {
                includeFilterMainType.visibility = View.VISIBLE
                includeFilterMainPrice.visibility = View.GONE

                rvFilterItemMainHome0.visibility = View.GONE
                rvFilterItemMainHome1.visibility = View.GONE
                rvFilterItemMainHome3.visibility = View.VISIBLE

                rvFilterItemSubHome0.visibility = View.GONE
                rvFilterItemSubHome1_1.visibility = View.GONE
                rvFilterItemSubHome1_2.visibility = View.GONE
            }
            else -> {
                includeFilterMainType.visibility = View.GONE
                includeFilterMainPrice.visibility = View.VISIBLE
                addMinDropDown()
                addMaxDropDown()
            }
        }

    }

    private fun addMinDropDown() {
        val minPriceList = listOf<String>(
            "1000", "10000", "50000", "100000", "500000",
            "1000000", "5000000", "10000000", "50000000", "100000000"
        )

        val listPopupWindowMin = ListPopupWindow(this, null, R.attr.listPopupWindowStyle)
        listPopupWindowMin.anchorView = tvMinPrice
        val adapter = ArrayAdapter(this, R.layout.list_popup_window_item, minPriceList)
        listPopupWindowMin.setAdapter(adapter)

        // Set list popup's item click listener
        listPopupWindowMin.setOnItemClickListener { parent: AdapterView<*>?, view: View?, position: Int, id: Long ->
            // Respond to list popup window item click.
            tvMinPrice.setTextColor(Color.BLACK)
            tvMinPrice.text = minPriceList[position]
            passingMinPrice = minPriceList[position]
            checkMinPrice = true
            // Dismiss popup.
            listPopupWindowMin.dismiss()
            filterMinPriceCheck = false
        }
        tvMinPrice.setOnClickListener {
            if (!filterMinPriceCheck) {
                filterMinPriceCheck = true
                listPopupWindowMin.show()
            } else {
                filterMinPriceCheck = false
                listPopupWindowMin.dismiss()
            }
        }
    }

    private fun addMaxDropDown() {
        val maxPriceList = listOf<String>(
            "1000", "10000", "50000", "100000", "500000",
            "1000000", "5000000", "10000000", "50000000", "100000000"
        )

        val listPopupWindowMax = ListPopupWindow(this, null, R.attr.listPopupWindowStyle)
        listPopupWindowMax.anchorView = tvMaxPrice
        val adapter = ArrayAdapter(this, R.layout.list_popup_window_item, maxPriceList)
        listPopupWindowMax.setAdapter(adapter)

        // Set list popup's item click listener
        listPopupWindowMax.setOnItemClickListener { parent: AdapterView<*>?, view: View?, position: Int, id: Long ->
            // Respond to list popup window item click.
            tvMaxPrice.setTextColor(Color.BLACK)
            tvMaxPrice.text = maxPriceList[position]
            passingMaxPrice = maxPriceList[position]
            checkMaxPrice = true
            // Dismiss popup.
            listPopupWindowMax.dismiss()
            filterMaxPriceCheck = false
        }
        tvMaxPrice.setOnClickListener {
            if (!filterMaxPriceCheck) {
                filterMaxPriceCheck = true
                listPopupWindowMax.show()
            } else {
                filterMaxPriceCheck = false
                listPopupWindowMax.dismiss()
            }
        }
    }

    private fun selectedFilterMainItem(name: String) {
        when (name) {
            "Buy" -> {
                rvFilterItemSubHome0.visibility = View.GONE
                rvFilterItemSubHome1_1.visibility = View.GONE
                rvFilterItemSubHome1_2.visibility = View.GONE

            }
            "Residential" -> {
                passedHomeFilterType = "residential"
                homeViewModel.getHomeFilterCategories("0")
                /*rvFilterItemSubHome0.visibility=View.GONE
                rvFilterItemSubHome1_1.visibility=View.VISIBLE
                rvFilterItemSubHome1_2.visibility=View.GONE
                if(!(filteredList.contains("Bed"))){
                    filteredList.add("Bed")
                    homeSelectedFilteredItemAdapter.notifyItemInserted(filteredList.size-1)
                }*/

            }
            "Commercial" -> {
                passedHomeFilterType = "commercial"
                homeViewModel.getHomeFilterCategories("1")
                /*if(filteredList.contains("Bed")){
                    filteredList.removeAt(filteredList.size-1)
                    homeSelectedFilteredItemAdapter.notifyItemRemoved(filteredList.size)
                }
                rvFilterItemSubHome0.visibility=View.GONE
                rvFilterItemSubHome1_1.visibility=View.GONE
                rvFilterItemSubHome1_2.visibility=View.VISIBLE*/

            }
            else -> {
                rvFilterItemSubHome0.visibility = View.GONE
                rvFilterItemSubHome1_1.visibility = View.GONE
                rvFilterItemSubHome1_2.visibility = View.GONE
            }
        }
    }

    private fun selectedFilterSubItem(it: Int) {
        Log.e("selectedSub", it.toString())
    }

    private fun setPropertyItemListRecyclerViewTest() {

        etSearchHome.hint = ""
        etSearchHome.setCharacterDelay(300)
        etSearchHome.animateText(getString(R.string.etSearch))

        layoutManager = LinearLayoutManager(this)
        rvPropertyList.layoutManager = layoutManager
        testMainRecyclerViewAdapter = TestMainRecyclerViewAdapter(this,
            homePropertyEventModelList,
            { i: Int, s: String ->
                val intent = Intent(this, PropertyDetailsActivity::class.java)
                intent.putExtra(Constants.INTENT_PROPERTY_ID, i)
                startActivity(intent)
            },
            { propertyId -> openMapActivity(propertyId) },
            { showDateAndTimeDialog(it) },
            { it, pos -> addToFav(it, pos) },
            { it, pos -> removeFromFav(it, pos) })
        rvPropertyList.adapter = testMainRecyclerViewAdapter
        rvPropertyList.startLayoutAnimation()

        rvPropertyList.addOnScrolledToEnd {
            if (!isLoading) {
                Log.e("end", "reached")
                loadData()
                isLoading = true
            }
        }
    }

    private fun openMapActivity(propertyId: Int) {
        val intent = Intent(this, MapAndNearByActivity::class.java)
        intent.putExtra("property_id", propertyId.toString())
        intent.putExtra("from_type", "home_property_list")
        startActivity(intent)
    }

    private fun loadData() {
        if (filterApplyClicked) {
            if (passingFilterPageNo <= totalPageCount && this.isConnected) {
                passingFilterPageNo += 1
                homePropertyEventModelList.add(
                    HomePropertyEventModel(
                        3, listEvent,
                        Property(
                            -1, -1, "", -1, -1, -1, -1,
                            emptyList(), -1, -1, -1, -1, "", -1,
                            "", "", -1, "", "", "", -2
                        )
                    )
                )
                testMainRecyclerViewAdapter.notifyItemInserted(homePropertyEventModelList.size - 1)
                homeViewModel.fetchFilterPropertyList(
                    passingFilterPageNo.toString(), passingPropertyToForFilter,
                    passingCategoryTypeIdList, passingCategoryForFilter, passingMinPrice,
                    passingMaxPrice, "4", passingBedIdList, passingBedSevenPlus
                )
            }
        } else {
            if (pageNo <= totalPageCount && this.isConnected) {
                homePropertyEventModelList.add(
                    HomePropertyEventModel(
                        3, listEvent,
                        Property(
                            -1, -1, "", -1, -1, -1, -1,
                            emptyList(), -1, -1, -1, -1, "", -1,
                            "", "", -1, "", "", "", -2
                        )
                    )
                )
                testMainRecyclerViewAdapter.notifyItemInserted(homePropertyEventModelList.size - 1)
                homeViewModel.getPropertyList(
                    passLat.toString(),
                    passLng.toString(),
                    pageNo.toString()
                )
            }
        }
    }

    private fun removeFromFav(remFav: Int, pos: Int) {
        Log.e("remove", remFav.toString())
        homeViewModel.updateFavoriteProperty(remFav)
    }

    private fun addToFav(addFav: Int, pos: Int) {
        Log.e("addToFav", addFav.toString())
        homeViewModel.updateFavoriteProperty(addFav)
    }

    private fun showDateAndTimeDialog(it: Int) {
        var isLargeLayout = resources.getBoolean(R.bool.large_layout)
        val fragmentManager = supportFragmentManager
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

    private fun setSwipeToRefresh() {
        /* pageNo=1
         homePropertyEventModelList.clear()
         propertiesOld.clear()
         testMainRecyclerViewAdapter.notifyDataSetChanged()
         homeSwipeToRefresh.isRefreshing=false
         homeViewModel.getPropertyList("10.00084910","76.30134207",pageNo.toString())*/

        /* There is an issue with pagination, so starting homeActivity again instead of calling init() method */
        isLocationLoaded = true
        val intent = Intent(this, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        startActivity(intent)
    }

    override fun fragmentLaunch() {

    }

    override fun setupUI() {

    }

    override fun setupViewModel() {
        homeViewModel = HomeViewModel()
    }

    override fun setupObserver() {
        homeViewModel.getPropertyListLiveData().observe(this, androidx.lifecycle.Observer {
            when (it.status) {
                Status.LOADING ->
                    if (homePropertyEventModelList.size == 0) {
                        showLoader()
                    }
                Status.SUCCESS -> {
                    filterApplyClicked = false
                    if (homePropertyEventModelList.size == 0) {
                        dismissLoader()
                    }
                    if (it.data != null) {
                        if (it.data.property_data != null) {
                            totalPageCount = it.data.property_data.total_page_count
                            if (homePropertyEventModelList.size != 0) {
                                isLoading = false
                                pageNo += 1
                                homePropertyEventModelList.removeAt(homePropertyEventModelList.size - 1)
                                testMainRecyclerViewAdapter.notifyItemRemoved(
                                    homePropertyEventModelList.size
                                )
                                propertiesOld.addAll(it.data.property_data.properties)
                                for (i in 0 until 2) {
                                    if (i == 0) {
                                        for (j in it.data.property_data.properties.indices) {
                                            homePropertyEventModelList.add(
                                                HomePropertyEventModel(
                                                    1,
                                                    listEvent,
                                                    it.data.property_data.properties[j]
                                                )
                                            )
                                        }
                                    }
                                }
                                testMainRecyclerViewAdapter.notifyDataSetChanged()
                            } else {
                                if (it.data.property_data.properties.isNullOrEmpty()) {
                                    linearNoPropertyFound.visibility = View.VISIBLE
                                    rvPropertyList.visibility = View.GONE
                                } else {
                                    linearNoPropertyFound.visibility = View.GONE
                                    rvPropertyList.visibility = View.VISIBLE
                                    pageNo += 1
                                    propertiesOld =
                                        it.data.property_data.properties as ArrayList<Property>
                                    if (propertiesOld.size > 4) {
                                        for (i in 0 until 3) {
                                            if (i == 0) {
                                                for (j in 0 until 3) {
                                                    homePropertyEventModelList.add(
                                                        HomePropertyEventModel(
                                                            1,
                                                            listEvent,
                                                            it.data.property_data.properties[j]
                                                        )
                                                    )
                                                }
                                            } else if (i == 1) {
                                                if (!(it.data.events.isNullOrEmpty())) {
                                                    homePropertyEventModelList.add(
                                                        HomePropertyEventModel(
                                                            2, it.data.events,
                                                            Property(
                                                                -1, -1, "", -1, -1, -1, -1,
                                                                emptyList(), -1, -1, -1, -1, "", -1,
                                                                "", "", -1, "", "", "", -2
                                                            )

                                                        )
                                                    )
                                                }
                                            } else {
                                                for (k in 3 until it.data.property_data.properties.size) {
                                                    homePropertyEventModelList.add(
                                                        HomePropertyEventModel(
                                                            1,
                                                            listEvent,
                                                            it.data.property_data.properties[k]
                                                        )
                                                    )
                                                }
                                            }
                                        }
                                    } else {
                                        for (i in 0 until 2) {
                                            if (i == 0) {
                                                for (j in it.data.property_data.properties.indices) {
                                                    homePropertyEventModelList.add(
                                                        HomePropertyEventModel(
                                                            1,
                                                            listEvent,
                                                            it.data.property_data.properties[j]
                                                        )
                                                    )
                                                }
                                            } else {
                                                if (!(it.data.events.isNullOrEmpty())) {
                                                    homePropertyEventModelList.add(
                                                        HomePropertyEventModel(
                                                            2, it.data.events,
                                                            Property(
                                                                -1, -1, "", -1, -1, -1, -1,
                                                                emptyList(), -1, -1, -1, -1, "", -1,
                                                                "", "", -1, "", "", "", -2
                                                            )

                                                        )
                                                    )
                                                }
                                            }
                                        }
                                    }

                                    //startTutorial()
                                    setPropertyItemListRecyclerViewTest()
                                }
                            }
                        }
                    }
                }
                Status.DATA_EMPTY -> {
                    dismissLoader()
                    Toaster.showToast(
                        this, getString(R.string.data_empty),
                        Toaster.State.ERROR, Toast.LENGTH_LONG
                    )
                }
                Status.NO_INTERNET -> {
                    dismissLoader()
                    if (this.isConnected) {
                        Toaster.showToast(
                            this, getString(R.string.something_wrong),
                            Toaster.State.ERROR, Toast.LENGTH_LONG
                        )
                    } else {
                        includeNoInternetHome.visibility = View.VISIBLE
                        linearNoPropertyFound.visibility = View.GONE
                        rvPropertyList.visibility = View.GONE
                    }
                }

            }
        })

        homeViewModel.updateFavoritePropertyResponse().observe(this, androidx.lifecycle.Observer {
            when (it.status) {
                Status.LOADING -> {}
                Status.SUCCESS -> {
                    drawer_layout.snack(it.data!!.response)
                }
                Status.DATA_EMPTY -> {

                    Toaster.showToast(
                        this,
                        it.data!!.response,
                        Toaster.State.ERROR,
                        Toast.LENGTH_LONG
                    )
                }
                Status.NO_INTERNET -> {

                    if (this.isConnected) {
                        Toaster.showToast(
                            this, getString(R.string.something_wrong),
                            Toaster.State.ERROR, Toast.LENGTH_LONG
                        )
                    } else {
                        Toaster.showToast(
                            this, getString(R.string.no_internet),
                            Toaster.State.ERROR, Toast.LENGTH_LONG
                        )
                    }
                }

            }
        })
        homeViewModel.getUserLogoutResponse().observe(this, androidx.lifecycle.Observer {
            when (it.status) {
                Status.LOADING -> showLoader()
                Status.SUCCESS -> {
                    dismissLoader()
                    AppPreferences.logoutClearPreference()
                    dialog?.dismiss()
                    startActivity(Intent(this, AuthActivity::class.java))
                    finishAffinity()
                }
                Status.DATA_EMPTY -> {
                    dismissLoader()
                    Toaster.showToast(
                        this,
                        it.data!!.response,
                        Toaster.State.ERROR,
                        Toast.LENGTH_LONG
                    )
                }
                Status.NO_INTERNET -> {
                    dismissLoader()
                    if (this.isConnected) {
                        Toaster.showToast(
                            this, getString(R.string.something_wrong),
                            Toaster.State.ERROR, Toast.LENGTH_LONG
                        )
                    } else {
                        Toaster.showToast(
                            this, getString(R.string.no_internet),
                            Toaster.State.ERROR, Toast.LENGTH_LONG
                        )
                    }
                }

            }
        })

        homeViewModel.getHomeFilterCategoriesResponse().observe(this, Observer {
            when (it.status) {
                Status.LOADING -> showLoader()
                Status.SUCCESS -> {
                    dismissLoader()
                    if (it.data != null) {
                        selectedFilterMainSubCategories.clear()
                        if (!(it.data.categories.isNullOrEmpty())) {
                            selectedFilterMainSubCategories.add(Category(-1, -1, "Any", true))
                            for (x in it.data.categories.indices) {
                                selectedFilterMainSubCategories.add(
                                    Category(
                                        it.data.categories[x].category,
                                        it.data.categories[x].id, it.data.categories[x].type, false
                                    )
                                )
                            }

                            if (passedHomeFilterType == "residential") {
                                tvFilterItemBed.visibility = View.VISIBLE
                                rvFilterItemSubHome0.visibility = View.GONE
                                rvFilterItemSubHome1_1.visibility = View.VISIBLE
                                homeSelectedFilteredMainSubCategoryItemAdapter1_1.notifyDataSetChanged()
                                rvFilterItemSubHome1_2.visibility = View.GONE
                                if (!(filteredList.contains("Bed"))) {
                                    filteredList.add("Bed")
                                    homeSelectedFilteredItemAdapter.notifyItemInserted(filteredList.size - 1)
                                }
                            }
                            if (passedHomeFilterType == "commercial") {
                                tvFilterItemBed.visibility = View.GONE
                                if (filteredList.contains("Bed")) {
                                    filteredList.removeAt(filteredList.size - 1)
                                    homeSelectedFilteredItemAdapter.notifyItemRemoved(filteredList.size)
                                }
                                rvFilterItemSubHome0.visibility = View.GONE
                                rvFilterItemSubHome1_1.visibility = View.GONE
                                rvFilterItemSubHome1_2.visibility = View.VISIBLE
                                homeSelectedFilteredMainSubCategoryItemAdapter1_2.notifyDataSetChanged()
                            }
                        }
                    }

                }
                Status.DATA_EMPTY -> {
                    dismissLoader()
                    Toaster.showToast(
                        this,
                        it.data!!.status.toString(),
                        Toaster.State.ERROR,
                        Toast.LENGTH_LONG
                    )
                }
                Status.NO_INTERNET -> {
                    dismissLoader()
                    if (this.isConnected) {
                        Toaster.showToast(
                            this, getString(R.string.something_wrong),
                            Toaster.State.ERROR, Toast.LENGTH_LONG
                        )
                    } else {
                        Toaster.showToast(
                            this, getString(R.string.no_internet),
                            Toaster.State.ERROR, Toast.LENGTH_LONG
                        )
                    }
                }

            }
        })

        homeViewModel.getFilterPropertyListLiveData().observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    if (homePropertyEventModelList.size == 0) {
                        showLoader()
                    }
                }
                Status.SUCCESS -> {
                    if (homePropertyEventModelList.size == 0) {
                        dismissLoader()
                    }
                    Log.e("filterHomeResp", Gson().toJson(it))
                    if (it.data != null) {
                        if (it.data.property_data != null) {
                            filterApplyClicked = true
                            totalPageCount = it.data.property_data.total_page_count
                            if (homePropertyEventModelList.size != 0) {
                                isLoading = false
                                homePropertyEventModelList.removeAt(homePropertyEventModelList.size - 1)
                                testMainRecyclerViewAdapter.notifyItemRemoved(
                                    homePropertyEventModelList.size
                                )
                                if (!(it.data.property_data.properties.isNullOrEmpty())) {
                                    for (j in it.data.property_data.properties.indices) {
                                        homePropertyEventModelList.add(
                                            HomePropertyEventModel(
                                                1,
                                                listEvent,
                                                it.data.property_data.properties[j]
                                            )
                                        )
                                    }
                                    testMainRecyclerViewAdapter.notifyDataSetChanged()
                                }
                            } else {
                                if (!(it.data.property_data.properties.isNullOrEmpty())) {
                                    linearNoPropertyFound.visibility = View.GONE
                                    rvPropertyList.visibility = View.VISIBLE
                                    for (k in it.data.property_data.properties.indices) {
                                        homePropertyEventModelList.add(
                                            HomePropertyEventModel(
                                                1,
                                                listEvent,
                                                it.data.property_data.properties[k]
                                            )
                                        )
                                    }
                                    testMainRecyclerViewAdapter.notifyDataSetChanged()
                                } else {
                                    /*Toaster.showToast(this,getString(R.string.no_property_found),
                                        Toaster.State.ERROR,Toast.LENGTH_LONG)*/
                                    linearNoPropertyFound.visibility = View.VISIBLE
                                    rvPropertyList.visibility = View.GONE
                                }
                            }
                        }
                    }
                }
                Status.DATA_EMPTY -> {
                    dismissLoader()
                    Toaster.showToast(
                        this,
                        it.data!!.status.toString(),
                        Toaster.State.ERROR,
                        Toast.LENGTH_LONG
                    )
                }
                Status.NO_INTERNET -> {
                    dismissLoader()
                    if (this.isConnected) {
                        Toaster.showToast(
                            this, getString(R.string.something_wrong),
                            Toaster.State.ERROR, Toast.LENGTH_LONG
                        )
                    } else {
                        Toaster.showToast(
                            this, getString(R.string.no_internet),
                            Toaster.State.ERROR, Toast.LENGTH_LONG
                        )
                    }
                }

            }
        })
    }

    override fun onClicks() {
        homeSwipeToRefresh.setOnRefreshListener {
            setSwipeToRefresh()
        }
        tvArabic.setOnClickListener {
            Log.e("get language", AppPreferences.chooseLanguage.toString())
            if (tvArabic.text == "English") {
                AppPreferences.chooseLanguage = "english"
                changeLanguageAwareContext(this, Constants.ENGLISH_LAG)
                startActivity(Intent(this, HomeActivity::class.java))
                overridePendingTransition(0, 0)
            } else {
                AppPreferences.chooseLanguage = "arabic"
                changeLanguageAwareContext(this, Constants.ARABIC_LAG)
                startActivity(Intent(this, HomeActivity::class.java))
                overridePendingTransition(0, 0)
            }
        }

        ivMenuHome.setOnClickListener {
            if (CommonMethods.isOpenRecently()) return@setOnClickListener
            if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
                drawer_layout.closeDrawer(GravityCompat.START)
            } else {
                drawer_layout.openDrawer(GravityCompat.START)
            }
        }
        ivclose.setOnClickListener {
            if (CommonMethods.isOpenRecently()) return@setOnClickListener
            if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
                drawer_layout.closeDrawer(GravityCompat.START)
            } else {
                drawer_layout.openDrawer(GravityCompat.START)
            }
        }
        tvMyProfile.setOnClickListener {
            if (CommonMethods.isOpenRecently()) return@setOnClickListener
            startActivity(Intent(this, ProfileActivity::class.java))
        }
        tvFAQ.setOnClickListener {
            if (CommonMethods.isOpenRecently()) return@setOnClickListener
            startActivity(Intent(this, FAQActivity::class.java))
        }
        tvAboutUs.setOnClickListener {
            if (CommonMethods.isOpenRecently()) return@setOnClickListener
            startActivity(Intent(this, AboutUsActivity::class.java))
        }
        tvPrivacyPolicy.setOnClickListener {
            if (CommonMethods.isOpenRecently()) return@setOnClickListener
            startActivity(Intent(this, PrivacyPolicyActivity::class.java))
        }
        tvLegalInformation.setOnClickListener {
            if (CommonMethods.isOpenRecently()) return@setOnClickListener
            startActivity(Intent(this, LegalInformationActivity::class.java))
        }
        tvFeedback.setOnClickListener {
            if (CommonMethods.isOpenRecently()) return@setOnClickListener
            startActivity(Intent(this, FeedbackActivity::class.java))
        }
        tvMyProperties.setOnClickListener {
            if (CommonMethods.isOpenRecently()) return@setOnClickListener
            startActivity(Intent(this, MyPropertyActivity::class.java))
        }
        tvBecomeOwner.setOnClickListener {
            if (CommonMethods.isOpenRecently()) return@setOnClickListener
            val intent = Intent(this, SideMenuActivity::class.java)
            intent.putExtra(TYPE, TYPE_MENUE.BECOMEOWNER.name)
            startActivity(intent)
        }
        tvReferFriend.setOnClickListener {
            if (CommonMethods.isOpenRecently()) return@setOnClickListener
            val intent = Intent(this, SideMenuActivity::class.java)
            intent.putExtra(TYPE, TYPE_MENUE.REFERAL.name)
            startActivity(intent)
        }
        tvRewards.setOnClickListener {
            if (CommonMethods.isOpenRecently()) return@setOnClickListener
            val intent = Intent(this, SideMenuActivity::class.java)
            intent.putExtra(TYPE, TYPE_MENUE.REWARDS.name)
            startActivity(intent)
        }
        tvRequestedProperty.setOnClickListener {
            if (CommonMethods.isOpenRecently()) return@setOnClickListener
            val intent = Intent(this, SideMenuActivity::class.java)
            intent.putExtra(TYPE, TYPE_MENUE.REQUESTEDPROPERTY.name)
            startActivity(intent)
        }
        ivNotification.setOnClickListener {
            if (CommonMethods.isOpenRecently()) return@setOnClickListener
            startActivity(Intent(this, NotificationActivity::class.java))
        }
        ivFilter.setOnClickListener {
            if (CommonMethods.isOpenRecently()) return@setOnClickListener
            startActivity(Intent(this, FilterActivity::class.java))
        }
        ivMapHome.setOnClickListener {
            if (CommonMethods.isOpenRecently()) return@setOnClickListener
            val intent = Intent(this, MapAndNearByActivity::class.java)
            intent.putExtra("from_type", "home_bottom_map")
            startActivity(intent)
        }
        ivFavorite.setOnClickListener {
            if (CommonMethods.isOpenRecently()) return@setOnClickListener
            startActivity(Intent(this, FavoritePropertyActivity::class.java))
        }
        tvReferal.setOnClickListener {
            if (CommonMethods.isOpenRecently()) return@setOnClickListener
            if (tvReferal.text == "Referral") {
                if (arrowUP) {
                    tvReferal.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_group_gray,
                        0,
                        R.drawable.ic__arrow_down_24,
                        0
                    )
                    arrowUP = false

                    expandable_layout1.collapse()
                    //tvReferFriend.visibility = View.GONE
                    //tvRewards.visibility = View.GONE
                } else {
                    tvReferal.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_group_gray,
                        0,
                        R.drawable.ic_arrow_up_24,
                        0
                    )
                    arrowUP = true
                    expandable_layout1.expand()
                    //tvReferFriend.visibility = View.VISIBLE
                    //tvRewards.visibility = View.VISIBLE
                }
            } else {
                if (arrowUP) {
                    tvReferal.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic__arrow_down_24,
                        0,
                        R.drawable.ic_group_gray,
                        0
                    )
                    arrowUP = false
                    expandable_layout1.collapse()
                    //tvReferFriend.visibility = View.GONE
                    //tvRewards.visibility = View.GONE
                } else {
                    tvReferal.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_arrow_up_24,
                        0,
                        R.drawable.ic_group_gray,
                        0
                    )
                    arrowUP = true
                    expandable_layout1.expand()
                    //tvReferFriend.visibility = View.VISIBLE
                    //tvRewards.visibility = View.VISIBLE
                }
            }
        }
        ivQrcode.setOnClickListener {
            if (CommonMethods.isOpenRecently()) return@setOnClickListener
            startActivity(Intent(this, QrScannerActivity::class.java))
        }
        tvLogout.setOnClickListener {
            openLogoutDialog()
        }
        btnApplyHome.setOnClickListener {
            expand.collapse()
            // btnCancelHome.visibility = View.GONE
            // btnApplyHome.visibility = View.GONE
            // includeFilterMainType.visibility = View.GONE
            // includeFilterMainPrice.visibility = View.GONE
            passingCategoryTypeIdList = ArrayList()
            passingBedIdList = ArrayList()
            passingBedSevenPlus = ""
            passingFilterPageNo = 1

            if (!(selectedFilterMainItems0.filter { it -> it.checked }.isNullOrEmpty())) {
                passingPropertyToForFilter =
                    if (selectedFilterMainItems0.filter { it -> it.checked }[0].rentModelName == "Rent") {
                        "0"
                    } else {
                        "1"
                    }
            }
            if (!(selectedFilterMainItems1.filter { it -> it.checked }.isNullOrEmpty())) {
                passingCategoryForFilter =
                    if (selectedFilterMainItems1.filter { it -> it.checked }[0].typeModelName == "Residential") {
                        "0"
                    } else {
                        "1"
                    }
            }
            if (!(selectedFilterMainSubCategories.filter { it -> it.checked }.isNullOrEmpty())) {
                selectedFilterMainSubCategories.filter { it -> it.checked }.forEach {
                    if (it.id != -1) {
                        passingCategoryTypeIdList.add(it.id)
                    }
                }
            }
            if (!(selectedFilterMainItems3.filter { it -> it.checked }.isNullOrEmpty())) {
                selectedFilterMainItems3.filter { it -> it.checked }.forEach {
                    if (it.id != -1) {
                        passingBedIdList.add(it.id)
                    }
                }
                if (passingBedIdList.contains(7)) {
                    passingBedSevenPlus = "7"
                    passingBedIdList.remove(7)
                }
            }
            if (!(selectedFilterMainItems1.filter { it -> it.checked }.isNullOrEmpty())) {
                if (selectedFilterMainItems1.filter { it -> it.checked }[0].typeModelName == "Commercial") {
                    passingBedIdList = ArrayList()
                    passingBedSevenPlus = ""
                }
            }
            if (tvMinPrice.text.trim().toString() == "Any") {
                passingMinPrice = ""
            }
            if (tvMaxPrice.text.trim().toString() == "Any") {
                passingMaxPrice = ""
            }

            Log.e("rentFinal", Gson().toJson(passingPropertyToForFilter))
            Log.e("typeFinal", Gson().toJson(passingCategoryForFilter))
            Log.e("typeList", Gson().toJson(passingCategoryTypeIdList))
            Log.e("bedList", Gson().toJson(passingBedIdList))
            Log.e("maxBed", passingBedSevenPlus)
            Log.e("maxFinal", passingMaxPrice)
            Log.e("minFinal", passingMinPrice)
            homePropertyEventModelList.clear()
            homeViewModel.fetchFilterPropertyList(
                passingFilterPageNo.toString(), passingPropertyToForFilter,
                passingCategoryTypeIdList, passingCategoryForFilter, passingMinPrice,
                passingMaxPrice, "4", passingBedIdList, passingBedSevenPlus
            )
        }
        constraintAppBarHome.setOnClickListener {
            val intent = Intent(this, SearchPropertyActivity::class.java)
            startActivity(intent)
        }
        btnCancelHome.setOnClickListener {
            expand.collapse()
            // btnCancelHome.visibility = View.GONE
            // btnApplyHome.visibility = View.GONE
            //includeFilterMainType.visibility = View.GONE
            //includeFilterMainPrice.visibility = View.GONE
            tvFilterItemRent.background = ContextCompat.getDrawable(
                this,
                R.drawable.bg_round_border_white_radius_12
            )
            tvFilterItemRent.setTextColor(ContextCompat.getColor(this, R.color.white))
            tvFilterItemType.background =
                ContextCompat.getDrawable(this, R.drawable.bg_round_border_white_radius_12)
            tvFilterItemType.setTextColor(ContextCompat.getColor(this, R.color.white))
            tvFilterItemPrice.background =
                ContextCompat.getDrawable(this, R.drawable.bg_round_border_white_radius_12)
            tvFilterItemPrice.setTextColor(ContextCompat.getColor(this, R.color.white))
            tvFilterItemBed.background =
                ContextCompat.getDrawable(this, R.drawable.bg_round_border_white_radius_12)
            tvFilterItemBed.setTextColor(ContextCompat.getColor(this, R.color.white))
            initData()
        }
        tvFilterItemRent.setOnClickListener {
            selectedFilter(0)
            tvFilterItemRent.background = ContextCompat.getDrawable(
                this,
                R.drawable.bg_round_border_white_radius_12_inner_white
            )
            tvFilterItemRent.setTextColor(ContextCompat.getColor(this, R.color.green_solid))
            tvFilterItemType.background =
                ContextCompat.getDrawable(this, R.drawable.bg_round_border_white_radius_12)
            tvFilterItemType.setTextColor(ContextCompat.getColor(this, R.color.white))
            tvFilterItemPrice.background =
                ContextCompat.getDrawable(this, R.drawable.bg_round_border_white_radius_12)
            tvFilterItemPrice.setTextColor(ContextCompat.getColor(this, R.color.white))
            tvFilterItemBed.background =
                ContextCompat.getDrawable(this, R.drawable.bg_round_border_white_radius_12)
            tvFilterItemBed.setTextColor(ContextCompat.getColor(this, R.color.white))
        }
        tvFilterItemType.setOnClickListener {
            selectedFilter(1)
            tvFilterItemRent.background =
                ContextCompat.getDrawable(this, R.drawable.bg_round_border_white_radius_12)
            tvFilterItemRent.setTextColor(ContextCompat.getColor(this, R.color.white))
            tvFilterItemType.background = ContextCompat.getDrawable(
                this,
                R.drawable.bg_round_border_white_radius_12_inner_white
            )
            tvFilterItemType.setTextColor(ContextCompat.getColor(this, R.color.green_solid))
            tvFilterItemPrice.background =
                ContextCompat.getDrawable(this, R.drawable.bg_round_border_white_radius_12)
            tvFilterItemPrice.setTextColor(ContextCompat.getColor(this, R.color.white))
            tvFilterItemBed.background =
                ContextCompat.getDrawable(this, R.drawable.bg_round_border_white_radius_12)
            tvFilterItemBed.setTextColor(ContextCompat.getColor(this, R.color.white))
        }
        tvFilterItemPrice.setOnClickListener {
            selectedFilter(2)
            tvFilterItemRent.background =
                ContextCompat.getDrawable(this, R.drawable.bg_round_border_white_radius_12)
            tvFilterItemRent.setTextColor(ContextCompat.getColor(this, R.color.white))
            tvFilterItemType.background =
                ContextCompat.getDrawable(this, R.drawable.bg_round_border_white_radius_12)
            tvFilterItemType.setTextColor(ContextCompat.getColor(this, R.color.white))
            tvFilterItemPrice.background = ContextCompat.getDrawable(
                this,
                R.drawable.bg_round_border_white_radius_12_inner_white
            )
            tvFilterItemPrice.setTextColor(ContextCompat.getColor(this, R.color.green_solid))
            tvFilterItemBed.background =
                ContextCompat.getDrawable(this, R.drawable.bg_round_border_white_radius_12)
            tvFilterItemBed.setTextColor(ContextCompat.getColor(this, R.color.white))
        }
        tvFilterItemBed.setOnClickListener {
            selectedFilter(3)
            tvFilterItemRent.background =
                ContextCompat.getDrawable(this, R.drawable.bg_round_border_white_radius_12)
            tvFilterItemRent.setTextColor(ContextCompat.getColor(this, R.color.white))
            tvFilterItemType.background =
                ContextCompat.getDrawable(this, R.drawable.bg_round_border_white_radius_12)
            tvFilterItemType.setTextColor(ContextCompat.getColor(this, R.color.white))
            tvFilterItemPrice.background =
                ContextCompat.getDrawable(this, R.drawable.bg_round_border_white_radius_12)
            tvFilterItemPrice.setTextColor(ContextCompat.getColor(this, R.color.white))
            tvFilterItemBed.background = ContextCompat.getDrawable(
                this,
                R.drawable.bg_round_border_white_radius_12_inner_white
            )
            tvFilterItemBed.setTextColor(ContextCompat.getColor(this, R.color.green_solid))
        }
        btnTryAgain.setOnClickListener {
            if (this.isConnected) {
                includeNoInternetHome.visibility = View.GONE
                pageNo = 1
                homeViewModel.getPropertyList(
                    passLat.toString(),
                    passLng.toString(),
                    pageNo.toString()
                )
            } else {
                includeNoInternetHome.visibility = View.VISIBLE
                linearNoPropertyFound.visibility = View.GONE
                rvPropertyList.visibility = View.GONE
            }
        }
        tvNotification.setOnClickListener {
            if (CommonMethods.isOpenRecently()) return@setOnClickListener
            startActivity(Intent(this, NotificationActivity::class.java))
        }

        var click = false
        tvFindProperty.setCompoundDrawablesWithIntrinsicBounds(
            R.drawable.ic_search_gray,
            0,
            R.drawable.ic__arrow_down_24,
            0
        )
        tvFindProperty.setOnClickListener {
            if (CommonMethods.isOpenRecently()) return@setOnClickListener
            if (!click) {
                expandable_layout.expand()
                //lvq.visibility = View.VISIBLE
                click = true
                tvFindProperty.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_search_gray,
                    0,
                    R.drawable.ic_arrow_up_24,
                    0
                )
            } else {
                expandable_layout.collapse()
                //lvq.visibility = View.GONE
                click = false
                tvFindProperty.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_search_gray,
                    0,
                    R.drawable.ic__arrow_down_24,
                    0
                )
            }
        }
        tvFind.setOnClickListener {
            if (CommonMethods.isOpenRecently()) return@setOnClickListener
            val intent = Intent(this, SideMenuActivity::class.java)
            intent.putExtra(TYPE, TYPE_MENUE.FINDPROPERTY.name)
            startActivity(intent)
        }
    }

    /*  fun View.fadeVisibility(visibility: Int, duration: Long = 500) {
          val transition: Transition = Fade()
          transition.duration = duration
          transition.addTarget(this)
          TransitionManager.beginDelayedTransition(this.parent as ViewGroup, transition)
          this.visibility = visibility
      }*/

    private fun openLogoutDialog() {
        dialog = Dialog(this)
        dialog?.setCancelable(true)
        dialog?.setContentView(R.layout.layout_logout)

        val btnOk = dialog?.findViewById(R.id.btnOk) as Button
        val btnCancel = dialog?.findViewById(R.id.btnCancel) as Button
        val ivClose = dialog?.findViewById(R.id.ivClose) as ImageView

        dialog?.show()
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog?.window?.attributes)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog?.window?.attributes = layoutParams

        btnOk.setOnClickListener {
            homeViewModel.userLogout()
        }
        btnCancel.setOnClickListener {
            dialog?.dismiss()
        }
        ivClose.setOnClickListener {
            dialog?.dismiss()
        }

    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            if (doubleBackToExitPressedOnce) {
                finishAffinity()
            } else {

                this.doubleBackToExitPressedOnce = true
                drawer_layout.snack(getString(R.string.toastPressBack))
                // Toast.makeText(this, getString(R.string.toastPressBack), Toast.LENGTH_SHORT).show()
                Timer().schedule(2000) {
                    doubleBackToExitPressedOnce = false
                }
                //super.onBackPressed()
            }
        }
    }

    override fun getViewBinging(): ActivityHomeBinding = ActivityHomeBinding.inflate(layoutInflater)

}