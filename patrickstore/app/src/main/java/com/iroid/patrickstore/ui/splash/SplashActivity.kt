package com.iroid.patrickstore.ui.splash

import android.Manifest
import android.content.Intent
import android.content.IntentSender
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.RequiresApi
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.iroid.patrickstore.R
import com.iroid.patrickstore.base.BaseActivity
import com.iroid.patrickstore.databinding.ActivitySplashBinding
import com.iroid.patrickstore.preference.AppPreferences.isLogin
import com.iroid.patrickstore.ui.home.HomeActivity
import com.iroid.patrickstore.ui.location.LocationActivity
import com.iroid.patrickstore.ui.login.LoginActivity
import com.iroid.patrickstore.utils.Constants
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_location.*
import kotlinx.android.synthetic.main.activity_splash.*


class SplashActivity :BaseActivity<SplashViewModal,ActivitySplashBinding>(){
    private val REQUEST_CHECK_SETTINGS = 123

    override val layoutId: Int
        get() = R.layout.activity_splash

    override val setToolbar: Boolean
        get() = true

    override val hideStatusBar: Boolean
        get() = true



    private fun navigateToMainScreen() {
        if(isLogin){
            startActivity(Intent(this, LocationActivity::class.java))
            finish()
        }else{
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        //val topAnim: Animation = AnimationUtils.loadAnimation(this,R.anim.top_anim)
        //ivLogo.animation=topAnim
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
    override fun getViewBinding(): ActivitySplashBinding {
        return ActivitySplashBinding.inflate(layoutInflater)
    }

    override fun initViews() {
        checkLocationTest()
    }
    private fun displayLocationSettingsRequest() {
        val locationRequestHighAccuracy: LocationRequest = LocationRequest.create()
        locationRequestHighAccuracy.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequestHighAccuracy.interval = 10000
        locationRequestHighAccuracy.fastestInterval = 10000 / 2
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequestHighAccuracy)
        builder.setAlwaysShow(true)
        val result: Task<LocationSettingsResponse> =
            LocationServices.getSettingsClient(this).checkLocationSettings(builder.build())
        result.addOnCompleteListener { task ->
            try {
                val response: LocationSettingsResponse = task.getResult(ApiException::class.java)
                navigateToMainScreen()
            } catch (exception: ApiException) {
                when (exception.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                        val resolvable = exception as ResolvableApiException
                        resolvable.startResolutionForResult(
                            this,
                            REQUEST_CHECK_SETTINGS
                        )
                    } catch (e: IntentSender.SendIntentException) {
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
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when {
            resultCode == AutocompleteActivity.RESULT_ERROR -> {
                val status: Status = Autocomplete.getStatusFromIntent(data!!)
            }
            resultCode == RESULT_CANCELED -> {
                finish()
            }
            requestCode == REQUEST_CHECK_SETTINGS -> {
                when (resultCode) {
                    RESULT_OK -> {
                        navigateToMainScreen()
                    }
                    RESULT_CANCELED -> {
                    }
                    else -> {
                    }
                }
            }
        }
    }

}
