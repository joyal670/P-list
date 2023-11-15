package com.property.propertyuser.ui.main.filter

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.allShouldShowRationale
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.extension.send
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.slider.RangeSlider
import com.google.gson.Gson
import com.property.propertyuser.R
import com.property.propertyuser.base.BaseActivity
import com.property.propertyuser.databinding.ActivityFilterBinding
import com.property.propertyuser.dialogs.book_a_tour.BookATourDateAndTimeDialogFragment
import com.property.propertyuser.modal.main_filter_details.Amenity
import com.property.propertyuser.modal.main_filter_details.Category
import com.property.propertyuser.modal.main_filter_details.Frequency
import com.property.propertyuser.modal.property_list.Event
import com.property.propertyuser.modal.property_list.Property
import com.property.propertyuser.modal.testing.HomePropertyEventModel
import com.property.propertyuser.pojo.AvailabilityModelData
import com.property.propertyuser.pojo.BathsModelData
import com.property.propertyuser.pojo.BedRoomsModelData
import com.property.propertyuser.pojo.FurnishingModelData
import com.property.propertyuser.preference.AppPreferences
import com.property.propertyuser.ui.main.filter.adapter.*
import com.property.propertyuser.ui.main.home.HomeViewModel
import com.property.propertyuser.ui.main.home.adapter.TestMainRecyclerViewAdapter
import com.property.propertyuser.ui.main.map_and_nearby.MapAndNearByActivity
import com.property.propertyuser.ui.main.property_details.PropertyDetailsActivity
import com.property.propertyuser.utils.*
import kotlinx.android.synthetic.main.activity_filter.*
import kotlinx.android.synthetic.main.layout_no_network.*
import kotlinx.android.synthetic.main.toolbar_filter.*
import kotlin.collections.ArrayList

class FilterActivity : BaseActivity<ActivityFilterBinding>() {
    override fun getViewBinging(): ActivityFilterBinding = ActivityFilterBinding.inflate(layoutInflater)
    private lateinit var filterViewModel: FilterViewModel
    private lateinit var homeViewModel: HomeViewModel
    private var rentalFrequencyList = ArrayList<Frequency>()
    private lateinit var selectedFrequencyArrayList:ArrayList<Int>
    private val bedRoomList = ArrayList<BedRoomsModelData>()
    private lateinit var selectedBedArrayList:ArrayList<Int>
    private val bathList = ArrayList<BathsModelData>()
    private lateinit var selectedBathArrayList:ArrayList<Int>
    private val furnishingList = ArrayList<FurnishingModelData>()
    private lateinit var selectedFurnishedArrayList:ArrayList<Int>
    private val availabilityList = ArrayList<AvailabilityModelData>()
    private var amenitiesList = ArrayList<Amenity>()
    private lateinit var selectedAmenitiesArrayList:ArrayList<Int>
    private lateinit var selectedPropertyTypeArrayList:ArrayList<Int>
    private var filterMainSubCategory=ArrayList<Category>()

    private var homePropertyEventModelList=ArrayList<HomePropertyEventModel>()
    private val listEvent : List<Event> = listOf()
    private val listProperty : List<Property> = listOf()
    private lateinit var newFragment: DialogFragment
    private var pageNo=0
    private var totalPageCount=0
    private var isLoading: Boolean = false
    private lateinit var layoutManager : LinearLayoutManager
    private lateinit var testMainRecyclerViewAdapter: TestMainRecyclerViewAdapter

    private var bedId=""
    private var bathId=""
    private var areaId=""
    private var minPrice=""
    private var maxPrice=""
    private var minArea=""
    private var maxArea=""
    private var selectedCategoryType=""
    private var selectedPropertyTo=""
    private var passingBedSevenPlus=""
    private var passingBathSevenPlus=""
    private var premissionGranded:Boolean=false
    private var placesClient: PlacesClient? = null
    private val PLACE_PICKER_REQUEST = 3
    private var passLng=0.0
    private var passLat=0.0
    private var filterParamsVisible=false
    private var propertyVisible=false

    override val layoutId: Int
        get() = R.layout.activity_filter
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = false

