package com.property.propertyuser.ui.main.side_menu.find_property

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListPopupWindow
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
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
import com.property.propertyuser.base.BaseFragment
import com.property.propertyuser.databinding.FragmentFindPropertyBinding
import com.property.propertyuser.databinding.FragmentOwnedViewDetailsBinding
import com.property.propertyuser.listeners.ActivityListener
import com.property.propertyuser.modal.fetch_requested_property_data.PropertyCategory
import com.property.propertyuser.modal.main_filter_details.Category
import com.property.propertyuser.preference.AppPreferences
import com.property.propertyuser.ui.main.payment.PaymentActivity
import com.property.propertyuser.ui.main.side_menu.SideMenuActivity
import com.property.propertyuser.ui.main.side_menu.requested_property.RequestedPropertyFragment
import com.property.propertyuser.utils.Status
import com.property.propertyuser.utils.Toaster
import com.property.propertyuser.utils.currencyCountWithSuffix
import com.property.propertyuser.utils.isConnected
import com.skydoves.powerspinner.SpinnerGravity
import kotlinx.android.synthetic.main.activity_filter.*
import kotlinx.android.synthetic.main.fragment_dialog_book_tour_date_time.*
import kotlinx.android.synthetic.main.fragment_find_property.*
import kotlinx.android.synthetic.main.fragment_find_property.priceRangeSlider
import kotlinx.android.synthetic.main.fragment_signup.*
import kotlinx.android.synthetic.main.layout_no_network.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt


