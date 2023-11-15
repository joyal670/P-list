package com.iroid.emergency.start_up.splash

import android.Manifest
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.iroid.emergency.R
import com.iroid.emergency.base.BaseActivity
import com.iroid.emergency.databinding.ActivitySplashBinding
import com.iroid.emergency.main.home.HomeActivity
import com.iroid.emergency.main.home.LocationUpdateViewModel
import com.iroid.emergency.preference.AppPreferences
import com.iroid.emergency.start_up.intro.IntroActivity
import com.iroid.emergency.start_up.utils.PermissionUtils
import com.iroid.emergency.start_up.utils.nextActivity
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener


class SplashActivity : BaseActivity<SplashViewModal, ActivitySplashBinding>() {
    private var MY_PERMISSIONS_REQUEST_LOCATION=300
    private var locationServicesWarning: Snackbar? = null
    private var MY_PERMISSIONS_REQUEST_BACKGROUND_LOCATION=500
    private val locationUpdateViewModel by lazy {
        ViewModelProvider(this)[LocationUpdateViewModel::class.java]
    }
    private var dialog1: AlertDialog? = null
    private val REQUEST_CHECK_SETTING=200
    override val setToolbar: Boolean
        get() = false
    override val hideStatusBar: Boolean
        get() = true

    override fun initViews() {
        AppPreferences.userLat=""
        AppPreferences.userLan=""


    }

    override fun onResume() {
        super.onResume()
        checkLocationTest()
    }

    private fun checkLocationTest() {
        Dexter.withContext(this)
            .withPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            .withListener(object : MultiplePermissionsListener {
                @RequiresApi(Build.VERSION_CODES.Q)
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    checkLocationPermission()
                }

                @RequiresApi(Build.VERSION_CODES.Q)
                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) { /* ... */

                }
            }).check()
    }

    private fun callBackgroundLocation() {

    }

    override fun getViewBinding(): ActivitySplashBinding {
        return ActivitySplashBinding.inflate(layoutInflater)
    }
    @RequiresApi(Build.VERSION_CODES.Q)
    private fun checkLocationSetting()
    {
        val locationRequest = LocationRequest.create()
        locationRequest.apply {
            priority=LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 5000
            fastestInterval = 2000
        }

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        builder.setAlwaysShow(true)

        val result: Task<LocationSettingsResponse> = LocationServices.getSettingsClient(applicationContext)
            .checkLocationSettings(builder.build())

        result.addOnCompleteListener {
            try{
                AppPreferences.isLocationEnabled=true
                val response: LocationSettingsResponse = it.getResult(ApiException::class.java)
                checkLocationPermission()

            }catch(e:ApiException){

                when(e.statusCode){
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->{
                        val resolvableApiException = e as ResolvableApiException
                        resolvableApiException.startResolutionForResult(this, REQUEST_CHECK_SETTING)
                        Log.d(TAG, "checkSetting: RESOLUTION_REQUIRED")
                    }

                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        // USER DEVICE DOES NOT HAVE LOCATION OPTION
                    }
                }
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode)
        {
            REQUEST_CHECK_SETTING ->{
                when(resultCode){
                    Activity.RESULT_OK->{
                        AppPreferences.isLocationEnabled=true
//                        checkLocationPermission()
                    }
                    Activity.RESULT_CANCELED ->{
                        Toast.makeText(this, "GPS is Required to use this app", Toast.LENGTH_SHORT).show()
                    }
                }
            }


        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode){
            MY_PERMISSIONS_REQUEST_LOCATION->{
                if(grantResults.isNotEmpty()&& grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    locationUpdateViewModel.hasRequestedBackgroundLocation = true
                    AppPreferences.isLocationEnabled=true
                    if (AppPreferences.isLogin) {
                        this.nextActivity(HomeActivity::class.java, Bundle())
                    } else {
                        this.nextActivity(IntroActivity::class.java,Bundle())
                    }
                }else{
                    showLocationPermissionsWarning()
                }
            }
            MY_PERMISSIONS_REQUEST_BACKGROUND_LOCATION->{
                locationUpdateViewModel.hasRequestedBackgroundLocation = true
                if(grantResults.isNotEmpty()&& grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    AppPreferences.isLocationEnabled=true
                    if (AppPreferences.isLogin) {
                        this.nextActivity(HomeActivity::class.java, Bundle())
                    } else {
                        this.nextActivity(IntroActivity::class.java,Bundle())
                    }
                }else{
                    showLocationPermissionsWarning()
                }
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
    override fun setOnClick() {

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
                    requestBackgroundLocationPermission()

                }
            }
            else -> {
                AppPreferences.isLocationEnabled=true
                if (AppPreferences.isLogin) {
                    val intent=Intent(this,HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    val intent=Intent(this,IntroActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    private fun showBackgroundLocationRationale() {
        if (dialog1 == null) {
            dialog1 = MaterialAlertDialogBuilder(this, R.style.MyDialogTheme)
                .setTitle(R.string.location_rationale_title)
                .setMessage(R.string.location_rationale)
                .setPositiveButton(getString(R.string.got_it)) { _, _ ->
                    requestBackgroundLocationPermission() }
                .setNegativeButton(getString(R.string.no_thanks)) { _, _ ->
                    locationPermissionsDenied()
                }
                .create()
        }

        if (dialog1?.isShowing == false) {
            dialog1?.show()
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

    private fun requestBackgroundLocationPermission() {
        // Open the location permissions for the app where the user can select "Allow all the time".
        // If we have asked previously but the user has denied it, that request will fail. So instead
        // open the app settings where the user can navigate manually to the location permissions
        // and modify it.
        if (!locationUpdateViewModel.hasRequestedBackgroundLocation) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION
                    ),
                    MY_PERMISSIONS_REQUEST_BACKGROUND_LOCATION
                )
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_LOCATION
                )
            }




        } else {
            PermissionUtils.openAppSettings(this)
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
}