    override fun fragmentLaunch() {
    }

    override fun initData() {
        homePropertyEventModelList= ArrayList()
        filterMainSubCategory=ArrayList()
        rentalFrequencyList= ArrayList()
        amenitiesList= ArrayList()
        selectedPropertyTypeArrayList= ArrayList<Int>()
        selectedFrequencyArrayList= ArrayList<Int>()
        selectedAmenitiesArrayList= ArrayList<Int>()
        selectedFurnishedArrayList= ArrayList<Int>()
        selectedBedArrayList= ArrayList<Int>()
        selectedBathArrayList= ArrayList<Int>()

        if (!Places.isInitialized()) {
            Places.initialize(this, getString(R.string.google_maps_key))
        }

        // Create a new Places client instance.
        placesClient = Places.createClient(this)

        setPropertyTypeRecyclerView()
        setRentalFrequencyRecyclerView()
        setBedRoomyRecyclerView()
        setBathRecyclerView()
        setFurnishingRecyclerView()
        setAvailabilityRecyclerView()
        setAmenitiesRecyclerView()
        priceRangeSlider.setLabelFormatter { value: Float ->
            getString(R.string.sar).plus(" ").plus(value.toDouble().currencyCountWithSuffix())
        }
        areaRangeSlider.setLabelFormatter{area: Float ->
            area.toInt().toString().plus(getString(R.string.sq_m))
        }

        priceRangeSlider.addOnSliderTouchListener(object : RangeSlider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: RangeSlider) {
                // Responds to when slider's touch event is being started
                Log.e("onStart price",slider.values.toString())

                minPrice=slider.values[0].toFloat().toLong().toBigInteger().toString()
                maxPrice=slider.values[1].toFloat().toLong().toBigInteger().toString()
            }

            override fun onStopTrackingTouch(slider: RangeSlider) {
                // Responds to when slider's touch event is being stopped
                Log.e("onStop price",slider.values.toString())
                minPrice=slider.values[0].toFloat().toLong().toBigInteger().toString()
                maxPrice=slider.values[1].toFloat().toLong().toBigInteger().toString()

                tvPriceMinRange.text=slider.values[0].toDouble().currencyCountWithSuffix()
                tvPriceMaxRange.text=slider.values[1].toDouble().currencyCountWithSuffix()


            }
        })
        areaRangeSlider.addOnSliderTouchListener(object : RangeSlider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: RangeSlider) {
                // Responds to when slider's touch event is being started
                Log.e("onStart area",slider.values.toString())

                minArea=slider.values[0].toFloat().toLong().toBigInteger().toString()
                maxArea=slider.values[1].toFloat().toLong().toBigInteger().toString()
            }

            override fun onStopTrackingTouch(slider: RangeSlider) {
                // Responds to when slider's touch event is being stopped
                Log.e("onStop area",slider.values.toString())
                minArea=slider.values[0].toFloat().toLong().toBigInteger().toString()
                maxArea=slider.values[1].toFloat().toLong().toBigInteger().toString()

                tvAreaInitialRange.text=slider.values[0].toFloat().toLong().toBigInteger().toString()
                tvAreaFinalRange.text=slider.values[1].toFloat().toLong().toBigInteger().toString()
            }
        })

        filterViewModel.fetchFilterDetails()
    }
    private fun setPropertyItemListRecyclerViewTest() {
        layoutManager = LinearLayoutManager(this)
        rvFilterPropertyList.layoutManager = layoutManager
        testMainRecyclerViewAdapter=TestMainRecyclerViewAdapter(this,homePropertyEventModelList,
            { i: Int, s: String ->
                val intent=Intent(this, PropertyDetailsActivity::class.java)
                intent.putExtra(Constants.INTENT_PROPERTY_ID,i)
                startActivity(intent)
            },
            { propertyId ->openMapActivity(propertyId) },
            {showDateAndTimeDialog(it)},{it,pos->addToFav(it,pos)},{it,pos->removeFromFav(it,pos)})
        rvFilterPropertyList.adapter = testMainRecyclerViewAdapter

        rvFilterPropertyList.addOnScrolledToEnd{
            if (!isLoading) {
                Log.e("end","reached")
                loadData()
                isLoading = true
            }
        }
    }

    private fun openMapActivity(propertyId: Int) {
        val intent=Intent(this, MapAndNearByActivity::class.java)
        intent.putExtra("property_id",propertyId.toString())
        intent.putExtra("from_type","home_property_list")
        startActivity(intent)
    }


    private fun loadData() {
        if(pageNo<=totalPageCount && this.isConnected){
            pageNo+=1
            homePropertyEventModelList.add(HomePropertyEventModel(3,listEvent,
                Property(-1,-1,"",-1,-1,-1,-1,
                    emptyList(),-1,-1,-1,-1,"",-1,
                    "","",-1,"","","",-2)))
            testMainRecyclerViewAdapter.notifyItemInserted(homePropertyEventModelList.size-1)
            /*homeViewModel.fetchFilterPropertyList(pageNo.toString(),selectedPropertyTo,
                selectedPropertyTypeArrayList ,selectedCategoryType,minPrice,
                maxPrice,bedId,selectedBedArrayList,passingBedSevenPlus)*/
            if((passLat.toString() == "0.0") &&(passLng.toString() == "0.0")){
                filterViewModel.fetchMainFilterPropertyList(pageNo.toString(),selectedPropertyTo,selectedPropertyTypeArrayList,
                    selectedCategoryType,minPrice,maxPrice,bedId,selectedBedArrayList,passingBedSevenPlus,
                    bathId,selectedBathArrayList,passingBathSevenPlus,areaId,minArea,maxArea,selectedAmenitiesArrayList,
                    "","")
            }
            else{
                filterViewModel.fetchMainFilterPropertyList(pageNo.toString(),selectedPropertyTo,selectedPropertyTypeArrayList,
                    selectedCategoryType,minPrice,maxPrice,bedId,selectedBedArrayList,passingBedSevenPlus,
                    bathId,selectedBathArrayList,passingBathSevenPlus,areaId,minArea,maxArea,selectedAmenitiesArrayList,
                    passLat.toString(),passLng.toString())
            }

        }
    }

    private fun removeFromFav(remFav: Int,pos:Int) {
        Log.e("remove", remFav.toString())
        homeViewModel.updateFavoriteProperty(remFav)
    }

    private fun addToFav(addFav: Int,pos:Int) {
        Log.e("addToFav", addFav.toString())
        homeViewModel.updateFavoriteProperty(addFav)
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
    override fun onClicks() {
        btnToogleRent.setOnClickListener {
            btnToogleRent.setTextColor(ContextCompat.getColor(this,R.color.white))
            btnToogleBuy.setTextColor(ContextCompat.getColor(this,R.color.green_light_1))
            btnToogleBuy.setBackgroundColor(ContextCompat.getColor(this,R.color.white))
            btnToogleRent.setBackgroundColor(ContextCompat.getColor(this,R.color.green_light_1))
            selectedPropertyTo="0"
            btnToogleBuy.setTextIsSelectable(false)
            tvRentalFrequency.visibility=View.VISIBLE
            rvRentalFrequency.visibility=View.VISIBLE
        }
        btnToogleBuy.setOnClickListener {
            btnToogleBuy.setTextColor(ContextCompat.getColor(this,R.color.white))
            btnToogleRent.setTextColor(ContextCompat.getColor(this,R.color.green_light_1))
            btnToogleRent.setBackgroundColor(ContextCompat.getColor(this,R.color.white))
            btnToogleBuy.setBackgroundColor(ContextCompat.getColor(this,R.color.green_light_1))
            selectedPropertyTo="1"
            btnToogleRent.setTextIsSelectable(false)
            tvRentalFrequency.visibility=View.GONE
            rvRentalFrequency.visibility=View.GONE
        }
        btnResidential.setOnClickListener {
            btnResidential.setTextColor(ContextCompat.getColor(this,R.color.white))
            btnCommercial.setTextColor(ContextCompat.getColor(this,R.color.green_light_1))
            btnCommercial.setBackgroundColor(ContextCompat.getColor(this,R.color.white))
            btnResidential.setBackgroundColor(ContextCompat.getColor(this,R.color.green_light_1))
            selectedCategoryType="0"
            btnCommercial.setTextIsSelectable(false)
            filterViewModel.fetchMainFilterCategories("0")
        }
        btnCommercial.setOnClickListener {
            btnCommercial.setTextColor(ContextCompat.getColor(this,R.color.white))
            btnResidential.setTextColor(ContextCompat.getColor(this,R.color.green_light_1))
            btnResidential.setBackgroundColor(ContextCompat.getColor(this,R.color.white))
            btnCommercial.setBackgroundColor(ContextCompat.getColor(this,R.color.green_light_1))
            selectedCategoryType="1"
            btnResidential.setTextIsSelectable(false)
            filterViewModel.fetchMainFilterCategories("1")
        }
        ivBack.setOnClickListener {
            onBackPressed()
        }
        ivFilterShowHide.setOnClickListener {
            linearFilterList.visibility= View.GONE
            nestedMainFilter.visibility=View.VISIBLE
            linearNoPropertyFoundFilter.visibility=View.GONE
        }
        tvResetFilter.setOnClickListener {
            val intent = intent
            finish()
            startActivity(intent)
        }
        etLocationAddress.setOnClickListener {
            getLocationDetails()
        }
        btnApply.setOnClickListener {
            passingBedSevenPlus=""
            passingBathSevenPlus=""
            if(!(selectedBedArrayList.isNullOrEmpty())){
                if(selectedBedArrayList.contains(7)){
                    passingBedSevenPlus="7"
                    selectedBedArrayList.remove(7)
                }
            }
            if(!(selectedBathArrayList.isNullOrEmpty())){
                if(selectedBathArrayList.contains(7)){
                    passingBathSevenPlus="7"
                    selectedBathArrayList.remove(7)
                }
            }

            Log.e("propertyto",selectedPropertyTo)
            Log.e("typeList",Gson().toJson(selectedPropertyTypeArrayList))
            Log.e("category",selectedCategoryType)
            Log.e("maxFinalPrice",maxPrice)
            Log.e("minFinalPrice",minPrice)
            Log.e("bedid",bedId)
            Log.e("bedList",Gson().toJson(selectedBedArrayList))
            Log.e("maxBed",passingBedSevenPlus)
            Log.e("bathid",bathId)
            Log.e("bathList",Gson().toJson(selectedBathArrayList))
            Log.e("maxBed",passingBathSevenPlus)
            Log.e("areaid",areaId)
            Log.e("maxFinalArea",maxArea)
            Log.e("minFinalArea",minArea)
            Log.e("amenities",Gson().toJson(selectedAmenitiesArrayList))
            Log.e("lat",passLat.toString())
            Log.e("lng",passLng.toString())

           callFilterPropertyListApi()
        }
        btnTryAgain.setOnClickListener {
            if(this.isConnected){
                includeNoInternetMainFilter.visibility=View.GONE
                if(filterParamsVisible){
                    filterParamsVisible=false
                    nestedMainFilter.visibility=View.VISIBLE
                    filterViewModel.fetchFilterDetails()
                }
                else{
                    propertyVisible=false
                    callFilterPropertyListApi()
                }
            }
        }

    }

    private fun callFilterPropertyListApi() {
        pageNo=1
        homePropertyEventModelList.clear()

        /*homeViewModel.fetchFilterPropertyList(pageNo.toString(),selectedPropertyTo,
            selectedPropertyTypeArrayList ,selectedCategoryType,minPrice,
            maxPrice,bedId,selectedBedArrayList,passingBedSevenPlus)*/
        if((passLat.toString() == "0.0") &&(passLng.toString() == "0.0")){
            filterViewModel.fetchMainFilterPropertyList(pageNo.toString(),selectedPropertyTo,selectedPropertyTypeArrayList,
                selectedCategoryType,minPrice,maxPrice,bedId,selectedBedArrayList,passingBedSevenPlus,
                bathId,selectedBathArrayList,passingBathSevenPlus,areaId,minArea,maxArea,selectedAmenitiesArrayList,
                "","")
        }
        else{
            filterViewModel.fetchMainFilterPropertyList(pageNo.toString(),selectedPropertyTo,selectedPropertyTypeArrayList,
                selectedCategoryType,minPrice,maxPrice,bedId,selectedBedArrayList,passingBedSevenPlus,
                bathId,selectedBathArrayList,passingBathSevenPlus,areaId,minArea,maxArea,selectedAmenitiesArrayList,
                passLat.toString(),passLng.toString())
        }
    }

    private fun getLocationDetails(){
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            methodWithPermissions()
            return
        }
        premissionGranded=true
        val fields: List<Place.Field> = listOf(
            Place.Field.ID, Place.Field.NAME,
            Place.Field.ADDRESS,
            Place.Field.LAT_LNG)
        val intent: Intent = Autocomplete.IntentBuilder(
            AutocompleteActivityMode.FULLSCREEN, fields
        )
            .build(this)
        startActivityForResult(intent, PLACE_PICKER_REQUEST)
    }
    private fun methodWithPermissions() {
        permissionsBuilder(Manifest.permission.ACCESS_FINE_LOCATION).build().send { result ->
            if (result.allGranted()) {
                permissionsBuilder(Manifest.permission.ACCESS_COARSE_LOCATION).build().send { result1 ->
                    if(result1.allGranted()){
                        getLocationDetails()
                    }
                }
            }
            if(result.allShouldShowRationale()){
                Toaster.showToast(this,"Please allow permissions", Toaster.State.WARNING,Toast.LENGTH_SHORT)
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                val place = Autocomplete.getPlaceFromIntent(data!!)
                Log.e("placedata", Gson().toJson(place))
                Log.e("place details", "Place: " + place.name + ", " + place.id)
                passLat = place.latLng!!.latitude
                passLng = place.latLng!!.longitude
                etLocationAddress.text = place.name
                etLocationAddress.setTextColor(ContextCompat.getColor(this, R.color.black))

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                val status = Autocomplete.getStatusFromIntent(data!!)
                Log.e("onstatus error", status.statusMessage.toString())
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }
    private fun setRentalFrequencyRecyclerView() {
        rvRentalFrequency.adapter = RentalFrequencyAdapter(this,rentalFrequencyList,{
            selectedFrequencyArrayList.add(it)
            Log.e("FrequencyArrayList",selectedFrequencyArrayList.toString())
        },{
            selectedFrequencyArrayList.remove(it)
            Log.e("FrequencyArrayList",selectedFrequencyArrayList.toString())
        })
    }

    @SuppressLint("LongLogTag")
    private fun setPropertyTypeRecyclerView() {
        rvPropertyType.layoutManager = GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false)
        rvPropertyType.adapter = PropertyTypeAdapter(this,filterMainSubCategory,{
            selectedPropertyTypeArrayList.add(it)
            Log.e("selectedPropertyTypeArrayList",selectedPropertyTypeArrayList.toString())
        },{
            selectedPropertyTypeArrayList.remove(it)
            Log.e("selectedPropertyTypeArrayList",selectedPropertyTypeArrayList.toString())
        })
    }
    private fun setBedRoomyRecyclerView() {
        addBedRoomsItems()
        rvBedRooms.layoutManager = GridLayoutManager(this,4,GridLayoutManager.VERTICAL,false)
        rvBedRooms.adapter = BedRoomAdapter(this,bedRoomList,{
            selectedBedArrayList.add(it)
            Log.e("BedArrayList",selectedAmenitiesArrayList.toString())
        },{
            selectedBedArrayList.remove(it)
            Log.e("BedArrayList",selectedAmenitiesArrayList.toString())
        })
    }
    private fun setBathRecyclerView() {
        addBathItems()
        rvBaths.layoutManager = GridLayoutManager(this,4,GridLayoutManager.VERTICAL,false)
        rvBaths.adapter = BathAdapter(this,bathList,{
            selectedBathArrayList.add(it)
            Log.e("BathArrayList",selectedAmenitiesArrayList.toString())
        },{
            selectedBathArrayList.remove(it)
            Log.e("BathArrayList",selectedAmenitiesArrayList.toString())
        })
    }
    private fun setAmenitiesRecyclerView() {
        rvAmenities.layoutManager = GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false)
        rvAmenities.adapter = AmenitiesAdapter(this,amenitiesList,{
            selectedAmenitiesArrayList.add(it)
            Log.e("AmenitiesArrayList",selectedAmenitiesArrayList.toString())
        },{
            selectedAmenitiesArrayList.remove(it)
            Log.e("AmenitiesArrayList",selectedAmenitiesArrayList.toString())
        })
    }
    private fun setAvailabilityRecyclerView() {
        addAvailabilityItems()
        rvAvailability.layoutManager = GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false)
        rvAvailability.adapter = AvailabilityAdapter(this,availabilityList)
    }
    private fun setFurnishingRecyclerView() {
        addFurnishingItems()
        rvFurnishing.layoutManager = GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false)
        rvFurnishing.adapter = FurnishingAdapter(this,furnishingList,{
            selectedFurnishedArrayList.add(it)
            Log.e("furnishedArrayList",selectedFurnishedArrayList.toString())
        },{
            selectedFurnishedArrayList.remove(it)
            Log.e("furnishedArrayList",selectedFurnishedArrayList.toString())
        })
    }
    override fun setupUI() {
    }

    override fun setupViewModel() {
        filterViewModel= FilterViewModel()
        homeViewModel= HomeViewModel()
    }

    override fun setupObserver() {
        filterViewModel.getFilterDetails().observe(this, Observer { it ->
            when(it.status){
                Status.LOADING->{
                    showLoader()
                }
                Status.SUCCESS->{
                    dismissLoader()
                    Log.e("responsecheck", Gson().toJson(it))
                    if(it.data!=null){
                        if(it.data.data!=null){
                            includeNoInternetMainFilter.visibility=View.GONE
                            nestedMainFilter.visibility=View.VISIBLE
                            if(!(it.data.data.category.isNullOrEmpty())){
                                filterMainSubCategory=it.data.data.category as ArrayList<Category>
                                filterMainSubCategory.forEach { it.checked=false }
                                setPropertyTypeRecyclerView()
                            }
                            if(!(it.data.data.frequencies.isNullOrEmpty())){
                                rentalFrequencyList=it.data.data.frequencies as ArrayList<Frequency>
                                rentalFrequencyList.forEach { it.checked=false }
                                setRentalFrequencyRecyclerView()
                            }
                            if(!(it.data.data.details.isNullOrEmpty())){
                                it.data.data.details.forEach {
                                    when (it.name) {
                                        "Beds" -> {
                                            bedId=it.id.toString()
                                        }
                                        "area" -> {
                                            areaId=it.id.toString()
                                        }
                                        "Bathroom" -> {
                                            bathId=it.id.toString()
                                        }
                                    }
                                }
                                Log.e("bedid=", "$bedId,bath=$bathId,area=$areaId")
                            }
                            if (!(it.data.data.amenities.isNullOrEmpty())){
                                amenitiesList=it.data.data.amenities as ArrayList<Amenity>
                                amenitiesList.forEach { it.checked=false }
                                setAmenitiesRecyclerView()
                            }
                        }
                    }

                }
                Status.DATA_EMPTY->{
                    dismissLoader()
                    Toaster.showToast(this,getString(R.string.data_empty), Toaster.State.ERROR, Toast.LENGTH_LONG)
                }
                Status.NO_INTERNET->{
                    dismissLoader()
                    if(this.isConnected){
                        Toaster.showToast(this,getString(R.string.something_wrong),
                            Toaster.State.ERROR, Toast.LENGTH_LONG)
                    }else{
                        includeNoInternetMainFilter.visibility=View.VISIBLE
                        linearNoPropertyFoundFilter.visibility=View.GONE
                        linearFilterList.visibility=View.GONE
                        nestedMainFilter.visibility=View.GONE
                        filterParamsVisible=true
                    }
                }

            }
        })
        filterViewModel.getMainFilterCategoriesResponse().observe(this, Observer { it ->
            when(it.status){
                Status.LOADING->{
                    showLoader()
                }
                Status.SUCCESS->{
                    dismissLoader()
                    Log.e("responsecheck", Gson().toJson(it))
                    if(it.data!=null){
                        filterMainSubCategory.clear()
                        selectedPropertyTypeArrayList.clear()
                        if(!(it.data.categories.isNullOrEmpty())){
                            for(i in it.data.categories.indices){
                                filterMainSubCategory.add(Category(it.data.categories[i].category,
                                it.data.categories[i].id,it.data.categories[i].type,false))
                            }
                            setPropertyTypeRecyclerView()
                        }
                    }

                }
                Status.DATA_EMPTY->{
                    dismissLoader()
                    Toaster.showToast(this,getString(R.string.data_empty), Toaster.State.ERROR, Toast.LENGTH_LONG)
                }
                Status.NO_INTERNET->{
                    dismissLoader()
                    if(this.isConnected){
                        Toaster.showToast(this,getString(R.string.something_wrong),
                            Toaster.State.ERROR, Toast.LENGTH_LONG)
                    }else{
                        Toaster.showToast(this,getString(R.string.no_internet),
                            Toaster.State.ERROR, Toast.LENGTH_LONG)
                    }
                }

            }
        })

        filterViewModel.getMainFilterPropertyListLiveData().observe(this, Observer { it ->
            when(it.status){
                Status.LOADING->{
                    if(homePropertyEventModelList.size==0){
                        showLoader()
                    }
                }
                Status.SUCCESS->{
                    if(homePropertyEventModelList.size==0){
                        dismissLoader()
                    }
                    Log.e("filterHomeResp",Gson().toJson(it))
                    if(it.data!=null){
                        if(it.data.property_data!=null){
                            ivFilterShowHide.visibility=View.VISIBLE
                            linearFilterList.visibility= View.VISIBLE
                            nestedMainFilter.visibility=View.GONE
                            totalPageCount=it.data.property_data.total_page_count
                            if(homePropertyEventModelList.size!=0) {
                                isLoading = false
                                homePropertyEventModelList.removeAt(homePropertyEventModelList.size - 1)
                                testMainRecyclerViewAdapter.notifyItemRemoved(
                                    homePropertyEventModelList.size)
                                if(!(it.data.property_data.properties.isNullOrEmpty())){
                                    for(j in it.data.property_data.properties.indices){
                                        homePropertyEventModelList.
                                        add(HomePropertyEventModel(1,listEvent,it.data.property_data.properties[j]))
                                    }
                                    testMainRecyclerViewAdapter.notifyDataSetChanged()
                                }
                            }
                            else{
                                if(!(it.data.property_data.properties.isNullOrEmpty())){
                                    includeNoInternetMainFilter.visibility=View.GONE
                                    linearNoPropertyFoundFilter.visibility=View.GONE
                                    linearFilterList.visibility= View.VISIBLE
                                    for(k in it.data.property_data.properties.indices){
                                        homePropertyEventModelList.
                                        add(HomePropertyEventModel(1,listEvent,it.data.property_data.properties[k]))
                                    }
                                    setPropertyItemListRecyclerViewTest()
                                }
                                else{
                                    /*constraintMainFilterLayout.snackWhite(getString(R.string.no_property_found))*/
                                    linearNoPropertyFoundFilter.visibility=View.VISIBLE
                                    linearFilterList.visibility= View.GONE
                                }
                            }
                        }
                    }
                }
                Status.DATA_EMPTY->{
                    dismissLoader()
                    Toaster.showToast(this,it.data!!.status.toString(), Toaster.State.ERROR,Toast.LENGTH_LONG)
                }
                Status.NO_INTERNET->{
                    dismissLoader()
                    if(this.isConnected){
                        Toaster.showToast(this,getString(R.string.something_wrong),
                            Toaster.State.ERROR,Toast.LENGTH_LONG)
                    }else{
                        includeNoInternetMainFilter.visibility=View.VISIBLE
                        linearNoPropertyFoundFilter.visibility=View.GONE
                        linearFilterList.visibility=View.GONE
                        nestedMainFilter.visibility=View.GONE
                        propertyVisible=true
                    }
                }

            }
        })
        homeViewModel.getFilterPropertyListLiveData().observe(this,Observer {
            when(it.status){
                Status.LOADING->{
                    if(homePropertyEventModelList.size==0){
                        showLoader()
                    }
                }
                Status.SUCCESS->{
                    if(homePropertyEventModelList.size==0){
                        dismissLoader()
                    }
                    Log.e("filterHomeResp",Gson().toJson(it))
                    if(it.data!=null){
                        if(it.data.property_data!=null){
                            ivFilterShowHide.visibility=View.VISIBLE
                            rvFilterPropertyList.visibility= View.VISIBLE
                            nestedMainFilter.visibility=View.GONE
                            totalPageCount=it.data.property_data.total_page_count
                            if(homePropertyEventModelList.size!=0) {
                                isLoading = false
                                homePropertyEventModelList.removeAt(homePropertyEventModelList.size - 1)
                                testMainRecyclerViewAdapter.notifyItemRemoved(
                                    homePropertyEventModelList.size)
                                if(!(it.data.property_data.properties.isNullOrEmpty())){
                                    for(j in it.data.property_data.properties.indices){
                                        homePropertyEventModelList.
                                        add(HomePropertyEventModel(1,listEvent,it.data.property_data.properties[j]))
                                    }
                                    testMainRecyclerViewAdapter.notifyDataSetChanged()
                                }
                            }
                            else{
                                if(!(it.data.property_data.properties.isNullOrEmpty())){
                                    for(k in it.data.property_data.properties.indices){
                                        homePropertyEventModelList.
                                        add(HomePropertyEventModel(1,listEvent,it.data.property_data.properties[k]))
                                    }
                                    setPropertyItemListRecyclerViewTest()
                                }
                                else{
                                    Toaster.showToast(this,getString(R.string.no_property_found),
                                        Toaster.State.ERROR,Toast.LENGTH_LONG)
                                }
                            }
                        }
                    }
                }
                Status.DATA_EMPTY->{
                    dismissLoader()
                    Toaster.showToast(this,it.data!!.status.toString(), Toaster.State.ERROR,Toast.LENGTH_LONG)
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
        homeViewModel.updateFavoritePropertyResponse().observe(this, androidx.lifecycle.Observer {
            when(it.status){
                Status.LOADING->showLoader()
                Status.SUCCESS->{
                    dismissLoader()
                    Log.e("updateFav", Gson().toJson(it))
                    AppPreferences.reload_property_list=true
                    constraintMainFilterLayout.snack(it.data!!.response)
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
    }



    fun addBedRoomsItems() {
        bedRoomList.add(BedRoomsModelData(1,"1",false))
        bedRoomList.add(BedRoomsModelData(2,"2",false))
        bedRoomList.add(BedRoomsModelData(3,"3",false))
        bedRoomList.add(BedRoomsModelData(4,"4",false))
        bedRoomList.add(BedRoomsModelData(5,"5",false))
        bedRoomList.add(BedRoomsModelData(6,"6",false))
        bedRoomList.add(BedRoomsModelData(7,"7+",false))
    }
    fun addBathItems() {
        bathList.add(BathsModelData(1,"1",false))
        bathList.add(BathsModelData(2,"2",false))
        bathList.add(BathsModelData(3,"3",false))
        bathList.add(BathsModelData(4,"4",false))
        bathList.add(BathsModelData(5,"5",false))
        bathList.add(BathsModelData(6,"6",false))
        bathList.add(BathsModelData(7,"7+",false))
    }
    fun addFurnishingItems() {
        furnishingList.add(FurnishingModelData(0,"Unfurnished",false))
        furnishingList.add(FurnishingModelData(1,"Semi Furnished ",false))
        furnishingList.add(FurnishingModelData(2,"Furnished",false))
    }
    fun addAvailabilityItems() {
        availabilityList.add(AvailabilityModelData("Within A Week"))
        availabilityList.add(AvailabilityModelData("Within 15 Days"))
        availabilityList.add(AvailabilityModelData("Within A Month"))
        availabilityList.add(AvailabilityModelData("After A Month"))
    }


}