class FindPropertyFragment:BaseFragment() {
    private lateinit var findPropertyViewModel: FindPropertyViewModel
    private lateinit var activityListener: ActivityListener
    private var  buildingTypeList =ArrayList<PropertyCategory>()
    private var  buildingTypeNameList =ArrayList<String>()
    private var  buildingTypeIdList =ArrayList<String>()
    private var propertyToSelected=""
    private var categorySelected=""
    private var typeSelected=""
    private var selectedMinRange=""
    private var selectedMaxRange=""
    private lateinit var binding: FragmentFindPropertyBinding
    private var premissionGranded:Boolean=false
    private var placesClient: PlacesClient? = null
    private val PLACE_PICKER_REQUEST = 3
    private var passLng=0.0
    private var passLat=0.0
    private var passLocation=""
    private var spPropertyToCheck=false
    private var spCategoryCheck=false
    private var spBuildingTypeCheck=false
    override fun setView(
        inflater: LayoutInflater?,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentFindPropertyBinding.inflate(inflater!!,container,false)
        val view=binding.root
        return view
       /* return inflater?.inflate(R.layout.fragment_find_property,container,false)*/
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityListener=activity as SideMenuActivity

    }
    override fun initData() {
        activityListener.setTitle(getString(R.string.findProperty))
        buildingTypeList= ArrayList()
        buildingTypeIdList= ArrayList()
        buildingTypeNameList= ArrayList()

        if (!Places.isInitialized()) {
            Places.initialize(requireContext(), getString(R.string.google_maps_key))
        }

        // Create a new Places client instance.
        placesClient = Places.createClient(requireContext())

        findPropertyViewModel.fetchRequestedPropertyData()
//        shimmer_view_container.startShimmer()
      /*  if(AppPreferences.chooseLanguage=="arabic"){
            spPropertyTo.arrowGravity= SpinnerGravity.START
            spCategory.arrowGravity= SpinnerGravity.START
            spBuildingType.arrowGravity= SpinnerGravity.START
        }else{
            spPropertyTo.arrowGravity= SpinnerGravity.END
            spCategory.arrowGravity= SpinnerGravity.END
            spBuildingType.arrowGravity= SpinnerGravity.END
        }*/
        setupPropertyToDropDown()
        setupCategoryDropDown()
        //setupBuildingTypeDropDown()
//        shimmer_view_container.stopShimmer()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                val place = Autocomplete.getPlaceFromIntent(data!!)
                Log.e("placedata", Gson().toJson(place))
                Log.e("place details", "Place: " + place.name + ", " + place.id)
                passLat=place.latLng!!.latitude
                passLng=place.latLng!!.longitude
                tvPreferredLocation.text=place.name
                tvPreferredLocation.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
                passLocation= place.name!!

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                val status = Autocomplete.getStatusFromIntent(data!!)
                Log.e("onstatus error", status.statusMessage.toString())
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }
    private fun setupPropertyToDropDown(){
       /* val properties= listOf<String>(
            getString(R.string.for_rent),getString(R.string.for_sale))
        spPropertyTo.setItems(properties)
        spPropertyTo.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newText ->
            Log.e("propertyTo",newIndex.toString())
            propertyToSelected=newIndex.toString()
           //Toast.makeText(context,newText,Toast.LENGTH_SHORT).show()
        }*/
        val properties= listOf<String>(
            getString(R.string.for_rent),getString(R.string.for_sale))

        val listPopupWindowPropertyTo = ListPopupWindow(requireContext(), null, R.attr.listPopupWindowStyle)
        listPopupWindowPropertyTo.anchorView =spPropertyTo
        val adapter = ArrayAdapter(requireContext(), R.layout.list_popup_window_item, properties)
        listPopupWindowPropertyTo.setAdapter(adapter)

        // Set list popup's item click listener
        listPopupWindowPropertyTo.setOnItemClickListener { parent: AdapterView<*>?, view: View?, position: Int, id: Long ->
            // Respond to list popup window item click.
            spPropertyTo.setTextColor(Color.BLACK)
            spPropertyTo.text=properties[position]
            propertyToSelected=position.toString()
            // Dismiss popup.
            listPopupWindowPropertyTo.dismiss()
            spPropertyToCheck=false
        }
        spPropertyTo.setOnClickListener {
            if(!spPropertyToCheck){
                spPropertyToCheck=true
                listPopupWindowPropertyTo.show()
            }
            else{
                spPropertyToCheck=false
                listPopupWindowPropertyTo.dismiss()
            }
        }
    }
    private fun setupCategoryDropDown(){
        /*val category= listOf<String>(
            getString(R.string.residential),getString(R.string.commercial))
        spCategory.setItems(category)
        spCategory.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newText ->
            Log.e("category",newIndex.toString())
            categorySelected=newIndex.toString()
           // Toast.makeText(context,newText,Toast.LENGTH_SHORT).show()
        }*/
        val category= listOf<String>(
            getString(R.string.residential),getString(R.string.commercial))

        val listPopupWindowCategory = ListPopupWindow(requireContext(), null, R.attr.listPopupWindowStyle)
        listPopupWindowCategory.anchorView =spCategory
        val adapter = ArrayAdapter(requireContext(), R.layout.list_popup_window_item, category)
        listPopupWindowCategory.setAdapter(adapter)

        // Set list popup's item click listener
        listPopupWindowCategory.setOnItemClickListener { parent: AdapterView<*>?, view: View?, position: Int, id: Long ->
            // Respond to list popup window item click.
            spCategory.setTextColor(Color.BLACK)
            spCategory.text=category[position]
            categorySelected=position.toString()
            // Dismiss popup.
            listPopupWindowCategory.dismiss()
            spCategoryCheck=false
            findPropertyViewModel.fetchMainFilterCategories(position.toString())
        }
        spCategory.setOnClickListener {
            if(!spCategoryCheck){
                spCategoryCheck=true
                listPopupWindowCategory.show()
            }
            else{
                spCategoryCheck=false
                listPopupWindowCategory.dismiss()
            }
        }
    }
    private fun setupBuildingTypeDropDown(){
        for (i in 0 until buildingTypeList.size){
            buildingTypeNameList.add(buildingTypeList[i].type)
            buildingTypeIdList.add(buildingTypeList[i].id.toString())
        }
        priceRangeSlider.setLabelFormatter { value: Float ->
            getString(R.string.sar).plus(" ").plus(value.toDouble().currencyCountWithSuffix())
        }

        val listPopupWindowBuildingType = ListPopupWindow(requireContext(), null, R.attr.listPopupWindowStyle)
        listPopupWindowBuildingType.anchorView =spBuildingType
        val adapter = ArrayAdapter(requireContext(), R.layout.list_popup_window_item, buildingTypeNameList)
        listPopupWindowBuildingType.setAdapter(adapter)

        // Set list popup's item click listener
        listPopupWindowBuildingType.setOnItemClickListener { parent: AdapterView<*>?, view: View?, position: Int, id: Long ->
            // Respond to list popup window item click.
            spBuildingType.setTextColor(Color.BLACK)
            spBuildingType.text=buildingTypeNameList[position]
            typeSelected=buildingTypeIdList[position]
            // Dismiss popup.
            listPopupWindowBuildingType.dismiss()
            spBuildingTypeCheck=false
        }
        spBuildingType.setOnClickListener {
            if(!spBuildingTypeCheck){
                spBuildingTypeCheck=true
                listPopupWindowBuildingType.show()
            }
            else{
                spBuildingTypeCheck=false
                listPopupWindowBuildingType.dismiss()
            }
        }
    }
    override fun setupUI() {

    }

    override fun setupViewModel() {
        findPropertyViewModel=FindPropertyViewModel()
    }

