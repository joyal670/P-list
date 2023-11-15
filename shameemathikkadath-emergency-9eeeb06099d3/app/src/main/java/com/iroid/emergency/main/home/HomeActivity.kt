package com.iroid.emergency.main.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import com.example.awesomedialog.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsStates
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.iroid.emergency.R
import com.iroid.emergency.base.BaseActivity
import com.iroid.emergency.databinding.ActivityHomeBinding
import com.iroid.emergency.main.settings.SettingsActivity
import com.iroid.emergency.modal.common.Location_new
import com.iroid.emergency.preference.AppPreferences
import com.iroid.emergency.preference.ConstantPreference
import com.iroid.emergency.start_up.auth.AuthActivity
import com.iroid.emergency.start_up.utils.PermissionUtils
import com.iroid.emergency.start_up.utils.nextActivity
import kotlinx.android.synthetic.main.activity_settings.view.*
import kotlinx.android.synthetic.main.content_home.*
import java.util.*


class HomeActivity : BaseActivity<HomeViewModal, ActivityHomeBinding>(), LocationListener {
    private val permissionId: Int=100
    private lateinit var mFusedLocationClient: FusedLocationProviderClient


    private var locationServicesWarning: Snackbar? = null
    private var dialog1: AlertDialog? = null
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = false
    private val locationUpdateViewModel by lazy {
        ViewModelProvider(this)[LocationUpdateViewModel::class.java]
    }

