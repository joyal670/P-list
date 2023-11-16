package com.iroid.patrickstore.ui.location


//import com.iroid.patrickstore.ui.location.places_api.PlaceAutoSuggestAdapter
//import com.iroid.patrickstore.utils.CommonUtils.Companion.getCityNameByCoordinates

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationServices.getFusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.iroid.patrickstore.R
import com.iroid.patrickstore.base.BaseActivity
import com.iroid.patrickstore.databinding.ActivityLocationBinding
import com.iroid.patrickstore.preference.AppPreferences
import com.iroid.patrickstore.ui.home.HomeActivity
import com.iroid.patrickstore.ui.home.HomeViewModel
import com.iroid.patrickstore.utils.*
import com.iroid.patrickstore.utils.Constants.INTENT_ADDRESS
import com.iroid.patrickstore.utils.Constants.INTENT_LOCATION
import com.iroid.patrickstore.utils.Constants.INTENT_TYPE
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_location.*
import okio.IOException
import java.util.*


class LocationActivity : BaseActivity<HomeViewModel, ActivityLocationBinding>(),
    OnMapReadyCallback ,LocationListener{
    override fun onLocationChanged(p0: Location?) {
        latitude=p0!!.latitude
        longitude=p0!!.longitude
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        animateCamera(latitude, longitude)
    }

    private val AUTOCOMPLETE_REQUEST_CODE = 120
    private var mMap: GoogleMap? = null
    private var cameraPosition: CameraPosition? = null
    private var mapView: View? = null
    private var cameraMove = false

    private val REQUEST_CHECK_SETTINGS = 123
    private val REQUEST_LOCATION = 1234

    private lateinit var placesClient: PlacesClient
    private var myLocationButton: View? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var locationPermissionGranted = false
    private var locationDevicePermissionGranted = false
    private var lastKnownLocation: Location? = null
    private lateinit var prevCameraPosition: CameraPosition
    private var isFromAddress: Boolean = false
    private var latitude:Double =0.0
    private var longitude:Double =0.0
    private var locationManager: LocationManager? = null
    private var mLocationRequest: LocationRequest? = null
    private lateinit var locationCallback:LocationCallback

    private val UPDATE_INTERVAL = (10 * 1000 /* 10 secs */).toLong()
    private val FASTEST_INTERVAL: Long = 2000 /* 2 sec */


    val fields = listOf(Place.Field.ID, Place.Field.NAME)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkLocationTest()
        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION)
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION)
        }
        isFromAddress = intent.getStringExtra(INTENT_TYPE) == INTENT_ADDRESS
        Places.initialize(applicationContext, resources.getString(R.string.google_maps_key))
        placesClient = Places.createClient(this)

        // Construct a FusedLocationProviderClient.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        // Build the map.
        // [START maps_current_place_map_fragment]

        if(isConnected){
            val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment?
            mapView = mapFragment!!.view
            mapFragment?.getMapAsync(this)
            locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        }else {
            CommonUtils.noInternet(lifecycle, this)

        }
            flMyLocation.setOnClickListener {
            myLocationButton!!.callOnClick()
        }
        tvSetLocation.setOnClickListener {
            if (isFromAddress) {
                val intent = Intent()
                intent.putExtra(INTENT_LOCATION, tvLocation.text.toString())
                setResult(REQUEST_LOCATION, intent)
                finish()

            } else {
                AppPreferences.lat= mMap!!.cameraPosition.target.latitude.toString()
                AppPreferences.lan= mMap!!.cameraPosition.target.longitude.toString()
                viewModel.setLocation(
                    mMap!!.cameraPosition.target.latitude.toString(),
                    mMap!!.cameraPosition.target.longitude.toString()
                )

            }

        }
         etSearch.setOnClickListener {
             val fields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)
             val intent = Autocomplete.IntentBuilder(
                 AutocompleteActivityMode.FULLSCREEN,
                 fields
             )
                 .build(this)
             startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
         }

    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when {
            resultCode == REQUEST_LOCATION -> {
                val place = Autocomplete.getPlaceFromIntent(data!!)
                tvLocation.text = place.name
                animateCamera(place.latLng!!.latitude, place.latLng!!.longitude)
            }
            requestCode == AUTOCOMPLETE_REQUEST_CODE -> {
                try {
                    val place = Autocomplete.getPlaceFromIntent(data!!)
                    val zoomLevel = 15f
                    val homeLatLng = LatLng(place.latLng!!.latitude, place.latLng!!.longitude)
                    mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(homeLatLng, zoomLevel))

                    tvLocation.text = getAddress(place.latLng!!.latitude, place.latLng!!.longitude)
                    tvLocationInfo.text = getAddress(place.latLng!!.latitude, place.latLng!!.longitude)
                }catch (ex:Exception){

                }

            }
            resultCode == AutocompleteActivity.RESULT_ERROR -> {
                val status: Status = Autocomplete.getStatusFromIntent(data!!)
            }
            resultCode == RESULT_CANCELED -> {
                finish()
            }
            requestCode == REQUEST_CHECK_SETTINGS -> {
                when (resultCode) {
                    RESULT_OK -> {
                        recreate()
                        overridePendingTransition(0, 0)
                    }
                    RESULT_CANCELED -> {
                    }
                    else -> {
                    }
                }
                finish()
            }
        }
    }


    private fun checkLocationTest() {
        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            .withListener(object : MultiplePermissionsListener {
                @RequiresApi(Build.VERSION_CODES.Q)
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    displayLocationSettingsRequest()
                }

                @RequiresApi(Build.VERSION_CODES.Q)
                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) { /* ... */

                }
            }).check()
    }
    private fun animateCamera(latitude: Double, longitude: Double) {
        val latLng = LatLng(latitude, longitude)
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15f)
        mMap!!  .animateCamera(cameraUpdate)
    }


    @SuppressLint("MissingSuperCall")
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        map?.let { map ->
            outState.putParcelable(KEY_CAMERA_POSITION, mMap!!.cameraPosition)
            outState.putParcelable(KEY_LOCATION, lastKnownLocation)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onMapReady(map: GoogleMap) {
        mMap = map
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        } else {
            mMap!!.setMyLocationEnabled(true)
        }

        if (mapView != null &&
            mapView!!.findViewById<View?>("1".toInt()) != null
        ) {
            myLocationButton =
                (mapView!!.findViewById<View>("1".toInt()).parent as View).findViewById("2".toInt())

        }


        map.setOnCameraMoveStartedListener {
            cameraMove = true
            binding.tvLocationInfo.text = "Loading..";
        }
        map.setOnCameraMoveCanceledListener {
            cameraMove = false
        }
        map.setOnMapLoadedCallback {
            prevCameraPosition = map.cameraPosition

            //map.moveCamera(CameraUpdateFactory.newLatLngZoom(homeLatLng, 15F))
            try {
                val homeLatLng = LatLng(
                    Location.convert(map.cameraPosition.target.latitude.toString()),
                    Location.convert(map.cameraPosition.target.longitude.toString())
                )
                map.clear()
                tvLocationInfo.text = getAddress(
                    Location.convert(map.cameraPosition.target.latitude.toString()),
                    Location.convert(map.cameraPosition.target.longitude.toString())
                )
                tvLocation.text = getAddress(
                    Location.convert(map.cameraPosition.target.latitude.toString()),
                    Location.convert(map.cameraPosition.target.longitude.toString())
                )
            }catch (ex:Exception){

            }
        }
        map.setOnCameraIdleListener {
            prevCameraPosition = map.cameraPosition

            //map.moveCamera(CameraUpdateFactory.newLatLngZoom(homeLatLng, 15F))
            try {
                binding.cardLocation.visibility=View.VISIBLE
                val homeLatLng = LatLng(
                    Location.convert(map.cameraPosition.target.latitude.toString()),
                    Location.convert(map.cameraPosition.target.longitude.toString())
                )
                map.clear()
                constraintFetchLocation.visibility=View.GONE
                tvLocationInfo.text = getAddress(
                    Location.convert(map.cameraPosition.target.latitude.toString()),
                    Location.convert(map.cameraPosition.target.longitude.toString())
                )
                tvLocation.text = getAddress(
                    Location.convert(map.cameraPosition.target.latitude.toString()),
                    Location.convert(map.cameraPosition.target.longitude.toString())
                )
            }catch (ex:Exception){

            }


        }

    }

    private fun displayLocationSettingsRequest() {
        val locationRequestHighAccuracy: LocationRequest = LocationRequest.create()
        locationRequestHighAccuracy.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        locationRequestHighAccuracy.setInterval(10000)
        locationRequestHighAccuracy.fastestInterval = 10000 / 2
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequestHighAccuracy)
        builder.setAlwaysShow(true)
        val result: Task<LocationSettingsResponse> =
            LocationServices.getSettingsClient(this).checkLocationSettings(builder.build())
        result.addOnCompleteListener { task ->
            try {
                val response: LocationSettingsResponse = task.getResult(ApiException::class.java)
                startLocation()

            } catch (exception: ApiException) {
                when (exception.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                        val resolvable = exception as ResolvableApiException
                        resolvable.startResolutionForResult(
                            this,
                            REQUEST_CHECK_SETTINGS
                        )
                    } catch (e: SendIntentException) {
                        // Ignore the error.
                    } catch (e: ClassCastException) {
                        // Ignore, should be an impossible error.
                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                    }
                }
            }
        }
    }
    private fun startLocation(){
        // Create the location request to start receiving updates
        // Create the location request to start receiving updates
        mLocationRequest = LocationRequest()
        mLocationRequest!!.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        mLocationRequest!!.setInterval(UPDATE_INTERVAL)
        mLocationRequest!!.setFastestInterval(FASTEST_INTERVAL)

        // Create LocationSettingsRequest object using location request

        // Create LocationSettingsRequest object using location request
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest!!)
        val locationSettingsRequest = builder.build()

        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient

        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        val settingsClient = LocationServices.getSettingsClient(this)
        settingsClient.checkLocationSettings(locationSettingsRequest)

        // new Google API SDK v11 uses getFusedLocationProviderClient(this)

        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
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
        locationCallback=object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                // do work here

                onLocationChanged(locationResult.lastLocation)
            }
        }
        fusedLocationProviderClient.requestLocationUpdates(
            mLocationRequest,locationCallback,
            Looper.myLooper()
        )
    }




    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    locationPermissionGranted = true
                }
            }
        }

    }



    private fun getAddress(lat: Double, lng: Double): String {

        val geocoder: Geocoder
        var addresses: List<Address>? = null
        var address: String? = null
        geocoder = Geocoder(this, Locale.getDefault())
        try {
            addresses = geocoder.getFromLocation(
                lat,
                lng,
                1
            ) // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            if (addresses != null && addresses.size > 0) {
                address = addresses[0].getAddressLine(0)
                val city = addresses[0].locality
                val state = addresses[0].adminArea
                val country = addresses[0].countryName
                val postalCode = addresses[0].postalCode
                val knownName = addresses[0].featureName
                val premises = addresses[0].premises
                val countryCode = addresses[0].countryCode
                val subAdminArea = addresses[0].subAdminArea
                val thoroughfare = addresses[0].thoroughfare
            }
            return address!!
        } catch (e: IOException) {

            return "Not defined"
        }




    }


    // [END maps_current_place_update_location_ui]

    companion object {
        private const val DEFAULT_ZOOM = 15
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1

        // Keys for storing activity state.
        // [START maps_current_place_state_keys]
        private const val KEY_CAMERA_POSITION = "camera_position"
        private const val KEY_LOCATION = "location"
        // [END maps_current_place_state_keys]

        // Used for selecting the current place.
        private const val M_MAX_ENTRIES = 5
    }

    override val layoutId: Int
        get() = R.layout.activity_location
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = false

    override fun initViews() {
        setUpObserver()
    }

    private fun setUpObserver() {
        viewModel.locationLiveData.observe(this) {
            when (it.status) {
                com.iroid.patrickstore.utils.Status.SUCCESS -> {
                    dismissProgress()
                    AppPreferences.franchiseId=""
                    startActivity(Intent(this, HomeActivity::class.java))
                }
                com.iroid.patrickstore.utils.Status.LOADING -> {
                    showProgress()
                }
                com.iroid.patrickstore.utils.Status.ERROR -> {
                    dismissProgress()
                    Toaster.showToast(this, it.message!!, Toaster.State.ERROR, Toast.LENGTH_LONG)
                }
                com.iroid.patrickstore.utils.Status.NO_INTERNET -> {
                    if (this.isConnected) {
                        Toaster.showToast(
                            this,
                            SOMETHING_WENT_WRONG,
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    } else {
                        Toaster.showToast(
                            this,
                            NO_INTERNET_MESSAGE,
                            Toaster.State.ERROR,
                            Toast.LENGTH_LONG
                        )
                    }
                }
            }
        }
    }

    override fun getViewBinding(): ActivityLocationBinding {
        return ActivityLocationBinding.inflate(layoutInflater)
    }


}