    override fun setupObserver() {
        findPropertyViewModel.getRequestedPropertyDataResponse().observe(this, Observer {
            when(it.status){
                Status.LOADING->showLoader()
                Status.SUCCESS->{
                    dismissLoader()
                    includeNoInternet.visibility=View.GONE
                    if(it.data!=null){
                        Log.e("response event pacage", Gson().toJson(it))
                        if(it.data.response_data!=null){
                            linearMain.visibility=View.VISIBLE
                            linearNoDataFound.visibility=View.GONE
                            if(!(it.data.response_data.property_category.isNullOrEmpty())){
                                buildingTypeList=it.data.response_data.property_category as ArrayList<PropertyCategory>
                            }
                            if(it.data.response_data.price_range!=null){
                                //priceRangeSlider.valueFrom=it.data.response_data.price_range.min_price.toFloat()
                                //priceRangeSlider.valueTo=it.data.response_data.price_range.max_price.toFloat()

                                selectedMinRange=
                                    priceRangeSlider.valueFrom.toLong().toBigInteger().toString()
                                selectedMaxRange=
                                    priceRangeSlider.valueTo.toLong().toBigInteger().toString()
                                Log.e("selected", "$selectedMinRange---$selectedMaxRange")

                            }
                            setupBuildingTypeDropDown()
                        }
                    }
                    else{
                        linearMain.visibility=View.GONE
                        linearNoDataFound.visibility=View.VISIBLE
                    }
                }
                Status.DATA_EMPTY->{
                    dismissLoader()
                    Toaster.showToast(requireContext(),getString(R.string.data_empty), Toaster.State.ERROR, Toast.LENGTH_LONG)
                }
                Status.NO_INTERNET->{
                    dismissLoader()
                    if(requireContext().isConnected){
                        Toaster.showToast(requireContext(),getString(R.string.something_wrong),
                            Toaster.State.ERROR, Toast.LENGTH_LONG)
                    }else{
                        includeNoInternet.visibility=View.VISIBLE
                        linearMain.visibility=View.GONE
                        linearNoDataFound.visibility=View.GONE
                    }
                }

            }
        })
        findPropertyViewModel.getRequestedDesiredPropertyResponse().observe(this, Observer {
            when(it.status){
                Status.LOADING->showLoader()
                Status.SUCCESS->{
                    dismissLoader()
                    if(it.data!=null){
                        Log.e("response event pacage", Gson().toJson(it))
                        Toaster.showToast(requireContext(),it.data.response,
                            Toaster.State.SUCCESS, Toast.LENGTH_LONG)
                        activityListener.navigateToFragment(RequestedPropertyFragment())
                    }
                    else{
                        Toaster.showToast(requireContext(),getString(R.string.data_empty),
                            Toaster.State.ERROR, Toast.LENGTH_LONG)
                    }
                }
                Status.DATA_EMPTY->{
                    dismissLoader()
                    Toaster.showToast(requireContext(),getString(R.string.data_empty), Toaster.State.ERROR, Toast.LENGTH_LONG)
                }
                Status.NO_INTERNET->{
                    dismissLoader()
                    if(requireContext().isConnected){
                        Toaster.showToast(requireContext(),getString(R.string.something_wrong),
                            Toaster.State.ERROR, Toast.LENGTH_LONG)
                    }else{
                        Toaster.showToast(requireContext(),getString(R.string.no_internet),
                            Toaster.State.ERROR, Toast.LENGTH_LONG)
                    }
                }

            }
        })
        findPropertyViewModel.getMainFilterCategoriesResponse().observe(this, Observer { it ->
            when(it.status){
                Status.LOADING->{
                    showLoader()
                }
                Status.SUCCESS->{
                    dismissLoader()
                    Log.e("responsecheck", Gson().toJson(it))
                    if(it.data!=null){
                        buildingTypeList.clear()
                        typeSelected=""
                        spBuildingType.text=getString(R.string.spCategoryHint)
                        if(!(it.data.categories.isNullOrEmpty())){
                            for(i in it.data.categories.indices){
                                buildingTypeList.add(
                                    PropertyCategory(it.data.categories[i].category,it.data.categories[i].id,
                                    it.data.categories[i].type)
                                )
                            }
                            buildingTypeIdList.clear()
                            buildingTypeNameList.clear()
                            setupBuildingTypeDropDown()
                        }
                    }

                }
                Status.DATA_EMPTY->{
                    dismissLoader()
                    Toaster.showToast(requireContext(),getString(R.string.data_empty), Toaster.State.ERROR, Toast.LENGTH_LONG)
                }
                Status.NO_INTERNET->{
                    dismissLoader()
                    if(requireContext().isConnected){
                        Toaster.showToast(requireContext(),getString(R.string.something_wrong),
                            Toaster.State.ERROR, Toast.LENGTH_LONG)
                    }else{
                        Toaster.showToast(requireContext(),getString(R.string.no_internet),
                            Toaster.State.ERROR, Toast.LENGTH_LONG)
                    }
                }

            }
        })
    }
    private fun getLocationDetails(){
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            methodWithPermissions()
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
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
            .build(requireContext())
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
                Toaster.showToast(requireContext(),getString(R.string.allow_location_permission), Toaster.State.WARNING,Toast.LENGTH_SHORT)
            }
        }
    }
    override fun onClicks() {
        btnDiscard.setOnClickListener {
            val intent = requireActivity().intent
            requireActivity().finish()
            startActivity(intent)
        }
        priceRangeSlider.addOnSliderTouchListener(object : RangeSlider.OnSliderTouchListener {
            @SuppressLint("RestrictedApi")
            override fun onStartTrackingTouch(slider: RangeSlider) {
                // Responds to when slider's touch event is being started
                Log.e("onStart",slider.values.toString())

                selectedMinRange=slider.values[0].toString()
                selectedMaxRange=slider.values[1].toString()
            }

            @SuppressLint("RestrictedApi")
            override fun onStopTrackingTouch(slider: RangeSlider) {
                // Responds to when slider's touch event is being stopped
                Log.e("onStop",slider.values.toString())
                selectedMinRange=slider.values[0].toString()
                selectedMaxRange=slider.values[1].toString()
            }
        })
        btnOpenMap.setOnClickListener {
            getLocationDetails()
        }

        btnViewStatus.setOnClickListener {
            activityListener.navigateToFragment(RequestedPropertyFragment())
        }
        btnRequest.setOnClickListener {
            if(propertyToSelected.isBlank()){
                Toaster.showToast(requireContext(),getString(R.string.property_to_required),
                    Toaster.State.WARNING, Toast.LENGTH_LONG)
            }
            else if(categorySelected.isBlank()){
                Toaster.showToast(requireContext(),getString(R.string.category_required),
                    Toaster.State.WARNING, Toast.LENGTH_LONG)
            }
            else if(typeSelected.isBlank()){
                Toaster.showToast(requireContext(),getString(R.string.property_type_required),
                    Toaster.State.WARNING, Toast.LENGTH_LONG)
            }
            else if(etArea.text.trim().toString().isBlank()){
                Toaster.showToast(requireContext(),getString(R.string.area_required),
                    Toaster.State.WARNING, Toast.LENGTH_LONG)
            }
            else if(etArea.text.trim().toString().isBlank()){
                Toaster.showToast(requireContext(),getString(R.string.area_required),
                    Toaster.State.WARNING, Toast.LENGTH_LONG)
            }
            else if(etAdditionalDetails.text.trim().toString().isBlank()){
                Toaster.showToast(requireContext(),getString(R.string.description_required),
                    Toaster.State.WARNING, Toast.LENGTH_LONG)
            }
            else if(selectedMinRange.isBlank()&&selectedMaxRange.isBlank()){
                Toaster.showToast(requireContext(),getString(R.string.range_required),
                    Toaster.State.WARNING, Toast.LENGTH_LONG)
            }
            else if(passLat.toString().isNullOrEmpty()||passLng.toString().isNullOrEmpty()){
                Toaster.showToast(requireContext(),getString(R.string.location_details_needed),
                    Toaster.State.WARNING, Toast.LENGTH_LONG)
            }
            else{
                Log.e("minprice",selectedMinRange)
                Log.e("minprice1",selectedMinRange.toFloat().toString())
                Log.e("minprice11",selectedMinRange.toFloat().toLong().toBigInteger().toString())
                Log.e("maxprice",selectedMaxRange)
                Log.e("maxprice1",selectedMaxRange.toFloat().toString())
                Log.e("maxprice11",selectedMaxRange.toFloat().toLong().toBigInteger().toString())
                Log.e("property to",propertyToSelected)
                Log.e("category",categorySelected)
                Log.e("type",typeSelected)
                Log.e("area",etArea.text.trim().toString())
                Log.e("des",etAdditionalDetails.text.trim().toString())

                findPropertyViewModel.requestDesiredProperty(propertyToSelected,categorySelected,typeSelected,
                    etArea.text.trim().toString(),selectedMinRange.toFloat().toLong().toBigInteger(),
                    selectedMaxRange.toFloat().toLong().toBigInteger(),passLat.toString(),
                    passLng.toString(),etAdditionalDetails.text.trim().toString(),passLocation)

            }
        }
        btnTryAgain.setOnClickListener {
            if(requireContext().isConnected){
                includeNoInternet.visibility=View.GONE
                linearMain.visibility=View.VISIBLE
                linearNoDataFound.visibility=View.GONE
                findPropertyViewModel.fetchRequestedPropertyData()
            }
        }
    }
}