    private val locationViewModel by lazy {
        ViewModelProvider(this)[LocationHistoryViewModel::class.java]
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onResume() {
        super.onResume()
        if (AppPreferences.isLocationEnabled) {
            dismissWarning()
        } else {
            showLocationServicesWarning()

        }
        binding.appBarHome.tvProfile.text= AppPreferences.userName!![0].toString().toUpperCase()



    }


    override fun setOnClick() {
        binding.appBarHome.floatingActionButton.setOnClickListener {
            binding.drawerLayout.open()
        }
        binding.lyDrawer.floatingActionButton.setOnClickListener {
            binding.drawerLayout.close()
        }
        binding.appBarHome.tvProfile.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(ConstantPreference.TYPE, ConstantPreference.PROFILE)
            this.nextActivity(SettingsActivity::class.java, bundle)
        }
        binding.lyDrawer.tvFaq.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(ConstantPreference.TYPE, ConstantPreference.FAQ)
            this.nextActivity(SettingsActivity::class.java, bundle)
        }
        binding.lyDrawer.tvFeedback.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(ConstantPreference.TYPE, ConstantPreference.FEEDBACK)
            this.nextActivity(SettingsActivity::class.java, bundle)
        }
        binding.lyDrawer.tvAbout.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(ConstantPreference.TYPE, ConstantPreference.ABOUT)
            this.nextActivity(SettingsActivity::class.java, bundle)
        }
        binding.lyDrawer.tvLogout.setOnClickListener {
            openLogoutDialog()
        }

    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onStart() {
        super.onStart()

        checkLocationPermission()
    }






    override fun initViews() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getLocation()
        Log.e("#33333", "initViews: ", )
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }
    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            permissionId
        )
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == permissionId) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLocation()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private val resolutionForResult =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { activityResult ->
            val states = activityResult.data?.let { LocationSettingsStates.fromIntent(it) }
            when (activityResult.resultCode) {
                RESULT_OK ->
                    if (states?.isLocationUsable == true) {
                        AppPreferences.isLocationEnabled = true
                        locationUpdateViewModel.startLocationUpdates()

                    }
                RESULT_CANCELED ->
                    showLocationServicesWarning()
            }
        }
    @SuppressLint("MissingPermission", "SetTextI18n")
    private fun getLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    val location: Location? = task.result
                    if (location != null) {
                        try
                        {
                            val geocoder = Geocoder(this, Locale.getDefault())
                            val list: List<Address> =
                                geocoder.getFromLocation(location.latitude, location.longitude, 1)
                            AppPreferences.userLat=list[0].latitude.toString()
                            AppPreferences.userLan=list[0].longitude.toString()
                            val db = Firebase.firestore
                            db.collection("Location").document(AppPreferences.userMobile!!)
                                .set(Location_new(list[0].latitude,
                                    list[0].longitude,AppPreferences.userMobile!!))
                                .addOnSuccessListener { documentReference ->

                                }
                                .addOnFailureListener { e ->

                                }
                        }catch(ex:Exception){

                        }
                        }

                }
            } else {
                Toast.makeText(this, "Please turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }
    @RequiresApi(Build.VERSION_CODES.Q)
    private fun showLocationServicesWarning() {
        if (locationServicesWarning == null) {
            locationServicesWarning = Snackbar.make(
                binding.root,
                R.string.enable_location_services_text,
                Snackbar.LENGTH_INDEFINITE
            )
                .setAction(R.string.ok) {
                    locationUpdateViewModel.requestLocationServices(this, resolutionForResult)
                }
        }

        if (locationServicesWarning?.isShown == false) {
            locationServicesWarning?.show()
        }
    }

    private fun dismissWarning() {
        locationServicesWarning?.dismiss()
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun checkLocationPermission() {
        when {
            !PermissionUtils.isForegroundLocationPermissionGranted(this) -> {
                PermissionUtils.requestPermission(
                    this,
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
            !PermissionUtils.isBackgroundLocationPermissionGranted(this) -> {
                if (PermissionUtils.shouldShowRationale(
                        this,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION
                    )

                ) {
                    showBackgroundLocationRationale()
                } else {
                    // This will never get reached as a rationale should always be shown for this permission
                    requestBackgroundLocationPermission()
                }
            }
            else -> {
                locationUpdateViewModel.startLocationUpdates()
                locationPermissionGranted()
            }
        }
    }

    private fun showBackgroundLocationRationale() {
        if (dialog1 == null) {
            dialog1 = MaterialAlertDialogBuilder(this,R.style.MyDialogTheme)
                .setTitle(R.string.location_rationale_title)
                .setMessage(R.string.location_rationale)
                .setPositiveButton(getString(R.string.got_it)) { _, _ -> requestBackgroundLocationPermission() }
                .setNegativeButton(getString(R.string.no_thanks)) { _, _ ->
                    locationPermissionsDenied()
                }
                .create()
        }

        if (dialog1?.isShowing == false) {
            dialog1?.show()
        }
    }



    private fun requestBackgroundLocationPermission() {
        if (!locationUpdateViewModel.hasRequestedBackgroundLocation) {
            PermissionUtils.requestPermission(
                this,
                arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
            )
            locationUpdateViewModel.hasRequestedBackgroundLocation = true
        } else {
            PermissionUtils.openAppSettings(this)
        }
    }

    private fun locationPermissionsDenied() {
        showLocationPermissionsWarning()
    }

    private fun showLocationPermissionsWarning() {
        Snackbar.make(
            binding.root,
            R.string.error_permissions_denied,
            Snackbar.LENGTH_INDEFINITE
        )
            .setAction(R.string.settings) {
                PermissionUtils.openAppSettings(this)
            }
            .show()
    }

    private fun locationPermissionGranted() {

    }

    override fun getViewBinding(): ActivityHomeBinding {
        return ActivityHomeBinding.inflate(layoutInflater)
    }

    private fun openLogoutDialog() {
        AwesomeDialog.build(this)
            .title("Logout")
            .body("Are you sure you want to logout?")
            .icon(R.drawable.ic_logout)
            .position(AwesomeDialog.POSITIONS.CENTER)
            .onPositive("Yes", buttonBackgroundColor = R.drawable.drawable_rounded_corners) {
                AppPreferences.cleareSharedPreference()
                val intent = Intent(this, AuthActivity::class.java)
                startActivity(intent)
                finishAffinity()
            }
            .onNegative("No", buttonBackgroundColor = R.drawable.drawable_rounded_corners) {
                Log.d("TAG", "negative ")
            }
    }



    override fun onLocationChanged(p0: Location) {
        AppPreferences.userLat = p0.latitude.toString()
        AppPreferences.userLan =p0.longitude.toString()
        val db = Firebase.firestore
        db.collection("Location").document(AppPreferences.userMobile!!)
            .set(
                Location_new(p0.latitude,
                    p0.longitude,AppPreferences.userMobile!!)
            )
            .addOnSuccessListener { documentReference ->

            }
            .addOnFailureListener { e ->

            }
    }

//    private val backPressedDispatcher = object : OnBackPressedCallback(true) {
//        override fun handleOnBackPressed() {
//            // Redirect to our own functio
//            onBackPressed()
//
//        }
//    }
}
