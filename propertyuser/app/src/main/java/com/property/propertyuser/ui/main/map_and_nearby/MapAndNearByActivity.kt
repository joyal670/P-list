package com.property.propertyuser.ui.main.map_and_nearby

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.allShouldShowRationale
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.extension.send
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.gson.Gson
import com.google.maps.android.ui.IconGenerator
import com.property.propertyuser.R
import com.property.propertyuser.base.BaseActivity
import com.property.propertyuser.databinding.ActivityMapAndNearByBinding
import com.property.propertyuser.modal.map_near_places.Result
import com.property.propertyuser.ui.main.map_and_nearby.adapter.NearestCommonPlacesAdapter
import com.property.propertyuser.ui.main.map_and_nearby.model.MarkerData
import com.property.propertyuser.ui.main.property_details.PropertyDetailsActivity
import com.property.propertyuser.utils.*
import kotlinx.android.synthetic.main.activity_map_and_near_by.*
import kotlinx.android.synthetic.main.toolbar_map.*

class MapAndNearByActivity : BaseActivity<ActivityMapAndNearByBinding>(), OnMapReadyCallback {
    override fun getViewBinging(): ActivityMapAndNearByBinding =
        ActivityMapAndNearByBinding.inflate(layoutInflater)

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mapAndNearByViewModel: MapAndNearByViewModel
    private var googleMap: GoogleMap? = null
    private var mapInsideClick: Boolean? = true
    private var marker: Marker? = null
    private var fadeOut: Animation? = null
    private var fadeIn: Animation? = null
    private var markerTag: Int = 0
    private var passLng = 0.0
    private var passLat = 0.0
    private var resultsNearByPlaces = ArrayList<Result>()
    private var markerList = ArrayList<Marker>()
    private var markerDataList = ArrayList<MarkerData>()
    private lateinit var tc: IconGenerator
    private lateinit var tcNotSelected: IconGenerator
    private var propertyVisible = false
    private var nearByPlacesVisible = false
    private var searchPlacesVisible = false
    private var nearByPropertyVisible = false
    private var isLocationPermissionOk = false
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private var permissionToRequest = mutableListOf<String>()
    private var placesClient: PlacesClient? = null
    private val PLACE_PICKER_REQUEST = 3
    private var passedPropertyIdMarkerClicked = ""
    private var passedPropertyId = ""
    private var passedFromType = ""
    private var amountPassToMap = ""
    private var passedLatFromOtherClass = ""
    private var passedLngFromOtherClass = ""
    private var isLocationFound = true
    private var selectedPlace = ""

    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 999
    }

    override val layoutId: Int
        get() = R.layout.activity_map_and_near_by
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = false

    override fun fragmentLaunch() {
    }

    override fun initData() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        setCommonPlacesRecyclerView()
        passedPropertyId = intent.getStringExtra("property_id").toString()
        passedFromType = intent.getStringExtra("from_type").toString()
        passedLatFromOtherClass = intent.getStringExtra("passed_lat").toString()
        passedLngFromOtherClass = intent.getStringExtra("passed_lng").toString()


        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        resultsNearByPlaces = ArrayList()
        markerList = ArrayList()
        markerDataList = ArrayList()
        //getLastLocationFun()

        if (!Places.isInitialized()) {
            Places.initialize(this, getString(R.string.google_maps_key))
        }

        // Create a new Places client instance.
        placesClient = Places.createClient(this)


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
                googleMap?.clear()
                marker?.remove()
                tvShowProperties.visibility = View.GONE
                mapAndNearByViewModel.fetchShowAllPropertyLocationDetails(
                    passLat.toString(),
                    passLng.toString()
                )
                //setUpMarkerData()

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                val status = Autocomplete.getStatusFromIntent(data!!)
                Log.e("onstatus error", status.statusMessage.toString())
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        this.googleMap = googleMap
        when {
            PermissionUtils.isAccessFineLocationGranted(this) -> {
                when {
                    PermissionUtils.isLocationEnabled(this) -> {
                        isLocationPermissionOk = true
                        setUpGoogleMap()
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
            setUpGoogleMap()
        }

        task.addOnFailureListener { exception ->
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
                //startLocationUpdates() or do whatever you want
                Log.e("on resolution result", "success")
                setUpGoogleMap()
            } else {
                Toast.makeText(this, "we can't determine your location", Toast.LENGTH_SHORT).show()
            }
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
                            isLocationPermissionOk = true
                            setUpGoogleMap()
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

    private fun setUpGoogleMap() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            isLocationPermissionOk = false
            return
        }
        googleMap?.isMyLocationEnabled = true
        googleMap?.uiSettings?.isTiltGesturesEnabled = true

        if (passedFromType == "home_property_list") {
            mapAndNearByViewModel.fetchHomeListingPropertyLocation(passedPropertyId)
        } else if (passedFromType == "property_details") {
            mapAndNearByViewModel.fetchShowAllNearbyPropertyLocationDetails(
                passedPropertyId,
                passedLatFromOtherClass,
                passedLngFromOtherClass
            )
        } else if (passedFromType == "home_bottom_map") {
            constraintBottom.visibility = View.GONE
            Toast.makeText(
                this,
                getString(R.string.fetching_location), Toast.LENGTH_LONG
            ).show()
            getLastLocationFun()
        } else {
            Toast.makeText(
                this,
                getString(R.string.fetching_location), Toast.LENGTH_LONG
            ).show()
            getLastLocationFun()
        }

    }

    private fun getLastLocationFun() {
        permissionsBuilder(Manifest.permission.ACCESS_FINE_LOCATION).build().send { result ->
            if (result.allGranted()) {
                permissionsBuilder(Manifest.permission.ACCESS_COARSE_LOCATION).build()
                    .send { result ->
                        if (result.allGranted()) {
                            if (ActivityCompat.checkSelfPermission(
                                    this,
                                    Manifest.permission.ACCESS_FINE_LOCATION
                                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                                    this,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                                ) != PackageManager.PERMISSION_GRANTED
                            ) {
                                isLocationPermissionOk = false
                                Toast.makeText(this, "Allow permission", Toast.LENGTH_SHORT).show()
                            }
                            /*fusedLocationClient.lastLocation
                                .addOnSuccessListener { location : Location? ->
                                    // Got last known location. In some rare situations this can be null.
                                    if(location!=null){
                                        passLat=location.latitude
                                        passLng=location.longitude
                                        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(LatLng(passLat, passLng), 12.0f)
                                        googleMap?.animateCamera(cameraUpdate)
                                        //setUpMarkerData()
                                        googleMap?.clear()
                                        marker?.remove()
                                        tvShowProperties.visibility=View.GONE
                                        mapAndNearByViewModel.fetchShowAllPropertyLocationDetails(passLat.toString(),passLng.toString())
                                        Log.e("location details",location.toString())
                                    }
                                    else{
                                        Log.e("location else","null")
                                    }
                                }
                                .addOnFailureListener {
                                    Toast.makeText(this, "${it.toString()}", Toast.LENGTH_SHORT).show()
                                }*/
                            val locationRequest =
                                LocationRequest().setInterval(2000).setFastestInterval(2000)
                            fusedLocationClient.requestLocationUpdates(
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
                                                    passLat = location.latitude
                                                    passLng = location.longitude
                                                    val cameraUpdate =
                                                        CameraUpdateFactory.newLatLngZoom(
                                                            LatLng(
                                                                passLat,
                                                                passLng
                                                            ), 12.0f
                                                        )
                                                    googleMap?.animateCamera(cameraUpdate)
                                                    //setUpMarkerData()
                                                    googleMap?.clear()
                                                    marker?.remove()
                                                    tvShowProperties.visibility = View.GONE
                                                    mapAndNearByViewModel.fetchShowAllPropertyLocationDetails(
                                                        passLat.toString(),
                                                        passLng.toString()
                                                    )
                                                    Log.e("location details", location.toString())
                                                    break
                                                }
                                            }

                                        }
                                    }
                                },
                                Looper.myLooper()
                            )
                        }
                    }
            }
            if (result.allShouldShowRationale()) {
                Toaster.showToast(
                    this,
                    "Please allow permissions",
                    Toaster.State.WARNING,
                    Toast.LENGTH_SHORT
                )
            }
        }
    }

    private fun setUpMarkerData() {
        tc = IconGenerator(this)
        tcNotSelected = IconGenerator(this)

        tc.setTextAppearance(R.style.iconGenText)
        tc.setContentPadding(50, 55, 50, 0)

        tcNotSelected.setTextAppearance(R.style.iconGenText)
        tcNotSelected.setContentPadding(40, 20, 40, 0)

        tc.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_mark_plain_tick))
        tcNotSelected.setBackground(ContextCompat.getDrawable(this, R.drawable.ic_mark_plain))


        markerList.clear()
        markerTag = 0
        if (!(markerDataList.isNullOrEmpty())) {
            for (i in 0 until markerDataList.size) {

                createMarker(
                    markerDataList[i].lat, markerDataList[i].lng,
                    if (i == 0) tc.makeIcon(markerDataList[i].price)
                    else tcNotSelected.makeIcon(markerDataList[i].price)
                )?.let {
                    markerList.add(
                        it
                    )
                }
            }

            tvPropertyAmountMap.text = markerDataList[markerDataList.size - 1].price
            googleMap!!.setOnMarkerClickListener { marker ->
                Log.e("marker click", "Clicked on  ${marker.tag}")
//           googleMap!!.clear()
                if (propertyVisible) {
                    constraintBottom.visibility = View.VISIBLE
                    propertyVisible = false
                    for (i in 0 until markerList.size) {
                        markerList[i].setIcon(
                            BitmapDescriptorFactory.fromBitmap(tcNotSelected.makeIcon(markerDataList[i].price))
                        )
                    }
                    marker.setIcon(BitmapDescriptorFactory.fromBitmap(tc.makeIcon(markerDataList[marker.tag as Int].price)))
                    tvPropertyAmountMap.text = markerDataList[marker.tag as Int].price
                }
                if (nearByPropertyVisible) {
                    constraintBottom.visibility = View.VISIBLE
                    for (i in 0 until markerList.size) {
                        markerList[i].setIcon(
                            BitmapDescriptorFactory.fromBitmap(tcNotSelected.makeIcon(markerDataList[i].price))
                        )
                    }
                    marker.setIcon(BitmapDescriptorFactory.fromBitmap(tc.makeIcon(markerDataList[marker.tag as Int].price)))
                    passLat = markerDataList[marker.tag as Int].lat
                    passLng = markerDataList[marker.tag as Int].lng
                    mapAndNearByViewModel.fetchPropertyLocationDetails(markerDataList[marker.tag as Int].id.toString())
                }
                if (nearByPlacesVisible) {
                    Log.e("near", "byplaces")
                    marker.showInfoWindow()
                    googleMap!!.setOnInfoWindowClickListener { markerClose ->
                        if (this.isConnected) {
                            if (markerClose.isInfoWindowShown) {
                                markerClose.hideInfoWindow()
                            } else {
                                markerClose.showInfoWindow()
                            }
                        } else {
                            Toast.makeText(this, "No Internet", Toast.LENGTH_LONG).show()
                        }
                    }
                }

                true
            }
        }
    }

    private fun setUpMarkerDataNearByPlaces() {
        val markerDataListNearByPlaces = resultsNearByPlaces
        nearByPlacesVisible = true
        propertyVisible = false
        nearByPropertyVisible = false
        tvShowProperties.visibility = View.VISIBLE
        constraintBottom.visibility = View.GONE
        for (i in 0 until markerDataListNearByPlaces.size) {
            addMarker(markerDataListNearByPlaces[i], i)
        }
    }

    private fun addMarker(resultDetails: Result, position: Int) {
        val markerOptions = MarkerOptions()
            .position(
                LatLng(
                    resultDetails.geometry.location.lat,
                    resultDetails.geometry.location.lng
                )
            )
            .title(resultDetails.name)
            .snippet(resultDetails.vicinity)

        markerOptions.icon(getCustomIcon())
        googleMap?.addMarker(markerOptions)?.tag = position

    }

    private fun getCustomIcon(): BitmapDescriptor {

        Log.e("TAG", "getCustomIcon: $selectedPlace")
        var background: Drawable? = null

        when (selectedPlace) {
            getString(R.string.school) -> {
                background = ContextCompat.getDrawable(this, R.drawable.map_school)
            }
            getString(R.string.mosque) -> {
                background = ContextCompat.getDrawable(this, R.drawable.map_mosque)
            }
            getString(R.string.hotel) -> {
                background = ContextCompat.getDrawable(this, R.drawable.map_hotel)
            }
            getString(R.string.malls) -> {
                background = ContextCompat.getDrawable(this, R.drawable.map_mall)
            }
            getString(R.string.hospital) -> {
                background = ContextCompat.getDrawable(this, R.drawable.map_hospital)
            }
            getString(R.string.bank) -> {
                background = ContextCompat.getDrawable(this, R.drawable.map_bank)
            }
            else -> {
                background = ContextCompat.getDrawable(this, R.drawable.map_location)
            }


        }

        //background?.setTint(ContextCompat.getColor(this,R.color.white))
        background?.setBounds(0, 0, background.intrinsicWidth, background.intrinsicHeight)
        val bitmap = Bitmap.createBitmap(
            background?.intrinsicWidth!!, background.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        background.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)

    }

    private fun createMarker(
        latitude: Double,
        longitude: Double,
        bmp: Bitmap
    ): Marker? {
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(LatLng(latitude, longitude), 12.0f)
        googleMap?.animateCamera(cameraUpdate)
        marker = googleMap!!.addMarker(
            MarkerOptions()
                .position(LatLng(latitude, longitude))
                .anchor(0.5f, 0.5f)
                .draggable(false)
                .icon(BitmapDescriptorFactory.fromBitmap(bmp))
        )
        marker?.tag = markerTag
        Log.e("markerTag", " is $markerTag")
        markerTag += 1
        return marker
    }

    private fun createMarkerNearPlaces(
        latitude: Double,
        longitude: Double,
        bmp: Bitmap
    ): Marker? {
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(LatLng(latitude, longitude), 12.0f)
        googleMap?.animateCamera(cameraUpdate)
        marker = googleMap!!.addMarker(
            MarkerOptions()
                .position(LatLng(latitude, longitude))
                .anchor(0.5f, 0.5f)
                .draggable(false)
                .icon(BitmapDescriptorFactory.fromBitmap(bmp))
        )
        marker?.tag = markerTag
        Log.e("markerTag", " is $markerTag")
        markerTag += 1
        return marker
    }

    private fun setCommonPlacesRecyclerView() {
        rvCommonPlaces.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvCommonPlaces.adapter = NearestCommonPlacesAdapter(this) { selectedNearBy(it) }
    }

    private fun selectedNearBy(placeName: String) {
        selectedPlace = placeName
        if (isLocationPermissionOk) {
            mapAndNearByViewModel.fetchNearByPlaces(
                "$passLat,$passLng",
                "1500", placeName, placeName,
                resources.getString(R.string.google_maps_key)
            )
        } else {
            Toast.makeText(this, "Allow Permission", Toast.LENGTH_SHORT).show()
        }

    }

    override fun setupUI() {

    }

    override fun setupViewModel() {
        mapAndNearByViewModel = MapAndNearByViewModel()
    }

    @SuppressLint("SetTextI18n")
    override fun setupObserver() {
        mapAndNearByViewModel.getNearByPlacesListResponse()
            .observe(this, androidx.lifecycle.Observer {
                when (it.status) {
                    Status.LOADING -> showLoader()
                    Status.SUCCESS -> {
                        dismissLoader()
                        Log.e("responseplacesnearby", Gson().toJson(it))
                        if (it.data != null) {
                            if (!(it.data.results.isNullOrEmpty())) {
                                resultsNearByPlaces = it.data.results as ArrayList<Result>
                                googleMap!!.clear()
                                markerTag = 0
                                if (marker != null) {
                                    Log.e("check", "marker if")
                                    marker!!.remove()
                                }
                                setUpMarkerDataNearByPlaces()
                            }
                        }

                    }
                    Status.DATA_EMPTY -> {
                        dismissLoader()
                        Toaster.showToast(
                            this,
                            it.message.toString(),
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
        mapAndNearByViewModel.getHomeListingPropertyLocationResponse().observe(this, Observer {
            when (it.status) {
                Status.LOADING -> showLoader()
                Status.SUCCESS -> {
                    dismissLoader()
                    Log.e("updateFav", Gson().toJson(it))
                    if (it.data != null) {
                        if (it.data.property_details != null) {
                            passLat = it.data.property_details.latitude.toDouble()
                            passLng = it.data.property_details.longitude.toDouble()
                            propertyVisible = true
                            nearByPropertyVisible = false
                            if (it.data.property_details.property_priority_image != null) {
                                roundedPropertyImage.loadImagesWithGlideExt(it.data.property_details.property_priority_image.document)
                            }
                            tvPropertyNameMap.text = it.data.property_details.property_name
                            tvRatingMap.text = it.data.property_details.rating
                            if (it.data.property_details.property_to == 0) {
                                tvPropertyAmountMap.text = getString(R.string.sar) + " " +
                                        it.data.property_details.rent
                                amountPassToMap = getString(R.string.sar) + " " +
                                        it.data.property_details.rent
                                tvPropertyTypeDetailedMap.text =
                                    getString(R.string.property_for_rent)
                            } else {
                                tvPropertyAmountMap.text = getString(R.string.sar) + " " +
                                        it.data.property_details.selling_price
                                amountPassToMap = getString(R.string.sar) + " " +
                                        it.data.property_details.selling_price
                                tvPropertyTypeDetailedMap.text =
                                    getString(R.string.property_for_sale)
                            }
                            tvBedCountMap.text = it.data.property_details.Beds.toString()
                            tvBathCountMap.text = it.data.property_details.Bathroom.toString()
                            tvDiameterMap.text = it.data.property_details.area.toString() + " " +
                                    getString(R.string.sq_m)
                            markerDataList.clear()
                            markerList.clear()
                            markerDataList.add(MarkerData(-1, passLat, passLng, amountPassToMap))
                            setUpMarkerData()

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

        mapAndNearByViewModel.getShowAllNearbyPropertyLocationDetailsResponse()
            .observe(this, Observer {
                when (it.status) {
                    Status.LOADING -> showLoader()
                    Status.SUCCESS -> {
                        dismissLoader()
                        Log.e("updateFav", Gson().toJson(it))
                        if (it.data != null) {
                            if (it.data.data != null) {
                                markerDataList.clear()
                                markerList.clear()
                                constraintBottom.visibility = View.VISIBLE
                                if (it.data.data.main_property != null) {

                                    if (it.data.data.main_property.property_priority_image != null) {
                                        roundedPropertyImage.loadImagesWithGlideExt(it.data.data.main_property.property_priority_image.document)
                                    }
                                    tvPropertyNameMap.text = it.data.data.main_property.property_name
                                    tvPropertyAmountMap.text = getString(R.string.sar) + " " +
                                            it.data.data.main_property.selling_price
                                    tvRatingMap.text = it.data.data.main_property.rating
                                    if (it.data.data.main_property.property_to == 0) {
                                        tvPropertyAmountMap.text = getString(R.string.sar) + " " +
                                                it.data.data.main_property.rent
                                        tvPropertyTypeDetailedMap.text =
                                            getString(R.string.property_for_rent)
                                        markerDataList.add(
                                            MarkerData(
                                                it.data.data.main_property.id,
                                                it.data.data.main_property.latitude.toDouble(),
                                                it.data.data.main_property.longitude.toDouble(),
                                                getString(R.string.sar) + " " + it.data.data.main_property.rent
                                            )
                                        )
                                    } else {
                                        tvPropertyAmountMap.text = getString(R.string.sar) + " " +
                                                it.data.data.main_property.selling_price
                                        tvPropertyTypeDetailedMap.text =
                                            getString(R.string.property_for_sale)
                                        markerDataList.add(
                                            MarkerData(
                                                it.data.data.main_property.id,
                                                it.data.data.main_property.latitude.toDouble(),
                                                it.data.data.main_property.longitude.toDouble(),
                                                getString(R.string.sar) + " " + it.data.data.main_property.selling_price
                                            )
                                        )
                                    }
                                    tvBedCountMap.text = it.data.data.main_property.Beds.toString()
                                    tvBathCountMap.text =
                                        it.data.data.main_property.Bathroom.toString()
                                    tvDiameterMap.text =
                                        it.data.data.main_property.area.toString() + " " +
                                                getString(R.string.sq_m)
                                    passLat = it.data.data.main_property.latitude.toDouble()
                                    passLng = it.data.data.main_property.longitude.toDouble()
                                }
                                if (!(it.data.data.near_properties.isNullOrEmpty())) {
                                    for (d in it.data.data.near_properties.indices) {
                                        if (it.data.data.near_properties[d].property_to == 0) {
                                            markerDataList.add(
                                                MarkerData(
                                                    it.data.data.near_properties[d].id,
                                                    it.data.data.near_properties[d].latitude.toDouble(),
                                                    it.data.data.near_properties[d].longitude.toDouble(),
                                                    getString(R.string.sar) + " " + it.data.data.near_properties[d].rent
                                                )
                                            )
                                        } else {
                                            markerDataList.add(
                                                MarkerData(
                                                    it.data.data.near_properties[d].id,
                                                    it.data.data.near_properties[d].latitude.toDouble(),
                                                    it.data.data.near_properties[d].longitude.toDouble(),
                                                    getString(R.string.sar) + " " + it.data.data.near_properties[d].selling_price
                                                )
                                            )
                                        }
                                    }
                                    Log.e("nearbylist", Gson().toJson(markerDataList))
                                }
                                nearByPlacesVisible = false
                                propertyVisible = false
                                nearByPropertyVisible = true
                                setUpMarkerData()
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

        mapAndNearByViewModel.getPropertyLocationDetailsResponse().observe(this, Observer {
            when (it.status) {
                Status.LOADING -> showLoader()
                Status.SUCCESS -> {
                    dismissLoader()
                    Log.e("updateFav", Gson().toJson(it))
                    if (it.data != null) {
                        if (it.data.property_details != null) {
                            constraintBottom.visibility = View.VISIBLE
                            if (it.data.property_details.property_priority_image != null) {
                                roundedPropertyImage.loadImagesWithGlideExt(it.data.property_details.property_priority_image.document)
                            }
                            passedPropertyIdMarkerClicked = it.data.property_details.id.toString()
                            tvPropertyNameMap.text = it.data.property_details.property_name
                            tvRatingMap.text = it.data.property_details.rating
                            if (it.data.property_details.property_to == 0) {
                                tvPropertyAmountMap.text = getString(R.string.sar) + " " +
                                        it.data.property_details.rent
                                tvPropertyTypeDetailedMap.text =
                                    getString(R.string.property_for_rent)
                            } else {
                                tvPropertyAmountMap.text = getString(R.string.sar) + " " +
                                        it.data.property_details.selling_price
                                tvPropertyTypeDetailedMap.text =
                                    getString(R.string.property_for_sale)
                            }
                            tvBedCountMap.text = it.data.property_details.Beds.toString()
                            tvBathCountMap.text = it.data.property_details.Bathroom.toString()
                            tvDiameterMap.text = it.data.property_details.area.toString() + " " +
                                    getString(R.string.sq_m)
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

        mapAndNearByViewModel.getShowAllPropertyLocationDetailsResponse().observe(this, Observer {
            when (it.status) {
                Status.LOADING -> showLoader()
                Status.SUCCESS -> {
                    dismissLoader()
                    Log.e("updatemap check", Gson().toJson(it))
                    if (it.data != null) {
                        if (it.data.data != null) {
                            markerDataList.clear()
                            markerList.clear()
                            if (!(it.data.data.properties.isNullOrEmpty())) {
                                for (l in it.data.data.properties.indices) {
                                    if (it.data.data.properties[l].property_to == 0) {
                                        markerDataList.add(
                                            MarkerData(
                                                it.data.data.properties[l].id,
                                                it.data.data.properties[l].latitude.toDouble(),
                                                it.data.data.properties[l].longitude.toDouble(),
                                                getString(R.string.sar) + " " + it.data.data.properties[l].rent
                                            )
                                        )
                                    } else {
                                        markerDataList.add(
                                            MarkerData(
                                                it.data.data.properties[l].id,
                                                it.data.data.properties[l].latitude.toDouble(),
                                                it.data.data.properties[l].longitude.toDouble(),
                                                getString(R.string.sar) + " " + it.data.data.properties[l].selling_price
                                            )
                                        )
                                    }
                                }
                                nearByPlacesVisible = false
                                propertyVisible = false
                                nearByPropertyVisible = true
                                setUpMarkerData()
                            } else {
                                constraintBottom.visibility = View.GONE
                                Toaster.showToast(
                                    this, getString(R.string.no_property_found),
                                    Toaster.State.ERROR, Toast.LENGTH_LONG
                                )
                            }
                        }
                    }

                }
                Status.DATA_EMPTY -> {
                    dismissLoader()
                    Log.e("data empty", Gson().toJson(it))
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
                        Log.e("NO_INTERNET ", Gson().toJson(it))
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
        ivBack.setOnClickListener {
            onBackPressed()
        }
        tvShowProperties.setOnClickListener {
            if (!(markerDataList.isNullOrEmpty())) {
                constraintBottom.visibility = View.VISIBLE
            }
            googleMap!!.clear()
            if (marker != null) {
                marker!!.remove()
            }

            nearByPlacesVisible = false
            if (passedFromType == "property_details") {
                nearByPropertyVisible = true
            } else if (passedFromType == "home_property_list") {
                //propertyVisible=true
            }
            setUpMarkerData()
            //getLastLocationFun()
        }
        etSearchMap.setOnClickListener {
            val fields: List<Place.Field> =
                listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)
            val intent: Intent = Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields
            )
                .build(this)
            startActivityForResult(intent, PLACE_PICKER_REQUEST)
        }
        ivCloseMap.setOnClickListener {
            if (!nearByPropertyVisible) {
                propertyVisible = true
            }
            constraintBottom.visibility = View.GONE
        }
        constraintBottom.setOnClickListener {
            if (passedFromType == "home_property_list" || passedFromType == "property_details") {
                callPropertyDetails(passedPropertyId.toInt())
            } else {
                passedPropertyIdMarkerClicked.let {
                    callPropertyDetails(it.toInt())
                }
            }
        }
    }

    private fun callPropertyDetails(property_id: Int) {
        val intent = Intent(this, PropertyDetailsActivity::class.java)
        intent.putExtra(Constants.INTENT_PROPERTY_ID, property_id)
        startActivity(intent)
    }

}